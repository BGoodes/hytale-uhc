package fr.bgoodes.uhc.service.config.model;

import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class UHCConfig {

    private final List<Option<?>> options = new ArrayList<>();

    protected final <T extends Option<?>> T add(@NotNull T option) {
        options.add(option);
        return option;
    }

    public final @NotNull List<Option<?>> getOptions() {
        return List.copyOf(options);
    }

    public final @Nullable Option<?> getOption(@NotNull String key) {
        for (Option<?> option : options) {
            if (option.getKey().equals(key)) {
                return option;
            }
        }

        return null;
    }

    public final @NotNull BsonDocument encode() {
        BsonDocument document = new BsonDocument();
        options.forEach(option -> document.put(option.getKey(), option.encode()));
        return document;
    }

    public final void decode(@NotNull BsonDocument document) {
        for (Option<?> option : options) {
            BsonValue value = document.get(option.getKey());

            if (value != null) {
                option.decode(value);
            }
        }
    }
}
