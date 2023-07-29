package de.fr3qu3ncy.easytools.spigot.config;

import de.fr3qu3ncy.easyconfig.core.annotations.ConfigurableField;
import de.fr3qu3ncy.easyconfig.core.annotations.OptionalFields;
import de.fr3qu3ncy.easyconfig.core.registry.ConfigRegistry;
import de.fr3qu3ncy.easyconfig.core.serialization.Configurable;
import de.fr3qu3ncy.easytools.spigot.gui.item.GUIItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ConfigurableField
@AllArgsConstructor
@NoArgsConstructor
@OptionalFields
@Getter
public class ConfigGUIItem implements Configurable<ConfigGUIItem> {

    static {
        ConfigRegistry.register(ConfigGUIItem.class);
    }

    private ConfigItemStack item;
    private int slot;
    private int page = 1;

    public int getPage() {
        return page - 1;
    }

    public GUIItem toGUIItem() {
        return new GUIItem(item.createItemStack());
    }
}
