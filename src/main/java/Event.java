import java.time.LocalDateTime;

public class Event extends Task {
    private final LocalDateTime from;
    private final LocalDateTime to;
    private final boolean fromHasTime;
    private final boolean toHasTime;

    public Event(String description, String fromInput, String toInput) {
        super(description);
        var rf = DateTimeUtil.parseLenientResult(fromInput);
        var rt = DateTimeUtil.parseLenientResult(toInput);
        if (rt.dt.isBefore(rf.dt)) {
            throw new IllegalArgumentException("The /to date/time must be on or after the /from date/time.");
        }
        this.from = rf.dt;
        this.to = rt.dt;
        this.fromHasTime = rf.hasTime;
        this.toHasTime = rt.hasTime;
    }

    public Event(String description, LocalDateTime from, boolean fromHasTime,
                 LocalDateTime to, boolean toHasTime) {
        super(description);
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("The /to date/time must be on or after the /from date/time.");
        }
        this.from = from;
        this.to = to;
        this.fromHasTime = fromHasTime;
        this.toHasTime = toHasTime;
    }

    @Override
    protected String typeTag() { return "E"; }

    @Override
    protected String extra() {
        return " (from: " + DateTimeUtil.toPrettyString(from, fromHasTime)
                + ", to: "   + DateTimeUtil.toPrettyString(to,   toHasTime) + ")";
    }

    public String getFrom() { return DateTimeUtil.toStorageString(this.from, this.fromHasTime); }
    public String getTo()   { return DateTimeUtil.toStorageString(this.to,   this.toHasTime); }
}