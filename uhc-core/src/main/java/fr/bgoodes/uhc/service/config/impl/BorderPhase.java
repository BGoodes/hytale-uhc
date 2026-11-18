package fr.bgoodes.uhc.service.config.impl;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class BorderPhase {

    public static final BuilderCodec<BorderPhase> CODEC = BuilderCodec.builder(BorderPhase.class, BorderPhase::new)
            .append(new KeyedCodec<>("Time", Codec.INTEGER),
                    (phase, value) -> phase.time = value,
                    (phase) -> phase.time).add()
            .append(new KeyedCodec<>("Radius", Codec.INTEGER),
                    (phase, value) -> phase.radius = value,
                    (phase) -> phase.radius).add()
            .append(new KeyedCodec<>("Speed", Codec.DOUBLE),
                    (phase, value) -> phase.speed = value,
                    (phase) -> phase.speed).add()
            .build();

    public int time;
    public int radius;
    public double speed;

    public BorderPhase() {}

    public BorderPhase(int time, int radius, double speed) {
        this.time = time;
        this.radius = radius;
        this.speed = speed;
    }
}
