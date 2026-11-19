package fr.bgoodes.uhc.service.state.impl;

import com.hypixel.hytale.server.core.Message;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.service.state.model.IGameState;
import fr.bgoodes.uhc.util.UHCSoundUtil;
import fr.bgoodes.uhc.util.UHCTitleUtil;

public class StartingState implements IGameState {

    private int time;

    public static final String STATE_ID = "starting";

    @Override
    public void start() {
        this.time = 5;
    }

    @Override
    public void onClock() {
        if (time > 0) {
            UHCTitleUtil.broadcast(Message.raw(String.valueOf(time)), null, 1);
            UHCSoundUtil.broadcast("SFX_Axe_Iron_Impact");
        } else {
            UHCSoundUtil.broadcast("SFX_Rex_Alerted");
            UHC.get().getStateService().changeGameState(PlayingState.STATE_ID);
        }

        time--;
    }
}
