package duke.command;

import duke.task.Task;
import duke.task.TaskList;
import duke.ui.Ui;

/**
 * Command to initiate updating an existing task.
 * This command starts the interactive update process.
 */
public class UpdateCommand implements Command {
    private final int taskIndex;

    public UpdateCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    public int getTaskIndex() {
        return taskIndex;
    }

    @Override
    public void execute(TaskList tasks, Ui ui) {
        if (tasks.size() == 0) {
            ui.printNoTasksInList();
            return;
        }

        if (taskIndex < 1 || taskIndex > tasks.size()) {
            ui.printUsage("Invalid task number. Please specify a number between 1 and "
                    + tasks.size() + ".");
            return;
        }

        Task task = tasks.get(taskIndex - 1);
        ui.printUpdatePrompt(task, taskIndex);
    }
}
