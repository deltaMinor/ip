package herm35;

import herm35.command.AddTaskCommand;
import herm35.command.ClearCommand;
import herm35.command.DeleteCommand;
import herm35.command.ExitCommand;
import herm35.command.FindCommand;
import herm35.command.HelpCommand;
import herm35.command.ListCommand;
import herm35.command.MarkCommand;
import herm35.command.MessageCommand;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParserTest {

    @Test
    public void twoWordDateTests() {
        // existing
        assertEquals(LocalDate.of(LocalDate.now().getYear(), 10, 12), Parser.toDate("Oct 12").getTime());
        assertEquals(LocalDate.of(LocalDate.now().getYear(), 12, 12), Parser.toDate("12/12").getTime());
        assertEquals(LocalDate.of(LocalDate.now().getYear(), 12, 13), Parser.toDate("13-12").getTime());
        assertEquals(LocalDate.of(LocalDate.now().getYear(), 12, 13), Parser.toDate("12/13").getTime());
        assertEquals(LocalDate.of(LocalDate.now().getYear(), 4, 23), Parser.toDate("aPR-23").getTime());
        assertEquals(LocalDate.of(LocalDate.now().getYear(), 12, 1), Parser.toDate("December 01").getTime());
        assertEquals("ABC DEF", Parser.toDate("ABC DEF").getTime());
        assertEquals("Feb-30", Parser.toDate("Feb-30").getTime());
        assertEquals(LocalDate.of(LocalDate.now().getYear(), 1, 1), Parser.toDate("Jan 1").getTime());
        assertEquals(LocalDate.of(LocalDate.now().getYear(), 11, 9), Parser.toDate("NOV-09").getTime());
        assertEquals(LocalDate.of(LocalDate.now().getYear(), 6, 15), Parser.toDate("06/15").getTime());
        assertEquals(LocalDate.of(LocalDate.now().getYear(), 8, 31), Parser.toDate("31-08").getTime());
        assertEquals("hello world", Parser.toDate("hello world").getTime());
        assertEquals("99 99", Parser.toDate("99 99").getTime());
        assertEquals("March-99", Parser.toDate("March-99").getTime());
        assertEquals("00:00", Parser.toDate("00:00").getTime());
    }

    @Test
    public void threeWordDateTests() {
        // existing
        assertEquals(LocalDateTime.of(LocalDateTime.now().getYear(), 10, 12, 10, 30),
                Parser.toDate("Oct 12 10:30").getTime());
        assertEquals(LocalDate.of(2020, 12, 12),
                Parser.toDate("12/12 2020").getTime());
        assertEquals("12 34 5678",
                Parser.toDate("12 34 5678").getTime());
        assertEquals(LocalDateTime.of(LocalDateTime.now().getYear(), 12, 13, 18, 0),
                Parser.toDate("12/13 6PM").getTime());
        assertEquals(LocalDateTime.of(LocalDateTime.now().getYear(), 4, 23, 12, 34),
                Parser.toDate("aPR-23/12:34").getTime());
        assertEquals(LocalDate.of(2025, 12, 1),
                Parser.toDate("2025 December 01").getTime());
        assertEquals(LocalDateTime.of(LocalDateTime.now().getYear(), 1, 5, 0, 0),
                Parser.toDate("Jan 5 12AM").getTime());
        assertEquals(LocalDateTime.of(LocalDateTime.now().getYear(), 7, 20, 23, 59),
                Parser.toDate("Jul-20 23:59").getTime());
        assertEquals(LocalDate.of(1999, 1, 1), Parser.toDate("01/01 1999").getTime());
        assertEquals("foo bar baz", Parser.toDate("foo bar baz").getTime());
        assertEquals("13/13 2022", Parser.toDate("13/13 2022").getTime());
        assertEquals(LocalDateTime.of(LocalDateTime.now().getYear(), 9, 9, 9, 9),
                Parser.toDate("Sep 9 9:09").getTime());
        assertEquals("April 31 2023", Parser.toDate("April 31 2023").getTime());
        assertEquals(LocalDate.of(2000, 2, 29), Parser.toDate("2000 Feb 29").getTime());
        assertEquals("2100 Feb 29", Parser.toDate("2100 Feb 29").getTime());
    }

    @Test
    public void fourWordDateTests() {
        // existing
        assertEquals(LocalDateTime.of(2027, 10, 12, 3, 0), Parser.toDate("2027 Oct 12 3AM").getTime());
        assertEquals(LocalDateTime.of(2030, 1, 1, 0, 0), Parser.toDate("2030 Jan 01 12AM").getTime());
        assertEquals(LocalDateTime.of(1995, 12, 31, 23, 59), Parser.toDate("1995 Dec 31 23:59").getTime());
        assertEquals("abcd ef gh ij", Parser.toDate("abcd ef gh ij").getTime());
        assertEquals("9999 99 99 99", Parser.toDate("9999 99 99 99").getTime());
        assertEquals("year month day time", Parser.toDate("year month day time").getTime());
        assertEquals("2020 Apr 31 10AM", Parser.toDate("2020 Apr 31 10AM").getTime());
        assertEquals(LocalDateTime.of(600, 5, 4, 20, 20), Parser.toDate("2020 04 05 0600").getTime());
        assertEquals(LocalDateTime.of(0, 1, 1, 1, 0), Parser.toDate("00 Jan 01 1AM").getTime());
        assertEquals("2024 Feb 29 26:00", Parser.toDate("2024 Feb 29 26:00").getTime());
        assertEquals("1 2 3 4", Parser.toDate("1 2 3 4").getTime());
        assertEquals("!!!! !!!! !!!! !!!!", Parser.toDate("!!!! !!!! !!!! !!!!").getTime());
        assertEquals("2099 Dec 32 10AM", Parser.toDate("2099 Dec 32 10AM").getTime());
        assertEquals("2025 May 10 noon", Parser.toDate("2025 May 10 noon").getTime());
    }

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
