package fr.bgoodes.uhc.service.config.impl.option;

import com.hypixel.hytale.codec.Codec;
import org.jetbrains.annotations.NotNull;

public class IntOption extends NumberOption<Integer> {

    public IntOption(@NotNull String key, int defaultValue, int min, int max) {
        super(key, Codec.INTEGER, defaultValue, min, max);
    }
}
