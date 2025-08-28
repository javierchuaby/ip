package duke.command;

import duke.task.Task;
import duke.task.TaskList;
import duke.ui.Ui;

public class MarkCommand implements Command {
  private final int index;
  private final boolean mark;

  public MarkCommand(int index, boolean mark) {
    this.index = index;
    this.mark = mark;
  }

  @Override
  public void execute(TaskList tasks, Ui ui) {
    if (index < 1 || index > tasks.size()) {
      ui.printUsage("Please use a duke.task number between 1 and " + tasks.size() + ".");
      return;
    }
    if (mark) {
      tasks.mark(index - 1);
    } else {
      tasks.unmark(index - 1);
    }
    Task t = tasks.get(index - 1);
    ui.printMarked(t, mark);
  }
}
