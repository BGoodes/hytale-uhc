package fr.bgoodes.uhc.listener;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerSetupConnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.service.flag.FlagService;
import fr.bgoodes.uhc.service.flag.impl.UHCFlag;
import fr.bgoodes.uhc.service.permission.PermissionService;
import fr.bgoodes.uhc.service.player.PlayerService;
import fr.bgoodes.uhc.utils.TeleportUtil;
import fr.bgoodes.uhc.utils.LobbyUtil;
import fr.bgoodes.uhc.utils.UHCMessageUtil;
import fr.bgoodes.uhc.utils.UHCPlayerUtil;

import java.awt.*;

public class PlayerConnectionListener {

    public void register(EventRegistry eventRegistry) {
        eventRegistry.register(PlayerSetupConnectEvent.class, this::onPlayerSetupConnect);
        eventRegistry.registerGlobal(PlayerReadyEvent.class, this::onPlayerReady);
    }

    private void onPlayerSetupConnect(PlayerSetupConnectEvent event) {
        if (event.isCancelled()) {
            return;
        }

        PermissionService permissionService = UHC.get().getPermissionService();

        if (!permissionService.hasHost() && !permissionService.canHost(event.getUuid())) {
            event.setReason(UHCMessageUtil.tr("uhc.connection.denied.host").color(Color.RED));
            event.setCancelled(true);
            return;
        }

        FlagService flagService = UHC.get().getFlagService();
        PlayerService playerService = UHC.get().getPlayerService();

        if (!flagService.isEnabled(UHCFlag.NEW_PLAYERS) && !playerService.isParticipant(event.getUuid())) {
            event.setReason(UHCMessageUtil.tr("uhc.connection.denied.started").color(Color.RED));
            event.setCancelled(true);
        }
    }

    private void onPlayerReady(PlayerReadyEvent event) {
        Ref<EntityStore> ref = event.getPlayerRef();
        PlayerRef playerRef = ref.getStore().getComponent(ref, PlayerRef.getComponentType());

        if (playerRef == null || UHC.get().getPlayerService().isParticipant(playerRef.getUuid())) {
            return;
        }

        UHCPlayerUtil.reset(playerRef);
        TeleportUtil.teleport(playerRef, UHC.get().getWorldService().getGameWorld(), LobbyUtil.LOBBY_SPAWN);
    }
}
