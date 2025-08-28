package duke.command;

import duke.task.Event;
import duke.task.TaskList;
import duke.ui.Ui;

/**
 * Represents a command to create and add an event task to the task list.
 * An event task has a description, start time, and end time.
 */
public class EventCommand implements Command {
    /** The description of the event */
    private final String description;

    /** The start time input string */
    private final String fromInput;

    /** The end time input string */
    private final String toInput;

    /**
     * Constructs an EventCommand with the specified description and time range.
     *
     * @param description The description of the event
     * @param fromInput The start time string
     * @param toInput The end time string
     */
    public EventCommand(String description, String fromInput, String toInput) {
        this.description = description;
        this.fromInput = fromInput;
        this.toInput = toInput;
    }

    /**
     * Executes the event command by creating a new event task and adding it to the list.
     * If the description is empty or the time format is invalid, shows appropriate error messages.
     *
     * @param tasks The task list to add the event to
     * @param ui The user interface for displaying results and errors
     */
    @Override
    public void execute(TaskList tasks, Ui ui) {
        if (description == null || description.trim().isEmpty()) {
            ui.printEventFormat();
            return;
        }
        try {
            Event e = new Event(description.trim(), fromInput.trim(), toInput.trim());
            tasks.add(e);
            ui.printAdded(e, tasks.size());
        } catch (IllegalArgumentException ex) {
            ui.printUsage("I couldn’t read those dates/times: " + ex.getMessage());
        }
    }
}
