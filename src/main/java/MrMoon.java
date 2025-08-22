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

            items.add(new Task(input));
            printLine();
            System.out.println("    " + "added: " + input);
            printLine();
        }
        printLine();
        System.out.println("    " + "Bye. Hope to see you again soon!");
        printLine();
    }
}