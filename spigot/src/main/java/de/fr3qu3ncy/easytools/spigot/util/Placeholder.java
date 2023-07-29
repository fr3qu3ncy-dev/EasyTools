package de.fr3qu3ncy.easytools.spigot.util;

import java.util.List;

public record Placeholder(String key, String value) {

    /**
     * Creates a new placeholder instance.
     * @param key The key to replace.
     *            Note that this is WITHOUT "%" (e.g. "player" instead of "%player%"!
     * @param value The text to replace the key with.
     * @return A new placeholder instance.
     */
    public static Placeholder of(String key, String value) {
        return new Placeholder(key, value);
    }

    public static Placeholder player(String player) {
        return of("player", player);
    }

    public static Placeholder amount(int amount) {
        return of("amount", String.valueOf(amount));
    }

    public static String apply(String str, Placeholder... placeholders) {
        for (Placeholder placeholder : placeholders) {
            str = str.replace("%" + placeholder.key() + "%", placeholder.value());
        }
        return str;
    }

    public static List<String> apply(List<String> list, Placeholder... placeholders) {
        return list.stream()
            .map(line -> apply(line, placeholders))
            .toList();
    }

    public static String apply(String str, List<Placeholder> placeholders) {
        return apply(str, placeholders.toArray(new Placeholder[0]));
    }

    public static List<String> apply(List<String> list, List<Placeholder> placeholders) {
        return list.stream()
            .map(line -> apply(line, placeholders))
            .toList();
    }
}
