package fr.bgoodes.uhc.service.team;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TeamService {

    private final Set<Team> teams = new HashSet<>();

    public @NotNull Team createTeam(@NotNull String name) {
        Team team = new Team(name);
        teams.add(team);
        return team;
    }

    // team solo
    public void createPersonalTeam(@NotNull UUID playerUuid, @NotNull String username) {
        if (getPlayerTeam(playerUuid) != null) return;

        Team team = createTeam(username);
        this.addToTeam(team, playerUuid);
    }

    public void addToTeam(@NotNull Team team, @NotNull UUID playerUuid) {
        this.removeFromTeam(playerUuid);
        team.getMembers().add(playerUuid);
    }

    public void removeFromTeam(@NotNull UUID playerUuid) {
        Team team = getPlayerTeam(playerUuid);
        if (team == null) return;

        team.getMembers().remove(playerUuid);
        if (team.getMembers().isEmpty()) {
            this.teams.remove(team);
        }
    }

    @Nullable
    public Team getPlayerTeam(@NotNull UUID playerUuid) {
        for (Team team : teams) {
            if (team.getMembers().contains(playerUuid)) {
                return team;
            }
        }
        return null;
    }
}
