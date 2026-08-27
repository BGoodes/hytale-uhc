package fr.bgoodes.uhc.util;

import com.hypixel.hytale.component.ComponentAccessor;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.RemoveReason;
import com.hypixel.hytale.server.core.inventory.InventoryUtils;
import com.hypixel.hytale.server.core.modules.entity.gamemode.GameModeTypes;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.NotNull;

public class SpectatorUtil {

    public static final String GAME_MODE_TYPE_ID = "Spectator";

    private SpectatorUtil() {}

    public static void addSpectator(@NotNull PlayerRef playerRef, @NotNull ComponentAccessor<EntityStore> accessor) {
        Ref<EntityStore> ref = playerRef.getReference();

        if (ref != null && ref.isValid()) {
            InventoryUtils.clear(ref, accessor);
            Teleport teleport = Teleport.createForPlayer(LobbyUtil.LOBBY_SPAWN);
            accessor.addComponent(ref, Teleport.getComponentType(), teleport);

            GameModeTypes.enter(ref, accessor, GAME_MODE_TYPE_ID);
        }
    }
}
