package fr.bgoodes.uhc.util;

import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import fr.bgoodes.uhc.event.option.OptionChangeEvent;
import fr.bgoodes.uhc.service.config.impl.option.BooleanOption;
import fr.bgoodes.uhc.service.config.impl.option.FloatOption;
import fr.bgoodes.uhc.service.config.impl.option.IntOption;
import fr.bgoodes.uhc.service.config.model.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UHCOptionRowUtil {

    private UHCOptionRowUtil() {}

    public static boolean append(
            @NotNull Option<?> option,
            @NotNull String list, int index,
            @NotNull UICommandBuilder ui,
            @NotNull UIEventBuilder events
    ) {
        String row = list + "[" + index + "]";
        String input = row + " #Input";
        EventData data = new EventData().append("Action", "Option").append("Id", option.getKey());

        switch (option) {
            case IntOption intOption -> {
                ui.append(list, "Pages/Fields/IntRow.ui");
                ui.set(input + ".Value", intOption.get());
                data.append("@Number", input + ".Value");
            }
            case FloatOption floatOption -> {
                ui.append(list, "Pages/Fields/NumberRow.ui");
                ui.set(input + ".Value", floatOption.get());
                data.append("@Number", input + ".Value");
            }
            case BooleanOption booleanOption -> {
                ui.append(list, "Pages/Fields/CheckboxRow.ui");
                ui.set(input + ".Value", booleanOption.get());
                data.append("@Bool", input + ".Value");
            }
            default -> {
                return false;
            }
        }

        ui.set(row + " #Label.TextSpans", UHCMessageUtil.tr("uhc.option." + option.getKey()));

        if (option instanceof BooleanOption) {
            events.addEventBinding(CustomUIEventBindingType.ValueChanged, input, data, false);
        } else {
            events.addEventBinding(CustomUIEventBindingType.FocusLost, input, data, false);
            events.addEventBinding(CustomUIEventBindingType.Validating, input, data, false);
        }

        return true;
    }

    public static @Nullable OptionChangeEvent request(
            @NotNull Option<?> option,
            @Nullable Double number,
            @Nullable Boolean bool,
            @NotNull PlayerRef source
    ) {
        return switch (option) {
            case IntOption intOption when number != null -> new OptionChangeEvent(source, intOption, (int) Math.round(number));
            case FloatOption floatOption when number != null -> new OptionChangeEvent(source, floatOption, number.floatValue());
            case BooleanOption booleanOption when bool != null -> new OptionChangeEvent(source, booleanOption, bool);
            default -> null;
        };
    }
}
