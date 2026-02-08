package her.m35;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import her.m35.command.AddTaskCommand;
import her.m35.command.ClearCommand;
import her.m35.command.DeleteCommand;
import her.m35.command.ExitCommand;
import her.m35.command.FindCommand;
import her.m35.command.HelpCommand;
import her.m35.command.ListCommand;
import her.m35.command.MarkCommand;
import her.m35.command.MessageCommand;
import her.m35.parser.Parser;

public class ParserTest {

    @Test
    public void parseTests() {
        assertTrue(Parser.parse("list") instanceof ListCommand);
        assertTrue(Parser.parse("bye") instanceof ExitCommand);
        assertTrue(Parser.parse("help") instanceof HelpCommand);
        assertTrue(Parser.parse("clear") instanceof ClearCommand);
        assertTrue(Parser.parse("mark 1") instanceof MarkCommand);
        assertTrue(Parser.parse("unmark 2") instanceof MarkCommand);
        assertTrue(Parser.parse("delete task name") instanceof DeleteCommand);
        assertTrue(Parser.parse("find homework") instanceof FindCommand);
        assertTrue(Parser.parse("todo read book") instanceof AddTaskCommand);
        assertTrue(Parser.parse("deadline task /by Oct 12") instanceof AddTaskCommand);
        assertTrue(Parser.parse("event party /from Oct 10 /to Oct 11") instanceof AddTaskCommand);
        assertTrue(Parser.parse("mark") instanceof MessageCommand);
        assertTrue(Parser.parse("list list") instanceof MessageCommand);
        assertTrue(Parser.parse("deadline task") instanceof MessageCommand);
        assertTrue(Parser.parse("bye now") instanceof MessageCommand);
    }
}
