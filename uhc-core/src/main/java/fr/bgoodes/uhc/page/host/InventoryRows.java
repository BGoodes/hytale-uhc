package fr.bgoodes.uhc.page.host;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.protocol.packets.window.WindowType;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.windows.Window;
import com.hypixel.hytale.server.core.inventory.InventoryUtils;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.event.option.OptionChangeEvent;
import fr.bgoodes.uhc.service.flag.impl.UHCFlag;
import fr.bgoodes.uhc.service.config.impl.game.KitOption;
import fr.bgoodes.uhc.service.config.impl.game.KitSlot;
import fr.bgoodes.uhc.util.UHCKitUtil;
import fr.bgoodes.uhc.util.UHCMessageUtil;
import fr.bgoodes.uhc.util.UHCPlayerUtil;
import fr.bgoodes.uhc.util.UHCSoundUtil;

import java.util.function.Supplier;

final class InventoryRows {

    private static final String REFUSED_SOUND = "SFX_Bow_No_Ammo";

    private InventoryRows() {}

    static int append(KitOption option, String list, int index, UICommandBuilder ui, UIEventBuilder events) {
        String row = list + "[" + index + "]";

        ui.append(list, "Host/EditRow.ui");
        ui.set(row + " #Label.TextSpans", UHCMessageUtil.tr("uhc.option." + option.getKey()));

        events.addEventBinding(CustomUIEventBindingType.Activating, row + " #EditButton",
                new EventData().append("Action", "EditItems").append("Id", option.getKey()), false);

        return 1;
    }

    static boolean edit(KitOption option, PlayerRef playerRef, Ref<EntityStore> ref, Store<EntityStore> store) {
        Player player = store.getComponent(ref, Player.getComponentType());
        Supplier<? extends Window> crafting = Window.CLIENT_REQUESTABLE_WINDOW_TYPES.get(WindowType.PocketCrafting);

        if (!UHC.get().getFlagService().isEnabled(UHCFlag.CONFIG) || player == null || crafting == null) {
            UHCSoundUtil.send(playerRef, REFUSED_SOUND);
            return false;
        }

        Window window = crafting.get();
        window.registerCloseEvent(_ -> save(option, playerRef));

        InventoryUtils.clear(ref, store);
        Player.setGameMode(ref, GameMode.Creative, store);
        UHCKitUtil.put(ref, store, option.get());

        player.getPageManager().setPageWithWindows(ref, store, Page.Inventory, true, window);
        return true;
    }

    private static void save(KitOption option, PlayerRef playerRef) {
        Ref<EntityStore> ref = playerRef.getReference();

        if (ref == null || !ref.isValid()) {
            return;
        }

        KitSlot[] kit = UHCKitUtil.read(ref, ref.getStore());
        UHCPlayerUtil.reset(playerRef);

        OptionChangeEvent event = HytaleServer.get().getEventBus()
                .dispatchFor(OptionChangeEvent.class)
                .dispatch(new OptionChangeEvent(playerRef, option, kit));

        if (event.isCancelled()) {
            UHCSoundUtil.send(playerRef, REFUSED_SOUND);
        }

        if (UHC.get().getFlagService().isEnabled(UHCFlag.CONFIG)) {
            reopenHostPage(playerRef, ref);
        }
    }

    private static void reopenHostPage(PlayerRef playerRef, Ref<EntityStore> ref) {
        Store<EntityStore> store = ref.getStore();

        store.getExternalData().getWorld().execute(() -> {
            Player player = ref.isValid() ? store.getComponent(ref, Player.getComponentType()) : null;

            if (player != null) {
                player.getPageManager().openCustomPage(ref, store, new HostPage(playerRef));
            }
        });
    }
}
