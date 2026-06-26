package fr.bgoodes.uhc.util;

import com.hypixel.hytale.server.core.universe.world.World;

public class LobbyUtil {

    private static final int PLATFORM_SIZE = 50;
    private static final int WALL_HEIGHT = 5;

    private static final int PLATFORM_Y = 300;

    private static final String PLATFORM_BLOCK = "Barrier";
    private static final String WALL_BLOCK = "Rock_Peach_Brick";


    private LobbyUtil() {}

    public static void generate(World world) {
        generatePlatform(world);
        generateWalls(world);
    }

    private static void generatePlatform(World world) {
        for (int x = -PLATFORM_SIZE/2; x < PLATFORM_SIZE/2; x++) {
            for (int z = -PLATFORM_SIZE/2; z < PLATFORM_SIZE/2; z++) {
                world.setBlock(x, PLATFORM_Y, z, PLATFORM_BLOCK);
            }
        }
    }

    private static void generateWalls(World world) {
        int min = -PLATFORM_SIZE/2;
        int max = PLATFORM_SIZE/2 - 1;

        for (int h = 1; h <= WALL_HEIGHT; h++) {

            int y = PLATFORM_Y + h;

            for (int i = min; i <= max; i++) {

                // north - south
                world.setBlock(i, y, min, WALL_BLOCK);
                world.setBlock(i, y, max, WALL_BLOCK);

                // est - west
                world.setBlock(min, y, i, WALL_BLOCK);
                world.setBlock(max, y, i, WALL_BLOCK);
            }
        }
    }

    public static void remove(World world) {
        int min = -PLATFORM_SIZE/2;
        int max = PLATFORM_SIZE/2 - 1;

        for (int x = min; x <= max; x++) {
            for (int z = min; z <= max; z++) {

                for (int y = PLATFORM_Y; y <= PLATFORM_Y + WALL_HEIGHT; y++) {
                    world.breakBlock(x, y, z, 0);
                }
            }
        }
    }
}
