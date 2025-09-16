package duke.command;

import duke.task.Task;
import duke.task.TaskList;
import duke.ui.Ui;

/**
 * Represents a command to delete a task from the task list by its index. The task is permanently
 * removed from the list.
 */
public class DeleteCommand implements Command {
    /**
     * The 1-based index of the task to delete
     */
    private final int index;

    /**
     * Constructs a DeleteCommand with the specified task index.
     *
     * @param index The 1-based index of the task to delete
     */
    public DeleteCommand(int index) {
        this.index = index;
    }

    /**
     * Executes the delete command by removing the task at the specified index. If the index is out
     * of bounds, shows an error message.
     *
     * @param tasks The task list to remove the task from
     * @param ui    The user interface for displaying results and errors
     */
    @Override
    public void execute(TaskList tasks, Ui ui) {
        if (index < 1 || index > tasks.size()) {
            ui.printUsage("Please use a duke.task number between 1 and " + tasks.size() + ".");
            return;
        }
        Task removed = tasks.remove(index - 1);
        ui.printDelete(removed, tasks.size());
    }
}
