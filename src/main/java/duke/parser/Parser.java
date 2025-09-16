package duke.parser;

import duke.command.AgendaCommand;
import duke.command.ClearCommand;
import duke.command.Command;
import duke.command.DeadlineCommand;
import duke.command.DeleteCommand;
import duke.command.EmptyCommand;
import duke.command.EventCommand;
import duke.command.ExitCommand;
import duke.command.FindCommand;
import duke.command.ListCommand;
import duke.command.MarkCommand;
import duke.command.TodoCommand;
import duke.command.UnknownCommand;
import duke.command.UpdateCommand;

/**
 * Parses user input strings into Command objects. Uses constants instead of magic numbers and
 * strings for better maintainability. Responsible for extracting command words and arguments from
 * user input. Provides specialized parsing methods for complex commands like deadline and event.
 */
public class Parser {
    // Command parsing constants
    private static final String BY_DELIMITER = "/by";
    private static final String FROM_DELIMITER = "/from";
    private static final String TO_DELIMITER = "/to";

    // Magic number constants for substring operations
    private static final int BY_OFFSET = 3; // "/by".length()
    private static final int FROM_OFFSET = 5; // "/from".length()
    private static final int TO_OFFSET = 3; // "/to".length()

    // Command names
    private static final String CMD_EMPTY = "";
    private static final String CMD_BYE = "bye";
    private static final String CMD_LIST = "list";
    private static final String CMD_TODO = "todo";
    private static final String CMD_DEADLINE = "deadline";
    private static final String CMD_EVENT = "event";
    private static final String CMD_MARK = "mark";
    private static final String CMD_UNMARK = "unmark";
    private static final String CMD_DELETE = "delete";
    private static final String CMD_ON = "on";
    private static final String CMD_CLEAR = "clear";
    private static final String CMD_FIND = "find";
    private static final String CMD_UPDATE = "update";

    /**
     * Validates multiple string parts to ensure none are null or empty. Uses varargs to accept any
     * number of string arguments for validation.
     *
     * @param errorMessage The error message to throw if validation fails
     * @param parts        Variable number of string parts to validate
     * @throws IllegalArgumentException if any part is null, empty, or whitespace-only
     */
    private void validateParts(String errorMessage, String... parts) {
        assert errorMessage != null && !errorMessage.trim().isEmpty()
            : "Error message cannot be null or empty";

        for (String part : parts) {
            if (part == null || part.trim().isEmpty()) {
                throw new IllegalArgumentException(errorMessage);
            }
        }
    }

    /**
     * Parses a line of user input into the appropriate Command object. Uses constants instead of
     * magic strings for better maintainability.
     *
     * @param line The raw input command string from the user
     * @return The corresponding Command object to execute
     */
    public Command parseCommand(String line) {
        assert line != null : "Input line cannot be null";

        String cmd = getCommandWord(line);
        String args = getArguments(line);

        assert cmd != null : "Command word should never be null";
        assert args != null : "Arguments should never be null (can be empty)";

        switch (cmd) {
        case CMD_EMPTY:
            return new EmptyCommand();
        case CMD_BYE:
            return new ExitCommand();
        case CMD_LIST:
            return new ListCommand();
        case CMD_TODO:
            return parseTodoCommand(args, line);
        case CMD_DEADLINE:
            return parseDeadlineCommand(args, line);
        case CMD_EVENT:
            return parseEventCommand(args, line);
        case CMD_MARK:
            return new MarkCommand(parseOneBasedIndex(args), true);
        case CMD_UNMARK:
            return new MarkCommand(parseOneBasedIndex(args), false);
        case CMD_DELETE:
            return new DeleteCommand(parseOneBasedIndex(args));
        case CMD_ON:
            return new AgendaCommand(args);
        case CMD_CLEAR:
            return new ClearCommand();
        case CMD_FIND:
            return new FindCommand(args);
        case CMD_UPDATE:
            return parseUpdateCommand(args, line);
        default:
            return new UnknownCommand(line);
        }
    }

    /**
     * Parses todo command with error handling.
     */
    private Command parseTodoCommand(String args, String originalLine) {
        try {
            validateParts("Todo description cannot be empty", args);
            return new TodoCommand(args);
        } catch (IllegalArgumentException ex) {
            return new UnknownCommand(originalLine);
        }
    }

