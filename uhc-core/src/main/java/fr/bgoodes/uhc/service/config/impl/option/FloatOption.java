package fr.bgoodes.uhc.service.config.impl.option;

import com.hypixel.hytale.codec.Codec;
import org.jetbrains.annotations.NotNull;

public class FloatOption extends NumberOption<Float> {

    public FloatOption(@NotNull String key, float defaultValue, float min, float max) {
        super(key, Codec.FLOAT, defaultValue, min, max);
    }
}
