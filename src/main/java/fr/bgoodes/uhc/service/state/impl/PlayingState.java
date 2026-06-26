package fr.bgoodes.uhc.service.state.impl;

import com.hypixel.hytale.server.core.universe.world.World;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.context.GameContext;
import fr.bgoodes.uhc.service.state.model.IGameState;
import fr.bgoodes.uhc.util.BroadcastUtil;
import fr.bgoodes.uhc.util.LobbyUtil;

public class PlayingState implements IGameState {

    public static final String STATE_ID = "playing";

    private final GameContext gameContext = UHC.get().getGameContext();

    private int time;

    @Override
    public void onClock() {

        if (time == 60) {
            gameContext.getGamerules().invincible(false);
            BroadcastUtil.message("Vous n'êtes désormais plus invincible.");
            BroadcastUtil.sound("SFX_Crow_Death");
        }

        time++;
    }

    @Override
    public void onEnter() {
        World world = UHC.get().getGameContext().getWorld();

        gameContext.getGamerules().pvp(false);
        gameContext.getGamerules().building(true);
        gameContext.getGamerules().invincible(true);

        LobbyUtil.remove(world);

        this.time = 0;
    }

    @Override
    public void onExit() {}
}
