package duke.command;

import duke.task.TaskList;
import duke.ui.Ui;

public class UnknownCommand implements Command {
  private final String input;

  public UnknownCommand(String input) {
    this.input = input;
  }

  @Override
  public void execute(TaskList tasks, Ui ui) {
    ui.printUnknown(input);
  }
}
