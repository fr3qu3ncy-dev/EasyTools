package de.fr3qu3ncy.easytools.bukkit.setup.tasks;

import lombok.Getter;

import java.util.Map;

public class BaseTask extends SetupTask {

    @Getter
    private Runnable handler;

    @Override
    public void addTask(Map<TaskType, SetupTask> tasks) {
        tasks.put(TaskType.BASE, this);
    }

    @Override
    public void preRun() {
        handler.run();
        progressSetup();
    }

    @Override
    public void postRun() {}

    public BaseTask setHandler(Runnable handler) {
        this.handler = handler;
        return this;
    }
}
