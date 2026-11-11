package fr.bgoodes.uhc.mode.example;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

public class ExampleUHC extends JavaPlugin {

    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public ExampleUHC(JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        LOGGER.atInfo().log("Example UHC plugin setup.");
    }

    @Override
    protected void start() {
        LOGGER.atInfo().log("Example UHC plugin start.");
    }

    @Override
    protected void shutdown() {
    }
}
