package fr.bgoodes.uhc.service.scenario;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.service.config.model.ScenarioConfig;
import fr.bgoodes.uhc.util.UHCMessageUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class UHCScenario extends JavaPlugin {

    public static final String CONFIG_PREFIX = "scenario.";

    private final String id;

    protected UHCScenario(@NotNull JavaPluginInit init, @NotNull String id) {
        super(init);
        this.id = id;
    }

    @Override
    protected final void setup() {
        UHC.get().getConfigService().registerConfig(CONFIG_PREFIX + id, getConfig());
        UHC.get().getScenarioService().register(this);
    }

    public final @NotNull String getId() {
        return id;
    }

    public final boolean isEnabled() {
        return getConfig().enabled.get();
    }

    public final @NotNull Message getDisplayName() {
        return UHCMessageUtil.tr("uhc.scenario.%s.name".formatted(id));
    }

    public abstract @NotNull ScenarioConfig getConfig();

    public abstract @NotNull Set<String> getIncompatibilities();
}
