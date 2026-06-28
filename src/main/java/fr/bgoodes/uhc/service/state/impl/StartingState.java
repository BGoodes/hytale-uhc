package fr.bgoodes.uhc.service.state.impl;

import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.context.GameContext;
import fr.bgoodes.uhc.service.state.model.IGameState;
import fr.bgoodes.uhc.util.MessageUtil;
import org.jetbrains.annotations.NotNull;

public class StartingState implements IGameState {

    public static final String STATE_ID = "starting";

    private int time;

    @Override
    public void start(@NotNull GameContext context) {

        context.getGamerules().allowNewPlayers(false);
        context.getGamerules().pvp(false);
        context.getGamerules().building(false);
        context.getGamerules().invincible(true);

        this.time = 5;
    }

    @Override
    public void onClock() {

        if (time > 0) {
            MessageUtil.broadcastMessage("Début de la partie dans %ss".formatted(time));
            MessageUtil.broadcastSound("SFX_Axe_Iron_Impact");
        } else {
            MessageUtil.broadcastMessage("Début de la partie !!!");
            MessageUtil.broadcastSound("SFX_Rex_Alerted");
            UHC.get().getStateService().changeGameState(PlayingState.STATE_ID);
        }

        time--;
    }
}
