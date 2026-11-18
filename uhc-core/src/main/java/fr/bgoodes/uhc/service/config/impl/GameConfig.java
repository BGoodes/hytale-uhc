package fr.bgoodes.uhc.service.config.impl;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.ExtraInfo;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
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
            .append(new KeyedCodec<>("BorderStartRadius", Codec.INTEGER),
                    (config, value) -> config.borderStartRadius = value,
                    (config) -> config.borderStartRadius).add()
            .append(new KeyedCodec<>("BorderDamage", Codec.FLOAT),
                    (config, value) -> config.borderDamage = value,
                    (config) -> config.borderDamage).add()
            .append(new KeyedCodec<>("BorderPhases", ArrayCodec.ofBuilderCodec(BorderPhase.CODEC, BorderPhase[]::new)),
                    (config, value) -> config.borderPhases = value,
                    (config) -> config.borderPhases).add()
            .append(new KeyedCodec<>("DisconnectTimeout", Codec.INTEGER),
                    (config, value) -> config.disconnectTimeout = value,
                    (config) -> config.disconnectTimeout).add()
            .build();

    public int invincibilityTime = 60;
    public int pvpTime = 600;

    public int borderStartRadius = 1600;
    public float borderDamage = 10;
    public BorderPhase[] borderPhases = { new BorderPhase(2400, 100, 1) };

    public int disconnectTimeout = 600;

    @Override
    public @NotNull BsonDocument encode() {
        return CODEC.encode(this, new ExtraInfo());
    }

    @Override
    public void decode(@NotNull BsonDocument document) {
        CODEC.decode(document, this, new ExtraInfo());
    }
}
