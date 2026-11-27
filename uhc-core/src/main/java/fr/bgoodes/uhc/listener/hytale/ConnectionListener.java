package fr.bgoodes.uhc.listener.hytale;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerSetupConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.RemovedPlayerFromWorldEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.service.flag.impl.UHCFlag;
import fr.bgoodes.uhc.service.PermissionService;
import fr.bgoodes.uhc.util.UHCTeleportUtil;
import fr.bgoodes.uhc.util.UHCLobbyUtil;
import fr.bgoodes.uhc.util.UHCMessageUtil;
import fr.bgoodes.uhc.util.UHCPlayerUtil;

import java.awt.*;
import java.util.UUID;

public class ConnectionListener {

    public void register(EventRegistry eventRegistry) {
        eventRegistry.register(PlayerSetupConnectEvent.class, this::onPlayerSetupConnect);
        eventRegistry.registerGlobal(PlayerReadyEvent.class, this::onPlayerReady);
        eventRegistry.register(PlayerDisconnectEvent.class, this::onPlayerDisconnect);
        eventRegistry.registerGlobal(AddPlayerToWorldEvent.class, this::onAddPlayerToWorld);
        eventRegistry.registerGlobal(RemovedPlayerFromWorldEvent.class, this::onRemovedPlayerFromWorld);
    }

    private void onPlayerSetupConnect(PlayerSetupConnectEvent event) {
        if (event.isCancelled()) {
            return;
        }

        PermissionService permissionService = UHC.get().getPermissionService();
        permissionService.resetRoles(event.getUuid());

        if (!permissionService.hasHost() && !permissionService.canHost(event.getUuid())) {
            event.setReason(UHCMessageUtil.tr("uhc.connection.denied.host").color(Color.RED));
            event.setCancelled(true);
            return;
        }

        if (!UHC.get().getPlayerService().isParticipant(event.getUuid())) {
            Universe.get().getPlayerStorage().save(event.getUuid(), EntityStore.REGISTRY.newHolder(), true);
        }
    }

    private void onPlayerReady(PlayerReadyEvent event) {
        Ref<EntityStore> ref = event.getPlayerRef();
        PlayerRef playerRef = ref.getStore().getComponent(ref, PlayerRef.getComponentType());

        if (playerRef == null) {
            return;
        }

        UHC.get().getPlayerService().removeDisconnected(playerRef.getUuid());

        if (UHC.get().getPlayerService().isParticipant(playerRef.getUuid())) {
            return;
        }

        if (!UHC.get().getFlagService().isEnabled(UHCFlag.NEW_PLAYERS)) {
            UHCPlayerUtil.setSpectator(playerRef);
            return;
        }

        UHCPlayerUtil.reset(playerRef);
        UHCTeleportUtil.teleport(playerRef, UHC.get().getWorldService().getGameWorld(), UHCLobbyUtil.LOBBY_SPAWN);
    }

    private void onPlayerDisconnect(PlayerDisconnectEvent event) {
        UUID uuid = event.getPlayerRef().getUuid();
        if (UHC.get().getPlayerService().isParticipant(uuid)) {
            UHC.get().getPlayerService().setDisconnected(uuid);
        }
    }

    private void onAddPlayerToWorld(AddPlayerToWorldEvent event) {
        PlayerRef playerRef = event.getHolder().getComponent(PlayerRef.getComponentType());

        if (playerRef != null) {
            event.setJoinMessage(Message.join(
                    UHCMessageUtil.tag("+", UHCMessageUtil.SUCCESS_COLOR),
                    Message.raw(playerRef.getUsername()).bold(true)
            ));
        }
    }

    private void onRemovedPlayerFromWorld(RemovedPlayerFromWorldEvent event) {
        PlayerRef playerRef = event.getHolder().getComponent(PlayerRef.getComponentType());

        if (playerRef != null) {
            event.setLeaveMessage(Message.join(
                    UHCMessageUtil.tag("-", UHCMessageUtil.ERROR_COLOR),
                    Message.raw(playerRef.getUsername()).bold(true)
            ));
        }
    }
}
