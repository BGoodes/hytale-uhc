package fr.bgoodes.uhc.listener.game;

import com.hypixel.hytale.event.EventPriority;
import com.hypixel.hytale.event.EventRegistry;
import fr.bgoodes.uhc.event.game.GameStateChangedEvent;
import fr.bgoodes.uhc.event.option.OptionChangedEvent;
import fr.bgoodes.uhc.event.team.TeamChangeEvent;
import fr.bgoodes.uhc.page.team.TeamPage;

public class TeamPageListener {

    public void register(EventRegistry eventRegistry) {
        eventRegistry.register(EventPriority.LAST, TeamChangeEvent.class, this::onTeamChange);
        eventRegistry.register(EventPriority.LAST, OptionChangedEvent.class, _ -> TeamPage.refreshAll());
        eventRegistry.register(EventPriority.LAST, GameStateChangedEvent.class, _ -> TeamPage.refreshAll());
    }

    private void onTeamChange(TeamChangeEvent event) {
        if (!event.isCancelled()) {
            TeamPage.refreshAll();
        }
    }
}
