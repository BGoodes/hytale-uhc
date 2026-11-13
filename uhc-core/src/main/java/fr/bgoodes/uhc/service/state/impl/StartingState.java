package fr.bgoodes.uhc.service.state.impl;

import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.service.state.model.IGameState;
import fr.bgoodes.uhc.utils.UHCMessageUtil;
import fr.bgoodes.uhc.utils.UHCSoundUtil;

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
//            hudService.showTitle(world, store, Message.raw(String.valueOf(time)), null, TITLE_DURATION);
//            BroadcastUtil.sound("SFX_Axe_Iron_Impact");
            UHC.LOGGER.atInfo().log(String.valueOf(time));
        } else {
            UHCMessageUtil.broadcast(UHCMessageUtil.tr("uhc.gameState.starting.start"));
            UHCSoundUtil.broadcast("SFX_Rex_Alerted");
            UHC.get().getStateService().changeGameState(PlayingState.STATE_ID);
        }

        time--;
    }
}
