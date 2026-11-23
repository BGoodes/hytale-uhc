package fr.bgoodes.uhc.service.config.impl.game;

import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import fr.bgoodes.uhc.service.config.model.Option;
import org.jetbrains.annotations.NotNull;

public class BorderPhasesOption extends Option<BorderPhase[]> {

    public BorderPhasesOption(@NotNull String key, @NotNull BorderPhase... value) {
        super(key, new ArrayCodec<>(BorderPhase.CODEC, BorderPhase[]::new), value);
    }

    @Override
    public boolean isValid(BorderPhase @NotNull [] phases) {
        int previousTime = -1;
        int previousRadius = Integer.MAX_VALUE;

        for (BorderPhase phase : phases) {
            if (phase.time() <= previousTime || phase.radius() >= previousRadius || phase.radius() < 1 || phase.speed() <= 0) {
                return false;
            }

            previousTime = phase.time();
            previousRadius = phase.radius();
        }

        return true;
    }

    @Override
    protected BorderPhase @NotNull [] copy(BorderPhase @NotNull [] phases) {
        return phases.clone();
    }
}
