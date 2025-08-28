package duke.command;

import duke.task.Event;
import duke.task.TaskList;
import duke.ui.Ui;

public class EventCommand implements Command {
  private final String description;
  private final String fromInput;
  private final String toInput;

  public EventCommand(String description, String fromInput, String toInput) {
    this.description = description;
    this.fromInput = fromInput;
    this.toInput = toInput;
  }

  @Override
  public void execute(TaskList tasks, Ui ui) {
    if (description == null || description.trim().isEmpty()) {
      ui.printEventFormat();
      return;
    }
    try {
      Event e = new Event(description.trim(), fromInput.trim(), toInput.trim());
      tasks.add(e);
      ui.printAdded(e, tasks.size());
    } catch (IllegalArgumentException ex) {
      ui.printUsage("I couldn’t read those dates/times: " + ex.getMessage());
    }
  }
}
