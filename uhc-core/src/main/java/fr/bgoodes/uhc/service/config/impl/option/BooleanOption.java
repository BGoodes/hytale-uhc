package fr.bgoodes.uhc.service.config.impl.option;

import com.hypixel.hytale.codec.Codec;
import fr.bgoodes.uhc.service.config.model.Option;
import org.jetbrains.annotations.NotNull;

public class BooleanOption extends Option<Boolean> {

    public BooleanOption(@NotNull String key, boolean defaultValue) {
        super(key, Codec.BOOLEAN, defaultValue);
    }
}
