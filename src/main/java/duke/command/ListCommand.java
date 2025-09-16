package duke.command;

import duke.task.TaskList;
import duke.ui.Ui;

/**
 * Represents a command to display all tasks in the task list. Shows the current tasks with their
 * indices and completion status.
 */
public class ListCommand implements Command {
    /**
     * Executes the list command by displaying all tasks in the task list.
     *
     * @param tasks The task list to display
     * @param ui    The user interface for displaying the task list
     */
    @Override
    public void execute(TaskList tasks, Ui ui) {
        ui.printList(tasks.asUnmodifiable());
    }
}
