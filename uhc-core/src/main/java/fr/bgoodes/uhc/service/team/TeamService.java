package fr.bgoodes.uhc.service.team;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TeamService {

    private final Map<String, Team> teams = new HashMap<>();
    private final Map<UUID, Team> playerTeams = new HashMap<>();

    public synchronized @NotNull Team createTeam(@NotNull String id, @NotNull String name, @Nullable Color color) {
        if (teams.containsKey(id)) {
            throw new IllegalArgumentException("Team already exists: %s".formatted(id));
        }

        Team team = new Team(id, name, color);
        teams.put(id, team);

        return team;
    }

    public synchronized void removeTeam(@NotNull String id) {
        Team team = teams.remove(id);

        if (team != null) {
            team.getMembers().forEach(playerTeams::remove);
        }
    }

    public synchronized @Nullable Team getTeam(@NotNull String id) {
        return teams.get(id);
    }

    public synchronized @NotNull List<Team> getTeams() {
        return List.copyOf(teams.values());
    }

    public synchronized @Nullable Team getPlayerTeam(@NotNull UUID uuid) {
        return playerTeams.get(uuid);
    }

    public synchronized void addPlayer(@NotNull String teamId, @NotNull UUID uuid) {
        Team team = teams.get(teamId);

        if (team == null) {
            throw new IllegalArgumentException("Unknown team: %s".formatted(teamId));
        }

        removePlayer(uuid);
        team.addMember(uuid);
        playerTeams.put(uuid, team);
    }

    public synchronized void removePlayer(@NotNull UUID uuid) {
        Team team = playerTeams.remove(uuid);

        if (team != null) {
            team.removeMember(uuid);
        }
    }
}
