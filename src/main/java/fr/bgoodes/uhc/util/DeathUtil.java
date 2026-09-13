package fr.bgoodes.uhc.util;

import com.hypixel.hytale.component.AddReason;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Rotation3f;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.inventory.InventoryComponent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.modules.entity.component.HeadRotation;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.item.ItemComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.UHC;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeathUtil {

    private DeathUtil() {}


    public static void eliminate(@NotNull UUID playerUuid, @NotNull String username, @Nullable Message killerName) {
        UHC.get().getPlayerService().removeParticipant(playerUuid);
        broadcastDeathMessage(username, killerName);
    }

    public static void broadcastDeathMessage(@NotNull String username, @Nullable Message killerName) {
        Message deathMessage = MessageUtil.tr(killerName != null ? "uhc.death.killed" : "uhc.death.player")
                .param("player", Message.raw(username));

        if (killerName != null) {
            deathMessage.param("killer", killerName);
        }

        BroadcastUtil.message(deathMessage.color(Color.ORANGE).bold(true), false);
        BroadcastUtil.sound("SFX_Taiko");
    }

    public static void dropStoredInventory(@NotNull Holder<EntityStore> savedHolder, @NotNull Store<EntityStore> worldStore) {
        TransformComponent transform = savedHolder.getComponent(TransformComponent.getComponentType());
        if (transform == null) return;

        List<ItemStack> itemsToDrop = new ArrayList<>();
        for (var inventoryType : InventoryComponent.EVERYTHING) {
            InventoryComponent inventory = savedHolder.getComponent(inventoryType);
            if (inventory == null) continue;

            ItemContainer container = inventory.getInventory();
            for (short slot = 0; slot < container.getCapacity(); slot++) {
                ItemStack itemStack = container.getItemStack(slot);
                if (!ItemStack.isEmpty(itemStack)) {
                    itemsToDrop.add(itemStack);
                }
            }
        }

        if (itemsToDrop.isEmpty()) return;

        HeadRotation headRotation = savedHolder.getComponent(HeadRotation.getComponentType());
        Rotation3f rotation = headRotation != null ? headRotation.getRotation() : new Rotation3f();

        Vector3d dropPosition = new Vector3d(transform.getPosition()).add(0, 1, 0);
        Holder<EntityStore>[] drops = ItemComponent.generateItemDrops(worldStore, itemsToDrop, dropPosition, rotation);
        worldStore.addEntities(drops, AddReason.SPAWN);
    }
}
