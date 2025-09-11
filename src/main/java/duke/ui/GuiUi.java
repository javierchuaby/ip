package duke.ui;

import java.time.LocalDate;
import java.util.List;

import duke.task.Task;
import duke.task.TaskList;
import duke.util.CommandListingUtil;
import duke.util.DateTimeUtil;

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
    public void printNoTasksInList() {
        append("There are no tasks in your list!");
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
     * Prints the initial update prompt based on task type.
     */
    @Override
    public void printUpdatePrompt(Task task, int taskIndex) {
        append("Updating task " + taskIndex + ": " + task.toString());

        switch (task.getTaskType()) {
        case TODO:
            append("What would you like to rename this 'Todo' to?");
            break;
        case DEADLINE:
            append("What would you like to update?");
            append("1. Rename");
            append("2. Edit date/time");
            append("Please choose (1/2):");
            break;
        case EVENT:
            append("What would you like to update?");
            append("1. Rename");
            append("2. Edit date");
            append("Please choose (1/2):");
            break;
        }
    }

    /**
     * Prints prompt for description update.
     */
    @Override
    public void printUpdateDescriptionPrompt(Task task) {
        append("What would you like to rename this '" +
                task.getTaskType().getDisplayName() + "' to?");
    }

    /**
     * Prints prompt for date update (deadlines).
     */
    @Override
    public void printUpdateDatePrompt() {
        append("Please enter the new date/time:");
        append(DateTimeUtil.examplesHelp());
    }

    /**
     * Prints prompt for start date update (events).
     */
    @Override
    public void printUpdateStartDatePrompt() {
        append("Please enter the new start date/time:");
        append(DateTimeUtil.examplesHelp());
    }

    /**
     * Prints prompt for end date update (events).
     */
    @Override
    public void printUpdateEndDatePrompt() {
        append("Please enter the new end date/time:");
        append(DateTimeUtil.examplesHelp());
    }

    /**
     * Prints message for invalid choice.
     */
    @Override
    public void printInvalidChoice() {
        append("Invalid choice. Please enter 1 or 2:");
    }

    /**
     * Prints confirmation that task was updated.
     */
    @Override
    public void printTaskUpdated(Task task, String field) {
        append("Great! I've updated the " + field + ":");
        append(task.toString());
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
