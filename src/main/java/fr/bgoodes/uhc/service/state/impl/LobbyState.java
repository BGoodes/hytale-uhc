package fr.bgoodes.uhc.service.state.impl;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.inventory.InventoryUtils;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.context.GameContext;
import fr.bgoodes.uhc.service.state.model.IGameState;
import fr.bgoodes.uhc.util.LobbyUtil;
import org.jetbrains.annotations.NotNull;

public class LobbyState implements IGameState {

    public static final String STATE_ID = "lobby";

    @Override
    public void start(@NotNull GameContext context) {
        context.getGamerules().allowNewPlayers(true);
        context.getGamerules().pvp(false);
        context.getGamerules().building(false);
        context.getGamerules().invincible(true);
    }

    @Override
    public void onPlayerJoin(@NotNull PlayerReadyEvent event, @NotNull GameContext context) {
        if (!event.getPlayerRef().isValid()) {
            return;
        }

        Ref<EntityStore> playerRef = event.getPlayerRef();
        Store<EntityStore> store = playerRef.getStore();

        // heal
        EntityStatMap statMap = store.getComponent(playerRef, EntityStatMap.getComponentType());
        if (statMap != null) {
            statMap.maximizeStatValue(DefaultEntityStatTypes.getHealth());
        }

        // clear inventory & set gamemode
        InventoryUtils.clear(playerRef, store);
        Player.setGameMode(playerRef, GameMode.Adventure, store);

        // teleport
        Teleport teleport = Teleport.createForPlayer(LobbyUtil.LOBBY_SPAWN);
        store.addComponent(playerRef, Teleport.getComponentType(), teleport);
    }
}
