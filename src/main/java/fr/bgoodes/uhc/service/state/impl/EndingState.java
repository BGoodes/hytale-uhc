package fr.bgoodes.uhc.service.state.impl;

import com.hypixel.hytale.server.core.HytaleServer;
import fr.bgoodes.uhc.context.GameContext;
import fr.bgoodes.uhc.service.state.model.IGameState;
import fr.bgoodes.uhc.util.MessageUtil;
import org.jetbrains.annotations.NotNull;

public class EndingState implements IGameState {

    public static final String STATE_ID = "ending";

    private int time;

    @Override
    public void start(@NotNull GameContext context) {

        MessageUtil.broadcastTitle("Fin de la partie", "", true);

        context.getGamerules().allowNewPlayers(false);
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
}
