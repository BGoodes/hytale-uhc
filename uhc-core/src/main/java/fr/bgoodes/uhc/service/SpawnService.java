package fr.bgoodes.uhc.service;

import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.event.participant.ParticipantSpawnEvent;
import fr.bgoodes.uhc.service.team.Team;
import fr.bgoodes.uhc.util.UHCLobbyUtil;
import fr.bgoodes.uhc.util.UHCTeleportUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class SpawnService {

    public @NotNull CompletableFuture<Transform> findLocation(@NotNull World world, @NotNull PlayerRef playerRef, int radius) {
        if (radius <= 0) {
            return CompletableFuture.completedFuture(new Transform(UHCLobbyUtil.LOBBY_SPAWN));
        }

        return findLocation(world, playerRef, radius, UHC.get().getServerConfig().getSpawnMaxAttempts());
    }

    public void spawn(@NotNull PlayerRef playerRef, @NotNull World world, @NotNull Transform location) {
        ParticipantSpawnEvent event = HytaleServer.get().getEventBus()
                .dispatchFor(ParticipantSpawnEvent.class)
                .dispatch(new ParticipantSpawnEvent(playerRef, location));

        UHCTeleportUtil.teleport(playerRef, world, event.getSpawnPoint());
    }

    public @NotNull CompletableFuture<Void> spawnRandom(@NotNull PlayerRef playerRef, @NotNull World world, int radius) {
        return findLocation(world, playerRef, radius).thenAccept(location -> spawn(playerRef, world, location));
    }

    public @NotNull CompletableFuture<Void> spawnTeams(@NotNull World world, @NotNull Collection<Team> teams, int radius, boolean together) {
        List<CompletableFuture<Void>> spawns = new ArrayList<>();

        for (Team team : teams) {
            List<PlayerRef> members = team.getMembers().stream()
                    .map(uuid -> Universe.get().getPlayer(uuid))
                    .filter(Objects::nonNull)
                    .toList();

            if (members.isEmpty()) {
                continue;
            }

            if (together) {
                spawns.add(findLocation(world, members.getFirst(), radius).thenAccept(location ->
                        members.forEach(member -> spawn(member, world, location))));
            } else {
                for (PlayerRef member : members) {
                    spawns.add(spawnRandom(member, world, radius));
                }
            }
        }

        return CompletableFuture.allOf(spawns.toArray(CompletableFuture[]::new));
    }

    private CompletableFuture<Transform> findLocation(World world, PlayerRef playerRef, int radius, int attempts) {
        double x = ThreadLocalRandom.current().nextInt(-radius, radius) + 0.5;
        double z = ThreadLocalRandom.current().nextInt(-radius, radius) + 0.5;

        return UHCTeleportUtil.findSurface(world, playerRef, x, z).thenCompose(location ->
                UHCTeleportUtil.hasFluid(world, location).thenCompose(hasFluid -> {
                    if (!hasFluid) {
                        return CompletableFuture.completedFuture(location);
                    }

                    if (attempts <= 1) {
                        UHC.LOGGER.atWarning().log("No dry spawn point found for %s", playerRef.getUsername());
                        return CompletableFuture.completedFuture(location);
                    }

                    return findLocation(world, playerRef, radius, attempts - 1);
                }));
    }
}
