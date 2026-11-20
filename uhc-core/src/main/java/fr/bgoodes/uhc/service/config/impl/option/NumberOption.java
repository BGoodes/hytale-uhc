package fr.bgoodes.uhc.service.config.impl.option;

import com.hypixel.hytale.codec.Codec;
import fr.bgoodes.uhc.service.config.model.Option;
import org.jetbrains.annotations.NotNull;

public abstract class NumberOption<T extends Number & Comparable<T>> extends Option<T> {

    private final T min;
    private final T max;

    protected NumberOption(
            @NotNull String key,
            @NotNull Codec<T> codec,
            @NotNull T defaultValue,
            @NotNull T min,
            @NotNull T max
    ) {
        super(key, codec, defaultValue);
        this.min = min;
        this.max = max;
    }

    public @NotNull T getMin() {
        return min;
    }

    public @NotNull T getMax() {
        return max;
    }

    @Override
    public boolean isValid(@NotNull T value) {
        return value.compareTo(min) >= 0 && value.compareTo(max) <= 0 && super.isValid(value);
    }
}
