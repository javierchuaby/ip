package duke.command;

import java.util.List;

import duke.task.Task;
import duke.task.TaskList;
import duke.ui.Ui;

/**
 * Represents a command to find tasks that contain a specific keyword in their description.
 * Shows all matching tasks as a numbered list.
 */
public class FindCommand implements Command {
    /**
     * The keyword to search for in task descriptions
     */
    private final String keyword;

    /**
     * Constructs a FindCommand with the specified keyword.
     *
     * @param keyword The keyword to search for in task descriptions
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Executes the find command by searching for tasks containing the keyword.
     * If the keyword is empty, shows usage information.
     *
     * @param tasks The task list to search through
     * @param ui    The user interface for displaying results
     */
    @Override
    public void execute(TaskList tasks, Ui ui) {
        if (keyword == null || keyword.trim().isEmpty()) {
            ui.printUsage("Usage: find <keyword>");
            return;
        }

        List<Task> matches = tasks.findByKeyword(keyword.trim());
        ui.printFindResults(keyword.trim(), matches);
    }
}
