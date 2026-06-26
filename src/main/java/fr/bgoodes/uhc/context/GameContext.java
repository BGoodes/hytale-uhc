package fr.bgoodes.uhc.context;

import com.hypixel.hytale.server.core.universe.world.World;
import org.jetbrains.annotations.NotNull;

public class GameContext {

    private World world;

    @NotNull
    private final GameRules gamerules = new GameRules();

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
