package her.m35;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

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
        assertInstanceOf(ListCommand.class, Parser.parse("list"));
        assertInstanceOf(ExitCommand.class, Parser.parse("bye"));
        assertInstanceOf(HelpCommand.class, Parser.parse("help"));
        assertInstanceOf(ClearCommand.class, Parser.parse("clear"));
        assertInstanceOf(MarkCommand.class, Parser.parse("mark 1"));
        assertInstanceOf(MarkCommand.class, Parser.parse("unmark 2"));
        assertInstanceOf(DeleteCommand.class, Parser.parse("delete task name"));
        assertInstanceOf(FindCommand.class, Parser.parse("find homework"));
        assertInstanceOf(AddTaskCommand.class, Parser.parse("todo read book"));
        assertInstanceOf(AddTaskCommand.class, Parser.parse("deadline task /by Oct 12"));
        assertInstanceOf(AddTaskCommand.class, Parser.parse("event party /from Oct 10 /to Oct 11"));
        assertInstanceOf(MessageCommand.class, Parser.parse("mark"));
        assertInstanceOf(MessageCommand.class, Parser.parse("list list"));
        assertInstanceOf(MessageCommand.class, Parser.parse("deadline task"));
        assertInstanceOf(MessageCommand.class, Parser.parse("bye now"));
    }

    @Test
    public void parseFindPromptTests() throws Exception {
        ArrayList<TaskList.FilterCondition> filterConditions = new ArrayList<>();
        ArrayList<String> keywords = new ArrayList<>();

        Parser.parseFindPrompt("/done", filterConditions, keywords);
        assertEquals(1, filterConditions.size());
        assertEquals(TaskList.FilterCondition.IS_MARKED, filterConditions.get(0));
        assertEquals("", keywords.get(0));

        Parser.parseFindPrompt("/todo", filterConditions, keywords);
        assertEquals(1, filterConditions.size());
        assertEquals(TaskList.FilterCondition.IS_UNMARKED, filterConditions.get(0));
        assertEquals("", keywords.get(0));

        Parser.parseFindPrompt("/on 2026-10-12", filterConditions, keywords);
        assertEquals(1, filterConditions.size());
        assertEquals(TaskList.FilterCondition.ON_DATE, filterConditions.get(0));
        assertEquals("2026-10-12", keywords.get(0));

        Parser.parseFindPrompt("/before 28 May /after today",
                filterConditions, keywords);
        assertEquals(2, filterConditions.size());
        assertEquals(TaskList.FilterCondition.BEFORE, filterConditions.get(0));
        assertEquals("28 May", keywords.get(0));
        assertEquals(TaskList.FilterCondition.AFTER, filterConditions.get(1));
        assertEquals("today", keywords.get(1));

        Parser.parseFindPrompt("/contains something", filterConditions, keywords);
        assertEquals(1, filterConditions.size());
        assertEquals(TaskList.FilterCondition.KEYWORD, filterConditions.get(0));
        assertEquals("something", keywords.get(0));

        Parser.parseFindPrompt("pigeonhole", filterConditions, keywords);

        assertEquals(1, filterConditions.size());
        assertEquals(TaskList.FilterCondition.KEYWORD, filterConditions.get(0));
        assertEquals("pigeonhole", keywords.get(0));

        Parser.parseFindPrompt("/unknown something", filterConditions, keywords);
        assertEquals(2, filterConditions.size());
        assertEquals(TaskList.FilterCondition.ERROR_CONDITION, filterConditions.get(0));
        assertEquals("unknown", keywords.get(0));

        Parser.parseFindPrompt("/tag #nice", filterConditions, keywords);
        assertEquals(1, filterConditions.size());
        assertEquals(TaskList.FilterCondition.TAG, filterConditions.get(0));
        assertEquals("nice", keywords.get(0));

        Parser.parseFindPrompt("/tag #jeans #pocket", filterConditions, keywords);
        assertEquals(2, filterConditions.size());
        assertEquals(TaskList.FilterCondition.TAG, filterConditions.get(0));
        assertEquals("jeans", keywords.get(0));
        assertEquals(TaskList.FilterCondition.TAG, filterConditions.get(1));
        assertEquals("pocket", keywords.get(1));

        Exception exception = assertThrows(Exception.class, () ->
                Parser.parseFindPrompt("/tag work", filterConditions, keywords));
        assertEquals("Error: Notate tags with a # sign.", exception.getMessage());

        exception = assertThrows(Exception.class, () ->
                Parser.parseFindPrompt("/tag #work!", filterConditions, keywords));
        assertTrue(exception.getMessage().contains("Tags need to be strictly alphanumeric"));

        Parser.parseFindPrompt("conundrum /done /tag #pointless",
                filterConditions, keywords);

        assertEquals(3, filterConditions.size());
        assertEquals(TaskList.FilterCondition.KEYWORD, filterConditions.get(0));
        assertEquals("conundrum", keywords.get(0));
        assertEquals(TaskList.FilterCondition.IS_MARKED, filterConditions.get(1));
        assertEquals("", keywords.get(1));
        assertEquals(TaskList.FilterCondition.TAG, filterConditions.get(2));
        assertEquals("pointless", keywords.get(2));
    }
}
