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
    private final @Nullable Team from;
    private final @Nullable Team to;

    private boolean cancelled;

    public TeamChangeEvent(@NotNull UUID uuid, @Nullable Team to) {
        this.uuid = uuid;
        this.from = UHC.get().getTeamService().getPlayerTeam(uuid);
        this.to = to;
        this.cancelled = false;
    }

    public @NotNull UUID getUuid() {
        return uuid;
    }

    public @Nullable Team getFrom() {
        return from;
    }

    public @Nullable Team getTo() {
        return to;
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
