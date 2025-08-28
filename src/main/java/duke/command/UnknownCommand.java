package duke.command;

import duke.task.TaskList;
import duke.ui.Ui;

/**
 * Represents a command for handling unrecognized user input.
 * Displays an error message with the invalid input.
 */
public class UnknownCommand implements Command {
    /** The unrecognized input string */
    private final String input;

    /**
     * Constructs an UnknownCommand with the unrecognized input.
     *
     * @param input The unrecognized command input
     */
    public UnknownCommand(String input) {
        this.input = input;
    }

    /**
     * Executes the unknown command by displaying an error message.
     *
     * @param tasks The task list (not used in this command)
     * @param ui The user interface for displaying the error message
     */
    @Override
    public void execute(TaskList tasks, Ui ui) {
        ui.printUnknown(input);
    }
}
