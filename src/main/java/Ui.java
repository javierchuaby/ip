import java.io.PrintStream;
import java.util.List;

public record Ui(PrintStream out) {
    public void printLine() {
        out.println("    " + "____________________________________________________________");
    }

    public void printWelcome() {
        printLine();
        out.println("    Hello! I'm Mr Moon");
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
        out.println("    " + "Got it. I've added this task:");
        out.println("    " + " " + task.toString());
        out.println("    " + "Now you have " + newSize + " task(s) in the list.");
        printLine();
    }

    public void printMarked(Task task, boolean mark) {
        printLine();
        out.println(mark
                ? "    " + "Nice! I've marked this task as done!"
                : "    " + "Nice! I've marked this task as not done yet!");
        out.println("    " + task.toString());
        printLine();
    }

    public void printDelete(Task task, int newSize) {
        printLine();
        out.println("    " + "Noted. I've removed this task:");
        out.println("    " + " " + task.toString());
        out.println("    " + "Now you have " + newSize + " task(s) in the list.");
        printLine();
    }

    public void printUsage(String message) {
        printLine();
        out.println("    " + message);
        printLine();
    }
}
