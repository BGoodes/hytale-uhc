package fr.bgoodes.uhc.util;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.InventoryUtils;
import com.hypixel.hytale.server.core.modules.entity.gamemode.GameModeTypes;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.UHC;
import org.jetbrains.annotations.NotNull;

public class UHCPlayerUtil {

    public static final String SPECTATOR_GAME_MODE_TYPE_ID = "Spectator";

    private UHCPlayerUtil() {}

    public static void reset(@NotNull PlayerRef playerRef) {
        Ref<EntityStore> ref = playerRef.getReference();

        if (ref == null || !ref.isValid()) {
            return;
        }

        Store<EntityStore> store = ref.getStore();

        store.getExternalData().getWorld().execute(() -> {
            if (!ref.isValid()) {
                return;
            }

            GameModeTypes.exit(ref, store);
            Player.setGameMode(ref, GameMode.Adventure, store);
            InventoryUtils.clear(ref, store);

            EntityStatMap statMap = store.getComponent(ref, EntityStatMap.getComponentType());

            if (statMap != null) {
                statMap.maximizeStatValue(DefaultEntityStatTypes.getHealth());
            }
        });
    }

    public static void setSpectator(@NotNull PlayerRef playerRef) {
        Ref<EntityStore> ref = playerRef.getReference();

        if (ref == null || !ref.isValid()) {
            return;
        }

        Store<EntityStore> store = ref.getStore();

        store.getExternalData().getWorld().execute(() -> {
            if (!ref.isValid()) {
                return;
            }

            InventoryUtils.clear(ref, store);
            GameModeTypes.enter(ref, store, SPECTATOR_GAME_MODE_TYPE_ID);
        });

        UHC.get().getPermissionService().addSpectator(playerRef.getUuid());
        UHCTeleportUtil.teleport(playerRef, UHC.get().getWorldService().getGameWorld(), UHCLobbyUtil.LOBBY_SPAWN);
    }
}
