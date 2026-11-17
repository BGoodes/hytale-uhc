package fr.bgoodes.uhc.service.state.impl;

import com.hypixel.hytale.server.core.modules.time.WorldTimeResource;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.service.config.impl.GameConfig;
import fr.bgoodes.uhc.service.flag.impl.UHCFlag;
import fr.bgoodes.uhc.service.state.model.IGameState;
import fr.bgoodes.uhc.service.team.TeamService;
import fr.bgoodes.uhc.util.UHCLobbyUtil;
import fr.bgoodes.uhc.util.UHCMessageUtil;
import fr.bgoodes.uhc.util.UHCPlayerUtil;
import fr.bgoodes.uhc.util.UHCSoundUtil;
import fr.bgoodes.uhc.util.UHCSpawnUtil;
import fr.bgoodes.uhc.util.UHCTimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PlayingState implements IGameState {

    public static final String STATE_ID = "playing";

    private volatile boolean ready = false;
    private int time;

    @Override
    public void start() {
        UHC.get().getFlagService().setEnabled(UHCFlag.BUILD, true);
        UHC.get().getFlagService().setEnabled(UHCFlag.NEW_PLAYERS, false);

        this.ready = false;
        this.time = 0;

        World world = UHC.get().getWorldService().getGameWorld();

        List<CompletableFuture<Void>> spawns = new ArrayList<>();

        for (PlayerRef playerRef : Universe.get().getPlayers()) {
            UHC.get().getPlayerService().addParticipant(playerRef.getUuid());
            createSoloTeam(playerRef);

            UHCPlayerUtil.reset(playerRef);
            spawns.add(UHCSpawnUtil.spawn(playerRef, world));
        }

        CompletableFuture.allOf(spawns.toArray(CompletableFuture[]::new))
                .whenComplete((_, error) -> {
                    if (error != null) {
                        UHC.LOGGER.atSevere().withCause(error).log("Failed to spawn participants");
                    }

                    WorldTimeResource worldTimeResource = world.getEntityStore().getStore().getResource(WorldTimeResource.getResourceType());
                    worldTimeResource.setDayTime(UHCTimeUtil.SUNRISE, world, world.getEntityStore().getStore());

                    UHCLobbyUtil.remove(world);

                    this.ready = true;
                });
    }

    @Override
    public void onClock() {
        if (!ready) {
            return;
        }

        GameConfig config = UHC.get().getGameConfig();

        if (time == config.invincibilityTime) {
            UHC.get().getFlagService().setEnabled(UHCFlag.DAMAGE, true);
            UHCMessageUtil.broadcast(UHCMessageUtil.tr("uhc.game.invincibility.ended.chat"));
            UHCSoundUtil.broadcast("SFX_Drum_Stinger");
        }

        if (time == config.pvpTime) {
            UHC.get().getFlagService().setEnabled(UHCFlag.PVP, true);
            UHCMessageUtil.broadcast(UHCMessageUtil.tr("uhc.game.pvp.enabled.chat"));
            UHCSoundUtil.broadcast("SFX_Drum_Stinger");
        }

        time++;
    }

    private void createSoloTeam(PlayerRef playerRef) {
        TeamService teamService = UHC.get().getTeamService();
        String teamId = playerRef.getUuid().toString();

        if (teamService.getPlayerTeam(playerRef.getUuid()) != null) {
            return;
        }

        teamService.createTeam(teamId, playerRef.getUsername(), null);
        teamService.addPlayer(teamId, playerRef.getUuid());
    }
}
