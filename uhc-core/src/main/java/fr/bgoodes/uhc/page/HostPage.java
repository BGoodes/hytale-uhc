package fr.bgoodes.uhc.page;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.page.impl.page.core.CoreTab;
import fr.bgoodes.uhc.page.impl.page.mode.ModeTab;
import fr.bgoodes.uhc.page.impl.page.preset.PresetTab;
import fr.bgoodes.uhc.page.impl.page.scenario.ScenarioTab;
import fr.bgoodes.uhc.page.model.IHostTab;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HostPage extends InteractiveCustomUIPage<HostPageData> {

    private static final String PAGE = "UHC/Host/HostPage.ui";
    private static final String TAB_BUTTON = "UHC/Host/TabButton.ui";

    private final List<IHostTab> tabs;
    private int selectedTab = 0;

    public HostPage(@NotNull PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismiss, HostPageData.CODEC);
        this.tabs = List.of(new CoreTab(playerRef), new ModeTab(), new ScenarioTab(), new PresetTab());
    }

    @Override
    public void build(
            @NotNull Ref<EntityStore> ref,
            @NotNull UICommandBuilder ui,
            @NotNull UIEventBuilder events,
            @NotNull Store<EntityStore> store
    ) {
        ui.append(PAGE);

        for (int i = 0; i < tabs.size(); i++) {
            String selector = "#TabButtons[" + i + "]";

            ui.append("#TabButtons", TAB_BUTTON);
            ui.set(selector + ".TextSpans", tabs.get(i).getTitle());
            ui.set(selector + ".Disabled", i == selectedTab);

            events.addEventBinding(CustomUIEventBindingType.Activating, selector, EventData.of("Tab", String.valueOf(i)));
        }

        showTab(ui, events);
    }

    @Override
    public void handleDataEvent(@NotNull Ref<EntityStore> ref, @NotNull Store<EntityStore> store, @NotNull HostPageData data) {
        UICommandBuilder ui = new UICommandBuilder();
        UIEventBuilder events = new UIEventBuilder();

        if (data.getTab() != null) {
            selectTab(Integer.parseInt(data.getTab()), ui, events);
        } else {
            tabs.get(selectedTab).handle(data, ui, events);
        }

        sendUpdate(ui, events, false);
    }

    private void selectTab(int index, UICommandBuilder ui, UIEventBuilder events) {
        if (index == selectedTab || index < 0 || index >= tabs.size()) {
            return;
        }

        ui.set("#TabButtons[" + selectedTab + "].Disabled", false);
        ui.set("#TabButtons[" + index + "].Disabled", true);

        selectedTab = index;
        showTab(ui, events);
    }

    private void showTab(UICommandBuilder ui, UIEventBuilder events) {
        IHostTab tab = tabs.get(selectedTab);

        ui.clear("#TabContent");
        ui.append("#TabContent", tab.getDocument());
        tab.build(ui, events);
    }
}
