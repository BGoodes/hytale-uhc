package fr.bgoodes.uhc.context;

import com.hypixel.hytale.server.core.universe.world.World;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.UHCConfig;
import org.jetbrains.annotations.NotNull;

public class GameContext {

    private World world;
    @NotNull
    private final GameRules gamerules = new GameRules();

    public @NotNull UHCConfig getConfig() {
        return UHC.get().getConfig().get();
    }

    public void setWorld(@NotNull World world) {
        this.world = world;
    }

    public @NotNull World getWorld() {
        return world;
    }

    public @NotNull GameRules getGamerules() {
        return gamerules;
    }
}
