package fr.bgoodes.uhc.util;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.InventoryUtils;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.NotNull;

public class UHCPlayerUtil {

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

            Player.setGameMode(ref, GameMode.Adventure, store);
            InventoryUtils.clear(ref, store);

            EntityStatMap statMap = store.getComponent(ref, EntityStatMap.getComponentType());

            if (statMap != null) {
                statMap.maximizeStatValue(DefaultEntityStatTypes.getHealth());
            }
        });
    }
}
