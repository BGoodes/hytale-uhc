package fr.bgoodes.uhc.listeners;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import fr.bgoodes.uhc.context.GameContext;
import fr.bgoodes.uhc.service.state.StateService;
import org.jetbrains.annotations.NotNull;

public class PlayerListeners  {

    private final GameContext gameContext;
    private final StateService stateService;

    public PlayerListeners(@NotNull GameContext gameContext, @NotNull StateService stateService) {
        this.gameContext = gameContext;
        this.stateService = stateService;
    }

    public void register(@NotNull EventRegistry eventBus) {
        eventBus.register(PlayerConnectEvent.class, this::onPlayerConnect);
        eventBus.registerGlobal(PlayerReadyEvent.class, this::onPlayerJoin);
    }

    // PlayerConnectEvent -> AddPlayerToWorldEvent -> PlayerReadyEvent
    private void onPlayerConnect(@NotNull PlayerConnectEvent event) {
        event.setWorld(gameContext.getWorld());
    }

    private void onPlayerJoin(@NotNull PlayerReadyEvent event) {
        stateService.getCurrentState().onPlayerJoin(event, gameContext);
    }
}
