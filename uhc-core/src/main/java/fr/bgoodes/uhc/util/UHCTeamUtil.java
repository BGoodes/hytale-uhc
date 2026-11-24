package fr.bgoodes.uhc.util;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.service.PlayerService;
import fr.bgoodes.uhc.service.config.impl.game.GameConfig;
import fr.bgoodes.uhc.service.team.Team;
import fr.bgoodes.uhc.service.team.TeamColor;
import fr.bgoodes.uhc.service.team.TeamService;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class UHCTeamUtil {

    private UHCTeamUtil() {}

    public static void resetTeams() {
        TeamService teamService = UHC.get().getTeamService();
        GameConfig config = UHC.get().getGameConfig();

        for (Team team : teamService.getTeams()) {
            teamService.removeTeam(team.getId());
        }

        if (isSolo()) {
            return;
        }

        TeamColor[] colors = TeamColor.values();
        int count = Math.min(config.teamCount.get(), colors.length);

        for (int i = 0; i < count; i++) {
            teamService.createTeam(colors[i].getId(), colors[i].getName(), colors[i].getColor());
        }
    }

    public static boolean isSolo() {
        return UHC.get().getGameConfig().teamSize.get() <= 1;
    }

    public static boolean isVisibleOnMap(@NotNull UUID viewer, @NotNull UUID other) {
        PlayerService playerService = UHC.get().getPlayerService();

        if (!playerService.isParticipant(other)) {
            return false;
        }

        if (!playerService.isParticipant(viewer)) {
            return true;
        }

        TeamService teamService = UHC.get().getTeamService();
        Team viewerTeam = teamService.getPlayerTeam(viewer);

        return UHC.get().getGameConfig().teammatesOnMap.get() && viewerTeam == teamService.getPlayerTeam(other);
    }

    public static void createSoloTeam(@NotNull PlayerRef playerRef) {
        TeamService teamService = UHC.get().getTeamService();
        String teamId = playerRef.getUuid().toString();

        if (teamService.getPlayerTeam(playerRef.getUuid()) != null) {
            return;
        }

        teamService.createTeam(teamId, Message.raw(playerRef.getUsername()), null);
        teamService.addPlayer(teamId, playerRef.getUuid());
    }

    public static void fillTeams() {
        TeamService teamService = UHC.get().getTeamService();
        int teamSize = UHC.get().getGameConfig().teamSize.get();

        List<PlayerRef> players = new ArrayList<>(Universe.get().getPlayers());
        Collections.shuffle(players);

        for (PlayerRef playerRef : players) {
            if (teamService.getPlayerTeam(playerRef.getUuid()) != null) {
                continue;
            }

            List<Team> teams = new ArrayList<>(teamService.getTeams());
            Collections.shuffle(teams);

            Team smallest = teams.stream()
                    .min(Comparator.comparingInt(team -> team.getMembers().size()))
                    .orElse(null);

            if (smallest == null || smallest.getMembers().size() >= teamSize) {
                return;
            }

            teamService.addPlayer(smallest.getId(), playerRef.getUuid());
        }
    }

    public static void removeOfflineMembers() {
        TeamService teamService = UHC.get().getTeamService();

        for (Team team : teamService.getTeams()) {
            for (UUID uuid : team.getMembers()) {
                if (Universe.get().getPlayer(uuid) == null) {
                    teamService.removePlayer(uuid);
                }
            }
        }
    }

    public static void removeEmptyTeams() {
        TeamService teamService = UHC.get().getTeamService();

        for (Team team : teamService.getTeams()) {
            if (team.getMembers().isEmpty()) {
                teamService.removeTeam(team.getId());
            }
        }
    }
}
