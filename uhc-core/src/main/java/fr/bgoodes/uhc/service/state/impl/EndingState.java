package fr.bgoodes.uhc.service.state.impl;

import com.hypixel.hytale.server.core.HytaleServer;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.service.flag.impl.UHCFlag;
import fr.bgoodes.uhc.service.state.model.IGameState;
import fr.bgoodes.uhc.util.UHCMessageUtil;

public class EndingState implements IGameState {

    public static final String STATE_ID = "ending";

    private int time;

    @Override
    public void start() {
        UHC.get().getFlagService().setEnabled(UHCFlag.PVP, true);
        UHC.get().getFlagService().setEnabled(UHCFlag.BUILD, true);
        UHC.get().getFlagService().setEnabled(UHCFlag.GAME_ENDED, true);

        UHCMessageUtil.broadcast(UHCMessageUtil.tr("uhc.game.end.closing"));

        this.time = 60;
    }

    @Override
    public void onClock() {
        if (time <= 0) {
            HytaleServer.get().shutdownServer();
        }

        this.time --;
    }
}
