package fr.bgoodes.uhc.listener.game;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.nameplate.Nameplate;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.event.ClockEvent;
import fr.bgoodes.uhc.event.participant.ParticipantDeathEvent;
import fr.bgoodes.uhc.service.PlayerService;
import fr.bgoodes.uhc.util.UHCMessageUtil;
import org.joml.Vector3d;

import java.awt.Color;
import java.time.Instant;
import java.util.UUID;

public class DisconnectTimeoutListener {

    public void register(EventRegistry eventRegistry) {
        eventRegistry.register(ClockEvent.class, _ -> onClock());
    }

    private void onClock() {
        int timeout = UHC.get().getGameConfig().disconnectTimeout.get();

        if (timeout <= 0) {
            return;
        }

        PlayerService playerService = UHC.get().getPlayerService();

        playerService.getDisconnections().forEach((uuid, disconnectedAt) -> {
            if (disconnectedAt.isBefore(Instant.now().minusSeconds(timeout))) {
                playerService.removeDisconnected(uuid);
                kill(uuid);
            }
        });
    }

    private void kill(UUID uuid) {
        World world = UHC.get().getWorldService().getGameWorld();

        Universe.get().getPlayerStorage().load(uuid).thenAccept(holder -> world.execute(() -> {
            if (Universe.get().getPlayer(uuid) != null || !UHC.get().getPlayerService().isParticipant(uuid)) {
                return;
            }

            Nameplate nameplate = holder.getComponent(Nameplate.getComponentType());
            TransformComponent transform = holder.getComponent(TransformComponent.getComponentType());

            String username = nameplate != null ? nameplate.getText() : uuid.toString();
            Vector3d position = transform != null ? transform.getPosition() : new Vector3d();

            ParticipantDeathEvent event = new ParticipantDeathEvent(uuid, username, null, world, position, null, null);

            event.setMessage(Message.join(
                    UHCMessageUtil.tag("×", UHCMessageUtil.ERROR_COLOR),
                    UHCMessageUtil.tr("uhc.game.death.disconnected")
                            .param("player", username)
                            .color(Color.GRAY)
                            .bold(true)
            ));

            HytaleServer.get().getEventBus()
                    .dispatchFor(ParticipantDeathEvent.class)
                    .dispatch(event);
        }));
    }
}
