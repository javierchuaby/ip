import java.util.Scanner;
import java.util.ArrayList;

public class MrMoon {
    private static final ArrayList<Task> items = new ArrayList<>();

    private static void printList() {
        Ui.printLine();
        if (items.isEmpty()) {
            System.out.println("    " + "You have no tasks in your list!");
        } else {
            System.out.println("    " + "Here are the tasks in your list:");
            for (int i = 0; i < items.size(); i++) {
                int n = i + 1;
                System.out.println("    " + n + ". " + items.get(i));
            }
        }
        Ui.printLine();
    }

    private static void added(Task t) {
        items.add(t);
        Ui.printLine();
        System.out.println("    Got it! I've added this task:");
        System.out.println("      " + t);
        System.out.println("    Now you have " + items.size() + " tasks in the list.");
        Ui.printLine();
    }

    private static void deleteTask(int index) {
        Task task = items.remove(index);

        Ui.printLine();
        System.out.println("    No problem! I've deleted this task:");
        System.out.println("      " + task);
        System.out.println("    Now you have " + items.size() + " tasks in the list.");
        Ui.printLine();
    }

    private static boolean isInvalidOneBased(int n) {
        return n < 1 || n > items.size();
    }

    private static void markTask(int oneBasedIndex) {
        if (isInvalidOneBased(oneBasedIndex)) {
            Ui.printLine();
            System.out.println("    Please use a task number between 1 and " + items.size() + ".");
            Ui.printLine();
            return;
        }
        markOrUnmark(oneBasedIndex - 1, true);
    }

    private static void unmarkTask(int oneBasedIndex) {
        if (isInvalidOneBased(oneBasedIndex)) {
            Ui.printLine();
            System.out.println("    Please use a task number between 1 and " + items.size() + ".");
            Ui.printLine();
            return;
        }
        markOrUnmark(oneBasedIndex - 1, false);
    }

    private static void markOrUnmark(int index, boolean mark) {
        Task task = items.get(index);
        if (mark) task.mark();
        else task.unmark();

        Ui.printLine();
        System.out.println(mark
                ? "    " + "Nice! I've marked this task as done!"
                : "    " + "Nice! I've marked this task as not done yet!");
        System.out.println("    " + task);
        Ui.printLine();
    }

    private static void printUnknown(String input) {
        Ui.printLine();
        System.out.println("    IDK what " + input + " means!");
        System.out.println("    Try one of:");
        System.out.println("    - todo <description>");
        System.out.println("    - deadline <description> /by <date/time>");
        System.out.println("    - event <description> /from <start> /to <end>");
        System.out.println("    - list");
        System.out.println("    - mark <task-number> | unmark <task-number>");
        System.out.println("    - bye");
        Ui.printLine();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Ui.printWelcome();

        while (true) {
            String input = sc.nextLine().trim();
            String cmd  = Parser.getCommandWord(input);
            String arguments = Parser.getArguments(input);

            switch (cmd) {
                case "bye" -> {
                    Ui.printGoodbye();
                    return;
                }
                case "list" -> {
                    printList();
                    continue;
                }
                case "mark" -> {
                    Integer idx = Parser.getFirstIndex(arguments);
                    if (idx != null) {
                        markTask(idx);
                        continue;
                    }
                    Ui.printLine();
                    System.out.println("    Please use: mark <task-number>");
                    Ui.printLine();
                    continue;
                }
                case "unmark" -> {
                    Integer idx = Parser.getFirstIndex(arguments);
                    if (idx != null) {
                        unmarkTask(idx);
                        continue;
                    }
                    Ui.printLine();
                    System.out.println("    Please use: unmark <task-number>");
                    Ui.printLine();
                    continue;
                }
                case "delete" -> {
                    Integer idx = Parser.getFirstIndex(arguments);
                    if (idx != null) {
                        deleteTask(idx);
                        continue;
                    }
                    Ui.printLine();
                    System.out.println("    Please use: delete <task-number>");
                    Ui.printLine();
                    continue;
                }
                case "todo" -> {
                    if (arguments.isEmpty()) {
                        Ui.printLine();
                        System.out.println("    Please use: todo <description>");
                        Ui.printLine();
                        continue;
                    }
                    added(new Todo(arguments));
                    continue;
                }
                case "deadline" -> {
                    String[] parts = Parser.splitDeadline(arguments);

                    if (parts != null) {
                        added(new Deadline(parts[0], parts[1]));
                        continue;
                    }
                    Ui.printLine();
                    System.out.println("    Please use: deadline <description> /by <when>");
                    Ui.printLine();
                    continue;
                }
                case "event" -> {
                    String[] parts = Parser.splitEvent(arguments);

                    if (parts != null) {
                        added(new Event(parts[0], parts[1], parts[2]));
                        continue;
                    }
                    Ui.printLine();
                    System.out.println("    Please use: event <description> /from <start> /to <end>");
                    Ui.printLine();
                    continue;
                }
            }

            printUnknown(input);

        }
    }
}