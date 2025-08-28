public class ListCommand implements Command {
    @Override
    public void execute(TaskList tasks, Ui ui) {
        ui.printList(tasks.asUnmodifiable());
    }
}