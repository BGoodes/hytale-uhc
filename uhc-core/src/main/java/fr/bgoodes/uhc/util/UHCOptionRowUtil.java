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
                ui.append(list, "Host/NumberRow.ui");
                ui.set(input + ".Value", String.valueOf(intOption.get()));
                data.append("@Text", input + ".Value");
            }
            case FloatOption floatOption -> {
                ui.append(list, "Host/NumberRow.ui");
                ui.set(input + ".Value", String.valueOf(floatOption.get()));
                data.append("@Text", input + ".Value");
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
            @Nullable String text,
            @Nullable Boolean bool,
            @NotNull PlayerRef source
    ) {
        try {
            return switch (option) {
                case IntOption intOption when text != null -> new OptionChangeEvent(source, intOption, Integer.parseInt(text.trim()));
                case FloatOption floatOption when text != null -> new OptionChangeEvent(source, floatOption, Float.parseFloat(text.trim().replace(',', '.')));
                case BooleanOption booleanOption when bool != null -> new OptionChangeEvent(source, booleanOption, bool);
                default -> null;
            };
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
