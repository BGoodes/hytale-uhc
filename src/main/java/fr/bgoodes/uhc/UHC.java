package fr.bgoodes.uhc;

import com.hypixel.hytale.codec.ExtraInfo;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.shape.Box2D;
import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.protocol.packets.player.SyncPlayerPreferences;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.asset.type.gameplay.DeathConfig;
import com.hypixel.hytale.server.core.asset.type.gameplay.MutableDeathConfig;
import com.hypixel.hytale.server.core.io.PacketHandler;
import com.hypixel.hytale.server.core.io.adapter.PacketAdapters;
import com.hypixel.hytale.server.core.modules.voice.VoiceModule;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.WorldConfig;
import com.hypixel.hytale.server.core.universe.world.worldmap.WorldMapManager;
import com.hypixel.hytale.server.core.util.Config;
import fr.bgoodes.uhc.commands.UHCCommand;
import fr.bgoodes.uhc.context.GameContext;
import fr.bgoodes.uhc.ecs.system.DisconnectEliminationSystem;
import fr.bgoodes.uhc.ecs.system.GameClockSystem;
import fr.bgoodes.uhc.ecs.system.UpgradeBenchSystem;
import fr.bgoodes.uhc.ecs.system.player.PlayerBreakBlockSystem;
import fr.bgoodes.uhc.ecs.system.player.PlayerDamageSystem;
import fr.bgoodes.uhc.ecs.system.player.PlayerDeathSystems;
import fr.bgoodes.uhc.ecs.system.player.PlayerPlaceBlockSystem;
import fr.bgoodes.uhc.listeners.PlayerConnectionListeners;
import fr.bgoodes.uhc.service.hud.HUDService;
import fr.bgoodes.uhc.service.permission.PermissionService;
import fr.bgoodes.uhc.service.player.PlayerService;
import fr.bgoodes.uhc.service.state.StateService;
import fr.bgoodes.uhc.service.team.TeamService;
import fr.bgoodes.uhc.service.worldborder.WorldBorderService;
import fr.bgoodes.uhc.util.LobbyUtil;
import fr.bgoodes.uhc.util.SpectatorUtil;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2d;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

public class UHC extends JavaPlugin {

    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private static UHC instance;

    private final Config<UHCConfig> config = this.withConfig("uhc_config", UHCConfig.CODEC);

    private GameContext gameContext;
    private PlayerService playerService;
    private TeamService teamService;
    private HUDService hudService;
    private WorldBorderService worldBorderService;
    private PermissionService permissionService;
    private StateService stateService;

    public UHC(@NonNullDecl JavaPluginInit init) {
        super(init);
    }

    public static UHC get() {
        return instance;
    }

    @Override
    protected void setup() {
        instance = this;

        this.config.save();
        this.gameContext = new GameContext();

        //services (the order matter, state service at the end)
        this.playerService = new PlayerService();
        this.teamService = new TeamService();
        this.hudService = new HUDService();
        this.worldBorderService = new WorldBorderService(this.getGameContext());
        this.permissionService = new PermissionService();
        this.stateService = new StateService(this.getGameContext());

        //components
//        ComponentType<EntityStore, Spectator> spectatorComponentType = this.getEntityStoreRegistry()
//                .registerComponent(Spectator.class, Spectator::new);
//        Spectator.setComponentType(spectatorComponentType);

        //systems
        this.getEntityStoreRegistry().registerSystem(new GameClockSystem());
        this.getEntityStoreRegistry().registerSystem(new DisconnectEliminationSystem());

        this.getEntityStoreRegistry().registerSystem(new PlayerBreakBlockSystem());
        this.getEntityStoreRegistry().registerSystem(new PlayerPlaceBlockSystem());
        this.getEntityStoreRegistry().registerSystem(new PlayerDamageSystem());

        this.getEntityStoreRegistry().registerSystem(new PlayerDeathSystems.Eliminate());

        this.getChunkStoreRegistry().registerSystem(new UpgradeBenchSystem());

        //events
        new PlayerConnectionListeners(this.getGameContext()).register(this.getEventRegistry());

        //commands
        this.getCommandRegistry().registerCommand(new UHCCommand());

        LOGGER.atInfo().log("UHC plugin started.");
    }

