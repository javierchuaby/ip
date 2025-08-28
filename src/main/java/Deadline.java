import java.time.LocalDateTime;

public class Deadline extends Task {
    private final LocalDateTime by;
    private final boolean byHasTime;

    public Deadline(String description, String byInput) {
        super(description);
        var r = DateTimeUtil.parseLenientResult(byInput);
        this.by = r.dt;
        this.byHasTime = r.hasTime;
    }

    public Deadline(String description, LocalDateTime by, boolean hasTime) {
        super(description);
        this.by = by;
        this.byHasTime = hasTime;
    }

    @Override
    protected String typeTag() { return "D"; }

    @Override
    protected String extra() {
        return " (by " + DateTimeUtil.toPrettyString(by, byHasTime) + ")";
    }

    public String getBy() { return DateTimeUtil.toStorageString(this.by, this.byHasTime); }
}