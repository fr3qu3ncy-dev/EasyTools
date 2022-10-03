package de.fr3qu3ncy.easytools.bukkit.setup.tasks;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.function.Consumer;

public class ChatTask extends SetupTask {

    @Getter
    private Consumer<String> messageHandler;

    @Getter
    private String[] acceptedMessages;

    @Getter @Setter
    private boolean listeningToChat;

    @Override
    public void addTask(Map<TaskType, SetupTask> tasks) {
        tasks.put(TaskType.CHAT, this);
    }

    @Override
    public void preRun() {
        this.listeningToChat = true;
    }

    public boolean process(String message, JavaPlugin plugin) {
        if (this.listeningToChat && this.containsMessage(message)) {

            //Run these from main thread because method is called asynchronously
            Bukkit.getScheduler().runTask(plugin, () -> {
                messageHandler.accept(message);
                progressSetup();
            });

            return true;
        }
        return false;
    }

    private boolean containsMessage(String message) {
        if (getAcceptedMessages() == null) return true;
        for (String accept : getAcceptedMessages()) {
            if (accept.equalsIgnoreCase(message)) return true;
        }
        return false;
    }

    @Override
    public void postRun() {
        this.listeningToChat = false;
    }

    public ChatTask setMessageHandler(Consumer<String> messageHandler) {
        this.messageHandler = messageHandler;
        return this;
    }

    public ChatTask setAcceptedMessages(String... messages) {
        this.acceptedMessages = messages;
        return this;
    }
}
