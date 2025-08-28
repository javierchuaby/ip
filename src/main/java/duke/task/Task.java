package duke.task;

public abstract class Task {
  protected String description;
  protected boolean isDone;

  public Task(String description) {
    this.description = description;
    this.isDone = false;
  }

  public String getDescription() {
    return description;
  }

  public boolean isDone() {
    return isDone;
  }

  public void mark() {
    isDone = true;
  }

  public void unmark() {
    isDone = false;
  }

  public String getStatusIcon() {
    return isDone ? "X" : " ";
  }

  @Override
  public abstract String toString();
}
