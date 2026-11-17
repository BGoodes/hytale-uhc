package fr.bgoodes.uhc.utils;

import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.RotationTuple;
import com.hypixel.hytale.server.core.universe.world.SetBlockSettings;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.BlockOperations;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.util.FillerBlockUtil;
import fr.bgoodes.uhc.UHC;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LobbyUtil {

    public static final Transform LOBBY_SPAWN = new Transform(0, 305, 0);

    private static final int SIZE = 50;
    private static final int WALL_HEIGHT = 5;
    private static final int FLOOR_Y = 300;

    private static final String FLOOR_BLOCK = "Barrier";
    private static final String WALL_BLOCK = "Rock_Peach_Brick";

    private static final int MIN = -SIZE / 2;
    private static final int MAX = SIZE / 2 - 1;

    private LobbyUtil() {}

    public static void generate(World world) {
        int floorId = BlockType.getAssetMap().getIndex(FLOOR_BLOCK);
        int wallId = BlockType.getAssetMap().getIndex(WALL_BLOCK);

        loadSections(world)
                .thenRunAsync(() -> fill(world, floorId, wallId), world)
                .exceptionally(error -> {
                    UHC.LOGGER.atSevere().withCause(error).log("Failed to generate the lobby");
                    return null;
                });
    }

    public static void remove(World world) {
        world.execute(() -> fill(world, BlockType.EMPTY_ID, BlockType.EMPTY_ID));
    }

    private static CompletableFuture<Void> loadSections(World world) {
        List<CompletableFuture<?>> sections = new ArrayList<>();

        for (int cx = ChunkUtil.chunkCoordinate(MIN); cx <= ChunkUtil.chunkCoordinate(MAX); cx++) {
            for (int cy = ChunkUtil.chunkCoordinate(FLOOR_Y); cy <= ChunkUtil.chunkCoordinate(FLOOR_Y + WALL_HEIGHT); cy++) {
                for (int cz = ChunkUtil.chunkCoordinate(MIN); cz <= ChunkUtil.chunkCoordinate(MAX); cz++) {
                    sections.add(world.getChunkStore().getChunkSectionReferenceAsync(cx, cy, cz));
                }
            }
        }

        return CompletableFuture.allOf(sections.toArray(CompletableFuture[]::new));
    }

    private static void fill(World world, int floorId, int wallId) {
        for (int x = MIN; x <= MAX; x++) {
            for (int z = MIN; z <= MAX; z++) {
                setBlock(world, x, FLOOR_Y, z, floorId);

                if (x == MIN || x == MAX || z == MIN || z == MAX) {
                    for (int y = FLOOR_Y + 1; y <= FLOOR_Y + WALL_HEIGHT; y++) {
                        setBlock(world, x, y, z, wallId);
                    }
                }
            }
        }
    }

    private static void setBlock(World world, int x, int y, int z, int blockId) {
        ChunkStore chunkStore = world.getChunkStore();
        var sectionRef = chunkStore.getChunkSectionReferenceAtBlock(x, y, z);
        BlockType blockType = BlockType.getAssetMap().getAsset(blockId);

        if (sectionRef == null || !sectionRef.isValid() || blockType == null) {
            return;
        }

        BlockOperations.setBlock(chunkStore, sectionRef, x, y, z, blockId, blockType,
                RotationTuple.NONE_INDEX, FillerBlockUtil.NO_FILLER, SetBlockSettings.NONE);
    }
}
