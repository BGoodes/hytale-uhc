package fr.bgoodes.uhc.event.participant;

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
    private @Nullable PlayerRef killer;

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

    public void setKiller(@Nullable PlayerRef killer) {
        this.killer = killer;
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
        Message tag = UHCMessageUtil.tag("×", UHCMessageUtil.ERROR_COLOR);

        if (killer == null) {
            return Message.join(tag, UHCMessageUtil.tr("uhc.game.death.player")
                    .param("player", username)
                    .color(Color.GRAY)
                    .bold(true));
        }

        return Message.join(tag, UHCMessageUtil.tr("uhc.game.death.killed")
                .param("player", username)
                .param("killer", killer.getUsername())
                .color(Color.GRAY)
                .bold(true));
    }
}
