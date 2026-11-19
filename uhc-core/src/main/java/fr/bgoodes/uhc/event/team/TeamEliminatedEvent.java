package fr.bgoodes.uhc.event.team;

import com.hypixel.hytale.event.IEvent;
import fr.bgoodes.uhc.service.team.Team;
import org.jetbrains.annotations.NotNull;

public class TeamEliminatedEvent implements IEvent<Void> {

    private final Team team;

    public TeamEliminatedEvent(@NotNull Team team) {
        this.team = team;
    }

    public @NotNull Team getTeam() {
        return team;
    }
}
