package fr.bgoodes.uhc.service.state.impl;

import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.context.GameContext;
import fr.bgoodes.uhc.service.hud.HUDService;
import fr.bgoodes.uhc.service.player.PlayerService;
import fr.bgoodes.uhc.service.state.model.IGameState;
import fr.bgoodes.uhc.util.BroadcastUtil;
import fr.bgoodes.uhc.util.MessageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EndingState implements IGameState {

    public static final String STATE_ID = "ending";

    private final HUDService hudService = UHC.get().getHudService();
    private final PlayerService playerService = UHC.get().getPlayerService();

    private int time;

    @Override
    public void start(@NotNull GameContext context) {

        World world = context.getWorld();
        Store<EntityStore> store = world.getEntityStore().getStore();

        @Nullable PlayerRef winner = playerService.getWinner().orElse(null);
        Message winnerLine = winner == null ? null :
                MessageUtil.tr("uhc.hud.title.ending.winner").param("name", winner.getUsername());

        hudService.showTitle(world, store, MessageUtil.tr("uhc.gameState.ending"), winnerLine, 8);

        hudService.hideTimer(world, store);

        context.getGamerules().allowNewPlayers(false);
        context.getGamerules().pvp(true);
        context.getGamerules().building(true);
        context.getGamerules().invincible(false);

        this.time = 60;
        BroadcastUtil.message(MessageUtil.tr("uhc.gameState.ending.closing"));
    }

    @Override
    public void onClock(@NotNull World world, @NotNull Store<EntityStore> store) {

        if (time <= 0) {
            HytaleServer.get().shutdownServer();
        }

        this.time --;
    }
}
