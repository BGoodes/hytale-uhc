package fr.bgoodes.uhc;

import com.hypixel.hytale.event.EventPriority;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import fr.bgoodes.uhc.command.UHCCommand;
import fr.bgoodes.uhc.listener.PlayerConnectionListener;
import fr.bgoodes.uhc.listener.WorldListener;
import fr.bgoodes.uhc.service.ServiceRegistry;
import fr.bgoodes.uhc.event.ClockEvent;
import fr.bgoodes.uhc.service.ClockService;
import fr.bgoodes.uhc.service.config.ConfigService;
import fr.bgoodes.uhc.service.config.impl.GameConfig;
import fr.bgoodes.uhc.service.flag.FlagService;
import fr.bgoodes.uhc.service.PermissionService;
import fr.bgoodes.uhc.service.PlayerService;
import fr.bgoodes.uhc.service.state.StateService;
import fr.bgoodes.uhc.service.state.impl.EndingState;
import fr.bgoodes.uhc.service.state.impl.LobbyState;
import fr.bgoodes.uhc.service.state.impl.PlayingState;
import fr.bgoodes.uhc.service.state.impl.StartingState;
import fr.bgoodes.uhc.service.WorldService;
import fr.bgoodes.uhc.system.BlockSystems;
import fr.bgoodes.uhc.system.DamageSystem;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;

public class UHC extends JavaPlugin {

    private final ServiceRegistry services = new ServiceRegistry();

    private final Config<ServerConfig> serverConfig = withConfig(ServerConfig.CODEC);
    private final GameConfig gameConfig = new GameConfig();

    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    private static UHC instance;

    public UHC(JavaPluginInit init) {
        super(init);
        instance = this;
    }

    @Override
    protected void setup() {

        // server config
        if (!Files.exists(getDataDirectory().resolve("config.json"))) {
            serverConfig.save();
        }

        // services
        services.register(ConfigService.class, new ConfigService(getDataDirectory()));
        services.register(ClockService.class, new ClockService());
        services.register(StateService.class, new StateService());
        services.register(PermissionService.class, new PermissionService());
        services.register(PlayerService.class, new PlayerService());
        services.register(FlagService.class, new FlagService());
        services.register(WorldService.class, new WorldService());

        // game config
        getConfigService().registerConfig(GameConfig.CONFIG_ID, gameConfig);

        // states
        getStateService().registerGameState(LobbyState.STATE_ID, new LobbyState());
        getStateService().registerGameState(StartingState.STATE_ID, new StartingState());
        getStateService().registerGameState(PlayingState.STATE_ID, new PlayingState());
        getStateService().registerGameState(EndingState.STATE_ID, new EndingState());

        getStateService().changeGameState(LobbyState.STATE_ID);

        // commands
        this.getCommandRegistry().registerCommand(new UHCCommand());

        // systems
        getEntityStoreRegistry().registerSystem(new BlockSystems.BreakBlock());
        getEntityStoreRegistry().registerSystem(new BlockSystems.DamageBlock());
        getEntityStoreRegistry().registerSystem(new BlockSystems.PlaceBlock());
        getEntityStoreRegistry().registerSystem(new DamageSystem());

        // listeners
        getEventRegistry().register(EventPriority.FIRST, ClockEvent.class, _ -> getStateService().onClock());
        new PlayerConnectionListener().register(getEventRegistry());
        new WorldListener().register(getEventRegistry());

        LOGGER.atInfo().log("UHC Core plugin setup.");
    }

    @Override
    protected void start() {
        getPermissionService().initialize(getServerConfig());

        LOGGER.atInfo().log("UHC Core plugin start.");
    }

    @Override
    protected void shutdown() {}

    public static @NotNull UHC get() {
        return instance;
    }

    public @NotNull ServerConfig getServerConfig() {
        return serverConfig.get();
    }

    public @NotNull GameConfig getGameConfig() {
        return gameConfig;
    }

    public @NotNull ServiceRegistry getServices() {
        return services;
    }

    public @NotNull ConfigService getConfigService() {
        return services.get(ConfigService.class);
    }

    public @NotNull ClockService getClockService() {
        return services.get(ClockService.class);
    }

    public @NotNull FlagService getFlagService() {
        return services.get(FlagService.class);
    }

    public @NotNull PermissionService getPermissionService() {
        return services.get(PermissionService.class);
    }

    public @NotNull PlayerService getPlayerService() {
        return services.get(PlayerService.class);
    }

    public @NotNull StateService getStateService() {
        return services.get(StateService.class);
    }

    public @NotNull WorldService getWorldService() {
        return services.get(WorldService.class);
    }
}
