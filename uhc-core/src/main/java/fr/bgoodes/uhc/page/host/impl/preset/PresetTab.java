package fr.bgoodes.uhc.page.host.impl.preset;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import fr.bgoodes.uhc.page.host.model.IHostTab;
import fr.bgoodes.uhc.util.UHCMessageUtil;
import org.jetbrains.annotations.NotNull;

public class PresetTab implements IHostTab {

    private static final String DOCUMENT = "UHC/Host/Preset/PresetTab.ui";

    @Override
    public @NotNull Message getTitle() {
        return UHCMessageUtil.tr("uhc.host.tab.presets");
    }

    @Override
    public @NotNull String getDocument() {
        return DOCUMENT;
    }

    @Override
    public void build(@NotNull UICommandBuilder ui, @NotNull UIEventBuilder events) {}
}
