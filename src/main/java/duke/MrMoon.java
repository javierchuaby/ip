package duke;

import java.util.List;
import java.util.Scanner;

import duke.command.ClearCommand;
import duke.command.Command;
import duke.parser.Parser;
import duke.storage.Storage;
import duke.task.Task;
import duke.task.TaskList;
import duke.ui.Ui;


/**
 * Main Duke application class that coordinates all components.
 * Handles the main program loop, user input processing, and command execution.
 * Manages the interaction between UI, Parser, TaskList, and Storage components.
 */
public class MrMoon {

    /**
     * The task list containing all user tasks
     */
    private final TaskList tasks;

    /**
     * The user interface component for input/output
     */
    private final Ui ui;

    /**
     * The command parser for interpreting user input
     */
    private final Parser parser;

    /**
     * The scanner for reading user input
     */
    private final Scanner scanner;

    /**
     * Constructs the main Duke application with the specified storage file path.
     * Initializes all components and loads existing tasks from storage.
     *
     * @param filePath The file path for task data storage
     */
    public MrMoon(String filePath) {
        this.ui = new Ui(System.out);
        this.parser = new Parser();
        Storage storage = new Storage(filePath);
        this.scanner = new Scanner(System.in);

        List<Task> loaded;
        try {
            loaded = storage.load();
        } catch (Exception e) {
            ui.printUsage("Could not load existing tasks. Starting with an empty list.");
            loaded = List.of();
        }

        this.tasks = new TaskList(storage, loaded);
    }

    /**
     * Constructs the main Duke application with default storage path.
     * Uses "data/duke.txt" as the default storage file.
     */
    public MrMoon() {
        this("data/duke.txt");
    }

    /**
     * Main entry point for the Duke application.
     * Creates and runs the application with command line argument support.
     *
     * @param args Command line arguments, first argument used as storage file path
     */
    public static void main(String[] args) {
        String filePath = args.length > 0 ? args[0] : "data/duke.txt";
        new MrMoon(filePath).run();
    }

    /**
     * Runs the main application loop.
     * Processes user input, executes commands, and handles special clear confirmation logic.
     * Exits when an exit command is received.
     */
    public void run() {
        ui.printWelcome();
        boolean waitingForClearConfirmation = false;

        try (scanner) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (waitingForClearConfirmation) {
                    boolean validResponse = handleClearConfirmation(line.trim().toLowerCase());
                    if (validResponse) {
                        waitingForClearConfirmation = false;
                    }
                    continue;
                }

                Command command;
                try {
                    command = parser.parseCommand(line);
                } catch (Exception e) {
                    ui.printUsage("Error parsing duke.command: " + e.getMessage());
                    continue;
                }

                command.execute(tasks, ui);

                if (command instanceof ClearCommand && tasks.size() > 0) {
                    waitingForClearConfirmation = true;
                }

                if (command.isExit()) {
                    break;
                }
            }
        }
    }

    /**
     * Handles user responses to clear confirmation prompts.
     * Processes "yes" to clear tasks, "no" to cancel, and asks for clarification otherwise.
     *
     * @param response The user's response to the clear confirmation
     * @return true if the response was handled (valid), false to continue waiting
     */
    private boolean handleClearConfirmation(String response) {
        switch (response) {
        case "yes":
            tasks.clear();
            ui.printCleared();
            return true;
        case "no":
            ui.printClearCanceled();
            return true;
        default:
            ui.printPleaseTypeYesNo();
            return false; // Keep waiting
        }
    }
}
