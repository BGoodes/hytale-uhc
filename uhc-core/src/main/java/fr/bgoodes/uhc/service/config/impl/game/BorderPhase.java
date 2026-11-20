package fr.bgoodes.uhc.service.config.impl.game;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.record.RecordCodec;

public record BorderPhase(int time, int radius, double speed) {

    public static final RecordCodec<BorderPhase> CODEC = RecordCodec.builder(BorderPhase.class)
            .append(new KeyedCodec<>("Time", Codec.INTEGER), BorderPhase::time)
            .append(new KeyedCodec<>("Radius", Codec.INTEGER), BorderPhase::radius)
            .append(new KeyedCodec<>("Speed", Codec.DOUBLE), BorderPhase::speed)
            .build(BorderPhase::new);
}
