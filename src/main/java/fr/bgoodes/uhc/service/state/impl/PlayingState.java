package fr.bgoodes.uhc.service.state.impl;

import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.InventoryUtils;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.modules.time.WorldTimeResource;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.UHCConfig;
import fr.bgoodes.uhc.context.GameContext;
import fr.bgoodes.uhc.service.hud.HUDService;
import fr.bgoodes.uhc.service.player.PlayerService;
import fr.bgoodes.uhc.service.state.model.IGameState;
import fr.bgoodes.uhc.service.team.Team;
import fr.bgoodes.uhc.service.team.TeamService;
import fr.bgoodes.uhc.util.BroadcastUtil;
import fr.bgoodes.uhc.util.LobbyUtil;
import fr.bgoodes.uhc.util.MessageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class PlayingState implements IGameState {

    public static final String STATE_ID = "playing";

    private final HUDService hudService = UHC.get().getHudService();
    private final PlayerService playerService = UHC.get().getPlayerService();
    private final TeamService teamService = UHC.get().getTeamService();

    private GameContext gameContext;
    private int time = 0;

    @Override
    public void start(@NotNull GameContext context) {

        this.gameContext = context;
        World world = context.getWorld();

        // update gamerules
        context.getGamerules().allowNewPlayers(false);
        context.getGamerules().pvp(false);
        context.getGamerules().building(true);
        context.getGamerules().invincible(true);

        // teleport & gamemode adventure
        Teleport teleport = Teleport.createForPlayer(LobbyUtil.LOBBY_SPAWN);
        Store<EntityStore> store = world.getEntityStore().getStore();

        Universe.get().getPlayers().forEach(playerRef -> {
            playerService.addParticipant(playerRef.getUuid());
            teamService.createPersonalTeam(playerRef.getUuid(), playerRef.getUsername());

            if (playerRef.isValid() && playerRef.getReference() != null) {
                Player.setGameMode(playerRef.getReference(), GameMode.Adventure, store);
                store.addComponent(playerRef.getReference(), Teleport.getComponentType(), teleport);

                //clear
                InventoryUtils.clear(playerRef.getReference(), store);

                // heal
                EntityStatMap statMap = store.getComponent(playerRef.getReference(), EntityStatMap.getComponentType());
                if (statMap != null) {
                    statMap.maximizeStatValue(DefaultEntityStatTypes.getHealth());
                }
            }
        });

        //delete lobby
        LobbyUtil.remove(world);

        hudService.showTimer(world, store);

        // time set day
        WorldTimeResource worldTimeResource = world.getEntityStore().getStore().getResource(WorldTimeResource.getResourceType());
        worldTimeResource.setDayTime(0.2, world, world.getEntityStore().getStore());

        this.time = 0;
    }

    @Override
    public void onClock(@NotNull World world, @NotNull Store<EntityStore> store) {

        if (this.time == this.gameContext.getConfig().invincibilityTime) {
            this.gameContext.getGamerules().invincible(false);
            BroadcastUtil.message(MessageUtil.tr("uhc.gameState.playing.invincibility.off"));
            BroadcastUtil.sound("SFX_Drum_Stinger");
            hudService.showTitle(world, store,
                    MessageUtil.tr("uhc.hud.title.invincibility.off"), null);
        }

        if (this.time == this.gameContext.getConfig().pvpTime) {
            this.gameContext.getGamerules().pvp(true);
            BroadcastUtil.message(MessageUtil.tr("uhc.gameState.playing.pvp.on"));
            BroadcastUtil.sound("SFX_Drum_Stinger");
            hudService.showTitle(world, store,
                    MessageUtil.tr("uhc.hud.title.pvp.on"), null);
        }

        if (this.time == this.gameContext.getConfig().borderShrinkTime) {
            this.gameContext.getGamerules().borderShrinking(true);
            BroadcastUtil.message(MessageUtil.tr("uhc.gameState.playing.border"));
            BroadcastUtil.sound("SFX_Drum_Stinger");
            hudService.showTitle(world, store,
                    MessageUtil.tr("uhc.hud.title.border"), null);
        }

        // check to end the game
        if (this.gameContext.getConfig().allowVictory) {
            Set<Team> remainingTeams = playerService.getParticipants().stream()
                    .map(teamService::getPlayerTeam)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            if (remainingTeams.size() <= 1) {
                BroadcastUtil.message(MessageUtil.tr("uhc.gameState.ending"));
                BroadcastUtil.sound("SFX_Rex_Alerted");
                UHC.get().getStateService().changeGameState(EndingState.STATE_ID);
                return;
            }
        }

        NextEvent nextEvent = computeNextEvent();
        Message phaseLabel = nextEvent == null ? null : nextEvent.label();
        long secondsRemaining = nextEvent == null ? 0 : nextEvent.seconds();
        hudService.updateTimer(world, store, phaseLabel, secondsRemaining, time);

        this.time++;
    }

    //TODO: refacto phase management (is a phase a state ??)
    private @Nullable NextEvent computeNextEvent() {
        UHCConfig config = this.gameContext.getConfig();

        if (time < config.invincibilityTime) {
            return new NextEvent(MessageUtil.tr("uhc.hud.timer.phase.invincibility"), config.invincibilityTime - time);
        }

        if (time < config.pvpTime) {
            return new NextEvent(MessageUtil.tr("uhc.hud.timer.phase.pvp"), config.pvpTime - time);
        }

        if (time < config.borderShrinkTime) {
            return new NextEvent(MessageUtil.tr("uhc.hud.timer.phase.border"), config.borderShrinkTime - time);
        }

        return null;
    }

    private record NextEvent(@NotNull Message label, long seconds) {}
}
