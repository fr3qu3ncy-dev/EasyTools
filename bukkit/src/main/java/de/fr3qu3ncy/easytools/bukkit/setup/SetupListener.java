package de.fr3qu3ncy.easytools.bukkit.setup;

import de.fr3qu3ncy.easytools.bukkit.setup.tasks.SetupTask;
import de.fr3qu3ncy.easytools.bukkit.setup.tasks.TaskType;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface SetupListener<T extends SetupTask> extends Listener {

    default T findSetupTask(UUID player, TaskType type) {
        List<Setup<?>> activeSetups = findActiveSetups(player);
        if (activeSetups == null || activeSetups.size() == 0) return null;

        for (Setup<?> setup : activeSetups) {
            PhaseTask<?> currentTask = getCurrentTask(setup);
            if (currentTask == null) continue;

            T responsibleTask = getResponsibleTask(currentTask, type);
            if (responsibleTask == null) continue;
            return responsibleTask;
        }
        return null;
    }

    default List<Setup<?>> findActiveSetups(UUID player) {
        return Setup.SETUPS.get(player);
    }

    default PhaseTask<?> getCurrentTask(Setup<?> setup) {
        return setup.getCurrentTask();
    }

    default List<SetupTask> getRunningTasks(PhaseTask<?> phaseTask) {
        return new ArrayList<>(phaseTask.getTasks().values());
    }

    default T getResponsibleTask(PhaseTask<?> phaseTask, TaskType type) {
        return (T) phaseTask.getTasks().get(type);
    }
}
