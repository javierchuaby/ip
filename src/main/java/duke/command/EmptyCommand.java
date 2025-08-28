package duke.command;

import duke.task.TaskList;
import duke.ui.Ui;

/**
 * Represents a command executed when the user provides empty input.
 * Displays a message indicating that no command was recognized.
 */
public class EmptyCommand implements Command {
    /**
     * Executes the empty command by displaying an unknown empty input message.
     *
     * @param tasks The task list (not used in this command)
     * @param ui The user interface for displaying the message
     */
    @Override
    public void execute(TaskList tasks, Ui ui) {
        ui.printUnknownEmpty();
    }
}
