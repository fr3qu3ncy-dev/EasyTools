package de.fr3qu3ncy.easytools.spigot.util;

public class MessageUtils {

    private MessageUtils() {
    }

    public static String buildProgressString(String waiting, String done, int length, double progress) {
        progress = Math.min(progress, 1D);
        int doneAmount = (int) Math.round(progress * length);
        int waitingAmount = length - doneAmount;

        return done.repeat(doneAmount) + waiting.repeat(waitingAmount);
    }
}
