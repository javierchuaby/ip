public class Parser {

    /** First word in lower-case (e.g., "to-do", "deadline", "event", "mark"). */
    public static String getCommandWord(String input) {
        if (input == null) return "";
        String trimmed = input.trim();
        if (trimmed.isEmpty()) return "";
        int space = trimmed.indexOf(' ');
        String first = (space == -1) ? trimmed : trimmed.substring(0, space);
        return first.toLowerCase();
    }

    /** Everything after the first word (could be empty). */
    public static String getArguments(String input) {
        if (input == null) return "";
        String trimmed = input.trim();
        int space = trimmed.indexOf(' ');
        if (space == -1) return "";
        return trimmed.substring(space + 1).trim();
    }

    /** Try to parse the first integer inside a string (returns null if none). */
    public static Integer getFirstIndex(String s) {
        if (s == null) return null;
        int n = s.length();
        int i = 0;
        while (i < n) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                int j = i + 1;
                while (j < n && Character.isDigit(s.charAt(j))) j++;
                try {
                    return Integer.parseInt(s.substring(i, j));
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            i++;
        }
        return null;
    }

    /** Split "desc /by when" into [desc, when]; returns array length 2 or null if not valid. */
    public static String[] splitDeadline(String args) {
        if (args == null) return null;
        String flag = "/by";
        int idx = args.toLowerCase().indexOf(flag);
        if (idx == -1) return null;
        String desc = args.substring(0, idx).trim();
        String when = args.substring(idx + flag.length()).trim();
        if (desc.isEmpty() || when.isEmpty()) return null;
        return new String[]{desc, when};
    }

    /** Split "desc /from start /to end" into [desc, start, end]; returns len 3 or null. */
    public static String[] splitEvent(String args) {
        if (args == null) return null;
        String lo = args.toLowerCase();
        String f = "/from";
        String t = "/to";
        int iF = lo.indexOf(f);
        int iT = lo.indexOf(t, iF == -1 ? 0 : iF + f.length());
        if (iF == -1 || iT == -1) return null;
        String desc = args.substring(0, iF).trim();
        String start = args.substring(iF + f.length(), iT).trim();
        String end = args.substring(iT + t.length()).trim();
        if (desc.isEmpty() || start.isEmpty() || end.isEmpty()) return null;
        return new String[]{desc, start, end};
    }
}