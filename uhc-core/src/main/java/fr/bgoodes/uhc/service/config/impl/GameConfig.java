package fr.bgoodes.uhc.service.config.impl;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.ExtraInfo;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import fr.bgoodes.uhc.service.config.model.IConfig;
import org.bson.BsonDocument;
import org.jetbrains.annotations.NotNull;

public class GameConfig implements IConfig {

    public static final String CONFIG_ID = "uhc";

    public static final BuilderCodec<GameConfig> CODEC = BuilderCodec.builder(GameConfig.class, GameConfig::new)
            .append(new KeyedCodec<>("InvincibilityTime", Codec.INTEGER),
                    (config, value) -> config.invincibilityTime = value,
                    (config) -> config.invincibilityTime).add()
            .append(new KeyedCodec<>("PvpTime", Codec.INTEGER),
                    (config, value) -> config.pvpTime = value,
                    (config) -> config.pvpTime).add()
            .append(new KeyedCodec<>("BorderShrinkTime", Codec.INTEGER),
                    (config, value) -> config.borderShrinkTime = value,
                    (config) -> config.borderShrinkTime).add()
            .append(new KeyedCodec<>("BorderRadius", Codec.INTEGER),
                    (config, value) -> config.borderRadius = value,
                    (config) -> config.borderRadius).add()
            .build();

    public int invincibilityTime = 60;
    public int pvpTime = 600;
    public int borderShrinkTime = 2400;
    public int borderRadius = 500;

    @Override
    public @NotNull BsonDocument encode() {
        return CODEC.encode(this, new ExtraInfo());
    }

    @Override
    public void decode(@NotNull BsonDocument document) {
        CODEC.decode(document, this, new ExtraInfo());
    }
}
