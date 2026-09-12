package fr.bgoodes.uhc.listeners;

import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerSetupConnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.context.GameContext;
import fr.bgoodes.uhc.service.hud.HUDService;
import fr.bgoodes.uhc.service.permission.PermissionService;
import fr.bgoodes.uhc.service.player.PlayerService;
import fr.bgoodes.uhc.service.state.StateService;
import fr.bgoodes.uhc.util.SpectatorUtil;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class PlayerConnectionListeners {

    private final GameContext gameContext;

    private final StateService stateService = UHC.get().getStateService();
    private final HUDService hudService = UHC.get().getHudService();
    private final PermissionService permissionService = UHC.get().getPermissionService();
    private final PlayerService playerService = UHC.get().getPlayerService();

    public PlayerConnectionListeners(@NotNull GameContext gameContext) {
        this.gameContext = gameContext;
    }

    public void register(@NotNull EventRegistry eventBus) {
        eventBus.register(PlayerSetupConnectEvent.class, this::onPlayerSetupConnectEvent);
        eventBus.register(PlayerConnectEvent.class, this::onPlayerConnect);
        eventBus.registerGlobal(PlayerReadyEvent.class, this::onPlayerJoin);
        eventBus.registerGlobal(PlayerDisconnectEvent.class, this::onPlayerDisconnect);
    }

    // PlayerSetupConnectEvent -> PlayerConnectEvent -> AddPlayerToWorldEvent -> PlayerReadyEvent
    private void onPlayerSetupConnectEvent(PlayerSetupConnectEvent event) {

        if (!permissionService.hasHost() && !permissionService.canClaimHost(event.getUuid())) {
            event.setReason(Message.raw("Vous devez être membre du staff pour vous connecter.").color(Color.red));
            event.setCancelled(true);
        }
    }

    private void onPlayerConnect(@NotNull PlayerConnectEvent event) {
        event.setWorld(gameContext.getWorld());
    }

    private void onPlayerJoin(@NotNull PlayerReadyEvent event) {
        if (!event.getPlayerRef().isValid()) {
            return;
        }

        stateService.getCurrentState().onPlayerJoin(event, gameContext);

        Store<EntityStore> store = event.getPlayerRef().getStore();
        PlayerRef playerRef = store.getComponent(event.getPlayerRef(), PlayerRef.getComponentType());
        Player player = store.getComponent(event.getPlayerRef(), Player.getComponentType());

        if (player != null && playerRef != null) {
            playerService.removeDisconnectCountdown(playerRef.getUuid());
            hudService.addHUDToPlayer(playerRef, player);

            if (!gameContext.getGamerules().allowNewPlayers() && !playerService.isParticipant(playerRef.getUuid())) {
                SpectatorUtil.addSpectator(playerRef, store);
            }
        }
    }

    private void onPlayerDisconnect(@NotNull PlayerDisconnectEvent event) {
        int delay = UHC.get().getConfig().get().disconnectEliminationDelay;
        playerService.setDisconnectCountdown(event.getPlayerRef().getUuid(), delay);
    }
}
