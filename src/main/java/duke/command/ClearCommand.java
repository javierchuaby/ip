package duke.command;

import duke.task.TaskList;
import duke.ui.Ui;

/**
 * Represents a command to clear all tasks from the task list.
 * Provides user confirmation before clearing tasks.
 */
public class ClearCommand implements Command {

    /**
     * Executes the clear command by prompting the user for confirmation.
     * If no tasks exist, informs the user that there are no tasks to clear.
     *
     * @param tasks The task list to potentially clear
     * @param ui    The user interface for displaying prompts and messages
     */
    @Override
    public void execute(TaskList tasks, Ui ui) {
        if (tasks.size() == 0) {
            ui.printNoTasksToClear();
            return;
        }

        ui.printClearPrompt();
    }
}
