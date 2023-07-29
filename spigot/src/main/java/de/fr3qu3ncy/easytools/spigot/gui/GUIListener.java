package de.fr3qu3ncy.easytools.spigot.gui;

import de.fr3qu3ncy.easytools.spigot.EasyTools;
import de.fr3qu3ncy.easytools.spigot.gui.item.GUIItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.ToIntFunction;

public class GUIListener implements Listener {

    private static ToIntFunction<Player> cooldownFunc = player -> 0;
    private static final Map<Player, Long> cooldown = new HashMap<>();

    public static void setCooldown(ToIntFunction<Player> cooldownFunc) {
        GUIListener.cooldownFunc = cooldownFunc;
    }

    @EventHandler(ignoreCancelled = true)
    public void onMaxStack(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getAction() != InventoryAction.CLONE_STACK && event.getAction() != InventoryAction.COLLECT_TO_CURSOR)
            return;

        GUIInventory inv = GUIInventory.getOpenInventory(player, event.getClickedInventory());

        if (inv != null && !inv.isAllowDrop()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onGUIDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!(event.getInventory().getHolder() instanceof GUIInventoryHolder)) return;

        GUIInventory inv = GUIInventory.getOpenInventory(player);
        if (inv == null) return;

        if (!inv.isAllowMove()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onGUIClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!(event.getInventory().getHolder() instanceof GUIInventoryHolder)) return;

        GUIInventory inv = GUIInventory.getOpenInventory(player);
        if (inv == null) return;

        if (event.getClickedInventory() == null || !event.getClickedInventory().equals(inv.getInventory())) {
            event.setCancelled(true);
            return;
        }

        // Check if player has cooldown
        if (cooldown.containsKey(player)) {
            int cooldownTime = cooldownFunc.applyAsInt(player);
            if (cooldown.get(player) + cooldownTime > System.currentTimeMillis()) {
                event.setCancelled(true);
                return;
            }
            cooldown.remove(player);
        } else {
            cooldown.put(player, System.currentTimeMillis());
        }

        GUIItem item = inv.getItems().get(event.getSlot());
        if (item == null) {
            event.setCancelled(!inv.isAllowMove());
            if (event.isCancelled()) {
                resetOffHandItem(player);
            }
            return;
        }
        if (!item.isClickable()) {
            event.setCancelled(true);
            resetOffHandItem(player);
            return;
        }

        if (inv.itemClicked(event.getSlot(), event.getClick(), player)) {
            event.setCancelled(true);
            resetOffHandItem(player);
        } else {
            event.setCancelled(!item.isMovable() || !inv.isAllowMove());
            if (event.isCancelled()) {
                resetOffHandItem(player);
            }
        }
    }

    private void resetOffHandItem(Player player) {
        ItemStack item = player.getInventory().getItemInOffHand();
        Bukkit.getScheduler().runTaskLater(EasyTools.getPlugin(),
            () -> player.getInventory().setItemInOffHand(item),
            0L);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;

        //These are all inventories that have not been closed, that means the player is now closing these by himself
        GUIInventory inventory = GUIInventory.getOpenInventory(player);
        if (inventory == null) return;

        if (!inventory.isClosable()) {
            //Inventory must not be closed by the player, re-open 5 ticks later
            Bukkit.getScheduler().runTaskLater(EasyTools.getPlugin(), () -> inventory.open(), 5L);
        } else {
            //Closing is allowed and player is now closing these
            inventory.playerClosed(event);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onItemInsert(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        GUIInventory inv = GUIInventory.getOpenInventory(player);
        if (inv == null) return;

        if (event.getAction() != InventoryAction.PLACE_ALL
            && event.getAction() != InventoryAction.PLACE_ONE
            && event.getAction() != InventoryAction.PLACE_SOME) {
            event.setCancelled(!inv.isAllowStorage());
            return;
        }

        if (inv.isAllowStorage()) {
            inv.onItemInsert(event.getSlot(), event.getCursor(), event);
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemPickup(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getAction() != InventoryAction.PICKUP_ALL
            && event.getAction() != InventoryAction.PICKUP_HALF
            && event.getAction() != InventoryAction.PICKUP_ONE
            && event.getAction() != InventoryAction.PICKUP_SOME) return;

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
            Bukkit.getScheduler().runTask(EasyTools.getPlugin(), () -> item.didInputText(event.getMessage()));
            GUIItem.INPUT_TEXT.remove(player);
        }
    }
}
