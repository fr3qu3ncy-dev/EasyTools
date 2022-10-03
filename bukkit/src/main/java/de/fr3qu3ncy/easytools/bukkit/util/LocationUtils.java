package de.fr3qu3ncy.easytools.bukkit.util;

import org.bukkit.Location;

public class LocationUtils {

    public static boolean isNearlySame(Location loc1, Location loc2) {
        return loc1.getWorld().equals(loc2.getWorld()) &&
                loc1.getBlockX() == loc2.getBlockX() &&
                loc1.getBlockY() == loc2.getBlockY() &&
                loc1.getBlockZ() == loc2.getBlockZ();
    }

}
