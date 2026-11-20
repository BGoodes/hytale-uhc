package fr.bgoodes.uhc.page.impl.page.mode;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import fr.bgoodes.uhc.page.model.IHostTab;
import fr.bgoodes.uhc.util.UHCMessageUtil;
import org.jetbrains.annotations.NotNull;

public class ModeTab implements IHostTab {

    private static final String DOCUMENT = "UHC/Host/Mode/ModeTab.ui";

    @Override
    public @NotNull Message getTitle() {
        return UHCMessageUtil.tr("uhc.host.tab.mode");
    }

    @Override
    public @NotNull String getDocument() {
        return DOCUMENT;
    }

    @Override
    public void build(@NotNull UICommandBuilder ui, @NotNull UIEventBuilder events) {}
}
