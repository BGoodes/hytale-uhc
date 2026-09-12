package fr.bgoodes.uhc;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class UHCConfig {

    public static final BuilderCodec<UHCConfig> CODEC = BuilderCodec.builder(UHCConfig.class, UHCConfig::new)

            .append(new KeyedCodec<>("PregenerateWorld", Codec.BOOLEAN),
                    (config, value) -> config.pregenerateWorld = value, //setter
                    (config) -> config.pregenerateWorld).add() // getter

            .append(new KeyedCodec<>("ProximityChat", Codec.BOOLEAN),
                    (config, value) -> config.proximityChat = value,
                    (config) -> config.proximityChat).add()

            .append(new KeyedCodec<>("ProximityChatDistance", Codec.INTEGER),
                    (config, value) -> config.proximityChatDistance = value,
                    (config) -> config.proximityChatDistance).add()

            .append(new KeyedCodec<>("InvincibilityTime", Codec.INTEGER),
                    (config, value) -> config.invincibilityTime = value,
                    (config) -> config.invincibilityTime).add()

            .append(new KeyedCodec<>("PvpTime", Codec.INTEGER),
                    (config, value) -> config.pvpTime = value,
                    (config) -> config.pvpTime).add()

            .append(new KeyedCodec<>("BorderShrinkTime", Codec.INTEGER),
                    (config, value) -> config.borderShrinkTime = value,
                    (config) -> config.borderShrinkTime).add()

            .append(new KeyedCodec<>("DisconnectEliminationDelay", Codec.INTEGER),
                    (config, value) -> config.disconnectEliminationDelay = value,
                    (config) -> config.disconnectEliminationDelay).add()

            .append(new KeyedCodec<>("BorderSizeMax", Codec.INTEGER),
                    (config, value) -> config.borderSizeMax = value,
                    (config) -> config.borderSizeMax).add()

            .append(new KeyedCodec<>("BorderSizeMin", Codec.INTEGER),
                    (config, value) -> config.borderSizeMin = value,
                    (config) -> config.borderSizeMin).add()

            .append(new KeyedCodec<>("BorderDamage", Codec.INTEGER),
                    (config, value) -> config.borderDamage = value,
                    (config) -> config.borderDamage).add()

            .append(new KeyedCodec<>("BorderWarningDistance", Codec.INTEGER),
                    (config, value) -> config.borderWarningDistance = value,
                    (config) -> config.borderWarningDistance).add()

            .build();

    // variables
    public boolean pregenerateWorld = true;

    public boolean proximityChat = true;
    public int proximityChatDistance = 50;

    public int invincibilityTime = 60;
    public int pvpTime = 600;
    public int borderShrinkTime = 2400;

    public int disconnectEliminationDelay = 180;

    public int borderSizeMax = 1200;
    public int borderSizeMin = 200;
    public int borderDamage = 10;
    public int borderWarningDistance = 20;
}
