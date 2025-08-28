package duke.command;

import duke.task.Deadline;
import duke.task.TaskList;
import duke.ui.Ui;

public class DeadlineCommand implements Command {
    private final String description;
    private final String byInput;

    public DeadlineCommand(String description, String byInput) {
        this.description = description;
        this.byInput = byInput;
    }

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