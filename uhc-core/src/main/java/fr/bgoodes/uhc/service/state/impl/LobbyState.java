package fr.bgoodes.uhc.service.state.impl;

import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.service.flag.impl.UHCFlag;
import fr.bgoodes.uhc.service.state.model.IGameState;

public class LobbyState implements IGameState {

    public static final String STATE_ID = "lobby";

    @Override
    public void start() {
        UHC.get().getFlagService().setEnabled(UHCFlag.NEW_PLAYERS, true);
        UHC.get().getFlagService().setEnabled(UHCFlag.CONFIG, true);
    }

    @Override
    public void end() {
        UHC.get().getFlagService().setEnabled(UHCFlag.CONFIG, false);
    }
}
