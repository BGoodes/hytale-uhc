package fr.bgoodes.uhc;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.WorldConfig;
import com.hypixel.hytale.server.core.universe.world.worldmap.WorldMapManager;
import fr.bgoodes.uhc.commands.UHCCommand;
import fr.bgoodes.uhc.context.GameContext;
import fr.bgoodes.uhc.ecs.system.GameClockSystem;
import fr.bgoodes.uhc.ecs.system.listener.PlayerBreakBlockSystem;
import fr.bgoodes.uhc.ecs.system.listener.PlayerDamageSystem;
import fr.bgoodes.uhc.ecs.system.listener.PlayerDeathSystem;
import fr.bgoodes.uhc.ecs.system.listener.PlayerPlaceBlockSystem;
import fr.bgoodes.uhc.listeners.PlayerJoinListeners;
import fr.bgoodes.uhc.service.state.StateService;
import fr.bgoodes.uhc.util.LobbyUtil;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

public class UHC extends JavaPlugin {

    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private static UHC instance;

    private GameContext gameContext;
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

        this.gameContext = new GameContext();

        //services
        this.stateService = new StateService(this.getGameContext());

        //systems
        this.getEntityStoreRegistry().registerSystem(new GameClockSystem());

        this.getEntityStoreRegistry().registerSystem(new PlayerBreakBlockSystem());
        this.getEntityStoreRegistry().registerSystem(new PlayerPlaceBlockSystem());
        this.getEntityStoreRegistry().registerSystem(new PlayerDamageSystem());
        this.getEntityStoreRegistry().registerSystem(new PlayerDeathSystem());

        //events
        new PlayerJoinListeners(this.gameContext, this.stateService).register(this.getEventRegistry());

        //commands
        this.getCommandRegistry().registerCommand(new UHCCommand());

        LOGGER.atInfo().log("UHC plugin started.");
    }

    @Override
    protected void start() {

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

        this.gameContext.setWorld(defaultWorld);
        defaultWorld.execute(() -> LobbyUtil.generate(defaultWorld));

        // remove markers
        WorldMapManager mapManager = defaultWorld.getWorldMapManager();
        PlayerMarkerProvider markerProvider = new PlayerMarkerProvider();

        mapManager.addMarkerProvider("spawn", markerProvider);
        mapManager.addMarkerProvider("playerIcons", markerProvider);
        mapManager.addMarkerProvider("death", markerProvider);
        mapManager.addMarkerProvider("respawn", markerProvider);
        mapManager.addMarkerProvider("personal", markerProvider);
        mapManager.addMarkerProvider("shared", markerProvider);
        mapManager.addMarkerProvider("poi", markerProvider);
    }

    @Override
    protected void shutdown() {
        LOGGER.atInfo().log("Shutting down UHC plugin...");

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

    public @NotNull GameContext getGameContext() {
        return gameContext;
    }

    public @NotNull StateService getStateService() {
        return stateService;
    }
}
