package de.fr3qu3ncy.easytools.spigot.config;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.fr3qu3ncy.easyconfig.core.annotations.ConfigurableField;
import de.fr3qu3ncy.easyconfig.core.annotations.OptionalFields;
import de.fr3qu3ncy.easyconfig.core.registry.ConfigRegistry;
import de.fr3qu3ncy.easyconfig.core.serialization.Configurable;
import de.fr3qu3ncy.easyconfig.spigot.serializable.SerializablePotion;
import de.fr3qu3ncy.easytools.spigot.EasyTools;
import de.fr3qu3ncy.easytools.spigot.util.ItemUtils;
import de.fr3qu3ncy.easytools.spigot.util.Placeholder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@SuppressWarnings({"deprecation", "java:S3011"})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ConfigurableField
@OptionalFields
public class ConfigItemStack implements Configurable<ConfigItemStack> {

    static {
        ConfigRegistry.register(SerializablePotion.class);
        ConfigRegistry.register(ConfigItemStack.class);
    }

    public static final NamespacedKey SPAWNER_TYPE = new NamespacedKey(EasyTools.getPlugin(), "spawner_type");

    private Material type;
    private int amount = 1;
    private String displayName;
    private List<String> lore;
    private int customModelData = 0;
    private String spawnerType;
    private Map<String, Integer> enchantments = new HashMap<>();
    private String skin;
    private SerializablePotion potion;

    public ConfigItemStack(Material type, String displayName, List<String> lore) {
        this.type = type;
        this.displayName = displayName;
        this.lore = lore;
    }

    public ConfigItemStack(Material type, String displayName) {
        this(type, displayName, List.of());
    }

    public ConfigItemStack(ItemStack itemStack) {
        this(itemStack.getType(), itemStack.getItemMeta().getDisplayName(), itemStack.getItemMeta().getLore());
        this.amount = itemStack.getAmount();
    }

    @SuppressWarnings("IncompleteCopyConstructor")
    public ConfigItemStack(ConfigItemStack other) {
        this.type = other.type;
        this.amount = other.amount;
        this.displayName = other.displayName;
        this.lore = new ArrayList<>(other.lore);
        this.customModelData = other.customModelData;
        this.spawnerType = other.spawnerType;
        this.enchantments = new HashMap<>(other.enchantments);
        this.skin = other.skin;
        if (other.potion != null) this.potion = new SerializablePotion(other.potion);
    }

    public ItemStack createItemStack() {
        return createItemStack(null, amount);
    }

    public ItemStack createItemStack(int amount) {
        return createItemStack(null, amount);
    }

    public ItemStack createItemStack(@Nullable Player player, int amount, Placeholder... placeholders) {
        if (type == null) return null;
        ItemStack itemStack = new ItemStack(type, amount);
        ItemUtils.editMeta(itemStack, meta -> {
            if (displayName != null) meta.setDisplayName(Placeholder.apply(displayName, placeholders));
            if (lore != null && !lore.isEmpty()) meta.setLore(Placeholder.apply(lore.stream().map(line -> ChatColor.RESET + String.valueOf(ChatColor.WHITE) + line).toList(), placeholders));
            if (customModelData != 0) meta.setCustomModelData(customModelData);

            //Set spawner type if applicable
            setSpawnerType(meta);

            //Set skin
            setSkin(meta, skin, player);

            //Set potion effects
            setPotionEffects(meta);
        });

        //Set enchantments
        setEnchantments(itemStack);

        return itemStack;
    }

    private void setSpawnerType(ItemMeta meta) {
        if (spawnerType != null) {
            EntityType entityType = EntityType.fromName(spawnerType);
            if (entityType != null) {
                if (meta instanceof BlockStateMeta bsm) {
                    BlockState blockState = bsm.getBlockState();
                    if (blockState instanceof CreatureSpawner spawner) {
                        spawner.setSpawnedType(entityType);
                        bsm.setBlockState(spawner);

                        bsm.getPersistentDataContainer().set(SPAWNER_TYPE, PersistentDataType.STRING, spawnerType);
                    }
                }
            } else {
                EasyTools.getPlugin().getLogger().warning(() -> "Found invalid mob type " + spawnerType + "!");
            }
        }
    }

    private void setEnchantments(ItemStack itemStack) {
        enchantments.forEach((name, level) -> {
            Enchantment enchantment = tryParseEnchantment(name);
            if (enchantment != null) {
                // Check if item is an enchanted book
                if (itemStack.getType() == Material.ENCHANTED_BOOK
                    && itemStack.getItemMeta() instanceof EnchantmentStorageMeta esm) {
                    esm.addStoredEnchant(enchantment, level, true);
                    itemStack.setItemMeta(esm);
                } else {
                    itemStack.addUnsafeEnchantment(enchantment, level);
                }
            }
        });
    }

    private Enchantment tryParseEnchantment(String name) {
        Enchantment enchantment = Enchantment.getByName(name);
        if (enchantment != null) return enchantment;

        enchantment = Enchantment.getByKey(NamespacedKey.minecraft(name.toLowerCase()));
        if (enchantment == null) {
            EasyTools.getPlugin().getLogger().warning(() -> "Found invalid enchantment " + name + "!");
        }
        return enchantment;
    }

    private void setSkin(ItemMeta meta, @Nullable String texture, @Nullable Player player) {
        if (meta instanceof SkullMeta skullMeta) {
            if (player != null && "%player_skin%".equalsIgnoreCase(texture)) {
                skullMeta.setOwningPlayer(player);
                return;
            }

            if (texture == null || texture.isEmpty()) return;

            GameProfile profile = new GameProfile(UUID.randomUUID(), null);

            profile.getProperties().put("textures", new Property("textures", texture));

            try {
                Method mtd = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
                mtd.setAccessible(true);
                mtd.invoke(skullMeta, profile);
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                EasyTools.getPlugin().getLogger().warning(() -> "Could not set skull texture to texture " + texture + "!");
            }
        }
    }

    private void setPotionEffects(ItemMeta meta) {
        if (meta instanceof PotionMeta potionMeta && potion != null) {
            potion.apply(potionMeta);
        }
    }
}
