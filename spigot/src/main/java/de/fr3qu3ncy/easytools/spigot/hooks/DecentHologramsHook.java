package de.fr3qu3ncy.easytools.spigot.hooks;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.List;

public class DecentHologramsHook {

    private DecentHologramsHook() {}

    public static Hologram createHologram(String id, Location location, Vector offset, List<String> lines) {
        Location holoLocation = location.clone().add(offset);

        return DHAPI.createHologram(id, holoLocation, lines);
    }

    public static Hologram getHologram(String id) {
        return DHAPI.getHologram(id);
    }

    public static void updateHologram(String id, List<String> lines) {
        Hologram hologram = getHologram(id);
        if (hologram == null) {
            return;
        }
        for (int i = 0; i < hologram.getPage(0).getLines().size(); i++) {
            hologram.getPage(0).removeLine(i);
        }
        for (int i = 0; i < lines.size(); i++) {
            hologram.getPage(0).setLine(i, lines.get(i));
        }
    }

    public static void removeHologram(String id) {
        Hologram hologram = getHologram(id);
        if (hologram == null) return;
        hologram.delete();
    }
}
