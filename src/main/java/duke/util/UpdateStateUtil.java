package duke.util;

import duke.task.Task;
import duke.task.TaskType;

/**
 * Represents the current state of an interactive task update operation.
 * <p>
 * This utility tracks:
 * <ul>
 *   <li>Which task (by index and reference) is currently being updated</li>
 *   <li>The current step in the update flow (e.g., waiting for a new description or date)</li>
 *   <li>Any transient values entered by the user during multi-step updates (e.g., a new start date)</li>
 * </ul>
 * The instance is meant to be short-lived and scoped to a single ongoing update interaction.
 */
public class UpdateStateUtil {

    /**
     * The sequence of steps that an update interaction may require depending on the task type.
     * <p>
     * - TODO tasks usually skip the choice step and go straight to description updates.<br>
     * - DEADLINE tasks typically require a single date/time.<br>
     * - EVENT tasks generally require both start and end date/time values.
     */
    public enum Step {
        /** Waiting for the user to choose which field to update (e.g., description/date). */
        WAITING_FOR_CHOICE,
        /** Waiting for the user to provide a new description. */
        WAITING_FOR_DESCRIPTION,
        /** Waiting for a single new date/time (e.g., for Deadline). */
        WAITING_FOR_DATE,
        /** Waiting for a new start date/time (e.g., for Event). */
        WAITING_FOR_START_DATE,
        /** Waiting for a new end date/time (e.g., for Event). */
        WAITING_FOR_END_DATE
    }

    private final int taskIndex;
    private final Task originalTask;
    private Step currentStep;
    private String newStartDate;

    /**
     * Creates a new UpdateStateUtil for the given task index and task reference.
     *
     * @param taskIndex the zero-based index of the task being updated within the task list
     * @param task      the original task object under update
     */
    public UpdateStateUtil(int taskIndex, Task task) {
        this.taskIndex = taskIndex;
        this.originalTask = task;

        if (task.getTaskType() == TaskType.TODO) {
            this.currentStep = Step.WAITING_FOR_DESCRIPTION;
        } else {
            this.currentStep = Step.WAITING_FOR_CHOICE;
        }
    }

    /**
     * Returns the zero-based index of the task being updated.
     *
     * @return the index of the task in the current task list
     */
    public int getTaskIndex() {
        return taskIndex;
    }

    /**
     * Returns the original task that is being updated.
     *
     * @return the original {@link Task} reference
     */
    public Task getOriginalTask() {
        return originalTask;
    }

    /**
     * Returns the current step of the update interaction.
     *
     * @return the current {@link Step}
     */
    public Step getCurrentStep() {
        return currentStep;
    }

    /**
     * Returns the new start date/time captured so far for multi-step updates (e.g., for an Event),
     * or {@code null} if not yet provided.
     *
     * @return the new start date/time as a raw string entered by the user, or null if absent
     */
    public String getNewStartDate() {
        return newStartDate;
    }

    /**
     * Advances or changes the current step of the update interaction.
     *
     * @param step the next {@link Step} to set
     */
    public void setStep(Step step) {
        this.currentStep = step;
    }

    /**
     * Stores the newly entered start date/time (typically used during Event updates).
     * The value is expected to be the raw user input prior to parsing/validation.
     *
     * @param startDate the new start date/time as entered by the user
     */
    public void setNewStartDate(String startDate) {
        this.newStartDate = startDate;
    }
}
