package duke.util;

import java.util.List;

/**
 * Utility class for generating consistent command help listings. Provides centralized command list
 * management for UI classes.
 */
public final class CommandListingUtil {

    /**
     * List of available commands with their usage syntax.
     */
    private static final List<String> COMMAND_LIST = List.of(
        "- list", "- todo <description>",
        "- deadline <description> /by <date/time>",
        "- event <description> /from <start> /to <end>", "- mark | unmark <task_number>",
        "- delete <task_number>",
        "- update <task_number>",
        "- on <date>",
        "- clear (clear all tasks in list)",
        "- find <keyword>"
    );

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private CommandListingUtil() {
    }

    /**
     * Returns the list of available commands.
     *
     * @return Unmodifiable list of command strings
     */
    public static List<String> getCommandList() {
        return COMMAND_LIST;
    }

    /**
     * Appends all commands to the provided output method.
     *
     * @param outputMethod Method to call for each command line
     */
    public static void appendCommands(java.util.function.Consumer<String> outputMethod) {
        COMMAND_LIST.forEach(outputMethod);
    }
}
