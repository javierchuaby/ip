import java.util.List;
import java.util.Scanner;

public class MrMoon {
    private final TaskList tasks;
    private final Ui ui;
    private final Parser parser;

    public MrMoon() {
        this.ui = new Ui(System.out);
        this.parser = new Parser();
        Storage storage = new Storage();
        List<Task> loaded = storage.load();
        this.tasks = new TaskList(storage, loaded);
    }

    public static void main(String[] args) {
        new MrMoon().run();
    }

    public void run() {
        ui.printWelcome();
        try (Scanner sc = new Scanner(System.in)) {
            label:
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String cmd = parser.getCommandWord(line);
                String args = parser.getArguments(line);
                switch (cmd) {
                    case "":
                        ui.printUnknownEmpty();
                        continue;
                    case "bye":
                        ui.printGoodbye();
                        break label;
                    case "list":
                        ui.printList(tasks.asUnmodifiable());
                        continue;
                    case "todo":
                        handleTodo(args);
                        continue;
                    case "deadline":
                        handleDeadline(args);
                        continue;
                    case "event":
                        handleEvent(args);
                        continue;
                    case "mark":
                        handleMark(args, true);
                        continue;
                    case "unmark":
                        handleMark(args, false);
                        continue;
                    case "delete":
                        handleDelete(args);
                        continue;
                    default:
                        ui.printUnknown(line);
                }
            }
        }
    }

    private void handleTodo(String args) {
        String desc = (args == null) ? "" : args.trim();
        if (desc.isEmpty()) {
            ui.printUsage("Usage: todo <description>");
            return;
        }
        Task t = new Todo(desc);
        tasks.add(t);
        ui.printAdded(t, tasks.size());
    }

    private void handleDeadline(String args) {
        String[] parts;
        try {
            parts = parser.parseDeadlineArgs(args == null ? "" : args);
        } catch (IllegalArgumentException ex) {
            ui.printDeadlineFormat();
            return;
        }
        String desc = parts[0].trim();
        String whenStr = parts[1].trim();
        if (desc.isEmpty()) {
            ui.printDeadlineFormat();
            return;
        }
        try {
            Deadline d = new Deadline(desc, whenStr);
            tasks.add(d);
            ui.printAdded(d, tasks.size());
        } catch (IllegalArgumentException ex) {
            ui.printUsage("I couldn’t read that date/time: " + ex.getMessage());
        }
    }

    private void handleEvent(String args) {
        String[] parts;
        try {
            parts = parser.parseEventArgs(args == null ? "" : args);
        } catch (IllegalArgumentException ex) {
            ui.printEventFormat();
            return;
        }
        String desc = parts[0].trim();
        String fromStr = parts[1].trim();
        String toStr   = parts[2].trim();
        if (desc.isEmpty()) {
            ui.printEventFormat();
            return;
        }
        try {
            Event e = new Event(desc, fromStr, toStr);
            tasks.add(e);
            ui.printAdded(e, tasks.size());
        } catch (IllegalArgumentException ex) {
            ui.printUsage("I couldn’t read those dates/times: " + ex.getMessage());
        }
    }

    private void handleMark(String args, boolean mark) {
        Integer idx = parseOneBasedIndex(args);
        if (idx == null) {
            ui.printUsage("Please provide a valid task number.");
            return;
        }
        if (isInvalidOneBased(idx)) {
            ui.printUsage("Please use a task number between 1 and " + tasks.size() + ".");
            return;
        }
        if (mark) tasks.mark(idx - 1); else tasks.unmark(idx - 1);
        Task t = tasks.get(idx - 1);
        ui.printMarked(t, mark);
    }

    private void handleDelete(String args) {
        Integer idx = parseOneBasedIndex(args);
        if (idx == null) {
            ui.printUsage("Please provide a valid task number.");
            return;
        }
        if (isInvalidOneBased(idx)) {
            ui.printUsage("Please use a task number between 1 and " + tasks.size() + ".");
            return;
        }
        Task removed = tasks.remove(idx - 1);
        ui.printDelete(removed, tasks.size());
    }

    private Integer parseOneBasedIndex(String args) {
        if (args == null) return null;
        String s = args.trim();
        if (s.isEmpty()) return null;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private boolean isInvalidOneBased(int oneBasedIndex) {
        return oneBasedIndex < 1 || oneBasedIndex > tasks.size();
    }
}