package duke;

import java.util.List;
import java.util.Scanner;

import duke.command.ClearCommand;
import duke.command.Command;
import duke.command.EmptyCommand;
import duke.command.UpdateCommand;
import duke.parser.Parser;
import duke.storage.Storage;
import duke.task.Deadline;
import duke.task.Event;
import duke.task.Task;
import duke.task.TaskList;
import duke.task.Todo;
import duke.ui.GuiUi;
import duke.ui.Ui;
import duke.util.DateTimeUtil;
import duke.util.UpdateStateUtil;

/**
 * Main Duke application class that coordinates all components. Handles the main program loop, user
 * input processing, and command execution. Manages the interaction between UI, Parser, TaskList,
 * and Storage components.
 */
public class MrMoon {

    private final TaskList tasks;
    private final Ui ui;
    private final Parser parser;
    private final Scanner scanner;
    private UpdateStateUtil updateStateUtil;

    /**
     * Constructs the main Duke application with the specified storage file path. Initializes all
     * components and loads existing tasks from storage.
     *
     * @param filePath The file path for task data storage
     */
    public MrMoon(String filePath) {
        assert filePath != null && !filePath.trim().isEmpty() : "File path must be provided";

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
     * Constructs the main Duke application with a default storage path. Uses "data/duke.txt" as the
     * default storage file.
     */
    public MrMoon() {
        this("data/duke.txt");
    }

    /**
     * Main entry point for the Duke application. Creates and runs the application with command line
     * argument support.
     *
     * @param args Command line arguments, first argument used as a storage file path
     */
    public static void main(String[] args) {
        String filePath = args.length > 0 ? args[0] : "data/duke.txt";
        new MrMoon(filePath).run();
    }

    /**
     * Runs the main application loop. Processes user input, executes commands, and handles special
     * clear confirmation logic. Exits when an exit command is received.
     */
    public void run() {
        ui.printWelcome();
        boolean waitingForClearConfirmation = false;

        try (scanner) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (updateStateUtil != null) {
                    boolean updateComplete = handleUpdateState(line.trim());
                    if (updateComplete) {
                        updateStateUtil = null;
                    }
                    continue;
                }

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

                if (command instanceof UpdateCommand) {
                    UpdateCommand updateCmd = (UpdateCommand) command;
                    if (updateCmd.getTaskIndex() >= 1 && updateCmd.getTaskIndex() <= tasks.size()) {
                        updateStateUtil =
                            new UpdateStateUtil(
                                updateCmd.getTaskIndex(),
                                tasks.get(updateCmd.getTaskIndex() - 1));
                    }
                }

                if (command instanceof ClearCommand && tasks.size() > 0) {
                    waitingForClearConfirmation = true;
                }

                if (command.isExit()) {
                    break;
                }
            }
        }
    }

    public String getResponse(String input) {
        try {
            if (updateStateUtil != null) {
                return handleGuiUpdateState(input.trim());
            }

            Command c = parser.parseCommand(input);
            GuiUi guiUi = new GuiUi();

            c.execute(tasks, guiUi);

            if (c instanceof UpdateCommand) {
                UpdateCommand updateCmd = (UpdateCommand) c;
                if (updateCmd.getTaskIndex() >= 1 && updateCmd.getTaskIndex() <= tasks.size()) {
                    updateStateUtil =
                        new UpdateStateUtil(
                            updateCmd.getTaskIndex(),
                            tasks.get(updateCmd.getTaskIndex() - 1));
                    GuiUi updateUi = new GuiUi();
                    updateUi.printUpdatePrompt(
                        updateStateUtil.getOriginalTask(), updateStateUtil.getTaskIndex());
                    return updateUi.getResponse();
                }
            }

            String result = guiUi.getResponse();

            if (c instanceof EmptyCommand) {
                guiUi.printUnknownEmpty();
                return guiUi.getResponse();
            }

            return result;
        } catch (Exception e) {
            return "OOPS!!! I'm sorry, but I don't know what that means :-(";
        }
    }

    public TaskList getTasks() {
        return tasks;
    }

    /**
     * Handles the multi-step update conversation. Returns true when the update is complete, false
     * to continue.
     */
    private boolean handleUpdateState(String input) {
        switch (updateStateUtil.getCurrentStep()) {
        case WAITING_FOR_CHOICE:
            return handleUpdateChoice(input);
        case WAITING_FOR_DESCRIPTION:
            return handleDescriptionUpdate(input);
        case WAITING_FOR_DATE:
            return handleDateUpdate(input);
        case WAITING_FOR_START_DATE:
            return handleStartDateUpdate(input);
        case WAITING_FOR_END_DATE:
            return handleEndDateUpdate(input);
        default:
            return true; // Should not happen
        }
    }

    private boolean handleUpdateChoice(String input) {
        String choice = input.toLowerCase().trim();

        switch (choice) {
        case "1":
        case "rename":
            updateStateUtil.setStep(UpdateStateUtil.Step.WAITING_FOR_DESCRIPTION);
            ui.printUpdateDescriptionPrompt(updateStateUtil.getOriginalTask());
            return false;

        case "2":
        case "edit date":
            if (updateStateUtil.getOriginalTask() instanceof Deadline) {
                updateStateUtil.setStep(UpdateStateUtil.Step.WAITING_FOR_DATE);
                ui.printUpdateDatePrompt();
            } else if (updateStateUtil.getOriginalTask() instanceof Event) {
                updateStateUtil.setStep(UpdateStateUtil.Step.WAITING_FOR_START_DATE);
                ui.printUpdateStartDatePrompt();
            }
            return false;

        default:
            ui.printInvalidChoice();
            return false;
        }
    }

    private boolean handleDescriptionUpdate(String newDescription) {
        if (newDescription.trim().isEmpty()) {
            ui.printUsage("Description cannot be empty. Please try again:");
            return false;
        }

        // Create updated task with new description
        Task updatedTask =
            createUpdatedTask(updateStateUtil.getOriginalTask(), newDescription, null, null);
        replaceTask(updateStateUtil.getTaskIndex(), updatedTask);
        ui.printTaskUpdated(updatedTask, "description");
        return true; // Update complete
    }

    private boolean handleDateUpdate(String newDate) {
        try {
            // Validate the date
            DateTimeUtil.parseLenientResult(newDate);

            // Create updated task with new date
            Task updatedTask =
                createUpdatedTask(updateStateUtil.getOriginalTask(), null, newDate, null);
            replaceTask(updateStateUtil.getTaskIndex(), updatedTask);
            ui.printTaskUpdated(updatedTask, "date/time");
            return true; // Update complete
        } catch (Exception e) {
            ui.printUsage(
                "Invalid date/time format. "
                    + DateTimeUtil.examplesHelp()
                    + "\nPlease try again:");
            return false;
        }
    }

    private boolean handleStartDateUpdate(String newStartDate) {
        try {
            // Validate the date
            DateTimeUtil.parseLenientResult(newStartDate);

            updateStateUtil.setNewStartDate(newStartDate);
            updateStateUtil.setStep(UpdateStateUtil.Step.WAITING_FOR_END_DATE);
            ui.printUpdateEndDatePrompt();
            return false; // Continue to next step
        } catch (Exception e) {
            ui.printUsage(
                "Invalid date/time format. "
                    + DateTimeUtil.examplesHelp()
                    + "\nPlease try again:");
            return false;
        }
    }

    private boolean handleEndDateUpdate(String newEndDate) {
        try {
            // Validate the date
            DateTimeUtil.parseLenientResult(newEndDate);

            // Create updated task with new dates
            Task updatedTask =
                createUpdatedTask(
                    updateStateUtil.getOriginalTask(),
                    null,
                    updateStateUtil.getNewStartDate(),
                    newEndDate);
            replaceTask(updateStateUtil.getTaskIndex(), updatedTask);
            ui.printTaskUpdated(updatedTask, "dates");
            return true; // Update complete
        } catch (Exception e) {
            ui.printUsage(
                "Invalid date/time format. "
                    + DateTimeUtil.examplesHelp()
                    + "\nPlease try again:");
            return false;
        }
    }

    /**
     * Creates an updated version of a task with new values.
     */
    private Task createUpdatedTask(
        Task original, String newDesc, String newDate1, String newDate2) {
        String description = newDesc != null ? newDesc : original.getDescription();
        Task newTask;

        switch (original.getTaskType()) {
        case TODO:
            newTask = new Todo(description);
            break;

        case DEADLINE:
            Deadline originalDeadline = (Deadline) original;
            String deadlineDate = newDate1 != null ? newDate1 : originalDeadline.getBy();
            newTask = new Deadline(description, deadlineDate);
            break;

        case EVENT:
            Event originalEvent = (Event) original;
            String fromDate = newDate1 != null ? newDate1 : originalEvent.getFrom();
            String toDate = newDate2 != null ? newDate2 : originalEvent.getTo();
            newTask = new Event(description, fromDate, toDate);
            break;

        default:
            throw new IllegalArgumentException("Unknown task type");
        }

        // Preserve completion status
        if (original.isDone()) {
            newTask.mark();
        }

        return newTask;
    }

    /**
     * Replaces a task at the specified index (1-based).
     */
    private void replaceTask(int oneBasedIndex, Task newTask) {
        tasks.remove(oneBasedIndex - 1);
        tasks.add(oneBasedIndex - 1, newTask); // You'll need to add this method to TaskList
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
            return false;
        }
    }

    /**
     * Handles GUI update conversational flow.
     */
    private String handleGuiUpdateState(String input) {
        GuiUi guiUi = new GuiUi();

        switch (updateStateUtil.getCurrentStep()) {
        case WAITING_FOR_CHOICE:
            return handleGuiUpdateChoice(input, guiUi);
        case WAITING_FOR_DESCRIPTION:
            return handleGuiUpdateDescription(input, guiUi);
        case WAITING_FOR_DATE:
            return handleGuiUpdateDate(input, guiUi);
        case WAITING_FOR_START_DATE:
            return handleGuiUpdateStartDate(input, guiUi);
        case WAITING_FOR_END_DATE:
            return handleGuiUpdateEndDate(input, guiUi);
        default:
            updateStateUtil = null;
            guiUi.printUsage("Update completed.");
            return guiUi.getResponse();
        }
    }

    private String handleGuiUpdateChoice(String input, GuiUi guiUi) {
        String choice = input.toLowerCase().trim();
        switch (choice) {
        case "1":
        case "rename":
            updateStateUtil.setStep(UpdateStateUtil.Step.WAITING_FOR_DESCRIPTION);
            guiUi.printUpdateDescriptionPrompt(updateStateUtil.getOriginalTask());
            return guiUi.getResponse();
        case "2":
        case "edit date":
            if (updateStateUtil.getOriginalTask() instanceof Deadline) {
                updateStateUtil.setStep(UpdateStateUtil.Step.WAITING_FOR_DATE);
                guiUi.printUpdateDatePrompt();
            } else if (updateStateUtil.getOriginalTask() instanceof Event) {
                updateStateUtil.setStep(UpdateStateUtil.Step.WAITING_FOR_START_DATE);
                guiUi.printUpdateStartDatePrompt();
            }
            return guiUi.getResponse();
        default:
            guiUi.printInvalidChoice();
            return guiUi.getResponse();
        }
    }

    private String handleGuiUpdateDescription(String input, GuiUi guiUi) {
        if (input.trim().isEmpty()) {
            guiUi.printUsage("Description cannot be empty. Please try again:");
            return guiUi.getResponse();
        }

        Task updatedTask = createUpdatedTask(updateStateUtil.getOriginalTask(), input, null, null);
        replaceTask(updateStateUtil.getTaskIndex(), updatedTask);
        guiUi.printTaskUpdated(updatedTask, "description");
        updateStateUtil = null;
        return guiUi.getResponse();
    }

    private String handleGuiUpdateDate(String input, GuiUi guiUi) {
        try {
            DateTimeUtil.parseLenientResult(input);
            Task updatedTask =
                createUpdatedTask(updateStateUtil.getOriginalTask(), null, input, null);
            replaceTask(updateStateUtil.getTaskIndex(), updatedTask);
            guiUi.printTaskUpdated(updatedTask, "date/time");
            updateStateUtil = null;
            return guiUi.getResponse();
        } catch (Exception e) {
            guiUi.printUsage(
                "Invalid date/time format. "
                    + DateTimeUtil.examplesHelp()
                    + "\nPlease try again:");
            return guiUi.getResponse();
        }
    }

    private String handleGuiUpdateStartDate(String input, GuiUi guiUi) {
        try {
            DateTimeUtil.parseLenientResult(input);
            updateStateUtil.setNewStartDate(input);
            updateStateUtil.setStep(UpdateStateUtil.Step.WAITING_FOR_END_DATE);
            guiUi.printUpdateEndDatePrompt();
            return guiUi.getResponse();
        } catch (Exception e) {
            guiUi.printUsage(
                "Invalid date/time format. "
                    + DateTimeUtil.examplesHelp()
                    + "\nPlease try again:");
            return guiUi.getResponse();
        }
    }

    private String handleGuiUpdateEndDate(String input, GuiUi guiUi) {
        try {
            DateTimeUtil.parseLenientResult(input);
            Task updatedTask =
                createUpdatedTask(
                    updateStateUtil.getOriginalTask(),
                    null,
                    updateStateUtil.getNewStartDate(),
                    input);
            replaceTask(updateStateUtil.getTaskIndex(), updatedTask);
            guiUi.printTaskUpdated(updatedTask, "dates");
            updateStateUtil = null;
            return guiUi.getResponse();
        } catch (Exception e) {
            guiUi.printUsage(
                "Invalid date/time format. "
                    + DateTimeUtil.examplesHelp()
                    + "\nPlease try again:");
            return guiUi.getResponse();
        }
    }
}
