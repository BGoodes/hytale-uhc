package fr.bgoodes.uhc.service.config.model;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.ExtraInfo;
import fr.bgoodes.uhc.UHC;
import org.bson.BsonValue;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Option<T> {

    private final String key;
    private volatile T value;

    private final Codec<T> codec;

    public Option(@NotNull String key, @NotNull Codec<T> codec, @NotNull T defaultValue) {
        this.key = key;
        this.codec = codec;
        this.value = copy(defaultValue);
    }

    public final @NotNull String getKey() {
        return key;
    }

    public final @NotNull T get() {
        return copy(value);
    }

    public final synchronized boolean set(@NotNull T value) {
        if (!isValid(value)) {
            return false;
        }

        this.value = copy(value);
        return true;
    }

    public final @NotNull BsonValue encode() {
        return codec.encode(value, new ExtraInfo());
    }

    public final void decode(@NotNull BsonValue bson) {
        try {
            if (!set(Objects.requireNonNull(codec.decode(bson, new ExtraInfo())))) {
                UHC.LOGGER.atWarning().log("Option %s has an invalid value, current value kept.", key);
            }
        } catch (Exception e) {
            UHC.LOGGER.atWarning().log("Option %s could not be read, current value kept.", key);
        }
    }

    public boolean isValid(@NotNull T value) {
        return true;
    }

    protected @NotNull T copy(@NotNull T value) {
        return value;
    }
}
