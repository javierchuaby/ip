public class Parser {
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
}