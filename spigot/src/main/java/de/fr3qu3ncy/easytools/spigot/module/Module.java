package de.fr3qu3ncy.easytools.spigot.module;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import de.fr3qu3ncy.easyconfig.core.EasyConfig;
import de.fr3qu3ncy.easyconfig.spigot.SpigotConfig;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@Getter
public abstract class Module {

    @Setter
    private static ModuleHost moduleHost;

    @Setter
    private static JavaPlugin plugin;

    @Setter
    protected static PaperCommandManager commandManager;

    private final String name;

    private File moduleDirectory;
    private final Set<EasyConfig> configs = new HashSet<>();
    private Logger logger;

    protected Module(String name) {
        this.name = name;
    }

    /**
     * The internal function to begin loading a module.
     */
    void load() {
        this.logger = Logger.getLogger(plugin.getName() + " # " + name);
        onLoad();

        loadConfigs();

    }

    void createModuleDirectory() throws IOException {
        this.moduleDirectory = new File(plugin.getDataFolder(), name);
        if (!moduleDirectory.isDirectory() && !moduleDirectory.mkdirs()) {
            throw new IOException("Couldn't create module folder for module " + name + "!");
        }
    }

    /**
     * Override this to implement some logic to run when this module is being loaded.
     */
    protected void onLoad() {
        /* This can be overridden to implement an onLoad() function in a subclass */
    }

    /**
     * Override this to implement some logic to run after this module has been loaded.
     */
    protected void onPostLoad() {
        /* This can be overridden to implement an onPostLoad() function in a subclass */
    }

    /**
     * Override this to implement some logic after the API has initialized
     */
    public void onAPIInitialized() {
        /* This can be overridden to implement a onAPIInitialized() function in a subclass */
    }

    /**
     * Override this to implement some logic when the module's config has been reloaded
     */
    public void onConfigReloaded() {
        /* This can be overridden to implement a onConfigReloaded() function in a subclass */
    }

    protected void registerListener(Listener listener) {
        Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    protected void registerListeners(Listener... listeners) {
        Arrays.stream(listeners).forEach(this::registerListener);
    }

    protected void registerCommand(BaseCommand command) {
        commandManager.registerCommand(command);
    }

    /**
     * Registers a new config linked to this {@link Module}.
     * @param configName The name of the config to register WITHOUT file extension.
     * @param holder The class holding these config values.
     * @return An {@link EasyConfig} instance.
     */
    protected EasyConfig registerConfig(String configName, Class<?> holder) {
        EasyConfig config = new SpigotConfig(moduleDirectory, configName, holder);
        configs.add(config);

        return config;
    }

    protected EasyConfig createConfig(File file, Class<?> holder, Object holderInstance) {
        EasyConfig config = new SpigotConfig(file, holder, holderInstance);
        config.load();
        return config;
    }

    private void loadConfigs() {
        configs.forEach(EasyConfig::load);
    }

    public EasyConfig getConfig(Class<?> configClass) {
        return configs.stream().filter(cfg -> cfg.getHoldingClass().equals(configClass))
            .findFirst()
            .orElse(null);
    }

    public void saveConfigs() {
        configs.forEach(EasyConfig::saveConfig);
    }

    public void reloadConfigs() {
        configs.forEach(EasyConfig::reloadConfig);
        onConfigReloaded();
    }

    @Nullable
    public static <T extends Module> T getModule(Class<T> moduleClass) {
        return moduleHost.getModuleLoader().getModule(moduleClass);
    }
}
