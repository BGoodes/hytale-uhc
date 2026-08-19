package fr.bgoodes.uhc.util;

import java.time.Duration;

public class TimeUtil {

    private TimeUtil() {}

    public static String formatDuration(long totalSeconds) {
        if (totalSeconds < 0) {
            throw new IllegalArgumentException("Duration cannot be negative");
        }

        Duration duration = Duration.ofSeconds(totalSeconds);

        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        StringBuilder result = new StringBuilder();

        if (hours > 0) {
            result.append(hours).append('h');
        }

        if (minutes > 0) {
            result.append(minutes).append('m');
        }

        if (seconds > 0 || result.isEmpty()) {
            result.append(seconds).append('s');
        }

        return result.toString();
    }
}
