package fr.bgoodes.uhc.service.scenario;

import fr.bgoodes.uhc.UHC;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ScenarioService {

    private final Map<String, UHCScenario> scenarios = new LinkedHashMap<>();

    public synchronized void register(@NotNull UHCScenario scenario) {
        if (scenarios.putIfAbsent(scenario.getId(), scenario) != null) {
            throw new IllegalStateException("Scenario already registered: %s".formatted(scenario.getId()));
        }

        UHCScenario conflict = findConflict(scenario);

        if (scenario.isEnabled() && conflict != null) {
            scenario.getConfig().enabled.set(false);
            UHC.LOGGER.atWarning().log("Scenario %s disabled: incompatible with %s.", scenario.getId(), conflict.getId());
        }
    }

    public synchronized @Nullable UHCScenario getScenario(@NotNull String id) {
        return scenarios.get(id);
    }

    public synchronized @NotNull List<UHCScenario> getScenarios() {
        return List.copyOf(scenarios.values());
    }

    public synchronized @NotNull List<UHCScenario> getEnabledScenarios() {
        return scenarios.values().stream().filter(UHCScenario::isEnabled).toList();
    }

    public synchronized @Nullable UHCScenario findConflict(@NotNull UHCScenario scenario) {
        for (UHCScenario other : getEnabledScenarios()) {
            if (other != scenario && areIncompatible(scenario, other)) {
                return other;
            }
        }

        return null;
    }

    private static boolean areIncompatible(UHCScenario first, UHCScenario second) {
        return first.getIncompatibilities().contains(second.getId()) ||
                second.getIncompatibilities().contains(first.getId());
    }
}
