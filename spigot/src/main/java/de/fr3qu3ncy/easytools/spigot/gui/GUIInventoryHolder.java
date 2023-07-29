package de.fr3qu3ncy.easytools.spigot.gui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import javax.annotation.Nonnull;

public class GUIInventoryHolder implements InventoryHolder {

    private final int size;
    private final String title;

    public GUIInventoryHolder(int size, String title) {
        this.size = size;
        this.title = title;
    }

    @Override
    @Nonnull
    public Inventory getInventory() {
        return Bukkit.createInventory(this, size, title);
    }
}