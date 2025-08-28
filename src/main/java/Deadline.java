public class Deadline extends Task{
    private final String by;
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }
    @Override
    protected String typeTag() { return "D"; };
    @Override
    protected String extra() { return " (by: " + by + ")"; }

    public String getBy() { return this.by; }
}