package duke.command;

import duke.task.Task;
import duke.task.TaskList;
import duke.ui.Ui;

public class DeleteCommand implements Command {
  private final int index;

  public DeleteCommand(int index) {
    this.index = index;
  }

  @Override
  public void execute(TaskList tasks, Ui ui) {
    if (index < 1 || index > tasks.size()) {
      ui.printUsage("Please use a duke.task number between 1 and " + tasks.size() + ".");
      return;
    }
    Task removed = tasks.remove(index - 1);
    ui.printDelete(removed, tasks.size());
  }
}
