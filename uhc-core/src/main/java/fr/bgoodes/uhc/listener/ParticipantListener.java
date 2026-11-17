package fr.bgoodes.uhc.listener;

import com.hypixel.hytale.event.EventPriority;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.event.ParticipantDeathEvent;
import fr.bgoodes.uhc.event.ParticipantEliminatedEvent;
import fr.bgoodes.uhc.util.UHCMessageUtil;
import fr.bgoodes.uhc.util.UHCSoundUtil;

import java.awt.Color;
import java.util.UUID;

public class ParticipantListener {

    public void register(EventRegistry eventRegistry) {
        eventRegistry.register(EventPriority.LATE, ParticipantDeathEvent.class, this::onDeath);
        eventRegistry.register(EventPriority.LATE, ParticipantEliminatedEvent.class, this::onEliminated);
    }

    private void onDeath(ParticipantDeathEvent event) {
        UUID playerId = event.getPlayerRef().getUuid();
        PlayerRef killer = event.getKiller();

        Message message = UHCMessageUtil.tr(killer == null ? "uhc.death.player" : "uhc.death.killed")
                .param("player", Message.raw(event.getPlayerRef().getUsername()));

        if (killer != null) {
            message.param("killer", Message.raw(killer.getUsername()));
        }

        if (UHC.get().getPlayerService().removeParticipant(playerId)) {
            // call ParticipantEliminatedEvent
            HytaleServer.get().getEventBus()
                    .dispatchFor(ParticipantEliminatedEvent.class)
                    .dispatch(new ParticipantEliminatedEvent(playerId, message.color(Color.ORANGE)));
        }
    }

    private void onEliminated(ParticipantEliminatedEvent event) {
        UHC.get().getPermissionService().addSpectator(event.getUuid());

        Message message = event.getMessage();

        if (message != null) {
            UHCMessageUtil.broadcast(message);
            UHCSoundUtil.broadcast("SFX_Taiko");
        }
    }
}