    @Override
    protected void start() {

        // add permissions group
        this.permissionService.setup();

        // default world config
        LOGGER.atInfo().log("Checking for default world...");
        World defaultWorld = Universe.get().getDefaultWorld();

        if (defaultWorld == null) {
            LOGGER.atSevere().log("No default world found, shutting down...");
            HytaleServer.get().getPluginManager().shutdown();
            return;
        }

        // world config
        LOGGER.atInfo().log("Default world found");

        WorldConfig worldConfig = defaultWorld.getWorldConfig();
        worldConfig.setDeleteOnRemove(true);
        worldConfig.setPvpEnabled(true);

        // death config
        MutableDeathConfig deathConfig = new MutableDeathConfig();
        deathConfig.setGameModeTypeOnDeath(SpectatorUtil.GAME_MODE_TYPE_ID);
        deathConfig.setItemsLossMode(DeathConfig.ItemsLossMode.ALL);
        worldConfig.setDeathConfigOverride(deathConfig);

        // day/night cycle duration override
        BsonDocument dayNightPatch = new BsonDocument()
                .append("DaytimeDurationSeconds", new BsonInt32(this.getConfig().get().dayDuration))
                .append("NighttimeDurationSeconds", new BsonInt32(this.getConfig().get().nightDuration));
        WorldConfig.CODEC.decode(dayNightPatch, worldConfig, ExtraInfo.THREAD_LOCAL.get());

        //lobby
        defaultWorld.execute(() -> LobbyUtil.generate(defaultWorld));

        //pregenerate
        if (this.getConfig().get().pregenerateWorld) {
            worldConfig.getChunkConfig().setPregenerateRegion(
                    new Box2D(
                            new Vector2d(-worldBorderService.getWorldBorderSize(), -worldBorderService.getWorldBorderSize()),
                            new Vector2d(worldBorderService.getWorldBorderSize(), worldBorderService.getWorldBorderSize())
                    )
            );
        }

        this.gameContext.setWorld(defaultWorld);
        worldConfig.markChanged();

        // voice module config
        VoiceModule.get().setVoiceEnabled(this.getConfig().get().proximityChat);
        VoiceModule.get().setMaxHearingDistance(this.getConfig().get().proximityChatDistance);

        WorldMapManager mapManager = defaultWorld.getWorldMapManager();
        PlayerMarkerProvider markerProvider = new PlayerMarkerProvider();

        // reveal the whole play area on the world map
//        int borderRadiusBlocks = this.getConfig().get().borderSizeMax;
//        int neededChunkRadius = (int) Math.ceil((2.0 * borderRadiusBlocks) / ChunkUtil.SIZE);
//        int chunkRadius = Math.min(neededChunkRadius, WorldMapConfig.ABSOLUTE_MAX_VIEW_RADIUS);
//
//        WorldWorldMapConfig mapConfig = new WorldWorldMapConfig();
//        mapConfig.setViewRadiusMin(chunkRadius);
//        mapConfig.setViewRadiusMax(chunkRadius);
//        worldConfig.setWorldMapConfig(mapConfig);
//
//        mapManager.setGenerator(mapManager.getGenerator());

        // remove markers
        mapManager.addMarkerProvider("spawn", markerProvider);
        mapManager.addMarkerProvider("playerIcons", markerProvider);
        mapManager.addMarkerProvider("death", markerProvider);
        mapManager.addMarkerProvider("respawn", markerProvider);
        mapManager.addMarkerProvider("personal", markerProvider);
        mapManager.addMarkerProvider("shared", markerProvider);
        mapManager.addMarkerProvider("poi", markerProvider);

        // always show armor
        PacketAdapters.registerInbound((PacketHandler _, Packet packet) -> {
            if (packet instanceof SyncPlayerPreferences prefs) {
                prefs.hideHelmet = false;
                prefs.hideCuirass = false;
                prefs.hideGauntlets = false;
                prefs.hidePants = false;
            }
        });
    }

    @Override
    protected void shutdown() {
        LOGGER.atInfo().log("Shutting down UHC plugin...");

        //remove host & spec permissions
        this.permissionService.shutdown();

        //delete player storage
        Path universePath = Universe.get().getPath();
        Path playersPath = universePath.resolve("players");

        if (Files.notExists(playersPath)) {
            LOGGER.atInfo().log("No player storage found.");
            return;
        }

        try (Stream<Path> stream = Files.walk(playersPath)) {
            stream.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
            LOGGER.atInfo().log("Player storage deleted.");

        } catch (IOException | UncheckedIOException e) {
            LOGGER.atSevere().withCause(e).log("Failed to delete player storage.");
        }

        // delete default world
        World defaultWorld = Universe.get().getDefaultWorld();

        if (defaultWorld != null && Universe.get().removeWorld(defaultWorld.getName())) {
            LOGGER.atInfo().log("Default world deleted.");
        } else {
            LOGGER.atSevere().log("Failed to delete default world deleted.");
        }

        LOGGER.atInfo().log("UHC plugin shutdown.");
    }

    public Config<UHCConfig> getConfig() {
        return config;
    }

    public @NotNull GameContext getGameContext() {
        return gameContext;
    }

    public @NotNull StateService getStateService() {
        return stateService;
    }

    public @NotNull PlayerService getPlayerService() {
        return playerService;
    }

    public @NotNull TeamService getTeamService() {
        return teamService;
    }

    public @NotNull HUDService getHudService() {
        return hudService;
    }

    public @NotNull WorldBorderService getWorldBorderService() {
        return worldBorderService;
    }

    public @NotNull PermissionService getPermissionService() {
        return permissionService;
    }
}
