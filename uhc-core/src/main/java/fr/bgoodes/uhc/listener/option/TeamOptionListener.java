package fr.bgoodes.uhc.listener.option;

import com.hypixel.hytale.event.EventRegistry;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.event.option.OptionChangedEvent;
import fr.bgoodes.uhc.service.config.impl.game.GameConfig;
import fr.bgoodes.uhc.util.UHCTeamUtil;

public class TeamOptionListener {

    public void register(EventRegistry eventRegistry) {
        eventRegistry.register(OptionChangedEvent.class, this::onChanged);
    }

    private void onChanged(OptionChangedEvent event) {
        GameConfig config = UHC.get().getGameConfig();

        if (event.getOption() == config.teamSize || event.getOption() == config.teamCount || event.getOption() == config.teamChoice) {
            UHCTeamUtil.resetTeams();
        }
    }
}
