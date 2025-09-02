package duke.command;

import duke.task.TaskList;
import duke.ui.Ui;
import duke.util.DateTimeUtil;

/**
 * Represents a command to display tasks scheduled for a specific date.
 * Shows all tasks that are due or scheduled on the given date.
 */
public class AgendaCommand implements Command {

    /**
     * The date input string provided by the user
     */
    private final String dateInput;

    /**
     * Constructs an AgendaCommand with the specified date input.
     *
     * @param dateInput The date string to search for tasks
     */
    public AgendaCommand(String dateInput) {
        this.dateInput = dateInput;
    }

    /**
     * Executes the agenda command by parsing the date and displaying matching tasks.
     * If the date input is invalid or empty, shows usage information.
     *
     * @param tasks The task list to search through
     * @param ui    The user interface for displaying results
     */
    @Override
    public void execute(TaskList tasks, Ui ui) {
        if (dateInput == null || dateInput.trim().isEmpty()) {
            ui.printAgendaFormat();
            return;
        }

        try {
            var r = DateTimeUtil.parseLenientResult(dateInput.trim());
            var target = r.dt.toLocalDate();
            var matches = tasks.tasksOn(target);
            ui.printAgendaForDate(target, matches, tasks);
        } catch (IllegalArgumentException ex) {
            ui.printUsage("I couldn't read that date: " + ex.getMessage());
        }
    }
}
