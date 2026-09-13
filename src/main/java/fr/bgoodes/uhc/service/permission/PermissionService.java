package fr.bgoodes.uhc.service.permission;

import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import fr.bgoodes.uhc.UHC;

import java.util.Set;
import java.util.UUID;

public class PermissionService {

    private UUID hostID;

    public void setup() {
        PermissionsModule permissionsModule = PermissionsModule.get();
        UHC.LOGGER.atInfo().log(String.join(", ", permissionsModule.getAllRegisteredGroups()));

        // staff
        if (!permissionsModule.getAllRegisteredGroups().contains("Staff")) {
            Set<String> staffPerms = Set.of("uhc.host.claim");
            permissionsModule.addGroupPermission("Staff",  staffPerms);
        }

        // host
        if (!permissionsModule.getAllRegisteredGroups().contains("Host")) {
            Set<String> hostPerms = Set.of(
                    "uhc.host.*",
                    "hytale.command.gamemode.*",
                    "hytale.command.teleport.*",
                    "hytale.teleport.command.tp",
                    "hytale.command.give.*",
                    "hytale.system.command.inventory.clear",
                    "hytale.command.kick.*",
                    "hytale.command.kill.*",
                    "hytale.accesscontrolmodule.command.*",
                    "hytale.universe.command.world.settings.pregenerate.*",
                    "hytale.universe.command.world.perf.*",
                    "hytale.system.command.stop"
            );
            permissionsModule.addGroupPermission("Host", hostPerms);
        }
    }

    public void shutdown() {
        removeHost();

        PermissionsModule.get().removeUserPermissionFromAll("hytale.teleport.command.tp");
        PermissionsModule.get().removeUserPermissionFromAll("hytale.command.teleport.self");
    }

    public Boolean hasHost() {
        return this.hostID != null;
    }

    public Boolean canClaimHost(UUID playerUuid) {
        return PermissionsModule.get().hasPermission(playerUuid, "uhc.host.claim");
    }

    public void setHost(UUID playerUuid) {
        removeHost();
        PermissionsModule.get().addUserToGroup(playerUuid, "Host");
        this.hostID = playerUuid;
    }

    public void removeHost() {
        if (hostID != null) {
            PermissionsModule.get().removeUserFromGroup(hostID, "Host");
            this.hostID = null;
        }
    }

    public void addSpectator(UUID uuid) {
        PermissionsModule.get().addUserPermission(uuid, Set.of("hytale.teleport.command.tp"));
        PermissionsModule.get().addUserPermission(uuid, Set.of("hytale.command.teleport.self"));
    }
}
