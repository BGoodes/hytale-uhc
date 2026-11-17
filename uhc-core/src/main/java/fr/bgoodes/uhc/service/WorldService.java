package fr.bgoodes.uhc.service;

import com.hypixel.hytale.server.core.universe.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WorldService {

    private @Nullable World gameWorld;

    public synchronized @NotNull World getGameWorld() {
        if (gameWorld == null) {
            throw new IllegalStateException("Game world does not exist");
        }

        return gameWorld;
    }

    public synchronized void setGameWorld(@NotNull World gameWorld) {
        this.gameWorld = gameWorld;
    }
}
