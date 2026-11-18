package fr.bgoodes.uhc.listener.game;

import com.hypixel.hytale.event.EventPriority;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.Message;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.event.game.InvincibilityEndedEvent;
import fr.bgoodes.uhc.event.game.PvpEnabledEvent;
import fr.bgoodes.uhc.service.flag.impl.UHCFlag;
import fr.bgoodes.uhc.util.UHCMessageUtil;
import fr.bgoodes.uhc.util.UHCSoundUtil;
import org.jetbrains.annotations.Nullable;

public class GameListener {

    private static final String ANNOUNCE_SOUND = "SFX_Drum_Stinger";

    public void register(EventRegistry eventRegistry) {
        eventRegistry.register(EventPriority.LATE, InvincibilityEndedEvent.class, this::onInvincibilityEnded);
        eventRegistry.register(EventPriority.LATE, PvpEnabledEvent.class, this::onPvpEnabled);
    }

    private void onInvincibilityEnded(InvincibilityEndedEvent event) {
        UHC.get().getFlagService().setEnabled(UHCFlag.DAMAGE, true);
        announce(event.getMessage());
    }

    private void onPvpEnabled(PvpEnabledEvent event) {
        UHC.get().getFlagService().setEnabled(UHCFlag.PVP, true);
        announce(event.getMessage());
    }

    private void announce(@Nullable Message message) {
        if (message == null) {
            return;
        }

        UHCMessageUtil.broadcast(message);
        UHCSoundUtil.broadcast(ANNOUNCE_SOUND);
    }
}
