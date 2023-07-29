package de.fr3qu3ncy.easytools.spigot.gui.input;

import de.fr3qu3ncy.easytools.spigot.gui.GUIInventory;
import de.fr3qu3ncy.easytools.spigot.gui.item.GUIItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class GUIItemInput extends GUIInventory {

    public GUIItemInput(Player player) {
        super(player, "Input Item", 9);
        setAllowStorage(true);
    }

    @Override
    protected void addItems() {}

    protected GUIItem getWaitingItem() {
        return GUIItem.INPUT_ITEM.get(getPlayer());
    }

    @Override
    protected void onItemInsert(int slot, ItemStack itemStack, InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        GUIItem item = getWaitingItem();
        if (item != null) {
            GUIItem.INPUT_ITEM.remove(getPlayer());
            item.didInputItem((Player) event.getWhoClicked(), itemStack);
        }
    }

    @Override
    protected void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        GUIItem item = getWaitingItem();
        if (item != null) {
            GUIItem.INPUT_ITEM.remove(getPlayer());
            item.didInputItem((Player) event.getPlayer(), null);
        }
    }
}
