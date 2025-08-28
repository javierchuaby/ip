import java.util.List;
import java.util.Scanner;

public class MrMoon {
    private final TaskList tasks;
    private final Ui ui;
    private final Parser parser;
    private final Scanner scanner;

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

    public MrMoon() {
        this("data/duke.txt");
    }

    public static void main(String[] args) {
        String filePath = args.length > 0 ? args[0] : "data/duke.txt";
        new MrMoon(filePath).run();
    }

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
                    ui.printUsage("Error parsing command: " + e.getMessage());
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