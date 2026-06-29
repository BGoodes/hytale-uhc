package fr.bgoodes.uhc.service.state.impl;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.inventory.InventoryUtils;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.context.GameContext;
import fr.bgoodes.uhc.service.state.model.IGameState;
import fr.bgoodes.uhc.util.MessageUtil;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class EndingState implements IGameState {

    public static final String STATE_ID = "ending";

    private int time;

    @Override
    public void start(@NotNull GameContext context) {

        MessageUtil.broadcastTitle("Fin de la partie", "", true);

        context.getGamerules().allowNewPlayers(true);
        context.getGamerules().pvp(true);
        context.getGamerules().building(true);
        context.getGamerules().invincible(false);

        this.time = 60;
        MessageUtil.broadcastMessage("Fermeture du serveur dans 1 minute.");
    }

    @Override
    public void onClock() {

        if (time <= 0) {
            HytaleServer.get().shutdownServer();
        }

        this.time --;
    }

    @Override
    public void onPlayerJoin(@NotNull PlayerReadyEvent event, @NotNull GameContext context) {
        if (!event.getPlayerRef().isValid()) {
            return;
        }

        Ref<EntityStore> playerRef = event.getPlayerRef();
        Store<EntityStore> store = playerRef.getStore();

        Player.setGameMode(playerRef, GameMode.Creative, store);
    }
}
