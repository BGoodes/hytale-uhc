package fr.bgoodes.uhc.listener.option;

import com.hypixel.hytale.event.EventPriority;
import com.hypixel.hytale.event.EventRegistry;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.event.option.OptionChangeEvent;
import fr.bgoodes.uhc.service.scenario.ScenarioService;
import fr.bgoodes.uhc.service.scenario.UHCScenario;

public class ScenarioOptionListener {

    public void register(EventRegistry eventRegistry) {
        eventRegistry.register(EventPriority.EARLY, OptionChangeEvent.class, this::onCheck);
    }

    private void onCheck(OptionChangeEvent event) {
        if (!Boolean.TRUE.equals(event.getNewValue())) {
            return;
        }

        ScenarioService scenarioService = UHC.get().getScenarioService();

        for (UHCScenario scenario : scenarioService.getScenarios()) {
            if (event.getOption() == scenario.getConfig().enabled && scenarioService.findConflict(scenario) != null) {
                event.setCancelled(true);
                return;
            }
        }
    }
}
