import java.util.Scanner;

public class MrMoon {
    private static final int LINE_LENGTH = 80;
    private static final String LINE = "_".repeat(LINE_LENGTH);

    private static void printLine() {
        System.out.println(LINE);
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
            printLine();
            System.out.println("    " + input);
            printLine();
        }
        printLine();
        System.out.println("    " + "Bye. Hope to see you again soon!");
        printLine();
    }
}