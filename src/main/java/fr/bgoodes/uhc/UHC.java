package fr.bgoodes.uhc;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.WorldConfig;
import com.hypixel.hytale.server.core.universe.world.worldmap.WorldMapManager;
import com.hypixel.hytale.server.core.universe.world.worldmap.markers.providers.*;
import fr.bgoodes.uhc.commands.UHCCommand;
import fr.bgoodes.uhc.context.GameContext;
import fr.bgoodes.uhc.service.state.StateService;
import fr.bgoodes.uhc.service.state.impl.LobbyState;
import fr.bgoodes.uhc.system.GameClockSystem;
import fr.bgoodes.uhc.system.listeners.BreakBlockSystem;
import fr.bgoodes.uhc.system.listeners.DamagePlayerSystem;
import fr.bgoodes.uhc.system.listeners.DeathPlayerSystem;
import fr.bgoodes.uhc.util.LobbyUtil;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.jetbrains.annotations.NotNull;

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
        this.stateService = new StateService();

        //systems
        this.getEntityStoreRegistry().registerSystem(new GameClockSystem());

        this.getEntityStoreRegistry().registerSystem(new BreakBlockSystem());
        this.getEntityStoreRegistry().registerSystem(new DamagePlayerSystem());
        this.getEntityStoreRegistry().registerSystem(new DeathPlayerSystem());

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
        worldConfig.setPvpEnabled(false);

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

        // delete default world
        World defaultWorld = Universe.get().getDefaultWorld();

        if (defaultWorld != null) {
            Universe.get().removeWorld(defaultWorld.getName());
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
