import java.util.Scanner;
import java.util.ArrayList;

public class MrMoon {
    private static final int LINE_LENGTH = 50;
    private static final String LINE = "_".repeat(LINE_LENGTH);
    private static final ArrayList<String> items = new ArrayList<>();

    private static void printLine() {
        System.out.println(LINE);
    }

    private static void printList() {
        printLine();
        if (items.isEmpty()) {
            System.out.println("    " + "There are no items in your list!");
        } else {
            for (int i = 0; i < items.size(); i++) {
                int n = i + 1;
                System.out.println("    " + n + ". " + items.get(i));
            }
        }
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
            if (input.equalsIgnoreCase("bye")) {
                break;
            }
            if (input.equalsIgnoreCase("list")) {
                printList();
                continue;
            }
            items.add(input);
            printLine();
            System.out.println("    " + "added: " + input);
            printLine();
        }
        printLine();
        System.out.println("    " + "Bye. Hope to see you again soon!");
        printLine();
    }
}