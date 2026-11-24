package fr.bgoodes.uhc.event.team;

import com.hypixel.hytale.event.ICancellable;
import com.hypixel.hytale.event.IEvent;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.service.team.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class TeamChangeEvent implements IEvent<Void>, ICancellable {

    private final UUID uuid;
    private final @Nullable String fromId;
    private final @Nullable String toId;

    private boolean cancelled;

    public TeamChangeEvent(@NotNull UUID uuid, @Nullable String toId) {
        Team from = UHC.get().getTeamService().getPlayerTeam(uuid);

        this.uuid = uuid;
        this.fromId = from != null ? from.getId() : null;
        this.toId = toId;
        this.cancelled = false;
    }

    public @NotNull UUID getUuid() {
        return uuid;
    }

    public @Nullable String getFromId() {
        return fromId;
    }

    public @Nullable String getToId() {
        return toId;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
