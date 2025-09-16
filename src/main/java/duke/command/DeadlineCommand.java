package duke.command;

import duke.task.Deadline;
import duke.task.TaskList;
import duke.ui.Ui;

/**
 * Represents a command to create and add a deadline task to the task list. A deadline task has a
 * description and a due date/time.
 */
public class DeadlineCommand implements Command {

    /**
     * The description of the deadline task
     */
    private final String description;

    /**
     * The due date/time input string
     */
    private final String byInput;

    /**
     * The due date/time input string
     */
    public DeadlineCommand(String description, String byInput) {
        this.description = description;
        this.byInput = byInput;
    }

    /**
     * Executes the deadline command by creating a new deadline task and adding it to the list. If
     * the description is empty or the date format is invalid, shows appropriate error messages.
     *
     * @param tasks The task list to add the deadline to
     * @param ui    The user interface for displaying results and errors
     */
    @Override
    public void execute(TaskList tasks, Ui ui) {
        if (description == null || description.trim().isEmpty()) {
            ui.printDeadlineFormat();
            return;
        }
        try {
            Deadline d = new Deadline(description.trim(), byInput.trim());
            tasks.add(d);
            ui.printAdded(d, tasks.size());
        } catch (IllegalArgumentException ex) {
            ui.printUsage("I couldn’t read that date/time: " + ex.getMessage());
        }
    }
}
