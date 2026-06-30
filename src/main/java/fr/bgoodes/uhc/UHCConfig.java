package fr.bgoodes.uhc;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class UHCConfig {

    public static final BuilderCodec<UHCConfig> CODEC = BuilderCodec.builder(UHCConfig.class, UHCConfig::new)

            .append(new KeyedCodec<>("PregenerateWorld", Codec.BOOLEAN),
                    (config, value) -> config.pregenerateWorld = value, //setter
                    (config) -> config.pregenerateWorld).add() // getter

            .append(new KeyedCodec<>("InvincibilityTime", Codec.INTEGER),
                    (config, value) -> config.invincibilityTime = value,
                    (config) -> config.invincibilityTime).add()

            .append(new KeyedCodec<>("PvpTime", Codec.INTEGER),
                    (config, value) -> config.pvpTime = value,
                    (config) -> config.pvpTime).add()

            .append(new KeyedCodec<>("BorderShrinkTime", Codec.INTEGER),
                    (config, value) -> config.borderShrinkTime = value,
                    (config) -> config.borderShrinkTime).add()

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
    private boolean pregenerateWorld = true;

    private int invincibilityTime = 60;
    private int pvpTime = 600;
    private int borderShrinkTime = 2400;

    private int borderSizeMax = 2000;
    private int borderSizeMin = 200;
    private int borderDamage = 10;
    private int borderWarningDistance = 30;

    // getters & setters

    public boolean pregenerateWorld() {
        return pregenerateWorld;
    }

    public void pregenerateWorld(boolean value) {
        this.pregenerateWorld = value;
    }


    public int invincibilityTime() {
        return invincibilityTime;
    }

    public void invincibilityTime(int value) {
        this.invincibilityTime = value;
    }


    public int pvpTime() {
        return pvpTime;
    }

    public void pvpTime(int value) {
        this.pvpTime = value;
    }


    public int borderShrinkTime() {
        return borderShrinkTime;
    }

    public void borderShrinkTime(int value) {
        this.borderShrinkTime = value;
    }


    public int borderSizeMax() {
        return borderSizeMax;
    }

    public void borderSizeMax(int value) {
        this.borderSizeMax = value;
    }


    public int borderSizeMin() {
        return borderSizeMin;
    }

    public void borderSizeMin(int value) {
        this.borderSizeMin = value;
    }


    public int borderDamage() {
        return borderDamage;
    }

    public void borderDamage(int value) {
        this.borderDamage = value;
    }


    public int borderWarningDistance() {
        return borderWarningDistance;
    }

    public void borderWarningDistance(int value) {
        this.borderWarningDistance = value;
    }
}
