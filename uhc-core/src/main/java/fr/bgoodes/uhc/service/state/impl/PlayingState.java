package fr.bgoodes.uhc.service.state.impl;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.service.flag.impl.UHCFlag;
import fr.bgoodes.uhc.service.state.model.IGameState;
import fr.bgoodes.uhc.utils.UHCPlayerUtil;
import fr.bgoodes.uhc.utils.UHCSpawnUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PlayingState implements IGameState {

    public static final String STATE_ID = "playing";

    private volatile boolean ready = false;
    
    @Override
    public void start() {
        UHC.get().getFlagService().setEnabled(UHCFlag.BUILD, true);
        UHC.get().getFlagService().setEnabled(UHCFlag.NEW_PLAYERS, false);

        World world = Universe.get().getDefaultWorld();

        List<CompletableFuture<Void>> spawns = new ArrayList<>();

        for (PlayerRef playerRef : Universe.get().getPlayers()) {
            UHC.get().getPlayerService().addParticipant(playerRef.getUuid());

            UHCPlayerUtil.reset(playerRef);
            spawns.add(UHCSpawnUtil.spawn(playerRef, world));
        }

        CompletableFuture.allOf(spawns.toArray(CompletableFuture[]::new))
                .whenComplete((_, error) -> {
                    if (error != null) {
                        UHC.LOGGER.atSevere().withCause(error).log("Failed to spawn participants");
                    }
                    
                    this.ready = true;
                });
    }

    @Override
    public void onClock() {
        if (!ready) {
            return;
        }

        UHC.LOGGER.atInfo().log("PLAYYYINNNGGGG");
    }
}
