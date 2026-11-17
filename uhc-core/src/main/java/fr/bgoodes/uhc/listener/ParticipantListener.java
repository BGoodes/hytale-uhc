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
import fr.bgoodes.uhc.util.UHCPlayerUtil;
import fr.bgoodes.uhc.util.UHCSoundUtil;

import java.util.UUID;

public class ParticipantListener {

    public void register(EventRegistry eventRegistry) {
        eventRegistry.register(EventPriority.LATE, ParticipantDeathEvent.class, this::onDeath);
        eventRegistry.register(EventPriority.LATE, ParticipantEliminatedEvent.class, this::onEliminated);
    }

    private void onDeath(ParticipantDeathEvent event) {
        if (event.isCancelled()) {
            return;
        }

        UUID uuid = event.getUuid();
        PlayerRef playerRef = event.getPlayerRef();

        if (playerRef != null) {
            UHCPlayerUtil.dropInventory(playerRef, event.getPosition());
        } else {
            UHCPlayerUtil.dropStoredInventory(uuid, event.getWorld());
        }

        Message message = event.getMessage();

        if (message != null) {
            UHCMessageUtil.broadcast(message);
            UHCSoundUtil.broadcast("SFX_Taiko");
        }

        if (UHC.get().getPlayerService().removeParticipant(uuid)) {
            HytaleServer.get().getEventBus()
                    .dispatchFor(ParticipantEliminatedEvent.class)
                    .dispatch(new ParticipantEliminatedEvent(uuid));
        }
    }

    private void onEliminated(ParticipantEliminatedEvent event) {
        UHC.get().getPermissionService().addSpectator(event.getUuid());
    }
}
