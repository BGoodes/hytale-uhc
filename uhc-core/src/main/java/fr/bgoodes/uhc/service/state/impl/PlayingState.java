package fr.bgoodes.uhc.service.state.impl;

import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.service.state.model.IGameState;

public class PlayingState implements IGameState {

    public static final String STATE_ID = "playing";

    @Override
    public void start() {}

    @Override
    public void onClock() {
        UHC.LOGGER.atInfo().log("PLAYYYINNNGGGG");
    }
}
