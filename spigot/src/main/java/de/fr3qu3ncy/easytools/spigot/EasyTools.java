package de.fr3qu3ncy.easytools.spigot;

import de.fr3qu3ncy.easytools.spigot.gui.GUIInventory;
import de.fr3qu3ncy.easytools.spigot.gui.GUIListener;
import de.fr3qu3ncy.easytools.spigot.setup.SetupChatListener;
import de.fr3qu3ncy.easytools.spigot.setup.SetupInteractListener;
import org.bukkit.plugin.java.JavaPlugin;

public class EasyTools {

    private static JavaPlugin plugin;

    public void onDisable() {
        GUIInventory.closeAllInventories();
    }

    public static void setInternalUse(JavaPlugin plugin) {
        EasyTools.plugin = plugin;
    }

    /**
     * Call this method to enable the GUI system by registering its listeners.
     */
    public static void enableGUISystem() {
        plugin.getServer().getPluginManager().registerEvents(new GUIListener(), plugin);
    }

    /**
     * Call this method to enable the Setup system by registering its listeners.
     */
    public static void enableSetupSystem() {
        plugin.getServer().getPluginManager().registerEvents(new SetupChatListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new SetupInteractListener(), plugin);
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }
}
