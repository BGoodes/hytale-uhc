package fr.bgoodes.uhc.event;

import com.hypixel.hytale.event.ICancellable;
import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCause;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import fr.bgoodes.uhc.util.UHCMessageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import java.awt.Color;
import java.util.UUID;

public class ParticipantDeathEvent implements IEvent<Void>, ICancellable {

    private final UUID uuid;
    private final String username;
    private final @Nullable PlayerRef playerRef;
    private final World world;
    private final Vector3d position;
    private final @Nullable DamageCause cause;
    private final @Nullable PlayerRef killer;

    private @Nullable Message message;
    private boolean cancelled;

    public ParticipantDeathEvent(
            @NotNull UUID uuid,
            @NotNull String username,
            @Nullable PlayerRef playerRef,
            @NotNull World world,
            @NotNull Vector3d position,
            @Nullable DamageCause cause,
            @Nullable PlayerRef killer
    ) {
        this.uuid = uuid;
        this.username = username;
        this.playerRef = playerRef;
        this.world = world;
        this.position = new Vector3d(position);
        this.cause = cause;
        this.killer = killer;

        this.message = defaultMessage(username, killer);
        this.cancelled = false;
    }

    public @NotNull UUID getUuid() {
        return uuid;
    }

    public @NotNull String getUsername() {
        return username;
    }

    public @Nullable PlayerRef getPlayerRef() {
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

    public @Nullable Message getMessage() {
        return message;
    }

    public void setMessage(@Nullable Message message) {
        this.message = message;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    private static Message defaultMessage(String username, @Nullable PlayerRef killer) {
        Message message = UHCMessageUtil.tr(killer == null ? "uhc.death.player" : "uhc.death.killed")
                .param("player", Message.raw(username));

        if (killer != null) {
            message.param("killer", Message.raw(killer.getUsername()));
        }

        return message.color(Color.ORANGE);
    }
}
