package fr.bgoodes.uhc.event;

import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCause;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

public class ParticipantDeathEvent implements IEvent<Void> {

    private final PlayerRef playerRef;
    private final World world;
    private final Vector3d position;
    private final @Nullable DamageCause cause;
    private final @Nullable PlayerRef killer;

    public ParticipantDeathEvent(
            @NotNull PlayerRef playerRef,
            @NotNull World world,
            @NotNull Vector3d position,
            @Nullable DamageCause cause,
            @Nullable PlayerRef killer
    ) {
        this.playerRef = playerRef;
        this.world = world;
        this.position = new Vector3d(position);
        this.cause = cause;
        this.killer = killer;
    }

    public @NotNull PlayerRef getPlayerRef() {
        return playerRef;
    }

    public @NotNull World getWorld() {
        return world;
    }

    public @NotNull Vector3d getPosition() {
        return new Vector3d(position);
    }

    public @Nullable DamageCause getCause() {
        return cause;
    }

    public @Nullable PlayerRef getKiller() {
        return killer;
    }
}
