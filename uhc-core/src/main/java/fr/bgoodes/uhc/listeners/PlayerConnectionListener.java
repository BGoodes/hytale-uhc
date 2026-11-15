package fr.bgoodes.uhc.listeners;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.PlayerSetupConnectEvent;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.service.flag.FlagService;
import fr.bgoodes.uhc.service.flag.impl.UHCFlag;
import fr.bgoodes.uhc.service.permission.PermissionService;
import fr.bgoodes.uhc.service.player.PlayerService;
import fr.bgoodes.uhc.utils.UHCMessageUtil;

import java.awt.*;

public class PlayerConnectionListener {

    public void register(EventRegistry eventRegistry) {
        eventRegistry.register(PlayerSetupConnectEvent.class, this::onPlayerSetupConnect);
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
}
