package fr.bgoodes.uhc.context;

import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GameContext {

    private World world;
    @NotNull
    private final Set<UUID> players = new HashSet<>();
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

    public void addPlayer(@NotNull UUID uuid) {
        this.players.add(uuid);
    }

    public void removePlayer(@NotNull UUID uuid) {
        this.players.remove(uuid);
    }

    public boolean isPlayer(@NotNull UUID uuid) {
        return this.players.contains(uuid);
    }

    public int getConnectedPlayerCount() {
        return (int) players.stream()
                .filter(uuid -> Universe.get().getPlayer(uuid) != null)
                .count();
    }
}
