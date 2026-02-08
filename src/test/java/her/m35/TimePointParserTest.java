package her.m35;

import static her.m35.parser.TimePointParser.toDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;


public class TimePointParserTest {
    @Test
    public void twoWordDateTests() {
        // existing
        assertEquals(LocalDate.of(LocalDate.now().getYear(), 10, 12), toDate("Oct 12").getTime());
        assertEquals(LocalDate.of(LocalDate.now().getYear(), 12, 12), toDate("12/12").getTime());
        assertEquals(LocalDate.of(LocalDate.now().getYear(), 12, 13), toDate("13-12").getTime());
        assertEquals(LocalDate.of(LocalDate.now().getYear(), 12, 13), toDate("12/13").getTime());
        assertEquals(LocalDate.of(LocalDate.now().getYear(), 4, 23), toDate("aPR-23").getTime());
        assertEquals(LocalDate.of(LocalDate.now().getYear(), 12, 1), toDate("December 01").getTime());
        assertEquals("ABC DEF", toDate("ABC DEF").getTime());
        assertEquals("Feb-30", toDate("Feb-30").getTime());
        assertEquals(LocalDate.of(LocalDate.now().getYear(), 1, 1), toDate("Jan 1").getTime());
        assertEquals(LocalDate.of(LocalDate.now().getYear(), 11, 9), toDate("NOV-09").getTime());
        assertEquals(LocalDate.of(LocalDate.now().getYear(), 6, 15), toDate("06/15").getTime());
        assertEquals(LocalDate.of(LocalDate.now().getYear(), 8, 31), toDate("31-08").getTime());
        assertEquals("hello world", toDate("hello world").getTime());
        assertEquals("99 99", toDate("99 99").getTime());
        assertEquals("March-99", toDate("March-99").getTime());
        assertEquals("00:00", toDate("00:00").getTime());
    }

    @Test
    public void threeWordDateTests() {
        // existing
        assertEquals(LocalDateTime.of(LocalDateTime.now().getYear(), 10, 12, 10, 30),
                toDate("Oct 12 10:30").getTime());
        assertEquals(LocalDate.of(2020, 12, 12),
                toDate("12/12 2020").getTime());
        assertEquals("12 34 5678",
                toDate("12 34 5678").getTime());
        assertEquals(LocalDateTime.of(LocalDateTime.now().getYear(), 12, 13, 18, 0),
                toDate("12/13 6PM").getTime());
        assertEquals(LocalDateTime.of(LocalDateTime.now().getYear(), 4, 23, 12, 34),
                toDate("aPR-23/12:34").getTime());
        assertEquals(LocalDate.of(2025, 12, 1),
                toDate("2025 December 01").getTime());
        assertEquals(LocalDateTime.of(LocalDateTime.now().getYear(), 1, 5, 0, 0),
                toDate("Jan 5 12AM").getTime());
        assertEquals(LocalDateTime.of(LocalDateTime.now().getYear(), 7, 20, 23, 59),
                toDate("Jul-20 23:59").getTime());
        assertEquals(LocalDate.of(1999, 1, 1), toDate("01/01 1999").getTime());
        assertEquals("foo bar baz", toDate("foo bar baz").getTime());
        assertEquals("13/13 2022", toDate("13/13 2022").getTime());
        assertEquals(LocalDateTime.of(LocalDateTime.now().getYear(), 9, 9, 9, 9),
                toDate("Sep 9 9:09").getTime());
        assertEquals("April 31 2023", toDate("April 31 2023").getTime());
        assertEquals(LocalDate.of(2000, 2, 29), toDate("2000 Feb 29").getTime());
        assertEquals("2100 Feb 29", toDate("2100 Feb 29").getTime());
    }

    @Test
    public void fourWordDateTests() {
        // existing
        assertEquals(LocalDateTime.of(2027, 10, 12, 3, 0), toDate("2027 Oct 12 3AM").getTime());
        assertEquals(LocalDateTime.of(2030, 1, 1, 0, 0), toDate("2030 Jan 01 12AM").getTime());
        assertEquals(LocalDateTime.of(1995, 12, 31, 23, 59), toDate("1995 Dec 31 23:59").getTime());
        assertEquals("abcd ef gh ij", toDate("abcd ef gh ij").getTime());
        assertEquals("9999 99 99 99", toDate("9999 99 99 99").getTime());
        assertEquals("year month day time", toDate("year month day time").getTime());
        assertEquals("2020 Apr 31 10AM", toDate("2020 Apr 31 10AM").getTime());
        assertEquals(LocalDateTime.of(600, 5, 4, 20, 20), toDate("2020 04 05 0600").getTime());
        assertEquals(LocalDateTime.of(0, 1, 1, 1, 0), toDate("00 Jan 01 1AM").getTime());
        assertEquals("2024 Feb 29 26:00", toDate("2024 Feb 29 26:00").getTime());
        assertEquals("1 2 3 4", toDate("1 2 3 4").getTime());
        assertEquals(LocalDateTime.of(2016, 10, 10, 20, 30), toDate("2016 oct 10 8:30PM").getTime());
        assertEquals("2025 May 10 noon", toDate("2025 May 10 noon").getTime());
    }
}
