package duke.command;

import duke.task.TaskList;
import duke.ui.Ui;

/**
 * Represents a command to exit the Duke application. This command terminates the program
 * gracefully.
 */
public class ExitCommand implements Command {
    /**
     * Executes the exit command by displaying a goodbye message.
     *
     * @param tasks The task list (not used in this command)
     * @param ui    The user interface for displaying the goodbye message
     */
    @Override
    public void execute(TaskList tasks, Ui ui) {
        ui.printGoodbye();
    }

    /**
     * Indicates that this command should terminate the application.
     *
     * @return true, as this command always exits the application
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
