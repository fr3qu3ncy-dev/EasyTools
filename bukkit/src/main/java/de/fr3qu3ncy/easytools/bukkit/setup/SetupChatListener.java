package de.fr3qu3ncy.easytools.bukkit.setup;

import de.fr3qu3ncy.easytools.bukkit.BukkitTools;
import de.fr3qu3ncy.easytools.bukkit.setup.tasks.ChatTask;
import de.fr3qu3ncy.easytools.bukkit.setup.tasks.TaskType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class SetupChatListener implements SetupListener<ChatTask> {

    @EventHandler
    public void onChat(final AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        ChatTask chatTask = findSetupTask(player.getUniqueId(), TaskType.CHAT);
        if (chatTask != null) {
            boolean success = progressTask(chatTask, message);
            event.setCancelled(success);
        }
    }

    private boolean progressTask(ChatTask chatTask, String message) {
       return chatTask.process(message, BukkitTools.getInstance());
    }
}
