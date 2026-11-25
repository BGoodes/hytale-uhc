package fr.bgoodes.uhc.service.config.impl.option;

import com.hypixel.hytale.codec.codecs.EnumCodec;
import fr.bgoodes.uhc.service.config.model.Option;
import org.jetbrains.annotations.NotNull;

public class EnumOption<E extends Enum<E>> extends Option<E> {

    public EnumOption(@NotNull String key, @NotNull Class<E> type, @NotNull E defaultValue) {
        super(key, new EnumCodec<>(type), defaultValue);
    }
}
