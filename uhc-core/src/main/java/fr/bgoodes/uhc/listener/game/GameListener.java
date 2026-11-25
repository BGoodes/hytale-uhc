package fr.bgoodes.uhc.listener.game;

import com.hypixel.hytale.event.EventPriority;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.Message;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.event.AnnouncementEvent;
import fr.bgoodes.uhc.event.game.*;
import fr.bgoodes.uhc.service.flag.impl.UHCFlag;
import fr.bgoodes.uhc.service.hud.model.Title;
import fr.bgoodes.uhc.service.state.impl.EndingState;
import fr.bgoodes.uhc.util.UHCMessageUtil;
import fr.bgoodes.uhc.util.UHCSoundUtil;
import fr.bgoodes.uhc.util.UHCTitleUtil;

public class GameListener {

    public void register(EventRegistry eventRegistry) {
        eventRegistry.register(EventPriority.LATE, GameStartedEvent.class, this::onGameStarted);
        eventRegistry.register(EventPriority.LATE, InvincibilityEndedEvent.class, this::onInvincibilityEnded);
        eventRegistry.register(EventPriority.LATE, PvpEnabledEvent.class, this::onPvpEnabled);
        eventRegistry.register(EventPriority.LATE, BorderShrinkEvent.class, this::onBorderShrink);
        eventRegistry.register(EventPriority.LATE, BorderShrunkEvent.class, this::onBorderShrunk);
        eventRegistry.register(EventPriority.LATE, GameEndedEvent.class, this::onGameEnded);
    }

    private void onGameStarted(GameStartedEvent event) {
        announce(event);
    }

    private void onInvincibilityEnded(InvincibilityEndedEvent event) {
        UHC.get().getFlagService().setEnabled(UHCFlag.DAMAGE, true);
        announce(event);
    }

    private void onPvpEnabled(PvpEnabledEvent event) {
        UHC.get().getFlagService().setEnabled(UHCFlag.PVP, true);
        announce(event);
    }

    private void onBorderShrink(BorderShrinkEvent event) {
        UHC.get().getWorldBorderService().shrinkTo(event.getTargetRadius(), event.getSpeed());
        announce(event);
    }

    private void onBorderShrunk(BorderShrunkEvent event) {
        announce(event);
    }

    private void onGameEnded(GameEndedEvent event) {
        if (event.isCancelled()) {
            return;
        }

        announce(event);
        UHC.get().getStateService().changeGameState(EndingState.STATE_ID);
    }

    private void announce(AnnouncementEvent event) {
        Message message = event.getMessage();
        Title title = event.getTitle();
        String sound = event.getSound();

        if (message != null) {
            UHCMessageUtil.broadcast(message);
        }

        if (title != null) {
            UHCTitleUtil.broadcast(title);
        }

        if (sound != null) {
            UHCSoundUtil.broadcast(sound);
        }
    }
}
