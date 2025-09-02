package duke.command;

import duke.task.Task;
import duke.task.TaskList;
import duke.task.Todo;
import duke.ui.Ui;

/**
 * Represents a command to create and add a todo task to the task list.
 * A todo task has only a description without any time constraints.
 */
public class TodoCommand implements Command {

    /**
     * The description of the todo task
     */
    private final String description;

    /**
     * Constructs a TodoCommand with the specified description.
     *
     * @param description The description of the todo task
     */
    public TodoCommand(String description) {
        this.description = description;
    }

    /**
     * Executes the todo command by creating a new todo task and adding it to the list.
     * If the description is empty, shows usage information.
     *
     * @param tasks The task list to add the todo to
     * @param ui    The user interface for displaying results and errors
     */
    @Override
    public void execute(TaskList tasks, Ui ui) {
        if (description == null || description.trim().isEmpty()) {
            ui.printUsage("Usage: todo <description>");
            return;
        }

        Task t = new Todo(description.trim());
        tasks.add(t);
        ui.printAdded(t, tasks.size());
    }
}
