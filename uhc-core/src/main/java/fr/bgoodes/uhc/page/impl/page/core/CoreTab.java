package fr.bgoodes.uhc.page.impl.page.core;

import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.ui.Value;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.event.option.OptionChangeEvent;
import fr.bgoodes.uhc.page.HostPageData;
import fr.bgoodes.uhc.page.model.IHostTab;
import fr.bgoodes.uhc.page.impl.option.OptionRows;
import fr.bgoodes.uhc.page.model.IOptionRow;
import fr.bgoodes.uhc.service.config.impl.game.GameConfig;
import fr.bgoodes.uhc.util.UHCMessageUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class CoreTab implements IHostTab {

    private static final String DOCUMENT = "UHC/Host/Core/CoreTab.ui";
    private static final String CATEGORY_BUTTON = "UHC/Host/Core/CategoryButton.ui";

    private static final Value<String> CATEGORY_STYLE = Value.ref(CATEGORY_BUTTON, "LabelStyle");
    private static final Value<String> CATEGORY_SELECTED_STYLE = Value.ref(CATEGORY_BUTTON, "SelectedLabelStyle");

    private final PlayerRef playerRef;
    private final List<CoreCategory> categories;

    private int selectedCategory = 0;
    private List<IOptionRow> rows = List.of();

    public CoreTab(@NotNull PlayerRef playerRef) {
        this.playerRef = playerRef;
        this.categories = categories(UHC.get().getGameConfig());
    }

    @Override
    public @NotNull Message getTitle() {
        return UHCMessageUtil.tr("uhc.host.tab.core");
    }

    @Override
    public @NotNull String getDocument() {
        return DOCUMENT;
    }

    @Override
    public void build(@NotNull UICommandBuilder ui, @NotNull UIEventBuilder events) {
        for (int i = 0; i < categories.size(); i++) {
            String selector = "#CategoryList[" + i + "]";

            ui.append("#CategoryList", CATEGORY_BUTTON);
            ui.set(selector + ".TextSpans", categories.get(i).name());

            if (i == selectedCategory) {
                ui.set(selector + ".Style", CATEGORY_SELECTED_STYLE);
            }

            events.addEventBinding(CustomUIEventBindingType.Activating, selector,
                    new EventData().append("Action", "Category").append("Id", String.valueOf(i)));
        }

        buildRows(ui, events);
    }

    @Override
    public void handle(@NotNull HostPageData data, @NotNull UICommandBuilder ui, @NotNull UIEventBuilder events) {
        if ("Category".equals(data.getAction()) && data.getId() != null) {
            selectCategory(Integer.parseInt(data.getId()), ui, events);
        } else if (IOptionRow.ACTION.equals(data.getAction())) {
            changeOption(data, ui);
        }
    }

    private void selectCategory(int index, UICommandBuilder ui, UIEventBuilder events) {
        if (index == selectedCategory || index < 0 || index >= categories.size()) {
            return;
        }

        ui.set("#CategoryList[" + selectedCategory + "].Style", CATEGORY_STYLE);
        ui.set("#CategoryList[" + index + "].Style", CATEGORY_SELECTED_STYLE);

        selectedCategory = index;
        buildRows(ui, events);
    }

    private void buildRows(UICommandBuilder ui, UIEventBuilder events) {
        CoreCategory category = categories.get(selectedCategory);
        this.rows = category.options().stream().map(OptionRows::create).filter(Objects::nonNull).toList();

        ui.set("#CategoryTitle.TextSpans", category.name());
        ui.clear("#OptionList");

        for (int i = 0; i < this.rows.size(); i++) {
            IOptionRow row = this.rows.get(i);

            ui.append("#OptionList", row.getDocument());
            row.build("#OptionList[" + i + "]", ui, events);
        }
    }

    private void changeOption(HostPageData data, UICommandBuilder ui) {
        for (int i = 0; i < this.rows.size(); i++) {
            IOptionRow row = this.rows.get(i);

            if (!row.getOption().getKey().equals(data.getId())) {
                continue;
            }

            OptionChangeEvent event = row.request(data, playerRef);

            if (event != null) {
                HytaleServer.get().getEventBus().dispatchFor(OptionChangeEvent.class).dispatch(event);
            }

            row.refresh("#OptionList[" + i + "]", ui);
            return;
        }
    }

    private static List<CoreCategory> categories(GameConfig config) {
        return List.of(
                new CoreCategory(UHCMessageUtil.tr("uhc.host.core.category.time"), List.of(
                        config.invincibilityTime, config.pvpTime
                )),

                new CoreCategory(UHCMessageUtil.tr("uhc.host.core.category.border"), List.of(
                        config.borderStartRadius, config.borderDamage
                )),

                new CoreCategory(UHCMessageUtil.tr("uhc.host.core.category.other"), List.of(
                        config.disconnectTimeout
                ))
        );
    }
}
