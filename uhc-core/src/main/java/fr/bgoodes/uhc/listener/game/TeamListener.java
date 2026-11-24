package fr.bgoodes.uhc.listener.game;

import com.hypixel.hytale.event.EventPriority;
import com.hypixel.hytale.event.EventRegistry;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.event.team.TeamChangeEvent;
import fr.bgoodes.uhc.service.team.Team;
import fr.bgoodes.uhc.service.team.TeamService;

public class TeamListener {

    public void register(EventRegistry eventRegistry) {
        eventRegistry.register(EventPriority.EARLY, TeamChangeEvent.class, this::onCheck);
        eventRegistry.register(EventPriority.LATE, TeamChangeEvent.class, this::onApply);
    }

    private void onCheck(TeamChangeEvent event) {
        Team to = event.getTo();

        if (to == event.getFrom() || (to != null && UHC.get().getTeamService().getTeam(to.getId()) != to)) {
            event.setCancelled(true);
        }
    }

    private void onApply(TeamChangeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        TeamService teamService = UHC.get().getTeamService();
        Team to = event.getTo();

        if (to == null) {
            teamService.removePlayer(event.getUuid());
        } else {
            teamService.addPlayer(to.getId(), event.getUuid());
        }
    }
}
