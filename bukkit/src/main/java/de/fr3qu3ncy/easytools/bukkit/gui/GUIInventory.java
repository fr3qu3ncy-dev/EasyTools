package de.fr3qu3ncy.easytools.bukkit.gui;

import de.fr3qu3ncy.easytools.bukkit.gui.item.GUIItem;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GUIInventory {

    public static Map<Player, List<GUIInventory>> OPEN_INVENTORIES = new HashMap<>();

    @Getter
    private Player player;

    @Getter
    private Map<Integer, GUIItem> items;

    @Getter @Setter
    private boolean closable;

    @Getter @Setter
    private boolean allowStorage, allowDrop, allowMove;

    @Getter
    private boolean open;

    protected Inventory inventory;
    private final boolean externalInventory;

    public GUIInventory(Player player) {
        initDefault(player);
        this.inventory = player.getInventory();
        this.externalInventory = false;
    }

    public GUIInventory(Player player, String title, int size) {
        initDefault(player);
        this.inventory = Bukkit.createInventory(new GUIInventoryHolder(size, title), size, title);
        this.externalInventory = true;
    }

    private void initDefault(Player player) {
        this.player = player;
        this.items = new HashMap<>();
        this.closable = true;
    }

    protected abstract void addItems();

    protected void onItemInsert(int slot, ItemStack item, InventoryClickEvent event) {}
    protected void onItemPickup(int slot, ItemStack item, InventoryClickEvent event) {}
    protected void onInventoryOpen(InventoryOpenEvent event) {}
    protected void onInventoryClose(InventoryCloseEvent event) {}

    public void setItem(int slot, GUIItem item) {
        items.put(slot, item);
    }

    public void clear() {
        items.clear();
    }

    public GUIItem getItem(int slot) {
        return items.get(slot);
    }

    public void update() {
        reset();
        open();
    }

    private void reset() {
        clear();
        addItems();

        setItemsMovable(this.allowMove);
    }

    private void populateExternal() {
        this.inventory.clear();
        getItems().forEach((slot, item) -> this.inventory.setItem(slot, item.createItem()));
    }

    public void open() {
        reset();

        if (externalInventory) {
            populateExternal();
            player.openInventory(inventory);
        } else {
            player.getInventory().clear();
            items.forEach((slot, item) -> player.getInventory().setItem(slot, item.createItem()));
            player.updateInventory();
        }
        open = true;
        addOpenInventory(this);
    }

    private void internalClose() {
        open = false;

        if (!externalInventory) {
            player.getInventory().clear();
        }
        removeOpenInventory(this);
    }

    public void close() {
        internalClose();
        player.closeInventory();
    }

    public void playerClosed(InventoryCloseEvent event) {
        internalClose();
        onInventoryClose(event);
    }

    public boolean itemClicked(int slot, ClickType clickType, Player player) {
        GUIItem item = items.get(slot);
        if (item == null) return false;
        boolean actionPresent = item.didClick(player, clickType);

        if (item.isCloseOnClick()) {
            close();
        }

        return actionPresent;
    }

    public void itemInteracted(int slot, Action action) {
        GUIItem item = items.get(slot);
        if (item == null) return;
        item.didInteract(action);
    }

    public void setItemsMovable(boolean canMove) {
        this.allowMove = canMove;
        getItems().values().forEach(item -> item.setMovable(canMove));
    }

    public ItemStack[] getContents() {
        List<ItemStack> itemList = new ArrayList<>();
        items.values().forEach(item -> itemList.add(item.createItem()));

        return itemList.toArray(new ItemStack[0]);
    }

    public static List<GUIInventory> getOpenInventories(Player player) {
        return OPEN_INVENTORIES.getOrDefault(player, new ArrayList<>());
    }

    public static GUIInventory getOpenExternalInventory(Player player) {
        return OPEN_INVENTORIES.getOrDefault(player, new ArrayList<>()).stream().filter(inv -> inv.externalInventory)
            .findFirst().orElse(null);
    }

    public static GUIInventory getOpenInternalInventory(Player player) {
        return OPEN_INVENTORIES.getOrDefault(player, new ArrayList<>()).stream().filter(inv -> !inv.externalInventory)
            .findFirst().orElse(null);
    }

    public static GUIInventory getOpenInventory(Player player, Inventory inventory) {
        return getOpenInventories(player).stream().filter(inv -> inv.inventory.equals(inventory)).findFirst().orElse(null);
    }

    public static void closeOpenInventories(Player player) {
        new ArrayList<>(getOpenInventories(player)).forEach(GUIInventory::close);
    }

    public static void closeAllInventories() {
        OPEN_INVENTORIES.values().forEach(list -> list.forEach(GUIInventory::close));
    }

    private static void addOpenInventory(GUIInventory inventory) {
        List<GUIInventory> openInventories = OPEN_INVENTORIES.getOrDefault(inventory.player, new ArrayList<>());
        openInventories.add(inventory);
        OPEN_INVENTORIES.put(inventory.player, openInventories);
    }

    private static void removeOpenInventory(GUIInventory inventory) {
        List<GUIInventory> openInventories = OPEN_INVENTORIES.getOrDefault(inventory.player, new ArrayList<>());
        openInventories.remove(inventory);
        OPEN_INVENTORIES.put(inventory.player, openInventories);
    }
}
