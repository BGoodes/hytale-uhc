package fr.bgoodes.uhc.listener.game;

import com.hypixel.hytale.event.EventPriority;
import com.hypixel.hytale.event.EventRegistry;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.event.team.TeamChangeEvent;
import fr.bgoodes.uhc.service.team.Team;
import fr.bgoodes.uhc.service.team.TeamService;

import java.util.Objects;

public class TeamListener {

    public void register(EventRegistry eventRegistry) {
        eventRegistry.register(EventPriority.EARLY, TeamChangeEvent.class, this::onCheck);
        eventRegistry.register(EventPriority.LATE, TeamChangeEvent.class, this::onApply);
    }

    private void onCheck(TeamChangeEvent event) {
        String toId = event.getToId();

        if (Objects.equals(toId, event.getFromId())) {
            event.setCancelled(true);
            return;
        }

        if (toId == null) {
            return;
        }

        Team to = UHC.get().getTeamService().getTeam(toId);

        if (to == null || to.getMembers().size() >= UHC.get().getGameConfig().teamSize.get()) {
            event.setCancelled(true);
        }
    }

    private void onApply(TeamChangeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        TeamService teamService = UHC.get().getTeamService();
        String toId = event.getToId();

        if (toId == null) {
            teamService.removePlayer(event.getUuid());
        } else {
            teamService.addPlayer(toId, event.getUuid());
        }
    }
}
