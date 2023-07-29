package de.fr3qu3ncy.easytools.spigot.util;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public class WorldEditUtils {

    private WorldEditUtils() {}

    @Nullable
    public static Region getSelection(Player player) {
        // Get current WorldEdit selection of the player
        try {
            Region selection = WorldEdit.getInstance().getSessionManager().get(BukkitAdapter.adapt(player)).getSelection();

            // Check if the selection is valid
            if (selection == null) {
                player.sendMessage("§cYou need to select a region first!");
                return null;
            }
            return selection;
        } catch (IncompleteRegionException e) {
            player.sendMessage("§cYou need to select a region first!");
            return null;
        }
    }
}
