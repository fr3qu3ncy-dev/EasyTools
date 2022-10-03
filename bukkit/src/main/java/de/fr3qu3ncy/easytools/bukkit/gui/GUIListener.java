package de.fr3qu3ncy.easytools.bukkit.gui;

import de.fr3qu3ncy.easytools.bukkit.BukkitTools;
import de.fr3qu3ncy.easytools.bukkit.gui.item.GUIItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class GUIListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void checkItemClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();

        GUIInventory inv = GUIInventory.getOpenInventory(player, event.getClickedInventory());

        if (inv != null) {
            GUIItem item = inv.getItems().get(event.getSlot());
            if (item == null || !item.isClickable()) return;
            event.setCancelled(!item.isMovable());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onMaxStack(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (event.getAction() != InventoryAction.CLONE_STACK && event.getAction() != InventoryAction.COLLECT_TO_CURSOR) return;
        Player player = (Player) event.getWhoClicked();

        GUIInventory inv = GUIInventory.getOpenInventory(player, event.getClickedInventory());

        if (inv != null && !inv.isAllowDrop()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onGUIClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if (!(event.getInventory().getHolder() instanceof GUIInventoryHolder)) return;

        GUIInventory inv = GUIInventory.getOpenInventory(player, event.getClickedInventory());
        if (inv == null) return;

        GUIItem item = inv.getItems().get(event.getSlot());
        if (item == null || !item.isClickable()) return;

        if (inv.itemClicked(event.getSlot(), event.getClick(), player)) {
            event.setCancelled(true);
        } else {
            event.setCancelled(!item.isMovable());
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        int slot = player.getInventory().getHeldItemSlot();

        GUIInventory inv = GUIInventory.getOpenInternalInventory(player);

        if (inv == null) return;
        GUIItem item = inv.getItems().get(slot);
        if (item == null || !item.isInteractable()) return;

        event.setCancelled(true);
        inv.itemInteracted(slot, event.getAction());
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        Player player = (Player) event.getPlayer();

        //These are all inventories that have not been closed, that means the player is now closing these by himself
        GUIInventory inventory = GUIInventory.getOpenExternalInventory(player);
        if (inventory == null) return;

        if (!inventory.isClosable()) {
            //Inventory must not be closed by the player, re-open 5 ticks later
            Bukkit.getScheduler().runTaskLater(BukkitTools.getInstance(), inventory::open, 5L);
        } else {
            //Closing is allowed and player is now closing these
            inventory.playerClosed(event);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        boolean dropForbidden = GUIInventory.getOpenInventories(player).stream().anyMatch(inv -> !inv.isAllowDrop());
        if (dropForbidden) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemInsert(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (event.getAction() != InventoryAction.PLACE_ALL
            && event.getAction() != InventoryAction.PLACE_ONE
            && event.getAction() != InventoryAction.PLACE_SOME) return;

        Player player = (Player) event.getWhoClicked();

        GUIInventory inv = GUIInventory.getOpenInventory(player, event.getClickedInventory());
        if (inv == null) return;

        if (inv.isAllowStorage()) {
            inv.onItemInsert(event.getSlot(), event.getCursor(), event);
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemPickup(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (event.getAction() != InventoryAction.PICKUP_ALL
            && event.getAction() != InventoryAction.PICKUP_HALF
            && event.getAction() != InventoryAction.PICKUP_ONE
            && event.getAction() != InventoryAction.PICKUP_SOME) return;

        Player player = (Player) event.getWhoClicked();

        GUIInventory inv = GUIInventory.getOpenInventory(player, event.getClickedInventory());
        if (inv == null) return;

        if (inv.isAllowStorage()) {
            inv.onItemPickup(event.getSlot(), event.getCurrentItem(), event);
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInputText(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        GUIItem item = GUIItem.INPUT_TEXT.get(player);
        if (item != null) {
            Bukkit.getScheduler().runTask(BukkitTools.getInstance(), () -> item.didInputText(event.getMessage()));
            GUIItem.INPUT_TEXT.remove(player);
        }
    }
}
