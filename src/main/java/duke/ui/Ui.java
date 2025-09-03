package duke.ui;

import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import duke.task.Task;
import duke.task.TaskList;
import duke.util.CommandListingUtil;

/**
 * User Interface component handling console input/output operations.
 * Provides formatted output methods for different types of messages and task displays.
 * Uses PrintStream for testability and flexible output redirection.
 */
public class Ui {
    private final PrintStream out;

    /**
     *
     */
    public Ui(PrintStream out) {
        this.out = out;
    }

    /**
     * Prints a horizontal separator line to visually separate sections.
     */
    public void printLine() {
        out.println("    " + "__________________________________________________");
    }

    /**
     * Prints the welcome message when the application starts.
     */
    public void printWelcome() {
        printLine();
        out.println("    Hello! I'm Mr Moon!");
        out.println("    What can I do for you?");
        printLine();
    }

    /**
     * Prints the goodbye message when the application exits.
     */
    public void printGoodbye() {
        printLine();
        out.println("    Bye bye. Talk to you again tmr!");
        out.println();
        out.println("    Cheers,");
        out.println("    Mr Moon");
        printLine();
    }

    /**
     * Prints an error message for unknown user commands.
     * Includes a helpful list of available commands.
     *
     * @param input The unrecognized command string from the user
     */
    public void printUnknown(String input) {
        printLine();
        out.println("    " + "Sorry, I do not understand what " + input + " means.");
        out.println("    " + "Try one of these:");
        CommandListingUtil.appendCommands(cmd -> out.println("    " + cmd));
        printLine();
    }

    /**
     * Prints command instructions when the user provides empty input.
     */
    public void printUnknownEmpty() {
        printLine();
        out.println("    " + "Use the following commands:");
        CommandListingUtil.appendCommands(cmd -> out.println("    " + cmd));
        printLine();
    }

    /**
     * Prints usage instructions for the agenda command.
     */
    public void printAgendaFormat() {
        printLine();
        out.println("    Usage: on <date>");
        out.println("    Examples:");
        out.println("    on 9 Aug");
        out.println("    on 2025-12-02");
        out.println("    on 2/12/2025");
        printLine();
    }

    /**
     * Prints the agenda (list of tasks) for a specific date.
     * Shows task indices from the main list for user reference.
     *
     * @param date     The date to display tasks for
     * @param items    The list of tasks occurring on the specified date
     * @param fullList The complete task list for index reference
     */
    public void printAgendaForDate(LocalDate date, List<Task> items, TaskList fullList) {
        printLine();
        out.println("    Tasks on " + date.format(DateTimeFormatter.ofPattern("d MMM uuuu")) + ":");

        if (items.isEmpty()) {
            out.println("    (none)");
            printLine();
            return;
        }

        int i = 1;
        for (Task t : items) {
            int originalIdx = fullList.indexOf(t) + 1;
            out.println("    " + i + ". " + t.toString() + " [#" + originalIdx + " in main list]");
            i++;
        }

        printLine();
    }

    /**
     * Prints usage instructions for the deadline command.
     */
    public void printDeadlineFormat() {
        printLine();
        out.println("    " + "Usage: deadline <description> /by <date>");
        out.println("    " + "Example: deadline return book /by 12-3-2025 1800");
        printLine();
    }

    /**
     * Prints usage instructions for the event command.
     */
    public void printEventFormat() {
        printLine();
        out.println("    " + "Usage: event <description> /from <date> /to <date>");
        out.println("    " + "Examples:");
        out.println("    " + " event conference /from 9 Aug /to 10 Aug");
        out.println("    " + " event project meeting /from 2/12/2025 1800 /to 2/12/2025 2000");
        printLine();
    }

    /**
     * Prints the complete list of tasks with their indices and status.
     *
     * @param items The list of tasks to display
     */
    public void printList(List<Task> items) {
        printLine();
        if (items.isEmpty()) {
            out.println("    " + "You have no tasks in your list!");
        } else {
            out.println("    " + "Here are the tasks in your list:");
            for (int i = 0; i < items.size(); i++) {
                out.println("    " + (i + 1) + ". " + items.get(i).toString());
            }
        }
        printLine();
    }

    /**
     * Prints confirmation that a task has been successfully added.
     *
     * @param task    The task that was added
     * @param newSize The total number of tasks after addition
     */
    public void printAdded(Task task, int newSize) {
        printLine();
        out.println("    " + "Got it. I've added this duke.task:");
        out.println("    " + " " + task.toString());
        out.println("    " + "Now you have " + newSize + " duke.task(s) in the list.");
        printLine();
    }

    /**
     * Prints confirmation that a task has been marked or unmarked.
     *
     * @param task The task that was marked/unmarked
     * @param mark true if task was marked as done, false if unmarked
     */
    public void printMarked(Task task, boolean mark) {
        printLine();
        out.println(mark
                ? "    " + "Nice! I've marked this duke.task as done!"
                : "    " + "Nice! I've marked this duke.task as not done yet!");
        out.println(" " + task.toString());
        printLine();
    }

    /**
     * Prints confirmation that a task has been deleted.
     *
     * @param task    The task that was deleted
     * @param newSize The number of remaining tasks after deletion
     */
    public void printDelete(Task task, int newSize) {
        printLine();
        out.println("    " + "Noted. I've removed this duke.task:");
        out.println("    " + " " + task.toString());
        out.println("    " + "Now you have " + newSize + " duke.task(s) in the list.");
        printLine();
    }

    /**
     * Prints a usage message or error to the user.
     *
     * @param message The message to display
     */
    public void printUsage(String message) {
        printLine();
        out.println("    " + message);
        printLine();
    }

    /**
     * Prints a message indicating there are no tasks to clear.
     */
    public void printNoTasksToClear() {
        printLine();
        out.println("    " + "There are no tasks in your list, idiot!");
        printLine();
    }

    /**
     * Prints a confirmation prompt asking if the user wants to clear all tasks.
     */
    public void printClearPrompt() {
        printLine();
        out.println("    " + "Are you sure you want to clear all tasks?");
        out.println("    " + "Type 'yes/no' to proceed");
        printLine();
    }

    /**
     * Prints a message asking the user to type 'yes' or 'no'.
     */
    public void printPleaseTypeYesNo() {
        printLine();
        out.println("    Please type 'yes' or 'no'.");
        printLine();
    }

    /**
     * Prints confirmation that all tasks have been cleared.
     */
    public void printCleared() {
        printLine();
        out.println("    " + "All tasks have been cleared!");
        printLine();
    }

    /**
     * Prints a message indicating that the clear operation was canceled.
     */
    public void printClearCanceled() {
        printLine();
        out.println("    " + "lol gay");
        printLine();
    }

    /**
     * Prints the results of a find operation.
     * Shows all matching tasks with their indices from the main list.
     *
     * @param keyword The keyword that was searched for
     * @param matches The list of tasks that matched the keyword
     */
    public void printFindResults(String keyword, List<Task> matches) {
        printLine();
        out.println("    Here are the matching tasks in your list for '" + keyword + "':");

        if (matches.isEmpty()) {
            out.println("    (no matching tasks found)");
        } else {
            for (int i = 0; i < matches.size(); i++) {
                out.println("    " + (i + 1) + ". " + matches.get(i).toString());
            }
        }

        printLine();
    }

    public PrintStream out() {
        return out;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Ui) obj;
        return Objects.equals(this.out, that.out);
    }

    @Override
    public int hashCode() {
        return Objects.hash(out);
    }

    @Override
    public String toString() {
        return "Ui[" +
                "out=" + out + ']';
    }

}
