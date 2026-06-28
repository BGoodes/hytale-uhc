package fr.bgoodes.uhc.listeners;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerSetupConnectEvent;
import com.hypixel.hytale.server.core.universe.Universe;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.context.GameContext;
import fr.bgoodes.uhc.service.state.StateService;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class PlayerJoinListeners {

    private final GameContext gameContext;
    private final StateService stateService;

    public PlayerJoinListeners(@NotNull GameContext gameContext, @NotNull StateService stateService) {
        this.gameContext = gameContext;
        this.stateService = stateService;
    }

    public void register(@NotNull EventRegistry eventBus) {
        eventBus.register(PlayerSetupConnectEvent.class, this::onPlayerSetupConnectEvent);
        eventBus.register(PlayerConnectEvent.class, this::onPlayerConnect);
        eventBus.registerGlobal(PlayerReadyEvent.class, this::onPlayerJoin);
    }

    // PlayerSetupConnectEvent -> PlayerConnectEvent -> AddPlayerToWorldEvent -> PlayerReadyEvent
    private void onPlayerSetupConnectEvent(PlayerSetupConnectEvent event) {
        if (!gameContext.getGamerules().allowNewPlayers() && !gameContext.isPlayer(event.getUuid())) {
            event.setCancelled(true);
            event.setReason(Message.raw("Une partie est en cours !").color(Color.red));
        }
    }

    private void onPlayerConnect(@NotNull PlayerConnectEvent event) {
        event.setWorld(gameContext.getWorld());
    }

    private void onPlayerJoin(@NotNull PlayerReadyEvent event) {
        stateService.getCurrentState().onPlayerJoin(event, gameContext);
    }
}
