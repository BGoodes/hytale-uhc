package fr.bgoodes.uhc.util;

import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.modules.time.WorldTimeResource;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.NotNull;

public class DayNightUtil {

    public static final double SUNRISE = WorldTimeResource.SUNRISE_SECONDS / (double) WorldTimeResource.SECONDS_PER_DAY;
    public static final double SUNSET = (WorldTimeResource.SUNRISE_SECONDS + WorldTimeResource.DAYTIME_SECONDS) / (double) WorldTimeResource.SECONDS_PER_DAY;

    private DayNightUtil() {}

    public static boolean isDay(@NotNull Store<EntityStore> store) {
        WorldTimeResource time = store.getResource(WorldTimeResource.getResourceType());
        return time.isDayTimeWithinRange(SUNRISE, SUNSET);
    }

    public static boolean isNight(@NotNull Store<EntityStore> store) {
        return !isDay(store);
    }
}
