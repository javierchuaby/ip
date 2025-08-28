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
        out.println("    - deadline <description> /by <when>");
        out.println("    - event <description> /from <start> /to <end>");
        out.println("    - mark <task-number> | unmark <task-number>");
        out.println("    - delete <task-number>");
        printLine();
    }

    public void printUnknownEmpty() {
        printLine();
        out.println("    " + "Use the following commands:");
        out.println("    - list");
        out.println("    - todo <description>");
        out.println("    - deadline <description> /by <when>");
        out.println("    - event <description> /from <start> /to <end>");
        out.println("    - mark <task-number> | unmark <task-number>");
        out.println("    - delete <task-number>");
        printLine();
    }

    public String formatTask(Task t) {
        String status = t.isDone() ? "[X]" : "[ ]";
        String type = "[T]";
        String extra = "";
        if (t instanceof Deadline d) {
            type = "[D]";
            extra = " (by " + d.getBy() + ")";
        } else if (t instanceof Event e) {
            type = "[E]";
            extra = " (from " + e.getFrom() + " to " + e.getTo() + ")";
        }
        return type + status + " " + t.getDescription() + extra;
    }

    public void printList(List<Task> items) {
        printLine();
        if (items.isEmpty()) {
            out.println("    " + "You have no tasks in your list!");
        } else {
            out.println("    " + "Here are the tasks in your list:");
            for (int i = 0; i < items.size(); i++) {
                out.println("    " + (i + 1) + ". " + formatTask(items.get(i)));
            }
        }
        printLine();
    }

    public void printAdded(Task task, int newSize) {
        printLine();
        out.println("    " + "Got it. I've added this task:");
        out.println("    " + "  " + formatTask(task));
        out.println("    " + "Now you have " + newSize + " task(s) in the list.");
        printLine();
    }

    public void printMarked(Task task, boolean mark) {
        printLine();
        out.println(mark
                ? "    " + "Nice! I've marked this task as done!"
                : "    " + "Nice! I've marked this task as not done yet!");
        out.println("    " + formatTask(task));
        printLine();
    }

    public void printDelete(Task task, int newSize) {
        printLine();
        out.println("    " + "Noted. I've removed this task:");
        out.println("    " + "  " + formatTask(task));
        out.println("    " + "Now you have " + newSize + " task(s) in the list.");
        printLine();
    }

    public void printUsage(String message) {
        printLine();
        out.println("    " + message);
        printLine();
    }
}