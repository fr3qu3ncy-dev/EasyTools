package de.fr3qu3ncy.easytools.spigot.setup;

import de.fr3qu3ncy.easytools.spigot.setup.tasks.InteractTask;
import de.fr3qu3ncy.easytools.spigot.setup.tasks.TaskType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

public class SetupInteractListener implements SetupListener<InteractTask> {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        boolean success = progressTask(player.getUniqueId(), event);
        event.setCancelled(success);
    }

    private boolean progressTask(UUID player, PlayerInteractEvent event) {
        InteractTask interactTask = findSetupTask(player, TaskType.INTERACT);
        if (interactTask == null) return false;
        return interactTask.process(event);
    }
}