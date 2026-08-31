package fr.bgoodes.uhc.service.state.impl;

import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.context.GameContext;
import fr.bgoodes.uhc.service.hud.HUDService;
import fr.bgoodes.uhc.service.state.model.IGameState;
import fr.bgoodes.uhc.util.BroadcastUtil;
import fr.bgoodes.uhc.util.MessageUtil;
import org.jetbrains.annotations.NotNull;

public class StartingState implements IGameState {

    public static final String STATE_ID = "starting";

    private static final long TITLE_DURATION = 2;

    private final HUDService hudService = UHC.get().getHudService();

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
    public void onClock(@NotNull World world, @NotNull Store<EntityStore> store) {

        if (time > 0) {
            hudService.showTitle(world, store, Message.raw(String.valueOf(time)), null, TITLE_DURATION);
            BroadcastUtil.sound("SFX_Axe_Iron_Impact");
        } else {
            BroadcastUtil.message(MessageUtil.tr("uhc.gameState.starting.start"));
            BroadcastUtil.sound("SFX_Rex_Alerted");
            UHC.get().getStateService().changeGameState(PlayingState.STATE_ID);
        }

        time--;
    }
}
