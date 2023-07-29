package de.fr3qu3ncy.easytools.spigot.module;

import co.aikar.commands.PaperCommandManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

@RequiredArgsConstructor
public final class ModuleLoader {

    private final List<Module> modules = new ArrayList<>();

    private final Logger logger;

    public void loadModules(ModuleHost host, JavaPlugin plugin, PaperCommandManager commandManager) {
        Module.setModuleHost(host);
        Module.setPlugin(plugin);
        Module.setCommandManager(commandManager);

        modules.forEach(module -> {
            logger.info("Loading module " + module.getName() + "...");

            //Try creating this module's directory and then load it
            try {
                module.createModuleDirectory();

                //Module#load() also internally calls Module#onLoad()!
                module.load();

                module.onPostLoad();

                logger.info("Module " + module.getName() + " has been loaded!");
            } catch (IOException ex) {
                logger.warning("Couldn't create module folder for module " + module.getName() + "! " +
                    "(" + ex.getMessage() + ")");
            }
        });
    }

    /**
     * Get a list of all loaded modules.
     * @return An unmodifiable list of all loaded modules.
     */
    public List<Module> getModules() {
        return Collections.unmodifiableList(modules);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    <T extends Module> T getModule(Class<T> moduleClass) {
        return (T) modules.stream().filter(module -> module.getClass().equals(moduleClass))
            .findFirst()
            .orElse(null);
    }

    public boolean isModuleLoaded(Class<? extends Module> moduleClass) {
        return modules.stream().anyMatch(module -> module.getClass().equals(moduleClass));
    }

    public void registerModule(Module module) {
        modules.add(module);
    }
}
