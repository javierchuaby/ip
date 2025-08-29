package duke.parser;

import duke.command.*;

/**
 * Parses user input strings into Command objects.
 * Responsible for extracting command words and arguments from user input.
 * Provides specialized parsing methods for complex commands like deadline and event.
 */
public class Parser {

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

        return switch (cmd) {
            case "" -> new EmptyCommand();
            case "bye" -> new ExitCommand();
            case "list" -> new ListCommand();
            case "todo" -> new TodoCommand(args);
            case "deadline" -> {
                try {
                    String[] parts = parseDeadlineArgs(args == null ? "" : args);
                    yield new DeadlineCommand(parts[0], parts[1]);
                } catch (IllegalArgumentException ex) {
                    yield new UnknownCommand(line);
                }
            }
            case "event" -> {
                try {
                    String[] parts = parseEventArgs(args == null ? "" : args);
                    yield new EventCommand(parts[0], parts[1], parts[2]);
                } catch (IllegalArgumentException ex) {
                    yield new UnknownCommand(line);
                }
            }
            case "mark" -> new MarkCommand(parseOneBasedIndex(args), true);
            case "unmark" -> new MarkCommand(parseOneBasedIndex(args), false);
            case "delete" -> new DeleteCommand(parseOneBasedIndex(args));
            case "on" -> new AgendaCommand(args);
            case "clear" -> new ClearCommand();
            case "find" -> new FindCommand(args);
            default -> new UnknownCommand(line);
        };
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
        if (i < 0) throw new IllegalArgumentException("Missing /by");

        String desc = args.substring(0, i).trim();
        String byRaw = args.substring(i + 3).trim();

        if (desc.isEmpty() || byRaw.isEmpty()) throw new IllegalArgumentException("Usage");
        return new String[] {desc, byRaw};
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

        if (i < 0 || j < 0 || i >= j) throw new IllegalArgumentException("Missing /from or /to");

        String desc = args.substring(0, i).trim();
        String fromRaw = args.substring(i + 5, j).trim();
        String toRaw = args.substring(j + 3).trim();

        if (desc.isEmpty() || fromRaw.isEmpty() || toRaw.isEmpty())
            throw new IllegalArgumentException("Usage");

        return new String[] {desc, fromRaw, toRaw};
    }

    /**
     * Parses a one-based index from the argument string.
     * Returns -1 if parsing fails or the string is invalid.
     *
     * @param args The string to parse as an index number
     * @return The parsed one-based index, or -1 if invalid
     */
    private int parseOneBasedIndex(String args) {
        if (args == null) return -1;
        String s = args.trim();
        if (s.isEmpty()) return -1;

        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
