package duke.task;

/**
 * Abstract class representing a task in the Duke task management system.
 * All tasks have a description and completion status.
 * Subclasses must implement the toString method for display formatting.
 *
 * @author Your Name
 * @version 1.0
 */
public abstract class Task {

    /**
     * The description of the task
     */
    protected String description;

    /**
     * Whether the task is marked as completed
     */
    protected boolean isDone;

    /**
     * Constructs a Task with the specified description.
     * The task is initially marked as not done.
     *
     * @param description The description of the task
     */
    public Task(String description) {
        assert description != null && !description.trim().isEmpty() : "Task description cannot be null or empty";

        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the description of the task.
     *
     * @return The task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether the task is marked as done.
     *
     * @return true if the task is completed, false otherwise
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Marks the task as completed.
     */
    public void mark() {
        isDone = true;
    }

    /**
     * Marks the task as not completed.
     */
    public void unmark() {
        isDone = false;
    }

    /**
     * Returns a status icon representing the completion status.
     *
     * @return "X" if the task is done, " " (space) if not done
     */
    public String getStatusIcon() {
        return isDone ? "X" : "  ";
    }

    /**
     * Returns the type of this task.
     *
     * @return The TaskType enum value for this task
     */
    public abstract TaskType getTaskType();

    /**
     * Returns the string representation of the task.
     * Must be implemented by subclasses to define their specific format.
     *
     * @return A string representation of the task
     */
    @Override
    public abstract String toString();
}
