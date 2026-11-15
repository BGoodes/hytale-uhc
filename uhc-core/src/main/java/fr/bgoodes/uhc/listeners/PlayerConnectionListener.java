package fr.bgoodes.uhc.listeners;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.PlayerSetupConnectEvent;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.service.permission.PermissionService;
import fr.bgoodes.uhc.utils.UHCMessageUtil;

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
            event.setReason(UHCMessageUtil.tr("uhc.host.missing.disconnect"));
            event.setCancelled(true);
        }
    }
}
