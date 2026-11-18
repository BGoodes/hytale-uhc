package fr.bgoodes.uhc.service.state.impl;

import com.hypixel.hytale.server.core.Message;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.service.state.model.IGameState;
import fr.bgoodes.uhc.util.UHCMessageUtil;
import fr.bgoodes.uhc.util.UHCSoundUtil;

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
            UHCMessageUtil.broadcast(Message.raw(String.valueOf(time)));
//            hudService.showTitle(world, store, Message.raw(String.valueOf(time)), null, TITLE_DURATION);
            UHCSoundUtil.broadcast("SFX_Axe_Iron_Impact");
        } else {
            UHCMessageUtil.broadcast(UHCMessageUtil.tr("uhc.game.started.chat"));
            UHCSoundUtil.broadcast("SFX_Rex_Alerted");
            UHC.get().getStateService().changeGameState(PlayingState.STATE_ID);
        }

        time--;
    }
}
