import java.util.*;

public class Ui {
    private static final int LINE_LENGTH = 50;
    private static final String LINE = "_".repeat(LINE_LENGTH);

    public static void printLine() {
        System.out.println(LINE);
    }

    public static void printWelcome() {
        printLine();
        System.out.println("    Hello! I'm Mr Moon!");
        System.out.println("    What can I do for you?");
        printLine();
    }

    public static void printGoodbye() {
        printLine();
        System.out.println("""
                    Bye bye. Talk to you again tmr!

                    Cheers,
                    Mr Moon\
                """);
        printLine();
    }
}