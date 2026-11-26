package fr.bgoodes.uhc.service.config.impl.game;

import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import fr.bgoodes.uhc.service.config.model.Option;
import org.jetbrains.annotations.NotNull;

public class KitOption extends Option<KitSlot[]> {

    public KitOption(@NotNull String key, @NotNull KitSlot... value) {
        super(key, new ArrayCodec<>(KitSlot.CODEC, KitSlot[]::new), value);
    }

    @Override
    public boolean isValid(KitSlot @NotNull [] slots) {
        for (KitSlot slot : slots) {
            if (slot.slot() < 0 || slot.item() == null || slot.item().isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected KitSlot @NotNull [] copy(KitSlot @NotNull [] slots) {
        return slots.clone();
    }
}
