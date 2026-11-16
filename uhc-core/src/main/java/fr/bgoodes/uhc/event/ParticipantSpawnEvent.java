package fr.bgoodes.uhc.event;

import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.server.core.universe.PlayerRef;

public class ParticipantSpawnEvent implements IEvent<Void> {

    private final PlayerRef playerRef;
    private Transform location;

    public ParticipantSpawnEvent(PlayerRef playerRef, Transform location) {
        this.playerRef = playerRef;
        this.location = location;
    }

    public PlayerRef getPlayerRef() {
        return playerRef;
    }

    public Transform getSpawnPoint() {
        return location;
    }

    public void setSpawnPoint(Transform location) {
        this.location = location;
    }
}
