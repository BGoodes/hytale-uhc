package fr.bgoodes.uhc.service.config.impl;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.ExtraInfo;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import fr.bgoodes.uhc.service.config.model.IConfig;
import org.bson.BsonDocument;

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
            .build();

    public int invincibilityTime = 60;
    public int pvpTime = 600;
    public int borderShrinkTime = 2400;

    @Override
    public BsonDocument encode() {
        return CODEC.encode(this, new ExtraInfo());
    }

    @Override
    public void decode(BsonDocument document) {
        CODEC.decode(document, this, new ExtraInfo());
    }
}
