package de.fr3qu3ncy.easytools.spigot.setup;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class Setup<T> {

    public static final Map<UUID, List<Setup<?>>> SETUPS = new HashMap<>();

    protected List<PhaseTask<T>> phaseTasks;

    @Getter
    protected PhaseTask<T> currentTask, calledTask;

    protected int nextTaskOrder;

    @Getter
    protected final Player player;

    public Setup(Player player) {
        this.player = player;
        this.phaseTasks = new ArrayList<>();

        addSetup();
    }

    private void addSetup() {
        if (!SETUPS.containsKey(player.getUniqueId())) {
            SETUPS.put(player.getUniqueId(), new ArrayList<>());
        }
        List<Setup<?>> setups = SETUPS.get(player.getUniqueId());
        setups.add(this);
        SETUPS.put(player.getUniqueId(), setups);
    }

    @SafeVarargs
    protected final void addPhases(PhaseTask<T>... tasks) {
        for (int i = 0 ; i < tasks.length ; i++) {
            PhaseTask<T> task = tasks[i];
            task.setSetup(this);
            task.setOrder(i);
            phaseTasks.add(task);
        }
    }

    public void start() {
        runTask(0);
    }

    public void runTask(T phase) {
        for (PhaseTask<T> phaseTask : phaseTasks) {
            if (phaseTask.getPhase() == phase) {
                runTask(phaseTask);
                return;
            }
        }
    }

    protected void runTask(PhaseTask<?> newTask) {
        runTask(newTask.getOrder());
    }

    protected void runTask(int order) {
        if (order >= phaseTasks.size()) return;

        currentTask = phaseTasks.get(order);
        nextTaskOrder = order + 1;
        currentTask.preRun();
    }

    protected void progress() {
        if (currentTask.isAutoProgress()) {
            currentTask.postRun();
            runTask(nextTaskOrder);
        }
    }

    public void stop() {
        currentTask.postRun();
        done();
    }

    protected void done() {
        SETUPS.remove(player.getUniqueId());
    }
}