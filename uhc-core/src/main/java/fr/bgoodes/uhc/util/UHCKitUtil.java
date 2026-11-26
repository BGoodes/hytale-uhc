package fr.bgoodes.uhc.util;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.inventory.InventoryComponent;
import com.hypixel.hytale.server.core.inventory.InventoryUtils;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.service.config.impl.game.KitSlot;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UHCKitUtil {

    private static final int[] SECTIONS = {
            InventoryComponent.HOTBAR_SECTION_ID,
            InventoryComponent.STORAGE_SECTION_ID,
            InventoryComponent.ARMOR_SECTION_ID,
            InventoryComponent.UTILITY_SECTION_ID
    };

    private UHCKitUtil() {}

    public static void give(@NotNull PlayerRef playerRef, KitSlot @NotNull [] kit) {
        Ref<EntityStore> ref = playerRef.getReference();

        if (ref == null || !ref.isValid()) {
            return;
        }

        Store<EntityStore> store = ref.getStore();

        store.getExternalData().getWorld().execute(() -> {
            if (ref.isValid()) {
                put(ref, store, kit);
            }
        });
    }

    public static void put(@NotNull Ref<EntityStore> ref, @NotNull Store<EntityStore> store, KitSlot @NotNull [] kit) {
        for (KitSlot slot : kit) {
            ItemContainer container = InventoryUtils.getSectionById(ref, slot.section(), store);

            if (container != null && slot.slot() < container.getCapacity()) {
                container.setItemStackForSlot((short) slot.slot(), slot.item());
            }
        }
    }

    public static KitSlot @NotNull [] read(@NotNull Ref<EntityStore> ref, @NotNull Store<EntityStore> store) {
        List<KitSlot> kit = new ArrayList<>();

        for (int section : SECTIONS) {
            ItemContainer container = InventoryUtils.getSectionById(ref, section, store);

            if (container == null) {
                continue;
            }

            for (short slot = 0; slot < container.getCapacity(); slot++) {
                ItemStack item = container.getItemStack(slot);

                if (item != null && !item.isEmpty()) {
                    kit.add(new KitSlot(section, slot, item));
                }
            }
        }

        return kit.toArray(KitSlot[]::new);
    }
}
