package fr.bgoodes.uhc.util;

import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.RotationTuple;
import com.hypixel.hytale.server.core.universe.world.SetBlockSettings;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.BlockOperations;
import com.hypixel.hytale.server.core.util.FillerBlockUtil;
import fr.bgoodes.uhc.UHC;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LobbyUtil {

    public static final Transform LOBBY_SPAWN = new Transform(0, 305, 0);

    private static final int PLATFORM_SIZE = 50;
    private static final int WALL_HEIGHT = 5;

    private static final int PLATFORM_Y = 300;

    private static final String PLATFORM_BLOCK = "Barrier";
    private static final String WALL_BLOCK = "Rock_Peach_Brick";

    private static final int MIN = -PLATFORM_SIZE / 2;
    private static final int MAX = PLATFORM_SIZE / 2 - 1;


    private LobbyUtil() {}

    public static void generate(@NotNull World world) {
        var chunkStore = world.getChunkStore();
        List<CompletableFuture<?>> sections = new ArrayList<>();

        // pregen lobby zone
        for (int cx = ChunkUtil.chunkCoordinate(MIN); cx <= ChunkUtil.chunkCoordinate(MAX); cx++) {
            for (int cy = ChunkUtil.chunkCoordinate(PLATFORM_Y); cy <= ChunkUtil.chunkCoordinate(PLATFORM_Y + WALL_HEIGHT); cy++) {
                for (int cz = ChunkUtil.chunkCoordinate(MIN); cz <= ChunkUtil.chunkCoordinate(MAX); cz++) {
                    sections.add(chunkStore.getChunkSectionReferenceAsync(cx, cy, cz));
                }
            }
        }

        CompletableFuture.allOf(sections.toArray(CompletableFuture[]::new)).whenComplete((ignored, error) -> {
            if (error != null) {
                UHC.LOGGER.atSevere().withCause(error).log("Failed to load the lobby chunks.");
                return;
            }

            world.execute(() -> {
                generatePlatform(world);
                generateWalls(world);
            });
        });
    }

    public static void remove(@NotNull World world) {
        for (int x = MIN; x <= MAX; x++) {
            for (int z = MIN; z <= MAX; z++) {
                for (int y = PLATFORM_Y; y <= PLATFORM_Y + WALL_HEIGHT; y++) {
                    setBlock(world, x, y, z, BlockType.EMPTY_ID);
                }
            }
        }
    }

    private static void generatePlatform(@NotNull World world) {
        int blockId = BlockType.getAssetMap().getIndex(PLATFORM_BLOCK);

        for (int x = MIN; x <= MAX; x++) {
            for (int z = MIN; z <= MAX; z++) {
                setBlock(world, x, PLATFORM_Y, z, blockId);
            }
        }
    }

    private static void generateWalls(@NotNull World world) {
        int blockId = BlockType.getAssetMap().getIndex(WALL_BLOCK);

        for (int h = 1; h <= WALL_HEIGHT; h++) {

            int y = PLATFORM_Y + h;

            for (int i = MIN; i <= MAX; i++) {

                // north - south
                setBlock(world, i, y, MIN, blockId);
                setBlock(world, i, y, MAX, blockId);

                // est - west
                setBlock(world, MIN, y, i, blockId);
                setBlock(world, MAX, y, i, blockId);
            }
        }
    }

    private static void setBlock(@NotNull World world, int x, int y, int z, int blockId) {
        var chunkStore = world.getChunkStore();

        var sectionRef = chunkStore.getChunkSectionReferenceAtBlock(x, y, z);
        if (sectionRef == null || !sectionRef.isValid()) {
            return;
        }

        BlockType blockType = BlockType.getAssetMap().getAsset(blockId);
        if (blockType == null) {
            return;
        }

        BlockOperations.setBlock(chunkStore, sectionRef, x, y, z, blockId, blockType,
                RotationTuple.NONE_INDEX, FillerBlockUtil.NO_FILLER, SetBlockSettings.NONE);
    }
}
