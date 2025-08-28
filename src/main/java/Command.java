public interface Command {
    void execute(TaskList tasks, Ui ui);

    default boolean isExit() {
        return false;
    }
}
