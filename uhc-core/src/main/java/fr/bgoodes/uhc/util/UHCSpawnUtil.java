package fr.bgoodes.uhc.util;

import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.event.participant.ParticipantSpawnEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class UHCSpawnUtil {

    private UHCSpawnUtil() {}

    public static @NotNull CompletableFuture<Void> spawn(@NotNull PlayerRef playerRef, @NotNull World world) {
        int attempts = UHC.get().getServerConfig().getSpawnMaxAttempts();

        return findSpawnPoint(playerRef, world, attempts).thenAccept(location -> {
            ParticipantSpawnEvent event = HytaleServer.get().getEventBus()
                    .dispatchFor(ParticipantSpawnEvent.class)
                    .dispatch(new ParticipantSpawnEvent(playerRef, location));

            UHCTeleportUtil.teleport(playerRef, world, event.getSpawnPoint());
        });
    }

    private static CompletableFuture<Transform> findSpawnPoint(PlayerRef playerRef, World world, int attempts) {
        int radius = UHC.get().getGameConfig().borderRadius;
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

                    return findSpawnPoint(playerRef, world, attempts - 1);
                }));
    }
}
