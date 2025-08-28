import java.util.Scanner;
import java.util.ArrayList;

public class MrMoon {
    private final ArrayList<Task> items = new ArrayList<>();
    private final Ui ui;
    private final Parser parser;

    public MrMoon(Ui ui, Parser parser) {
        this.ui = ui;
        this.parser = parser;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Ui ui = new Ui(System.out);
        Parser parser = new Parser();
        new MrMoon(ui, parser).run(sc);
    }

    public void run(Scanner sc) {
        ui.printWelcome();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String cmd = parser.getCommandWord(input);
            String arguments = parser.getArguments(input);

            if (cmd.isEmpty()) continue;

            if (cmd.equals("bye")) {
                ui.printGoodbye();
                return;
            }

            if (cmd.equals("list")) {
                ui.printList(items);
                continue;
            }

            if (cmd.equals("mark")) {
                Integer idx = parser.getFirstIndex(arguments);
                if (idx != null) { markTask(idx); continue; }
                ui.printUsage("Please use: mark <task-number>");
                continue;
            }

            if (cmd.equals("unmark")) {
                Integer idx = parser.getFirstIndex(arguments);
                if (idx != null) { unmarkTask(idx); continue; }
                ui.printUsage("Please use: unmark <task-number>");
                continue;
            }

            if (cmd.equals("delete")) {
                Integer idx = parser.getFirstIndex(arguments);
                if (idx != null) { deleteTask(idx); continue; }
                ui.printUsage("Please use: delete <task-number>");
                continue;
            }

            if (cmd.equals("todo")) {
                if (arguments.isEmpty()) {
                    ui.printUsage("Please use: todo <description>");
                    continue;
                }
                added(new Todo(arguments));
                continue;
            }

            if (cmd.equals("deadline")) {
                String[] parts = parser.splitDeadline(arguments);
                if (parts != null) { added(new Deadline(parts[0], parts[1])); continue; }
                ui.printUsage("Please use: deadline <description> /by <when>");
                continue;
            }

            if (cmd.equals("event")) {
                String[] parts = parser.splitEvent(arguments);
                if (parts != null) { added(new Event(parts[0], parts[1], parts[2])); continue; }
                ui.printUsage("Please use: event <description> /from <start> /to <end>");
                continue;
            }

            ui.printUnknown(input);
        }
        ui.printGoodbye();
    }

    private boolean isInvalidOneBased(int n) {
        return n < 1 || n > items.size();
    }

    private void markTask(int oneBasedIndex) {
        if (isInvalidOneBased(oneBasedIndex)) {
            ui.printUsage("Please use a task number between 1 and " + items.size() + ".");
            return;
        }
        markOrUnmark(oneBasedIndex - 1, true);
    }

    private void unmarkTask(int oneBasedIndex) {
        if (isInvalidOneBased(oneBasedIndex)) {
            ui.printUsage("Please use a task number between 1 and " + items.size() + ".");
            return;
        }
        markOrUnmark(oneBasedIndex - 1, false);
    }

    private void markOrUnmark(int index, boolean mark) {
        Task task = items.get(index);
        if (mark) task.mark(); else task.unmark();
        ui.printMarked(task, mark);
    }

    private void deleteTask(int oneBasedIndex) {
        if (isInvalidOneBased(oneBasedIndex)) {
            ui.printUsage("Please use a task number between 1 and " + items.size() + ".");
            return;
        }
        Task removed = items.remove(oneBasedIndex - 1);
        ui.printDelete(removed, items.size());
    }

    private void added(Task t) {
        items.add(t);
        ui.printAdded(t, items.size());
    }
}