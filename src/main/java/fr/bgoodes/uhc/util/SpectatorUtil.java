package fr.bgoodes.uhc.util;

import com.hypixel.hytale.component.ComponentAccessor;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.InventoryUtils;
import com.hypixel.hytale.server.core.modules.entity.component.HiddenFromAdventurePlayers;
import com.hypixel.hytale.server.core.modules.entity.component.Intangible;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.ecs.components.Spectator;
import org.jetbrains.annotations.NotNull;

public class SpectatorUtil {

    private SpectatorUtil() {}

    public static void addSpectator(@NotNull PlayerRef playerRef, @NotNull ComponentAccessor<EntityStore> accessor) {
        Ref<EntityStore> ref = playerRef.getReference();

        if (ref != null && ref.isValid() && accessor.getComponent(ref, Spectator.getComponentType()) == null) {
            InventoryUtils.clear(ref, accessor);
            Player.setGameMode(ref, GameMode.Creative, accessor);

            EntityStatMap statMap = accessor.getComponent(ref, EntityStatMap.getComponentType());
            if (statMap != null) {
                statMap.maximizeStatValue(DefaultEntityStatTypes.getHealth());
            }

            accessor.addComponent(ref, HiddenFromAdventurePlayers.getComponentType());
            accessor.addComponent(ref, Intangible.getComponentType());
            accessor.addComponent(ref, Spectator.getComponentType());

            // teleport
            Teleport teleport = Teleport.createForPlayer(LobbyUtil.LOBBY_SPAWN);
            accessor.addComponent(ref, Teleport.getComponentType(), teleport);
        }
    }
}
