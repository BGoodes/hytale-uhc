package fr.bgoodes.uhc.page;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.Value;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.event.option.OptionChangeEvent;
import fr.bgoodes.uhc.service.config.impl.game.GameConfig;
import fr.bgoodes.uhc.service.config.model.Option;
import fr.bgoodes.uhc.util.UHCMessageUtil;
import fr.bgoodes.uhc.util.UHCOptionRowUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HostPage extends InteractiveCustomUIPage<HostPage.Data> {

    private static final String DOCUMENT = "UHC/Host/HostPage.ui";

    private static final String CATEGORY_BUTTON = "UHC/Host/CategoryButton.ui";
    private static final Value<String> CATEGORY_STYLE = Value.ref(CATEGORY_BUTTON, "LabelStyle");
    private static final Value<String> CATEGORY_SELECTED_STYLE = Value.ref(CATEGORY_BUTTON, "SelectedLabelStyle");

    private record Category(Message name, List<Option<?>> options) {}
    private final List<Category> categories;
    private int selectedCategory = 0;

    public HostPage(@NotNull PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismiss, Data.CODEC);
        this.categories = categories(UHC.get().getGameConfig());
    }

    @Override
    public void build(
            @NotNull Ref<EntityStore> ref,
            @NotNull UICommandBuilder ui,
            @NotNull UIEventBuilder events,
            @NotNull Store<EntityStore> store
    ) {
        ui.append(DOCUMENT);

        for (int i = 0; i < categories.size(); i++) {
            String button = "#CategoryList[" + i + "]";

            ui.append("#CategoryList", CATEGORY_BUTTON);
            ui.set(button + ".TextSpans", categories.get(i).name());
            ui.set(button + ".Style", i == selectedCategory ? CATEGORY_SELECTED_STYLE : CATEGORY_STYLE);

            events.addEventBinding(CustomUIEventBindingType.Activating, button,
                    new EventData().append("Action", "Category").append("Id", String.valueOf(i)));
        }

        buildOptions(ui, events);
    }

    @Override
    public void handleDataEvent(@NotNull Ref<EntityStore> ref, @NotNull Store<EntityStore> store, @NotNull Data data) {
        UICommandBuilder ui = new UICommandBuilder();
        UIEventBuilder events = new UIEventBuilder();

        if ("Category".equals(data.action) && data.id != null) {
            selectCategory(Integer.parseInt(data.id), ui, events);
        } else if ("Option".equals(data.action)) {
            changeOption(data, ui, events);
        }

        sendUpdate(ui, events, false);
    }

    private void buildOptions(UICommandBuilder ui, UIEventBuilder events) {
        Category category = categories.get(selectedCategory);

        ui.set("#CategoryTitle.TextSpans", category.name());
        ui.clear("#OptionList");

        int row = 0;

        for (Option<?> option : category.options()) {
            if (UHCOptionRowUtil.append(option, "#OptionList", row, ui, events)) {
                row++;
            }
        }
    }

    private void selectCategory(int index, UICommandBuilder ui, UIEventBuilder events) {
        if (index == selectedCategory || index < 0 || index >= categories.size()) {
            return;
        }

        ui.set("#CategoryList[" + selectedCategory + "].Style", CATEGORY_STYLE);
        ui.set("#CategoryList[" + index + "].Style", CATEGORY_SELECTED_STYLE);

        selectedCategory = index;
        buildOptions(ui, events);
    }

    private void changeOption(Data data, UICommandBuilder ui, UIEventBuilder events) {
        for (Option<?> option : categories.get(selectedCategory).options()) {
            if (!option.getKey().equals(data.id)) {
                continue;
            }

            OptionChangeEvent event = UHCOptionRowUtil.request(option, data.number, data.bool, playerRef);

            if (event != null) {
                HytaleServer.get().getEventBus().dispatchFor(OptionChangeEvent.class).dispatch(event);
            }

            buildOptions(ui, events);
            return;
        }
    }

    private static List<Category> categories(GameConfig config) {
        return List.of(
                new Category(UHCMessageUtil.tr("uhc.host.category.time"), List.of(
                        config.invincibilityTime, config.pvpTime
                )),

                new Category(UHCMessageUtil.tr("uhc.host.category.border"), List.of(
                        config.borderStartRadius, config.borderDamage
                )),

                new Category(UHCMessageUtil.tr("uhc.host.category.other"), List.of(
                        config.disconnectTimeout
                ))
        );
    }

    public static class Data {

        public static final BuilderCodec<Data> CODEC = BuilderCodec.builder(Data.class, Data::new)
                .append(new KeyedCodec<>("Action", Codec.STRING, false),
                        (data, value) -> data.action = value,
                        (data) -> data.action).add()
                .append(new KeyedCodec<>("Id", Codec.STRING, false),
                        (data, value) -> data.id = value,
                        (data) -> data.id).add()
                .append(new KeyedCodec<>("@Number", Codec.DOUBLE, false),
                        (data, value) -> data.number = value,
                        (data) -> data.number).add()
                .append(new KeyedCodec<>("@Bool", Codec.BOOLEAN, false),
                        (data, value) -> data.bool = value,
                        (data) -> data.bool).add()
                .build();

        private @Nullable String action;
        private @Nullable String id;
        private @Nullable Double number;
        private @Nullable Boolean bool;
    }
}
