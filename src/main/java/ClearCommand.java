public class ClearCommand implements Command {
    @Override
    public void execute(TaskList tasks, Ui ui) {
        if (tasks.size() == 0) {
            ui.printNoTasksToClear();
            return;
        }

        ui.printClearPrompt();
    }
}