package de.fr3qu3ncy.easytools.spigot.util;

import org.bukkit.Bukkit;

public enum ServerVersion {

    V_1_8_8,
    V_1_9_4,
    V_1_10_2,
    V_1_11_2,
    V_1_12_2,
    V_1_13_2,
    V_1_14_4,
    V_1_15_2,
    V_1_16_5,
    V_1_17_1,
    V_1_18_1;

    public static ServerVersion getServerVersion() {
        String version = Bukkit.getVersion();
        if (version.contains("1.8")) return V_1_8_8;
        if (version.contains("1.9")) return V_1_9_4;
        if (version.contains("1.10")) return V_1_10_2;
        if (version.contains("1.11")) return V_1_11_2;
        if (version.contains("1.12")) return V_1_12_2;
        if (version.contains("1.13")) return V_1_13_2;
        if (version.contains("1.14")) return V_1_14_4;
        if (version.contains("1.15")) return V_1_15_2;
        if (version.contains("1.16")) return V_1_16_5;
        if (version.contains("1.17")) return V_1_17_1;
        if (version.contains("1.18")) return V_1_18_1;
        return null;
    }
}
