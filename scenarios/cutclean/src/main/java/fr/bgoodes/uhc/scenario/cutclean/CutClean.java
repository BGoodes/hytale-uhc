package fr.bgoodes.uhc.scenario.cutclean;

import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import fr.bgoodes.uhc.service.scenario.UHCScenario;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class CutClean extends UHCScenario {

    public static final String SCENARIO_ID = "cutclean";

    private final CutCleanConfig config = new CutCleanConfig();

    public CutClean(JavaPluginInit init) {
        super(init, SCENARIO_ID);
    }

    @Override
    public @NotNull CutCleanConfig getConfig() {
        return config;
    }

    @Override
    public @NotNull Set<String> getIncompatibilities() {
        return Set.of();
    }
}
