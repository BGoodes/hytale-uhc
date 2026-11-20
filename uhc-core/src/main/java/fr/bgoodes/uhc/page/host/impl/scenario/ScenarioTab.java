package fr.bgoodes.uhc.page.host.impl.scenario;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import fr.bgoodes.uhc.page.host.model.IHostTab;
import fr.bgoodes.uhc.util.UHCMessageUtil;
import org.jetbrains.annotations.NotNull;

public class ScenarioTab implements IHostTab {

    private static final String DOCUMENT = "UHC/Host/Scenario/ScenarioTab.ui";

    @Override
    public @NotNull Message getTitle() {
        return UHCMessageUtil.tr("uhc.host.tab.scenarios");
    }

    @Override
    public @NotNull String getDocument() {
        return DOCUMENT;
    }

    @Override
    public void build(@NotNull UICommandBuilder ui, @NotNull UIEventBuilder events) {}
}
