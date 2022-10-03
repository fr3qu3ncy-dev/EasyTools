package de.fr3qu3ncy.easytools.bukkit.gui.item;

import de.fr3qu3ncy.easytools.bukkit.gui.GUIInventory;
import de.fr3qu3ncy.easytools.bukkit.gui.GUIUtils;
import de.fr3qu3ncy.easytools.bukkit.gui.input.GUIItemInput;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;
import java.util.function.Consumer;

@Getter
public class GUIItem {

    public static Map<Player, GUIItem> INPUT_TEXT = new HashMap<>();
    public static Map<Player, GUIItem> INPUT_ITEM = new HashMap<>();

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    private final Map<ClickType, Runnable> onClick;
    private final Map<ClickType, String> onClickMessages;

    private final Map<Action, Runnable> onInteract;

    private Consumer<String> inputText;
    private Consumer<ItemStack> inputItem;

    private boolean movable;
    private boolean closeOnClick;

    public GUIItem(Material material, int amount, short data) {
        this.itemStack = new ItemStack(material, amount, data);
        this.itemMeta = itemStack.getItemMeta();
        this.onInteract = new HashMap<>();
        this.onClick = new HashMap<>();
        this.onClickMessages = new HashMap<>();
    }

    public GUIItem(Material material, int amount) {
        this(material, amount, (short) 0);
    }

    public GUIItem(Material material) {
        this(material, 1, (short) 0);
    }

    public GUIItem(ItemStack item) {
        this(item.getType(), item.getAmount());

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        setDisplayName(meta.getDisplayName());
        setLore(meta.getLore());
    }

    public GUIItem setMovable(boolean canMove) {
        this.movable = canMove;
        return this;
    }

    public GUIItem setDisplayName(String displayName) {
        itemMeta.setDisplayName(displayName);
        return this;
    }

    public GUIItem setLore(String... lore) {
        itemMeta.setLore(Arrays.asList(lore));
        return this;
    }

    public GUIItem setLore(List<String> lore) {
        itemMeta.setLore(lore);
        return this;
    }

    public GUIItem addLore(String... lore) {
        List<String> currentLore = itemMeta.getLore();
        if (currentLore == null) currentLore = new ArrayList<>();
        currentLore.addAll(Arrays.asList(lore));
        itemMeta.setLore(currentLore);
        return this;
    }

    public GUIItem setOnClick(ClickType clickType, Runnable onClick) {
        this.onClick.put(clickType, onClick);
        return this;
    }

    public GUIItem setOnClick(ClickType clickType, String message, Runnable onClick) {
        setOnClick(clickType, onClick);
        onClickMessages.put(clickType, message);
        return this;
    }

    public GUIItem setOnInteract(Runnable onInteract, Action... actions) {
        Arrays.stream(actions).forEach(action -> this.onInteract.put(action, onInteract));
        return this;
    }

    public GUIItem inputText(ClickType type, Player player, String message, Consumer<String> inputText) {
        this.inputText = inputText;
        setOnClick(type, message, () -> openInputText(player));
        return this;
    }

    public GUIItem inputItem(ClickType type, Player player, ItemStack[] oldInventory, Consumer<ItemStack> inputItem) {
        this.inputItem = inputItem;
        setOnClick(type, () -> openInputItem(player, oldInventory));
        return this;
    }

    public GUIItem setCloseOnClick(boolean b) {
        this.closeOnClick = b;
        return this;
    }

    public GUIItem setSkull(String uuid) {
        SkullMeta skullMeta = (SkullMeta) getItemMeta();
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(uuid)));
        return this;
    }

    public GUIItem setSkullTexture(String texture) {
        GUIUtils.setSkullTexture(this.itemMeta, texture);
        return this;
    }

    public GUIItem setHideEnchantments() {
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public GUIItem addEnchantment(Enchantment enchantment, int level) {
        itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public boolean didClick(Player player, ClickType clickType) {
        Runnable action = onClick.get(clickType);
        if (action != null) {
            action.run();

            String message = onClickMessages.get(clickType);
            if (message != null) {
                player.sendMessage(message);
            }
            return true;
        }
        return false;
    }

    public void didInteract(Action action) {
        Runnable run = onInteract.get(action);
        if (run != null) run.run();
    }

    public ItemStack createItem() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void didInputText(String text) {
        inputText.accept(text);
    }

    public void didInputItem(Player player, ItemStack item) {
        player.getInventory().clear();
        player.updateInventory();

        inputItem.accept(item);
    }

    private void openInputText(Player player) {
        INPUT_TEXT.put(player, this);
        GUIInventory.closeOpenInventories(player);
    }

    private void openInputItem(Player player, ItemStack[] oldInventory) {
        GUIInventory.closeOpenInventories(player);
        player.getInventory().setContents(oldInventory);
        player.updateInventory();

        INPUT_ITEM.put(player, this);
        new GUIItemInput(player).open();
    }

    public boolean isClickable() {
        return onClick != null;
    }

    public boolean isInteractable() {
        return onInteract != null;
    }
}
