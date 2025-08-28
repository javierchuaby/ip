import java.time.LocalDateTime;

public class Deadline extends Task {
    private final LocalDateTime by;
    private final boolean hasTime;

    public Deadline(String description, LocalDateTime by, boolean hasTime) {
        super(description);
        this.by = by;
        this.hasTime = hasTime;
    }

    public Deadline(String description, String byString) {
        super(description);
        DateTimeUtil.ParseResult result = DateTimeUtil.parseLenientResult(byString);
        this.by = result.dt;
        this.hasTime = result.hasTime;
    }

    public LocalDateTime getByDateTime() { return by; }
    public String getBy() { return DateTimeUtil.toStorageString(by, hasTime); }

    @Override
    public String toString() {
        return "[D] [" + getStatusIcon() + "] " + description +
                " (by: " + DateTimeUtil.toPrettyString(by, hasTime) + ")";
    }
}