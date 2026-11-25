package fr.bgoodes.uhc.listener.hytale;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.event.EventPriority;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.event.ClockEvent;
import fr.bgoodes.uhc.event.game.GameStateChangedEvent;
import fr.bgoodes.uhc.service.hud.HudService;
import fr.bgoodes.uhc.service.hud.impl.StatsHud;
import fr.bgoodes.uhc.service.hud.impl.TitleHud;
import fr.bgoodes.uhc.service.hud.model.UHCHud;
import fr.bgoodes.uhc.service.state.model.IGameState;

public class HudListener {

    public void register(EventRegistry eventRegistry) {
        eventRegistry.registerGlobal(PlayerReadyEvent.class, this::onPlayerReady);
        eventRegistry.register(PlayerDisconnectEvent.class, this::onPlayerDisconnect);
        eventRegistry.register(EventPriority.LATE, GameStateChangedEvent.class, this::onGameStateChanged);
        eventRegistry.register(EventPriority.LATE, ClockEvent.class, _ -> UHC.get().getHudService().refreshAll());
    }

    private void onPlayerReady(PlayerReadyEvent event) {
        Ref<EntityStore> ref = event.getPlayerRef();
        PlayerRef playerRef = ref.getStore().getComponent(ref, PlayerRef.getComponentType());

        if (playerRef == null) {
            return;
        }

        UHC.get().getHudService().show(playerRef, new TitleHud(playerRef));
        UHC.get().getHudService().show(playerRef, new StatsHud(playerRef));
        showStateHud(playerRef);
    }

    private void onPlayerDisconnect(PlayerDisconnectEvent event) {
        UHC.get().getHudService().forget(event.getPlayerRef().getUuid());
    }

    private void onGameStateChanged(GameStateChangedEvent event) {
        Universe.get().getPlayers().forEach(this::showStateHud);
    }

    private void showStateHud(PlayerRef playerRef) {
        HudService hudService = UHC.get().getHudService();
        UHCHud hud = UHC.get().getStateService().getCurrentState().createHud(playerRef);

        if (hud == null) {
            hudService.hide(playerRef, IGameState.HUD_KEY);
        } else {
            hudService.show(playerRef, hud);
        }
    }
}
