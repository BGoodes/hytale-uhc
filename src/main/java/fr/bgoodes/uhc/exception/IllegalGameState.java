package fr.bgoodes.uhc.exception;

import org.jetbrains.annotations.NotNull;

public class IllegalGameState extends RuntimeException {

    public IllegalGameState(@NotNull String gameStateId) {
        super("Can't change to '%s' state because is not registered on state service".formatted(gameStateId));
    }
}
