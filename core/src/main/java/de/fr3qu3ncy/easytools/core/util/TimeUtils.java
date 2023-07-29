package de.fr3qu3ncy.easytools.core.util;

import java.time.Duration;

public class TimeUtils {

    private TimeUtils() {}

    public static String prettyFormatTime(long millis, boolean hasMonths, boolean hasWeeks, boolean hasDays,
                                          boolean hasHours, boolean hasMinutes, boolean hasSeconds) {
        Duration duration = Duration.ofMillis(millis);
        StringBuilder sb = new StringBuilder();

        int months = 0;
        int weeks = 0;
        long days = duration.toDaysPart();
        int hours = duration.toHoursPart();
        int minutes = duration.toMinutesPart();
        int seconds = duration.toSecondsPart();

        while (days >= 30) {
            months++;
            days -= 30;
        }

        while (days >= 7) {
            weeks++;
            days -= 7;
        }

        if (months > 0 && hasMonths) sb.append(months).append(" Month").append(months > 1 ? "s" : "").append(" ");
        if (weeks > 0 && hasWeeks) sb.append(weeks).append(" Week").append(weeks > 1 ? "s" : "").append(" ");
        if (days > 0 && hasDays) sb.append(days).append(" Day").append(days > 1 ? "s" : "").append(" ");
        if (hours > 0 && hasHours) sb.append(hours).append(" Hour").append(hours > 1 ? "s" : "").append(" ");
        if (minutes > 0 && hasMinutes) sb.append(minutes).append(" Minute").append(minutes > 1 ? "s" : "").append(" ");
        if (seconds > 0 && hasSeconds) sb.append(seconds).append(" Second").append(seconds > 1 ? "s" : "").append(" ");

        return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
    }

    /**
     * Convert a given time string to milliseconds.<br>
     * Format: "5m30s"
     *
     * @param timeString The string to convert.
     * @return The time in milliseconds.
     */
    public static long parseDuration(String timeString) {
        long millis = 0;
        millis += findConstraint(timeString, "s") * 1000L;
        millis += findConstraint(timeString, "m") * 1000L * 60;
        millis += findConstraint(timeString, "h") * 1000L * 60 * 60;
        millis += findConstraint(timeString, "w") * 1000L * 60 * 60 * 24 * 7;
        millis += findConstraint(timeString, "mo") * 1000L * 60 * 60 * 24 * 30;

        return millis;
    }

    private static int findConstraint(String timeString, String identifier) {
        if (!timeString.contains(identifier)) return 0;

        int endIndex = timeString.indexOf(identifier);
        int startIndex = findStartIndex(timeString, endIndex);

        if (startIndex == -1) return 0;

        try {
            return Integer.parseInt(timeString.substring(startIndex, endIndex));
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private static int findStartIndex(String timeString, int endIndex) {
        for (int index = endIndex - 1 ; index >= 0 ; index--) {
            if (!Character.isDigit(timeString.charAt(index))) {
                return index + 1;
            } else if (index == 0) {
                return 0;
            }
        }
        return -1;
    }
}
