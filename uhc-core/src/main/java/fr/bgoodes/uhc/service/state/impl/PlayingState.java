package fr.bgoodes.uhc.service.state.impl;

import com.hypixel.hytale.event.IEventBus;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.modules.time.WorldTimeResource;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.event.game.BorderShrinkEvent;
import fr.bgoodes.uhc.event.game.GameStartedEvent;
import fr.bgoodes.uhc.event.game.InvincibilityEndedEvent;
import fr.bgoodes.uhc.event.game.PvpEnabledEvent;
import fr.bgoodes.uhc.service.config.impl.game.BorderPhase;
import fr.bgoodes.uhc.service.config.impl.game.GameConfig;
import fr.bgoodes.uhc.service.flag.FlagService;
import fr.bgoodes.uhc.service.flag.impl.UHCFlag;
import fr.bgoodes.uhc.service.hud.impl.PlayingHud;
import fr.bgoodes.uhc.service.hud.model.UHCHud;
import fr.bgoodes.uhc.service.state.model.IGameState;
import fr.bgoodes.uhc.util.UHCKitUtil;
import fr.bgoodes.uhc.util.UHCLobbyUtil;
import fr.bgoodes.uhc.util.UHCPlayerUtil;
import fr.bgoodes.uhc.util.UHCTeamUtil;
import fr.bgoodes.uhc.util.UHCTimeUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class PlayingState implements IGameState {

    public static final String STATE_ID = "playing";

    private volatile boolean ready = false;
    private int time;

    @Override
    public void start() {
        this.ready = false;
        this.time = 0;

        prepareGame();
        assignTeams();
        setupParticipants();

        World world = UHC.get().getWorldService().getGameWorld();

        spawnParticipants(world).whenCompleteAsync((_, error) -> {
            if (error != null) {
                UHC.LOGGER.atSevere().withCause(error).log("Failed to spawn participants");
            }

            onSpawned(world);
            dispatchTimedEvents();

            this.ready = true;

        }, world);
    }

    @Override
    public void onClock() {
        if (!ready) {
            return;
        }

        time++;
        dispatchTimedEvents();
    }

    public int getTime() {
        return time;
    }

    @Override
    public @Nullable UHCHud createHud(@NotNull PlayerRef playerRef) {
        return new PlayingHud(playerRef, this);
    }

    protected void prepareGame() {
        FlagService flagService = UHC.get().getFlagService();

        flagService.setEnabled(UHCFlag.NEW_PLAYERS, false);
        flagService.setEnabled(UHCFlag.HUD_STATS, true);

        UHC.get().getWorldBorderService().setRadius(UHC.get().getGameConfig().borderStartRadius.get());
    }

    protected void assignTeams() {
        UHCTeamUtil.removeOfflineMembers();

        if (UHCTeamUtil.isSolo()) {
            Universe.get().getPlayers().forEach(UHCTeamUtil::createSoloTeam);
        } else {
            UHCTeamUtil.fillTeams();
        }

        UHCTeamUtil.removeEmptyTeams();
    }

    protected void setupParticipants() {
        for (PlayerRef playerRef : Universe.get().getPlayers()) {
            if (UHC.get().getTeamService().getPlayerTeam(playerRef.getUuid()) == null) {
                UHCPlayerUtil.setSpectator(playerRef);
                continue;
            }

            UHC.get().getPlayerService().addParticipant(playerRef.getUuid());
            UHCPlayerUtil.reset(playerRef);
            UHCKitUtil.give(playerRef, UHC.get().getGameConfig().startInventory.get());
        }
    }

    protected @NotNull CompletableFuture<Void> spawnParticipants(@NotNull World world) {
        GameConfig config = UHC.get().getGameConfig();
        int radius = config.spawnCenter.get() ? 0 : config.borderStartRadius.get();

        return UHC.get().getSpawnService().spawnTeams(world, UHC.get().getTeamService().getTeams(), radius, config.teamSpawnTogether.get());
    }

    protected void onSpawned(@NotNull World world) {
        WorldTimeResource worldTimeResource = world.getEntityStore().getStore().getResource(WorldTimeResource.getResourceType());
        worldTimeResource.setDayTime(UHCTimeUtil.SUNRISE, world, world.getEntityStore().getStore());

        UHCLobbyUtil.remove(world);

        UHC.get().getFlagService().setEnabled(UHCFlag.BUILD, true);

        HytaleServer.get().getEventBus()
                .dispatchFor(GameStartedEvent.class)
                .dispatch(new GameStartedEvent());
    }

    private void dispatchTimedEvents() {
        GameConfig config = UHC.get().getGameConfig();
        IEventBus eventBus = HytaleServer.get().getEventBus();

        if (time == config.invincibilityTime.get()) {
            eventBus.dispatchFor(InvincibilityEndedEvent.class).dispatch(new InvincibilityEndedEvent());
        }

        if (time == config.pvpTime.get()) {
            eventBus.dispatchFor(PvpEnabledEvent.class).dispatch(new PvpEnabledEvent());
        }

        for (BorderPhase phase : config.borderPhases.get()) {
            if (time == phase.time()) {
                eventBus.dispatchFor(BorderShrinkEvent.class).dispatch(new BorderShrinkEvent(phase.radius(), phase.speed()));
            }
        }
    }
}
