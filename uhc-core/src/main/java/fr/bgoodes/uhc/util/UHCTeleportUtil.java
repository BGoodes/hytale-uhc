package fr.bgoodes.uhc.util;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.math.util.MathUtil;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.server.core.asset.type.fluid.Fluid;
import com.hypixel.hytale.server.core.modules.collision.WorldUtil;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.heightmap.HeightmapColumn;
import com.hypixel.hytale.server.core.universe.world.spawn.FitToHeightMapSpawnProvider;
import com.hypixel.hytale.server.core.universe.world.spawn.GlobalSpawnProvider;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class UHCTeleportUtil {

    private UHCTeleportUtil() {}

    public static @NotNull CompletableFuture<Transform> findSurface(@NotNull World world, @NotNull PlayerRef playerRef, double x, double z) {
        Transform column = new Transform(x, HeightmapColumn.NO_HEIGHT, z);

        return new FitToHeightMapSpawnProvider(new GlobalSpawnProvider(column))
                .getSpawnPointAsync(world, playerRef.getUuid());
    }

    public static @NotNull CompletableFuture<Boolean> hasFluid(@NotNull World world, @NotNull Transform location) {
        int x = MathUtil.floor(location.getPosition().x());
        int y = MathUtil.floor(location.getPosition().y());
        int z = MathUtil.floor(location.getPosition().z());

        return world.getChunkStore().getChunkSectionReferenceAtBlockAsync(x, y, z).thenApplyAsync(
                _ -> WorldUtil.getFluidIdAtPosition(world.getChunkStore(), x, y, z) != Fluid.EMPTY_ID, world);
    }

    public static void teleport(@NotNull PlayerRef playerRef, @NotNull World world, @NotNull Transform destination) {
        Ref<EntityStore> ref = playerRef.getReference();

        if (ref == null || !ref.isValid()) {
            return;
        }

        ref.getStore().getExternalData().getWorld().execute(() -> {
            if (ref.isValid()) {
                ref.getStore().addComponent(ref, Teleport.getComponentType(), Teleport.createForPlayer(world, destination));
            }
        });
    }
}
