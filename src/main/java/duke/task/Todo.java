package duke.task;

/**
 * Represents a simple todo task without any time constraints.
 * A todo task only has a description and completion status.
 * Extends the Task class with basic functionality.
 */
public class Todo extends Task {

    /**
     * Constructs a Todo task with the specified description.
     *
     * @param description The description of the todo task
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.TODO;
    }

    /**
     * Returns the string representation of the todo task.
     * Format: [T] [status] description
     *
     * @return A formatted string describing the todo task
     */
    @Override
    public String toString() {
        return "[T] [" + getStatusIcon() + "] " + description;
    }
}
