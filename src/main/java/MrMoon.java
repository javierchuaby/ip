import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MrMoon {
    private final ArrayList<Task> items = new ArrayList<>();
    private final Ui ui;
    private final Parser parser;
    private final Storage storage;

    public MrMoon() {
        this.ui = new Ui(System.out);
        this.parser = new Parser();
        this.storage = new Storage();
        List<Task> loaded = storage.load();
        if (loaded != null) {
            this.items.addAll(loaded);
        }
    }

    public static void main(String[] args) {
        new MrMoon().run();
    }

    public void run() {
        ui.printWelcome();
        Scanner sc = new Scanner(System.in);
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
                    ui.printList(items);
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
            }
            ui.printUnknown(line);
        }
    }

    private void handleTodo(String args) {
        String desc = (args == null) ? "" : args.trim();
        if (desc.isEmpty()) {
            ui.printUsage("Usage: todo <desc>");
            return;
        }
        Task t = new Todo(desc);
        items.add(t);
        storage.save(items);
        ui.printAdded(t, items.size());
    }

    private void handleDeadline(String args) {
        String[] parts = parser.splitDeadline(args);
        if (parts == null) {
            ui.printUsage("Usage: deadline <desc> /by <time>");
            return;
        }
        Task t = new Deadline(parts[0], parts[1]);
        items.add(t);
        storage.save(items);
        ui.printAdded(t, items.size());
    }

    private void handleEvent(String args) {
        String[] parts = parser.splitEvent(args);
        if (parts == null) {
            ui.printUsage("Usage: event <desc> /from <start> /to <end>");
            return;
        }
        Task t = new Event(parts[0], parts[1], parts[2]);
        items.add(t);
        storage.save(items);
        ui.printAdded(t, items.size());
    }

    private void handleMark(String args, boolean mark) {
        Integer idx = parseOneBasedIndex(args);
        if (idx == null) {
            ui.printUsage("Please provide a valid task number.");
            return;
        }
        if (isInvalidOneBased(idx)) {
            ui.printUsage("Please use a task number between 1 and " + items.size() + ".");
            return;
        }
        Task t = items.get(idx - 1);
        if (mark) t.mark(); else t.unmark();
        storage.save(items);
        ui.printMarked(t, mark);
    }

    private void handleDelete(String args) {
        Integer idx = parseOneBasedIndex(args);
        if (idx == null) {
            ui.printUsage("Please provide a valid task number.");
            return;
        }
        if (isInvalidOneBased(idx)) {
            ui.printUsage("Please use a task number between 1 and " + items.size() + ".");
            return;
        }
        Task removed = items.remove(idx - 1);
        storage.save(items);
        ui.printDelete(removed, items.size());
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
        return oneBasedIndex < 1 || oneBasedIndex > items.size();
    }
}