package duke.util;

import duke.task.Task;
import duke.task.TaskType;

/**
 * Represents the current state of an update operation.
 * Tracks which task is being updated and what step we're on.
 */
public class UpdateStateUtil {
    public enum Step {
        WAITING_FOR_CHOICE,
        WAITING_FOR_DESCRIPTION,
        WAITING_FOR_DATE,
        WAITING_FOR_START_DATE,
        WAITING_FOR_END_DATE
    }

    private final int taskIndex;
    private final Task originalTask;
    private Step currentStep;
    private String newStartDate;

    public UpdateStateUtil(int taskIndex, Task task) {
        this.taskIndex = taskIndex;
        this.originalTask = task;

        if (task.getTaskType() == TaskType.TODO) {
            this.currentStep = Step.WAITING_FOR_DESCRIPTION;
        } else {
            this.currentStep = Step.WAITING_FOR_CHOICE;
        }
    }

    public int getTaskIndex() {
        return taskIndex;
    }

    public Task getOriginalTask() {
        return originalTask;
    }

    public Step getCurrentStep() {
        return currentStep;
    }

    public String getNewStartDate() {
        return newStartDate;
    }

    public void setStep(Step step) {
        this.currentStep = step;
    }

    public void setNewStartDate(String startDate) {
        this.newStartDate = startDate;
    }
}
