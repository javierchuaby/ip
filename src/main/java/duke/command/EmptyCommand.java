package duke.command;

import duke.task.TaskList;
import duke.ui.Ui;

public class EmptyCommand implements Command {
    @Override
    public void execute(TaskList tasks, Ui ui) {
        ui.printUnknownEmpty();
    }
}