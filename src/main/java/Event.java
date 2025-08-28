public class Event extends Task{
    private final String from;
    private final String to;
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }
    @Override
    protected String typeTag() { return "E"; };
    @Override
    protected String extra() { return " (from: " + from + ", to: " + to + ")"; }

    public String getFrom() { return this.from; }
    public String getTo() { return this.to; }
}