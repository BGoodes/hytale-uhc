package fr.bgoodes.uhc;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

public class UHC extends JavaPlugin {

    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public UHC(JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        LOGGER.atInfo().log("UHC Core plugin setup.");
    }

    @Override
    protected void start() {
        LOGGER.atInfo().log("UHC Core plugin start.");
    }

    @Override
    protected void shutdown() {
    }
}
