package duke.ui;

import duke.task.Task;
import duke.task.TaskList;

import java.io.PrintStream;
import java.util.List;
import java.time.LocalDate;

public record Ui(PrintStream out) {
    public void printLine() {
        out.println("    " + "__________________________________________________");
    }

    public void printWelcome() {
        printLine();
        out.println("    Hello! I'm Mr Moon!");
        out.println("    What can I do for you?");
        printLine();
    }

    public void printGoodbye() {
        printLine();
        out.println("    Bye bye. Talk to you again tmr!");
        out.println();
        out.println("    Cheers,");
        out.println("    Mr Moon");
        printLine();
    }

    public void printUnknown(String input) {
        printLine();
        out.println("    " + "Sorry, I do not understand what " + input + " means.");
        out.println("    " + "Try one of these:");
        out.println("    - list");
        out.println("    - todo <description>");
        out.println("    - deadline <description> /by <date> <time>");
        out.println("    - event <description> /from <date> <time> /to <date> <time>");
        out.println("    - mark | unmark <index>");
        out.println("    - delete <index>");
        out.println("    - on <date>");
        out.println("    - clear (clear all tasks in list)");
        printLine();
    }

    public void printUnknownEmpty() {
        printLine();
        out.println("    " + "Use the following commands:");
        out.println("    - list");
        out.println("    - todo <description>");
        out.println("    - deadline <description> /by <date time>");
        out.println("    - event <description> /from <date time> /to <date time>");
        out.println("    - mark | unmark <index>");
        out.println("    - delete <index>");
        out.println("    - on <date>");
        out.println("    - clear (clear all tasks in list)");
        printLine();
    }

    public void printAgendaFormat() {
        printLine();
        out.println("    Usage: on <date>");
        out.println("    Examples:");
        out.println("      on 9 Aug");
        out.println("      on 2025-12-02");
        out.println("      on 2/12/2025");
        printLine();
    }

    public void printAgendaForDate(LocalDate date, List<Task> items, TaskList fullList) {
        printLine();
        out.println("    Tasks on " + date.format(java.time.format.DateTimeFormatter.ofPattern("d MMM uuuu")) + ":");
        if (items.isEmpty()) {
            out.println("    (none)");
            printLine();
            return;
        }
        int i = 1;
        for (Task t : items) {
            // Optional: show the original index from the full list (nice UX)
            int originalIdx = fullList.indexOf(t) + 1; // task.TaskList should expose indexOf; if not, loop to find
            out.println("    " + i + ". " + t.toString() + "    [#"+ originalIdx + " in main list]");
            i++;
        }
        printLine();
    }

    public void printDeadlineFormat() {
        printLine();
        out.println("    " + "Usage: deadline <description> /by <date time>");
        out.println("    " + "Example: deadline return book /by 12-3-2025 1800");
        printLine();
    }

    public void printEventFormat() {
        printLine();
        out.println("    " + "Usage: event <description> /from <date> [time] /to <date> [time]");
        out.println("    " + "Examples:");
        out.println("    " + "  event conference /from 9 Aug /to 10 Aug");
        out.println("    " + "  event project meeting /from 2/12/2025 1800 /to 2/12/2025 2000");
        printLine();
    }

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

    public void printAdded(Task task, int newSize) {
        printLine();
        out.println("    " + "Got it. I've added this duke.task:");
        out.println("    " + " " + task.toString());
        out.println("    " + "Now you have " + newSize + " duke.task(s) in the list.");
        printLine();
    }

    public void printMarked(Task task, boolean mark) {
        printLine();
        out.println(mark
                ? "    " + "Nice! I've marked this duke.task as done!"
                : "    " + "Nice! I've marked this duke.task as not done yet!");
        out.println("    " + task.toString());
        printLine();
    }

    public void printDelete(Task task, int newSize) {
        printLine();
        out.println("    " + "Noted. I've removed this duke.task:");
        out.println("    " + " " + task.toString());
        out.println("    " + "Now you have " + newSize + " duke.task(s) in the list.");
        printLine();
    }

    public void printUsage(String message) {
        printLine();
        out.println("    " + message);
        printLine();
    }

    public void printNoTasksToClear() {
        printLine();
        out.println("    " + "There are no tasks in your list, idiot!");
        printLine();
    }

    public void printClearPrompt() {
        printLine();
        out.println("    " + "Are you sure you want to clear all tasks?");
        out.println("    " + "Type 'yes/no' to proceed");
        printLine();
    }

    public void printPleaseTypeYesNo() {
        printLine();
        out.println("    Please type 'yes' or 'no'.");
        printLine();
    }

    public void printCleared() {
        printLine();
        out.println("    " + "All tasks have been cleared!");
        printLine();
    }

    public void printClearCanceled() {
        printLine();
        out.println("    " + "lol gay");
        printLine();
    }
}
