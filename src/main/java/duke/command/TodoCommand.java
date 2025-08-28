package duke.command;

import duke.task.Task;
import duke.task.TaskList;
import duke.task.Todo;
import duke.ui.Ui;

public class TodoCommand implements Command {
  private final String description;

  public TodoCommand(String description) {
    this.description = description;
  }

  @Override
  public void execute(TaskList tasks, Ui ui) {
    if (description == null || description.trim().isEmpty()) {
      ui.printUsage("Usage: todo <description>");
      return;
    }
    Task t = new Todo(description.trim());
    tasks.add(t);
    ui.printAdded(t, tasks.size());
  }
}
