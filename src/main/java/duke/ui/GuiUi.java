package duke.ui;

import java.time.LocalDate;
import java.util.List;

import duke.task.Task;
import duke.task.TaskList;
import duke.util.CommandListingUtil;

/**
 * GUI-specific UI that captures output as strings instead of printing to console
 */
public class GuiUi extends Ui {
    private StringBuilder output = new StringBuilder();

    public GuiUi() {
        super(System.out);
    }

    private void append(String text) {
        if (output.length() > 0) {
            output.append("\n");
        }
        output.append(text);
    }

    @Override
    public void printWelcome() {
        append("Hello! I'm Mr Moon!");
        append("What can I do for you?");
    }

    @Override
    public void printGoodbye() {
        append("Bye bye. Talk to you again tmr!\n");
        append("Cheers,");
        append("Mr Moon");
    }

    @Override
    public void printUsage(String message) {
        append(message);
    }

    @Override
    public void printList(List<Task> items) {
        if (items.isEmpty()) {
            append("You have no tasks in your list!");
        } else {
            append("Here are the tasks in your list:");
            for (int i = 0; i < items.size(); i++) {
                append((i + 1) + ". " + items.get(i).toString());
            }
        }
    }

    @Override
    public void printAdded(Task task, int newSize) {
        append("Got it. I've added this task:");
        append("  " + task.toString());
        append("Now you have " + newSize + " task(s) in the list.");
    }

    @Override
    public void printMarked(Task task, boolean mark) {
        append(mark ? "Nice! I've marked this task as done!" : "Nice! I've marked this task as not done yet!");
        append(task.toString());
    }

    @Override
    public void printDelete(Task task, int newSize) {
        append("Noted. I've removed this task:");
        append("  " + task.toString());
        append("Now you have " + newSize + " task(s) in the list.");
    }

    @Override
    public void printFindResults(String keyword, List<Task> matches) {
        append("Here are the matching tasks for '" + keyword + "':");
        if (matches.isEmpty()) {
            append("(no matching tasks found)");
        } else {
            for (int i = 0; i < matches.size(); i++) {
                append((i + 1) + ". " + matches.get(i).toString());
            }
        }
    }

    @Override
    public void printAgendaForDate(LocalDate date, List<Task> items, TaskList fullList) {
        append("Tasks on " + date.format(java.time.format.DateTimeFormatter.ofPattern("d MMM yyyy")) + ":");
        if (items.isEmpty()) {
            append("(none)");
        } else {
            for (int i = 0; i < items.size(); i++) {
                int originalIdx = fullList.indexOf(items.get(i)) + 1;
                append((i + 1) + ". " + items.get(i).toString() + " [#" + originalIdx + " in main list]");
            }
        }
    }

    @Override
    public void printClearPrompt() {
        append("Are you sure you want to clear all tasks?");
        append("Type 'yes' or 'no' to proceed");
    }

    @Override
    public void printCleared() {
        append("All tasks have been cleared!");
    }

    @Override
    public void printClearCanceled() {
        append("Clear operation canceled.");
    }

    @Override
    public void printNoTasksToClear() {
        append("There are no tasks in your list!");
    }

    /**
     * Prints an error message for unknown user commands.
     * Includes a helpful list of available commands.
     *
     * @param input The unrecognized command string from the user
     */
    @Override
    public void printUnknown(String input) {
        append("Sorry, I do not understand what " + input + " means.");
        append("Try one of these:");
        CommandListingUtil.appendCommands(this::append);
    }

    /**
     * Prints command instructions when the user provides empty input.
     */
    @Override
    public void printUnknownEmpty() {
        append("Use the following commands:");
        CommandListingUtil.appendCommands(this::append);
    }

    /**
     * Gets the captured output and clears the buffer
     */
    public String getResponse() {
        String result = output.toString();
        output.setLength(0);

        if (result.isEmpty()) {
            printUnknownEmpty();
            result = output.toString();
            output.setLength(0);
        }

        return result;
    }
}
