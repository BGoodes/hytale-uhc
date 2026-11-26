package fr.bgoodes.uhc.service.config.impl.game;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.record.RecordCodec;
import com.hypixel.hytale.server.core.inventory.ItemStack;

public record KitSlot(int section, int slot, ItemStack item) {

    public static final RecordCodec<KitSlot> CODEC = RecordCodec.builder(KitSlot.class)
            .append(new KeyedCodec<>("Section", Codec.INTEGER), KitSlot::section)
            .append(new KeyedCodec<>("Slot", Codec.INTEGER), KitSlot::slot)
            .append(new KeyedCodec<>("Item", ItemStack.CODEC), KitSlot::item)
            .build(KitSlot::new);
}
