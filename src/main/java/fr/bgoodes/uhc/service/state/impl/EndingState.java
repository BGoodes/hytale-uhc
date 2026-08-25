package fr.bgoodes.uhc.service.state.impl;

import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.context.GameContext;
import fr.bgoodes.uhc.service.state.model.IGameState;
import fr.bgoodes.uhc.util.BroadcastUtil;
import fr.bgoodes.uhc.util.MessageUtil;
import org.jetbrains.annotations.NotNull;

public class EndingState implements IGameState {

    public static final String STATE_ID = "ending";

    private int time;

    @Override
    public void start(@NotNull GameContext context) {

        BroadcastUtil.title(MessageUtil.tr("uhc.gameState.ending"), null, true);

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
