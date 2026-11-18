package fr.bgoodes.uhc.listener.game;

import com.hypixel.hytale.event.EventPriority;
import com.hypixel.hytale.event.EventRegistry;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.event.game.ClockEvent;
import fr.bgoodes.uhc.event.game.GameStateChangedEvent;

public class HudListener {

    public void register(EventRegistry eventRegistry) {
        eventRegistry.register(EventPriority.LATE, GameStateChangedEvent.class, _ -> UHC.get().getHudService().showAll());
        eventRegistry.register(EventPriority.LATE, ClockEvent.class, _ -> UHC.get().getHudService().refreshAll());
    }
}
