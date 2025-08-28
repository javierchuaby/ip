public class EmptyCommand implements Command {
    @Override
    public void execute(TaskList tasks, Ui ui) {
        ui.printUnknownEmpty();
    }
}