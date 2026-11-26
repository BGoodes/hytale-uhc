package fr.bgoodes.uhc.page.host;

import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import fr.bgoodes.uhc.event.option.OptionChangeEvent;
import fr.bgoodes.uhc.service.config.impl.game.BorderPhasesOption;
import fr.bgoodes.uhc.service.config.impl.game.KitOption;
import fr.bgoodes.uhc.service.config.impl.option.BooleanOption;
import fr.bgoodes.uhc.service.config.impl.option.FloatOption;
import fr.bgoodes.uhc.service.config.impl.option.IntOption;
import fr.bgoodes.uhc.service.config.model.Option;
import fr.bgoodes.uhc.util.UHCMessageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class OptionRows {

    private OptionRows() {}

    static int append(
            Option<?> option,
            String list, int index,
            UICommandBuilder ui,
            UIEventBuilder events
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

            case BorderPhasesOption phasesOption -> {
                return PhaseRows.append(phasesOption, list, index, ui, events);
            }

            case KitOption kitOption -> {
                return InventoryRows.append(kitOption, list, index, ui, events);
            }
            
            default -> {
                return 0;
            }
        }

        ui.set(row + " #Label.TextSpans", UHCMessageUtil.tr("uhc.option." + option.getKey()));

        if (option instanceof BooleanOption) {
            events.addEventBinding(CustomUIEventBindingType.ValueChanged, input, data, false);
        } else {
            events.addEventBinding(CustomUIEventBindingType.FocusLost, input, data, false);
            events.addEventBinding(CustomUIEventBindingType.Validating, input, data, false);
        }

        return 1;
    }

    public static @Nullable OptionChangeEvent request(
            @NotNull Option<?> option,
            @Nullable String text,
            @Nullable Boolean bool,
            @Nullable String phase,
            @Nullable String field,
            @NotNull PlayerRef source
    ) {
        try {
            return switch (option) {
                case IntOption intOption when text != null -> new OptionChangeEvent(source, intOption, Integer.parseInt(text.trim()));
                case FloatOption floatOption when text != null -> new OptionChangeEvent(source, floatOption, Float.parseFloat(text.trim().replace(',', '.')));
                case BooleanOption booleanOption when bool != null -> new OptionChangeEvent(source, booleanOption, bool);
                case BorderPhasesOption phasesOption when phase != null && field != null -> PhaseRows.request(phasesOption, phase, field, text, source);
                default -> null;
            };
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
