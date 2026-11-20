package fr.bgoodes.uhc.page.impl.option;

import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import fr.bgoodes.uhc.event.option.OptionChangeEvent;
import fr.bgoodes.uhc.page.HostPageData;
import fr.bgoodes.uhc.page.model.IOptionRow;
import fr.bgoodes.uhc.service.config.impl.option.FloatOption;
import fr.bgoodes.uhc.util.UHCMessageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FloatOptionRow implements IOptionRow {

    private static final String DOCUMENT = "Pages/Fields/NumberRow.ui";

    private final FloatOption option;

    public FloatOptionRow(@NotNull FloatOption option) {
        this.option = option;
    }

    @Override
    public @NotNull FloatOption getOption() {
        return option;
    }

    @Override
    public @NotNull String getDocument() {
        return DOCUMENT;
    }

    @Override
    public void build(@NotNull String selector, @NotNull UICommandBuilder ui, @NotNull UIEventBuilder events) {
        ui.set(selector + " #Label.TextSpans", UHCMessageUtil.tr("uhc.option." + option.getKey()));
        refresh(selector, ui);

        EventData data = new EventData()
                .append("Action", ACTION)
                .append("Id", option.getKey())
                .append("@Number", selector + " #Input.Value");

        events.addEventBinding(CustomUIEventBindingType.FocusLost, selector + " #Input", data, false);
        events.addEventBinding(CustomUIEventBindingType.Validating, selector + " #Input", data, false);
    }

    @Override
    public void refresh(@NotNull String selector, @NotNull UICommandBuilder ui) {
        ui.set(selector + " #Input.Value", option.get());
    }

    @Override
    public @Nullable OptionChangeEvent request(@NotNull HostPageData data, @NotNull PlayerRef source) {
        Double number = data.getNumber();
        return number == null ? null : new OptionChangeEvent(source, option, number.floatValue());
    }
}
