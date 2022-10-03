package de.fr3qu3ncy.easytools.bukkit;

import de.fr3qu3ncy.easytools.bukkit.gui.GUIInventory;
import de.fr3qu3ncy.easytools.bukkit.gui.GUIListener;
import de.fr3qu3ncy.easytools.bukkit.setup.SetupChatListener;
import de.fr3qu3ncy.easytools.bukkit.setup.SetupInteractListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitTools extends JavaPlugin {

    private PluginManager pluginManager;

    @Override
    public void onEnable() {
        pluginManager = getServer().getPluginManager();
    }

    @Override
    public void onDisable() {
        GUIInventory.closeAllInventories();
    }

    /**
     * Call this method to enable the GUI system by registering its listeners.
     */
    public void enableGUISystem() {
        pluginManager.registerEvents(new GUIListener(), this);
    }

    /**
     * Call this method to enable the Setup system by registering its listeners.
     */
    public void enableSetupSystem() {
        pluginManager.registerEvents(new SetupChatListener(), this);
        pluginManager.registerEvents(new SetupInteractListener(), this);
    }

    public static BukkitTools getInstance() {
        return getPlugin(BukkitTools.class);
    }
}
