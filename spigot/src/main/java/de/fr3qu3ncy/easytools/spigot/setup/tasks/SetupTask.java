package de.fr3qu3ncy.easytools.spigot.setup.tasks;

import de.fr3qu3ncy.easytools.spigot.setup.PhaseTask;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

public abstract class SetupTask {

    @Getter @Setter
    private PhaseTask<?> phaseTask;

    @Getter @Setter
    private String startMessage;

    public abstract void addTask(Map<TaskType, SetupTask> tasks);

    public abstract void preRun();

    public abstract void postRun();

    public void progressSetup() {
        phaseTask.progressSetup();
    }
}
