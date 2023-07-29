package de.fr3qu3ncy.easytools.spigot.gui;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class GUIUtils {

    public static void setSkullTexture(ItemMeta itemMeta, String texture) {
        SkullMeta headMeta = (SkullMeta) itemMeta;
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", texture));
        try {
            Method mtd = headMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
            mtd.setAccessible(true);
            mtd.invoke(headMeta, profile);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }

    public static List<Integer> getSlotsInSnakePattern(int inventorySize) {
        List<Integer> slots = new LinkedList<>();
        int rows = inventorySize / 9;
        int col = 1;

        while (col <= 9) {
            for (int i = 0; i < rows; i++) {
                slots.add(i * 9 + (col - 1));
            }
            slots.add(inventorySize - (9 - col));
            col += 2;
            for (int i = rows - 1; i >= 0; i--) {
                slots.add(i * 9 + (col - 1));
            }
            col += 2;
            slots.add(col - 2);
        }
        return slots;
    }
}
