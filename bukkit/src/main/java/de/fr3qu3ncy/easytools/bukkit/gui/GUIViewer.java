package de.fr3qu3ncy.easytools.bukkit.gui;

import lombok.Getter;

import javax.annotation.Nonnull;

public class GUIViewer {

    @Nonnull @Getter
    private final GUIInventory externalInventory, playerInventory;

    public GUIViewer(@Nonnull GUIInventory externalInv, @Nonnull GUIInventory playerInv) {
        this.externalInventory = externalInv;
        this.playerInventory = playerInv;
    }

    public void open() {
        externalInventory.open();
        playerInventory.open();
    }

    public void close() {
        externalInventory.close();
        playerInventory.close();
    }
}
