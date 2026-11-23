package fr.bgoodes.uhc.listener.option;

import com.hypixel.hytale.event.EventRegistry;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.event.option.OptionChangeEvent;
import fr.bgoodes.uhc.service.config.impl.game.BorderPhase;
import fr.bgoodes.uhc.service.config.impl.game.GameConfig;

public class BorderOptionListener {

    public void register(EventRegistry eventRegistry) {
        eventRegistry.register(OptionChangeEvent.class, this::onChange);
    }

    private void onChange(OptionChangeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        GameConfig config = UHC.get().getGameConfig();
        int startRadius = config.borderStartRadius.get();
        BorderPhase[] phases = config.borderPhases.get();

        if (event.getOption() == config.borderStartRadius && event.getNewValue() instanceof Integer radius) {
            startRadius = radius;
        } else if (event.getOption() == config.borderPhases && event.getNewValue() instanceof BorderPhase[] newPhases) {
            phases = newPhases;
        } else {
            return;
        }

        if (phases.length > 0 && phases[0].radius() >= startRadius) {
            event.setCancelled(true);
        }
    }
}
