public class Task {
    private final String description;
    private boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void mark() {
        this.isDone = true;
    }

    public void unmark() {
        this.isDone = false;
    }

    protected String statusIcon() {
        return this.isDone ? "X" : " ";
    }

    protected String typeTag() {
        return "?";
    }

    protected String extra() {
        return "";
    }

    @Override
    public String toString() {
        return "[" + typeTag() + "] [" + statusIcon() + "] " + description + extra();
    }
}
