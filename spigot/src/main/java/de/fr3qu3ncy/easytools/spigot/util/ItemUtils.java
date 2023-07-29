package de.fr3qu3ncy.easytools.spigot.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.Consumer;

public class ItemUtils {

    private ItemUtils() {}

    public static void editMeta(ItemStack item, Consumer<ItemMeta> metaConsumer) {
        ItemMeta meta = item.getItemMeta();
        metaConsumer.accept(meta);
        item.setItemMeta(meta);
    }
}
