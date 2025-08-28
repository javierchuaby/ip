public class Parser {

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
            default -> new UnknownCommand(line);
        };
    }

    public String getCommandWord(String line) {
        String s = line.trim();
        int sp = s.indexOf(' ');
        return (sp == -1 ? s : s.substring(0, sp)).toLowerCase();
    }

    public String getArguments(String line) {
        String s = line.trim();
        int sp = s.indexOf(' ');
        return sp == -1 ? "" : s.substring(sp + 1).trim();
    }

    public String[] parseDeadlineArgs(String args) {
        int i = args.lastIndexOf("/by");
        if (i < 0) throw new IllegalArgumentException("Missing /by");
        String desc = args.substring(0, i).trim();
        String byRaw = args.substring(i + 3).trim();
        if (desc.isEmpty() || byRaw.isEmpty()) throw new IllegalArgumentException("Usage");
        return new String[]{desc, byRaw};
    }

    public String[] parseEventArgs(String args) {
        int i = args.lastIndexOf("/from");
        int j = args.lastIndexOf("/to");
        if (i < 0 || j < 0 || i >= j) throw new IllegalArgumentException("Missing /from or /to");
        String desc = args.substring(0, i).trim();
        String fromRaw = args.substring(i + 5, j).trim();
        String toRaw = args.substring(j + 3).trim();
        if (desc.isEmpty() || fromRaw.isEmpty() || toRaw.isEmpty()) throw new IllegalArgumentException("Usage");
        return new String[]{desc, fromRaw, toRaw};
    }

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