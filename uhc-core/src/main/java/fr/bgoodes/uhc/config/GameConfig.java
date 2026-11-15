package fr.bgoodes.uhc.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class GameConfig {

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
}
