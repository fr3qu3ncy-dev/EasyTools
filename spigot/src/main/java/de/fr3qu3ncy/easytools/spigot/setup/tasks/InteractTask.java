package de.fr3qu3ncy.easytools.spigot.setup.tasks;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;
import java.util.function.Consumer;

public class InteractTask extends SetupTask {

    @Getter
    private Consumer<Location> handler;

    @Getter
    private Consumer<Location> undoHandler;

    @Getter
    private Action blockAction;

    @Getter
    private Material blockType;

    @Getter
    private boolean ignoreBlockType;

    @Getter
    private Material replaceSelectedWith;

    @Getter
    private Action undoAction;

    @Getter
    private boolean listeningToInteractions;

    public InteractTask() {
        this.ignoreBlockType = true;
    }

    @Override
    public void addTask(Map<TaskType, SetupTask> tasks) {
        tasks.put(TaskType.INTERACT, this);
    }

    @Override
    public void preRun() {
        this.listeningToInteractions = true;
    }

    public boolean process(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return false;
        Location location = event.getClickedBlock().getLocation();

        //Check if action is blockAction
        if (event.getAction() == blockAction) {
            //Check if is correct block
            if (location.getBlock().getType() != blockType && !ignoreBlockType) return false;

            progressSetup();
            handler.accept(location);

            //Check if replace is enabled
            if (replaceSelectedWith != null) {
                location.getWorld().setType(location, replaceSelectedWith);
            }

            return true;
        } else if (getUndoAction() != null && event.getAction() == getUndoAction()) {
            //Check if action is undo action
            undoHandler.accept(location);
            return true;
        }

        if (event.getAction() != blockAction) return false;
        if (location.getBlock().getType() != blockType && !ignoreBlockType) return false;
        progressSetup();
        handler.accept(location);
        return true;
    }

    @Override
    public void postRun() {
        this.listeningToInteractions = false;
    }

    public InteractTask setHandler(Consumer<Location> handler) {
        this.handler = handler;
        return this;
    }

    public InteractTask setUndoHandler(Consumer<Location> undoHandler) {
        this.undoHandler = undoHandler;
        return this;
    }

    public InteractTask setBlockAction(Action action) {
        this.blockAction = action;
        return this;
    }

    public InteractTask setBlockType(Material material) {
        this.blockType = material;
        this.ignoreBlockType = false;
        return this;
    }

    public InteractTask ignoreBlockType() {
        this.ignoreBlockType = true;
        return this;
    }

    public InteractTask replaceSelectedWith(Material material) {
        this.replaceSelectedWith = material;
        return this;
    }

    public InteractTask setUndoAction(Action action) {
        this.undoAction = action;
        return this;
    }
}
