package duke.command;

import duke.task.TaskList;
import duke.ui.Ui;
import duke.util.DateTimeUtil;

public class AgendaCommand implements Command {
  private final String dateInput;

  public AgendaCommand(String dateInput) {
    this.dateInput = dateInput;
  }

  @Override
  public void execute(TaskList tasks, Ui ui) {
    if (dateInput == null || dateInput.trim().isEmpty()) {
      ui.printAgendaFormat();
      return;
    }

    try {
      var r = DateTimeUtil.parseLenientResult(dateInput.trim());
      var target = r.dt.toLocalDate();
      var matches = tasks.tasksOn(target);
      ui.printAgendaForDate(target, matches, tasks);
    } catch (IllegalArgumentException ex) {
      ui.printUsage("I couldn't read that date: " + ex.getMessage());
    }
  }
}
