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

/**
 * Parses user input strings into Command objects.
 * Responsible for extracting command words and arguments from user input.
 * Provides specialized parsing methods for complex commands like deadline and event.
 */
public class Parser {

    /**
     * Validates multiple string parts to ensure none are null or empty.
     * Uses varargs to accept any number of string arguments for validation.
     *
     * @param errorMessage The error message to throw if validation fails
     * @param parts Variable number of string parts to validate
     * @throws IllegalArgumentException if any part is null, empty, or whitespace-only
     */
    private void validateParts(String errorMessage, String... parts) {
        for (String part : parts) {
            if (part == null || part.trim().isEmpty()) {
                throw new IllegalArgumentException(errorMessage);
            }
        }
    }

    /**
     * Parses a line of user input into the appropriate Command object.
     * Uses a switch statement to determine command type and create corresponding Command.
     *
     * @param line The raw input command string from the user
     * @return The corresponding Command object to execute
     */
    public Command parseCommand(String line) {
        String cmd = getCommandWord(line);
        String args = getArguments(line);

        switch (cmd) {
        case "":
            return new EmptyCommand();
        case "bye":
            return new ExitCommand();
        case "list":
            return new ListCommand();
        case "todo":
            try {
                validateParts("Todo description cannot be empty", args);
                return new TodoCommand(args);
            } catch (IllegalArgumentException ex) {
                return new UnknownCommand(line);
            }
        case "deadline":
            try {
                String[] parts = parseDeadlineArgs(args == null ? "" : args);
                return new DeadlineCommand(parts[0], parts[1]);
            } catch (IllegalArgumentException ex) {
                return new UnknownCommand(line);
            }
        case "event":
            try {
                String[] parts = parseEventArgs(args == null ? "" : args);
                return new EventCommand(parts[0], parts[1], parts[2]);
            } catch (IllegalArgumentException ex) {
                return new UnknownCommand(line);
            }
        case "mark":
            return new MarkCommand(parseOneBasedIndex(args), true);
        case "unmark":
            return new MarkCommand(parseOneBasedIndex(args), false);
        case "delete":
            return new DeleteCommand(parseOneBasedIndex(args));
        case "on":
            return new AgendaCommand(args);
        case "clear":
            return new ClearCommand();
        case "find":
            return new FindCommand(args);
        default:
            return new UnknownCommand(line);
        }
    }

    /**
     * Extracts the command word (first word) from the input line.
     *
     * @param line The full user input line
     * @return The command word as lowercase string, or empty string if no input
     */
    public String getCommandWord(String line) {
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
        String s = line.trim();
        int sp = s.indexOf(' ');
        return sp == -1 ? "" : s.substring(sp + 1).trim();
    }

    /**
     * Parses deadline command arguments into description and by-date components.
     * Expects format: "description /by date"
     *
     * @param args The argument string following 'deadline' command
     * @return String array where index 0 is description and index 1 is by-date
     * @throws IllegalArgumentException if /by is missing or components are empty
     */
    public String[] parseDeadlineArgs(String args) {
        int i = args.lastIndexOf("/by");
        if (i < 0) {
            throw new IllegalArgumentException("Missing /by");
        }

        String desc = args.substring(0, i).trim();
        String byRaw = args.substring(i + 3).trim();

        validateParts("Usage: deadline <description> /by <date>", desc, byRaw);

        return new String[]{desc, byRaw};
    }

    /**
     * Parses event command arguments into description, from-date, and to-date.
     * Expects format: "description /from date /to date"
     *
     * @param args The argument string following 'event' command
     * @return String array with description, from-date, and to-date respectively
     * @throws IllegalArgumentException if /from or /to is missing or components are empty
     */
    public String[] parseEventArgs(String args) {
        int i = args.lastIndexOf("/from");
        int j = args.lastIndexOf("/to");
        if (i < 0 || j < 0 || i >= j) {
            throw new IllegalArgumentException("Missing /from or /to");
        }

        String desc = args.substring(0, i).trim();
        String fromRaw = args.substring(i + 5, j).trim();
        String toRaw = args.substring(j + 3).trim();

        validateParts("Usage: event <description> /from <date> /to <date>", desc, fromRaw, toRaw);

        return new String[]{desc, fromRaw, toRaw};
    }

    /**
     * Parses a one-based index from the argument string.
     * Returns -1 if parsing fails or the string is invalid.
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
