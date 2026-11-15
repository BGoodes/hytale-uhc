package fr.bgoodes.uhc.service.permission;

import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import fr.bgoodes.uhc.ServerConfig;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PermissionService {

    private @Nullable UUID host;

    public static final String HOST_GROUP = "uhc:Host";
    public static final String SPECTATOR_GROUP = "uhc:Spectator";
    public static final String HOST_PERMISSION = "uhc.game.host";

    public synchronized void initialize(ServerConfig serverConfig) {
        createGroup(HOST_GROUP, serverConfig.getHost().getPermissions());
        createGroup(SPECTATOR_GROUP, serverConfig.getSpectator().getPermissions());
    }

    public synchronized boolean hasHost() {
        return host != null;
    }

    public synchronized boolean isHost(UUID uuid) {
        return uuid.equals(host);
    }

    public boolean canHost(UUID uuid) {
        return PermissionsModule.get().hasPermission(uuid, HOST_PERMISSION);
    }

    public synchronized void setHost(UUID uuid) {
        removeHost();

        host = uuid;
        PermissionsModule.get().addUserToGroup(uuid, HOST_GROUP);
    }

    public synchronized void removeHost() {
        if (host != null) {
            PermissionsModule.get().removeUserFromGroup(host, HOST_GROUP);
            host = null;
        }
    }

    public synchronized void addSpectator(UUID uuid) {
        PermissionsModule.get().addUserToGroup(uuid, SPECTATOR_GROUP);
    }

    public synchronized void removeSpectator(UUID uuid) {
        PermissionsModule.get().removeUserFromGroup(uuid, SPECTATOR_GROUP);
    }

    private void createGroup(String group, Set<String> permissions) {
        if (!PermissionsModule.get().getAllRegisteredGroups().contains(group)) {
            PermissionsModule.get().addGroupPermission(group, permissions);
        }
    }
}
