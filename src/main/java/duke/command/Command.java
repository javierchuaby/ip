package duke.command;

import duke.task.TaskList;
import duke.ui.Ui;

/**
 * Represents a command that can be executed in the Duke task management system.
 * All commands must implement the execute method to define their behavior.
 *
 * @author Javier Chua
 * @version 1.0
 */
public interface Command {

    /**
     * Executes the command with the given task list and user interface.
     *
     * @param tasks The task list to operate on
     * @param ui    The user interface for output and interaction
     */
    void execute(TaskList tasks, Ui ui);

    /**
     * Indicates whether this command should terminate the application.
     *
     * @return true if the application should exit after this command, false otherwise
     */
    default boolean isExit() {
        return false;
    }
}
