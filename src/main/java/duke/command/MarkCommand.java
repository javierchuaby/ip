package duke.command;

import duke.task.Task;
import duke.task.TaskList;
import duke.ui.Ui;

/**
 * Represents a command to mark or unmark a task as completed.
 * Changes the completion status of the specified task.
 */
public class MarkCommand implements Command {
    /** The 1-based index of the task to mark/unmark */
    private final int index;
    /** Whether to mark (true) or unmark (false) the task */
    private final boolean mark;

    /**
     * Constructs a MarkCommand with the specified task index and mark status.
     *
     * @param index The 1-based index of the task to mark/unmark
     * @param mark true to mark as completed, false to unmark
     */
    public MarkCommand(int index, boolean mark) {
        this.index = index;
        this.mark = mark;
    }

    /**
     * Executes the mark command by changing the completion status of the specified task.
     * If the index is out of bounds, shows an error message.
     *
     * @param tasks The task list containing the task to mark/unmark
     * @param ui The user interface for displaying results and errors
     */
    @Override
    public void execute(TaskList tasks, Ui ui) {
        if (index < 1 || index > tasks.size()) {
            ui.printUsage("Please use a duke.task number between 1 and " + tasks.size() + ".");
            return;
        }
        if (mark) {
            tasks.mark(index - 1);
        } else {
            tasks.unmark(index - 1);
        }
        Task t = tasks.get(index - 1);
        ui.printMarked(t, mark);
    }
}
