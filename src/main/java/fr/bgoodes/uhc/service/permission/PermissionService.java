package fr.bgoodes.uhc.service.permission;

import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import fr.bgoodes.uhc.UHC;
import org.jetbrains.annotations.NotNull;

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
                    "hytale.command.give.*",
                    "hytale.command.kick.*",
                    "hytale.accesscontrolmodule.command.*"
            );
            permissionsModule.addGroupPermission("Host", hostPerms);
        }
    }

    public Boolean hasHost() {
        return this.hostID != null;
    }

    public Boolean canClaimHost(UUID uuid) {
        return PermissionsModule.get().hasPermission(uuid, "uhc.host.claim");
    }

    public void setHost(UUID uuid) {
        PermissionsModule.get().addUserToGroup(uuid, "Host");
        this.hostID = uuid;
    }

    public void removeHost() {
        PermissionsModule.get().removeUserFromGroup(hostID, "Host");
        this.hostID = null;
    }
}
