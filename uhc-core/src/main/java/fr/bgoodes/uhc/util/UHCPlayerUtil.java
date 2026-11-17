package fr.bgoodes.uhc.util;

import com.hypixel.hytale.component.AddReason;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Rotation3f;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.InventoryComponent;
import com.hypixel.hytale.server.core.inventory.InventoryUtils;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.component.HeadRotation;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.gamemode.GameModeTypes;
import com.hypixel.hytale.server.core.modules.entity.item.ItemComponent;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.UHC;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public static void dropInventory(@NotNull PlayerRef playerRef, @NotNull Vector3d position) {
        Ref<EntityStore> ref = playerRef.getReference();

        if (ref == null || !ref.isValid()) {
            return;
        }

        Store<EntityStore> store = ref.getStore();

        store.getExternalData().getWorld().execute(() -> {
            if (!ref.isValid()) {
                return;
            }

            HeadRotation headRotation = store.getComponent(ref, HeadRotation.getComponentType());
            Rotation3f rotation = headRotation != null ? headRotation.getRotation() : new Rotation3f();

            spawnDrops(store, InventoryUtils.dropAllItemStacks(ref, store), position, rotation);
        });
    }

    public static void dropStoredInventory(@NotNull UUID uuid, @NotNull World world) {
        Universe.get().getPlayerStorage().update(uuid, holder -> {
            TransformComponent transform = holder.getComponent(TransformComponent.getComponentType());

            if (transform == null) {
                return;
            }

            List<ItemStack> items = new ArrayList<>();

            for (var inventoryType : InventoryComponent.EVERYTHING) {
                InventoryComponent inventory = holder.getComponent(inventoryType);

                if (inventory != null) {
                    items.addAll(inventory.getInventory().dropAllItemStacks());
                }
            }

            Vector3d position = new Vector3d(transform.getPosition());

            world.execute(() -> spawnDrops(world.getEntityStore().getStore(), items, position, new Rotation3f()));
        });
    }

    private static void spawnDrops(
            Store<EntityStore> store,
            List<ItemStack> items,
            Vector3d position,
            Rotation3f rotation
    ) {
        if (items.isEmpty()) {
            return;
        }

        Holder<EntityStore>[] drops = ItemComponent.generateItemDrops(
                store, items, new Vector3d(position).add(0, 1, 0), rotation
        );

        store.addEntities(drops, AddReason.SPAWN);
    }
}
