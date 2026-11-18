package fr.bgoodes.uhc.mode.example;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.ExtraInfo;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import fr.bgoodes.uhc.service.config.model.IConfig;
import org.bson.BsonDocument;
import org.jetbrains.annotations.NotNull;

public class ExampleConfig implements IConfig {

    public static final String CONFIG_ID = "example";

    public ExampleConfig() {}

    public static final BuilderCodec<ExampleConfig> CODEC = BuilderCodec.builder(ExampleConfig.class, ExampleConfig::new)
            .append(new KeyedCodec<>("A", Codec.INTEGER),
                    (config, value) -> config.a = value,
                    (config) -> config.a).add()
            .append(new KeyedCodec<>("B", Codec.INTEGER),
                    (config, value) -> config.b = value,
                    (config) -> config.b).add()
            .append(new KeyedCodec<>("C", Codec.BOOLEAN),
                    (config, value) -> config.c = value,
                    (config) -> config.c).add()
            .build();

    public int a = 1;
    public int b = 5;
    public boolean c = true;

    @Override
    public @NotNull BsonDocument encode() {
        return CODEC.encode(this, new ExtraInfo());
    }

    @Override
    public void decode(@NotNull BsonDocument document) {
        CODEC.decode(document, this, new ExtraInfo());
    }
}
