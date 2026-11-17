package fr.bgoodes.uhc.event;

import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.jetbrains.annotations.NotNull;

public class ParticipantSpawnEvent implements IEvent<Void> {

    private final PlayerRef playerRef;
    private Transform location;

    public ParticipantSpawnEvent(@NotNull PlayerRef playerRef, @NotNull Transform location) {
        this.playerRef = playerRef;
        this.location = location;
    }

    public @NotNull PlayerRef getPlayerRef() {
        return playerRef;
    }

    public @NotNull Transform getSpawnPoint() {
        return location;
    }

    public void setSpawnPoint(@NotNull Transform location) {
        this.location = location;
    }
}
