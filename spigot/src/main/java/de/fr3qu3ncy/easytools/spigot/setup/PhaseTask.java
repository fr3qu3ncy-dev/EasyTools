package de.fr3qu3ncy.easytools.spigot.setup;

import de.fr3qu3ncy.easytools.spigot.setup.tasks.SetupTask;
import de.fr3qu3ncy.easytools.spigot.setup.tasks.TaskType;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PhaseTask<S> {

    @Getter @Setter
    private Setup<S> setup;

    @Getter @Setter
    private S setupPhase;

    @Getter @Setter
    private int order;

    @Getter
    private boolean autoProgress;

    @Getter
    private String startMessage;

    @Getter
    private final Map<TaskType, SetupTask> tasks;

    public PhaseTask(S setupPhase) {
        this.setupPhase = setupPhase;
        this.tasks = new HashMap<>();
        this.autoProgress = true;
    }

    public S getPhase() {
        return setupPhase;
    }

    public PhaseTask<S> addTasks(SetupTask... addTasks) {
        Arrays.asList(addTasks).forEach(task -> {
            task.addTask(this.tasks);
            task.setPhaseTask(this);
        });
        return this;
    }

    public SetupTask getTask(TaskType type) {
        return tasks.get(type);
    }

    public void preRun() {
        if (getStartMessage() != null && !getStartMessage().isEmpty())
            getSetup().getPlayer().sendMessage(getStartMessage());
        tasks.values().forEach(SetupTask::preRun);
    }

    public void postRun() {
        tasks.values().forEach(SetupTask::postRun);
    }

    public void progressSetup() {
        setup.progress();
    }

    public PhaseTask<S> setStartMessage(String message) {
        this.startMessage = message;
        return this;
    }

    public PhaseTask<S> disableAutoProgress() {
        this.autoProgress = false;
        return this;
    }
}