    /**
     * Parses deadline command with error handling.
     */
    private Command parseDeadlineCommand(String args, String originalLine) {
        try {
            String[] parts = parseDeadlineArgs(args == null ? "" : args);
            return new DeadlineCommand(parts[0], parts[1]);
        } catch (IllegalArgumentException ex) {
            return new UnknownCommand(originalLine);
        }
    }

    /**
     * Parses event command with error handling.
     */
    private Command parseEventCommand(String args, String originalLine) {
        try {
            String[] parts = parseEventArgs(args == null ? "" : args);
            return new EventCommand(parts[0], parts[1], parts[2]);
        } catch (IllegalArgumentException ex) {
            return new UnknownCommand(originalLine);
        }
    }

    /**
     * Parses update command with error handling.
     */
    private Command parseUpdateCommand(String args, String originalLine) {
        try {
            int taskIndex = parseOneBasedIndex(args);
            if (taskIndex == -1) {
                throw new IllegalArgumentException("Invalid task number");
            }
            return new UpdateCommand(taskIndex);
        } catch (Exception ex) {
            return new UnknownCommand(originalLine);
        }
    }

    /**
     * Extracts the command word (first word) from the input line.
     *
     * @param line The full user input line
     * @return The command word as lowercase string, or empty string if no input
     */
    public String getCommandWord(String line) {
        assert line != null : "Line cannot be null";

        String s = line.trim();
        int sp = s.indexOf(' ');
        return (sp == -1 ? s : s.substring(0, sp)).toLowerCase();
    }

    /**
     * Extracts the arguments portion from the input line.
     *
     * @param line The full user input line
     * @return The arguments string (everything after the command word)
     */
    public String getArguments(String line) {
        assert line != null : "Line cannot be null";

        String s = line.trim();
        int sp = s.indexOf(' ');
        return sp == -1 ? "" : s.substring(sp + 1).trim();
    }

    /**
     * Parses deadline command arguments into description and by-date components. Uses constants
     * instead of magic strings and numbers. Expects format: "description /by date"
     *
     * @param args The argument string following 'deadline' command
     * @return String array where index 0 is description and index 1 is by-date
     * @throws IllegalArgumentException if /by is missing or components are empty
     */
    public String[] parseDeadlineArgs(String args) {
        assert args != null : "Arguments cannot be null";

        int i = args.lastIndexOf(BY_DELIMITER);
        if (i < 0) {
            throw new IllegalArgumentException("Missing " + BY_DELIMITER);
        }

        String desc = args.substring(0, i).trim();
        String byRaw = args.substring(i + BY_OFFSET).trim();

        validateParts("Usage: deadline <description> " + BY_DELIMITER + " <date>", desc, byRaw);

        return new String[]{desc, byRaw};
    }

    /**
     * Parses event command arguments into description, from-date, and to-date. Uses constants
     * instead of magic strings and numbers. Expects format: "description /from date /to date"
     *
     * @param args The argument string following 'event' command
     * @return String array with description, from-date, and to-date respectively
     * @throws IllegalArgumentException if /from or /to is missing or components are empty
     */
    public String[] parseEventArgs(String args) {
        assert args != null : "Arguments cannot be null";

        int i = args.lastIndexOf(FROM_DELIMITER);
        int j = args.lastIndexOf(TO_DELIMITER);
        if (i < 0 || j < 0 || i >= j) {
            throw new IllegalArgumentException("Missing " + FROM_DELIMITER + " or " + TO_DELIMITER);
        }

        String desc = args.substring(0, i).trim();
        String fromRaw = args.substring(i + FROM_OFFSET, j).trim();
        String toRaw = args.substring(j + TO_OFFSET).trim();

        validateParts(
            "Usage: event <description> "
                + FROM_DELIMITER
                + " <date> "
                + TO_DELIMITER
                + " <date>",
            desc,
            fromRaw,
            toRaw);

        return new String[]{desc, fromRaw, toRaw};
    }

    /**
     * Parses a one-based index from the argument string. Returns -1 if parsing fails or the string
     * is invalid.
     *
     * @param args The string to parse as an index number
     * @return The parsed one-based index, or -1 if invalid
     */
    private int parseOneBasedIndex(String args) {
        if (args == null) {
            return -1;
        }

        String s = args.trim();
        if (s.isEmpty()) {
            return -1;
        }

        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
