import java.util.Scanner;
import java.util.ArrayList;

public class MrMoon {
    private static final int LINE_LENGTH = 50;
    private static final String LINE = "_".repeat(LINE_LENGTH);
    private static final ArrayList<Task> items = new ArrayList<>();

    private static void printLine() {
        System.out.println(LINE);
    }

    private static void printList() {
        printLine();
        if (items.isEmpty()) {
            System.out.println("    " + "You have no tasks in your list!");
        } else {
            System.out.println("    " + "Here are the tasks in your list:");
            for (int i = 0; i < items.size(); i++) {
                int n = i + 1;
                System.out.println("    " + n + ". " + items.get(i));
            }
        }
        printLine();
    }

    private static void added(Task t) {
        items.add(t);
        printLine();
        System.out.println("    Got it! I've added this task:");
        System.out.println("      " + t);
        System.out.println("    Now you have " + items.size() + " tasks in the list.");
        printLine();
    }

    private static Integer parseIndex(String input, String command) {
        String lowerInput = input.toLowerCase();
        String lowerCommand = command.toLowerCase();

        if (!lowerInput.startsWith(lowerCommand + " ")) return null;

        String numPart = input.substring(command.length()).trim();
        try {
            int n = Integer.parseInt(numPart);
            if (n < 1 || n > items.size()) return null;
            return n - 1;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static void markOrUnmark(int index, boolean mark) {
        Task task = items.get(index);
        if (mark) task.mark();
        else task.unmark();

        printLine();

        System.out.println(mark
                ? "    " + "Nice! I've marked this task as done!"
                : "    " + "Nice! I've marked this task as not done yet!");

        System.out.println("    " + task);
        printLine();
    }

    private static void printUnknown(String input) {
        printLine();
        System.out.println("    Unknown command: " + input);
        System.out.println("    Try one of:");
        System.out.println("    - todo <description>");
        System.out.println("    - deadline <description> /by <date/time>");
        System.out.println("    - event <description> /from <start> /to <end>");
        System.out.println("    - list");
        System.out.println("    - mark <task-number> | unmark <task-number>");
        System.out.println("    - bye");
        printLine();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        printLine();
        System.out.println(
                "    " + "Hello! I'm Mr Moon!\n" +
                "    " + "What can I do for you?"
        );
        printLine();

        while (true) {
            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("bye")) { break; }

            if (input.equalsIgnoreCase("list")) {
                printList();
                continue;
            }

            String lower = input.toLowerCase();

            /* -------- todo -------- */
            if (lower.startsWith("todo ")) {
                String desc = input.substring(5).trim();
                if (desc.isEmpty()) {
                    printLine(); System.out.println("    Please use: todo <description>"); printLine(); continue;
                }
                added(new Todo(desc));
                continue;
            }

            /* -------- deadline -------- */
            if (lower.startsWith("deadline ")) {
                int byPos = lower.indexOf(" /by ");
                if (byPos == -1) {
                    printLine(); System.out.println("    Please use: deadline <description> /by <date/time>"); printLine(); continue;
                }
                String desc = input.substring(9, byPos).trim();            // 9 = "deadline ".length()
                String by   = input.substring(byPos + 5).trim();           // 5 = " /by ".length()
                if (desc.isEmpty() || by.isEmpty()) {
                    printLine(); System.out.println("    Please use: deadline <description> /by <date/time>"); printLine(); continue;
                }
                added(new Deadline(desc, by));
                continue;
            }

            /* -------- event -------- */
            if (lower.startsWith("event ")) {
                int fromPos = lower.indexOf(" /from ");
                int toPos   = (fromPos == -1) ? -1 : lower.indexOf(" /to ", fromPos + 7);
                if (fromPos == -1 || toPos == -1) {
                    printLine(); System.out.println("    Please use: event <description> /from <start> /to <end>"); printLine(); continue;
                }
                String desc = input.substring(6, fromPos).trim();          // 6 = "event ".length()
                String from = input.substring(fromPos + 7, toPos).trim();  // 7 = " /from ".length()
                String to   = input.substring(toPos + 5).trim();           // 5 = " /to ".length()
                if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
                    printLine(); System.out.println("    Please use: event <description> /from <start> /to <end>"); printLine(); continue;
                }
                added(new Event(desc, from, to));
                continue;
            }

            /* -------- mark -------- */
            Integer index = parseIndex(input, "mark");
            if (index != null) {
                markOrUnmark(index, true);
                continue;
            } else if (input.toLowerCase().startsWith("mark")) {
                printLine();
                System.out.println("    Please use: mark <task-number>");
                printLine();
                continue;
            }

            /* -------- unmark -------- */
            index = parseIndex(input, "unmark");
            if (index != null) {
                markOrUnmark(index, false);
                continue;
            } else if (input.toLowerCase().startsWith("unmark")) {
                printLine();
                System.out.println("    Please use: unmark <task-number>");
                printLine();
                continue;
            }

            printUnknown(input);
        }
        printLine();
        System.out.println("    " + "Bye. Hope to see you again soon!");
        printLine();
    }
}