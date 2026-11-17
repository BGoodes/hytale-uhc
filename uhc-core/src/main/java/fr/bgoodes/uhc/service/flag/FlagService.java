package fr.bgoodes.uhc.service.flag;

import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.service.flag.model.IFlag;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class FlagService {

    private final Set<IFlag> enabledFlags = new HashSet<>();

    public synchronized boolean isEnabled(@NotNull IFlag flag) {
        return enabledFlags.contains(flag);
    }

    public synchronized void setEnabled(@NotNull IFlag flag, boolean enabled) {
        boolean changed = enabled ? enabledFlags.add(flag) : enabledFlags.remove(flag);

        if (changed) {
            UHC.LOGGER.atInfo().log("Flag %s: %s", flag, enabled);
        }
    }
}
