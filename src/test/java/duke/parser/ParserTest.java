package duke.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import duke.command.Command;
import duke.command.DeadlineCommand;
import duke.command.DeleteCommand;
import duke.command.EmptyCommand;
import duke.command.EventCommand;
import duke.command.ExitCommand;
import duke.command.ListCommand;
import duke.command.MarkCommand;
import duke.command.TodoCommand;
import duke.command.UnknownCommand;

class ParserTest {
    private Parser parser;

    @BeforeEach
    void setUp() {
        parser = new Parser();
    }

    @Test
    void parseCommand_todoCommand_returnsTodoCommand() {
        Command command = parser.parseCommand("todo Buy groceries");
        assertInstanceOf(TodoCommand.class, command);
    }

    @Test
    void parseCommand_deadlineCommand_returnsDeadlineCommand() {
        Command command = parser.parseCommand("deadline Submit assignment /by 2025-12-25");
        assertInstanceOf(DeadlineCommand.class, command);
    }

    @Test
    void parseCommand_eventCommand_returnsEventCommand() {
        Command command = parser.parseCommand("event Team meeting /from 2025-12-25 /to 2025-12-26");
        assertInstanceOf(EventCommand.class, command);
    }

    @Test
    void parseCommand_listCommand_returnsListCommand() {
        Command command = parser.parseCommand("list");
        assertInstanceOf(ListCommand.class, command);
    }

    @Test
    void parseCommand_exitCommand_returnsExitCommand() {
        Command command = parser.parseCommand("bye");
        assertInstanceOf(ExitCommand.class, command);
    }

    @Test
    void parseCommand_markCommand_returnsMarkCommand() {
        Command command = parser.parseCommand("mark 1");
        assertInstanceOf(MarkCommand.class, command);
    }

    @Test
    void parseCommand_deleteCommand_returnsDeleteCommand() {
        Command command = parser.parseCommand("delete 1");
        assertInstanceOf(DeleteCommand.class, command);
    }

    @Test
    void parseCommand_emptyString_returnsEmptyCommand() {
        Command command = parser.parseCommand("");
        assertInstanceOf(EmptyCommand.class, command);
    }

    @Test
    void parseCommand_whitespaceOnly_returnsEmptyCommand() {
        Command command = parser.parseCommand("   ");
        assertInstanceOf(EmptyCommand.class, command);
    }

    @Test
    void parseCommand_unknownCommand_returnsUnknownCommand() {
        Command command = parser.parseCommand("invalid command");
        assertInstanceOf(UnknownCommand.class, command);
    }

    @Test
    void getCommandWord_validCommand_returnsLowerCase() {
        assertEquals("todo", parser.getCommandWord("TODO Buy groceries"));
        assertEquals("list", parser.getCommandWord("list"));
        assertEquals("bye", parser.getCommandWord("BYE"));
    }

    @Test
    void getArguments_commandWithArgs_returnsArgs() {
        assertEquals("Buy groceries", parser.getArguments("todo Buy groceries"));
        assertEquals("", parser.getArguments("list"));
    }

    @Test
    void parseDeadlineArgs_validArgs_returnsArray() {
        String[] result = parser.parseDeadlineArgs("Submit assignment /by 2025-12-25");
        assertEquals(2, result.length);
        assertEquals("Submit assignment", result[0]);
        assertEquals("2025-12-25", result[1]);
    }

    @Test
    void parseDeadlineArgs_missingBy_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> parser.parseDeadlineArgs("Submit assignment"));
    }
}
