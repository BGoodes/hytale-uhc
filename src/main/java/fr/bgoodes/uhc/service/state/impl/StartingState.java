package fr.bgoodes.uhc.service.state.impl;

import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.context.GameContext;
import fr.bgoodes.uhc.service.state.model.IGameState;
import fr.bgoodes.uhc.util.BroadcastUtil;
import fr.bgoodes.uhc.util.LobbyUtil;

public class StartingState implements IGameState {

    public static final String STATE_ID = "starting";

    private int time;

    @Override
    public void onClock() {

        if (time > 0) {
            BroadcastUtil.message("Début de la partie dans %ss".formatted(time));
            BroadcastUtil.sound("SFX_Axe_Iron_Impact");
        } else {
            UHC.get().getStateService().changeGameState(PlayingState.STATE_ID);
            BroadcastUtil.message("Début de la partie !!!");
            BroadcastUtil.sound("SFX_Rex_Alerted");
        }

        time--;
    }

    @Override
    public void onEnter() {
        GameContext gameContext = UHC.get().getGameContext();

        gameContext.getGamerules().pvp(false);
        gameContext.getGamerules().building(false);
        gameContext.getGamerules().invincible(true);

        this.time = 5;
    }

    @Override
    public void onExit() {}
}
