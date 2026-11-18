package fr.bgoodes.uhc.util;

import com.hypixel.hytale.server.core.modules.time.WorldTimeResource;
import com.hypixel.hytale.server.core.universe.world.World;
import org.jetbrains.annotations.NotNull;

public class UHCTimeUtil {

    public static final double SUNRISE = WorldTimeResource.SUNRISE_SECONDS / (double) WorldTimeResource.SECONDS_PER_DAY;
    public static final double SUNSET = (WorldTimeResource.SUNRISE_SECONDS + WorldTimeResource.DAYTIME_SECONDS) / (double) WorldTimeResource.SECONDS_PER_DAY;

    private UHCTimeUtil() {}

    public static boolean isDay(@NotNull World world) {
        return getTime(world).isDayTimeWithinRange(SUNRISE, SUNSET);
    }

    public static boolean isNight(@NotNull World world) {
        return !isDay(world);
    }

    private static WorldTimeResource getTime(World world) {
        return world.getEntityStore().getStore().getResource(WorldTimeResource.getResourceType());
    }

    public static @NotNull String formatDuration(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int rest = seconds % 60;

        return hours > 0
                ? "%d:%02d:%02d".formatted(hours, minutes, rest)
                : "%02d:%02d".formatted(minutes, rest);
    }
}
