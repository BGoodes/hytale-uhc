package fr.bgoodes.uhc.page.host;

import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import fr.bgoodes.uhc.event.option.OptionChangeEvent;
import fr.bgoodes.uhc.service.config.impl.game.BorderPhase;
import fr.bgoodes.uhc.service.config.impl.game.BorderPhasesOption;
import fr.bgoodes.uhc.util.UHCMessageUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class PhaseRows {

    private PhaseRows() {}

    static int append(
            BorderPhasesOption option,
            String list, int index,
            UICommandBuilder ui,
            UIEventBuilder events
    ) {
        BorderPhase[] phases = option.get();

        ui.append(list, "Host/Phase/Title.ui");
        ui.set(list + "[" + index + "] #PanelTitle.TextSpans", UHCMessageUtil.tr("uhc.page.host.phase.title"));
        index++;

        for (int i = 0; i < phases.length; i++) {
            String row = list + "[" + (index + i) + "]";

            ui.append(list, "Host/Phase/Row.ui");
            ui.set(row + " #Label.TextSpans", UHCMessageUtil.tr("uhc.page.host.phase.name").param("index", i + 1));
            ui.set(row + " #TimeLabel.TextSpans", UHCMessageUtil.tr("uhc.page.host.phase.time"));
            ui.set(row + " #RadiusLabel.TextSpans", UHCMessageUtil.tr("uhc.page.host.phase.radius"));
            ui.set(row + " #SpeedLabel.TextSpans", UHCMessageUtil.tr("uhc.page.host.phase.speed"));
            ui.set(row + " #RemoveButton.TextSpans", UHCMessageUtil.tr("uhc.page.host.phase.remove"));
            ui.set(row + " #Time.Value", String.valueOf(phases[i].time()));
            ui.set(row + " #Radius.Value", String.valueOf(phases[i].radius()));
            ui.set(row + " #Speed.Value", String.valueOf(phases[i].speed()));

            bindText(row + " #Time", data(option, i, "Time"), events);
            bindText(row + " #Radius", data(option, i, "Radius"), events);
            bindText(row + " #Speed", data(option, i, "Speed"), events);

            events.addEventBinding(CustomUIEventBindingType.Activating, row + " #RemoveButton", data(option, i, "Remove"), false);
        }

        ui.append(list, "Host/Phase/AddButton.ui");
        ui.set(list + "[" + (index + phases.length) + "].TextSpans", UHCMessageUtil.tr("uhc.page.host.phase.add"));
        events.addEventBinding(CustomUIEventBindingType.Activating, list + "[" + (index + phases.length) + "]",
                data(option, phases.length, "Add"), false);

        return phases.length + 2;
    }

    static @Nullable OptionChangeEvent request(
            BorderPhasesOption option,
            String phase,
            String field,
            String text,
            PlayerRef source
    ) {
        List<BorderPhase> phases = new ArrayList<>(Arrays.asList(option.get()));

        try {
            int index = Integer.parseInt(phase);

            switch (field) {
                case "Add" -> phases.add(next(phases));
                case "Remove" -> phases.remove(index);
                default -> {
                    BorderPhase edited = edit(phases.get(index), field, text);

                    if (edited == null) {
                        return null;
                    }

                    phases.set(index, edited);
                }
            }
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return null;
        }

        return new OptionChangeEvent(source, option, phases.toArray(BorderPhase[]::new));
    }

    private static @Nullable BorderPhase edit(BorderPhase phase, String field, @Nullable String text) {
        if (text == null) {
            return null;
        }

        String value = text.trim();

        return switch (field) {
            case "Time" -> new BorderPhase(Integer.parseInt(value), phase.radius(), phase.speed());
            case "Radius" -> new BorderPhase(phase.time(), Integer.parseInt(value), phase.speed());
            case "Speed" -> new BorderPhase(phase.time(), phase.radius(), Double.parseDouble(value.replace(',', '.')));
            default -> null;
        };
    }

    private static BorderPhase next(List<BorderPhase> phases) {
        if (phases.isEmpty()) {
            return new BorderPhase(600, 500, 1);
        }

        BorderPhase last = phases.getLast();
        return new BorderPhase(last.time() + 600, Math.max(1, last.radius() / 2), last.speed());
    }

    private static EventData data(BorderPhasesOption option, int index, String field) {
        return new EventData()
                .append("Action", "Option")
                .append("Id", option.getKey())
                .append("Phase", String.valueOf(index))
                .append("Field", field);
    }

    private static void bindText(String input, EventData data, UIEventBuilder events) {
        data.append("@Text", input + ".Value");

        events.addEventBinding(CustomUIEventBindingType.FocusLost, input, data, false);
        events.addEventBinding(CustomUIEventBindingType.Validating, input, data, false);
    }
}
