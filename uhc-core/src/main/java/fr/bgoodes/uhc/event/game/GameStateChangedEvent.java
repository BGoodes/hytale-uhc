package fr.bgoodes.uhc.event.game;

import com.hypixel.hytale.event.IEvent;
import org.jetbrains.annotations.NotNull;

public class GameStateChangedEvent implements IEvent<Void> {

    private final String stateId;

    public GameStateChangedEvent(@NotNull String stateId) {
        this.stateId = stateId;
    }

    public @NotNull String getStateId() {
        return stateId;
    }
}
