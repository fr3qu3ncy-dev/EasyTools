package de.fr3qu3ncy.easytools.spigot.gui;

import de.fr3qu3ncy.easytools.spigot.EasyTools;
import de.fr3qu3ncy.easytools.spigot.gui.item.GUIItem;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutOpenWindow;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.inventory.Containers;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

@SuppressWarnings("unused")
public abstract class GUIInventory {

    private static final Map<Player, GUIInventory> OPEN_INVENTORIES = new HashMap<>();

    @Getter
    private final String title;

    @Getter
    private Player player;

    @Getter
    private Map<Integer, GUIItem> items;

    @Getter
    @Setter
    private boolean closable;

    @Getter
    @Setter
    private boolean allowStorage;

    @Getter
    @Setter
    private boolean allowDrop;

    @Getter
    @Setter
    private boolean allowMove;

    @Setter
    private boolean openPreviousOnClose;

    @Setter
    private boolean openPreviousOnManualClose;

    private GUIInventory previousGUI;

    @Getter
    protected Inventory inventory;

    protected GUIInventory(Player player, String title, int size) {
        this.title = title;
        initDefault(player);
        this.inventory = Bukkit.createInventory(new GUIInventoryHolder(size, title), size, title);
    }

    private void initDefault(Player player) {
        this.player = player;
        this.items = new HashMap<>();
        this.closable = true;
    }

    protected abstract void addItems();

    protected void onItemInsert(int slot, ItemStack item, InventoryClickEvent event) {
    }

    protected void onItemPickup(int slot, ItemStack item, InventoryClickEvent event) {
    }

    protected void onInventoryOpen(InventoryOpenEvent event) {
    }

    protected void onInventoryClose(InventoryCloseEvent event) {
    }

    protected boolean hasPrevious() {
        return previousGUI != null;
    }

    public void setItem(int slot, GUIItem item) {
        if (item == null) return;
        items.put(slot, item);
    }

    public void fillEmpty(IntFunction<GUIItem> supplier) {
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            if (!items.containsKey(slot)) {
                setItem(slot, supplier.apply(slot));
            }
        }
    }

    public void clear() {
        items.clear();
        if (inventory != null) inventory.clear();
    }

    public GUIItem getItem(int slot) {
        return items.get(slot);
    }

    public void update() {
        update(null);
    }

    public void update(@Nullable Inventory newInventory) {
        reset(newInventory);
        updateTitle();
    }

    private void reset(@Nullable Inventory newInventory) {
        if (newInventory != null) {
            this.inventory = newInventory;
        }

        // Remove current cursor item
        player.getOpenInventory().setCursor(null);

        clear();
        addItems();
        items.forEach((slot, item) -> inventory.setItem(slot, item.createItem()));

        setItemsMovable(this.allowMove);
    }

    public void open() {
        open(true);
    }

    public void open(boolean changePrevious) {
        //Set previous inventory
        if (changePrevious) {
            this.previousGUI = getOpenInventory(player);
        }

        reset(null);

        //Get currently open inventory
        GUIInventory openInventory = getOpenInventory(player);
        if (openInventory == null) {
            //No open inventory, open this one
            player.openInventory(inventory);
        } else if (openInventory.getInventory().getSize() != this.getInventory().getSize()) {
            //Different size, open this one
            openInventory.close(true);
            player.openInventory(inventory);
        } else {
            //Same size, update
            update(openInventory.getInventory());
        }

        setOpenInventory(player, this);
    }

    private void internalClose(boolean manually, boolean forceClose) {
        // Remove current cursor item
        player.getOpenInventory().setCursor(null);

        //If we are not switching pages, open previous inventory
        if (!GUIPagedInventory.SWITCHING_PAGES.remove(getPlayer())
            && !forceClose
            && previousGUI != null
            && ((manually && openPreviousOnManualClose) || (!manually && openPreviousOnClose))) {
            Bukkit.getScheduler().runTaskLater(EasyTools.getPlugin(), () -> {
                removeInventory(player);
                previousGUI.open();
            }, 1L);
        } else {
            removeInventory(player);
        }
    }

    public void close() {
        close(false);
    }

    public void close(boolean forceClose) {
        internalClose(false, forceClose);
        if (forceClose || (!openPreviousOnClose || previousGUI == null)) player.closeInventory();
    }

    void playerClosed(InventoryCloseEvent event) {
        internalClose(true, false);
        onInventoryClose(event);
    }

    boolean itemClicked(int slot, ClickType clickType, Player player) {
        GUIItem item = items.get(slot);
        if (item == null) return false;
        boolean actionPresent = item.didClick(player, clickType);

        if (item.isCloseOnClick()) {
            close();
        }

        return actionPresent;
    }

    void itemInteracted(int slot, Action action) {
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

    public static GUIInventory getOpenInventory(Player player) {
        return OPEN_INVENTORIES.getOrDefault(player, null);
    }

    static GUIInventory getOpenInventory(Player player, Inventory clickedInventory) {
        GUIInventory inv = getOpenInventory(player);
        if (inv != null && inv.getInventory().equals(clickedInventory)) {
            return inv;
        }
        return null;
    }

    public static void closeOpenInventories(Player player) {
        GUIInventory inv = OPEN_INVENTORIES.get(player);
        if (inv != null) {
            inv.close();
        }
    }

    public static void closeAllInventories() {
        new ArrayList<>(OPEN_INVENTORIES.values()).forEach(GUIInventory::close);
    }

    private static void setOpenInventory(Player player, GUIInventory inventory) {
        OPEN_INVENTORIES.put(player, inventory);
    }

    private static void removeInventory(Player player) {
        OPEN_INVENTORIES.remove(player);
    }

    private void updateTitle() {
        if (getInventory().getSize() != 54) return;

        String parsedTitle = this.title;
        if (this instanceof GUIPagedInventory pagedInventory) {
            parsedTitle = parsedTitle.replace("%page%", String.valueOf(pagedInventory.getCurrentPage() + 1));
        }

        EntityPlayer ep = ((CraftPlayer) getPlayer()).getHandle();
        PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(
            ep.bP.j, Containers.f, IChatBaseComponent.a(parsedTitle));
        ep.b.a(packet);
        getPlayer().updateInventory();
    }
}
