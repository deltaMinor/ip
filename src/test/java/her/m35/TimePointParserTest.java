package her.m35;

import static her.m35.parser.TimePointParser.toTimePoint;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;


public class TimePointParserTest {
    @Test
    public void twoWordDateTests() {
        // existing
        assertEquals(LocalDate.of(LocalDate.now().getYear(), 10, 12), toTimePoint("Oct 12").getTime());
        assertEquals(LocalDate.of(LocalDate.now().getYear(), 12, 12), toTimePoint("12/12").getTime());
        assertEquals(LocalDate.of(LocalDate.now().getYear(), 12, 13), toTimePoint("13-12").getTime());
        assertEquals(LocalDate.of(LocalDate.now().getYear(), 12, 13), toTimePoint("12/13").getTime());
        assertEquals(LocalDate.of(LocalDate.now().getYear(), 4, 23), toTimePoint("aPR-23").getTime());
        assertEquals(LocalDate.of(LocalDate.now().getYear(), 12, 1), toTimePoint("December 01").getTime());
        assertEquals("ABC DEF", toTimePoint("ABC DEF").getTime());
        assertEquals("Feb-30", toTimePoint("Feb-30").getTime());
        assertEquals(LocalDate.of(LocalDate.now().getYear(), 1, 1), toTimePoint("Jan 1").getTime());
        assertEquals(LocalDate.of(LocalDate.now().getYear(), 11, 9), toTimePoint("NOV-09").getTime());
        assertEquals(LocalDate.of(LocalDate.now().getYear(), 6, 15), toTimePoint("06/15").getTime());
        assertEquals(LocalDate.of(LocalDate.now().getYear(), 8, 31), toTimePoint("31-08").getTime());
        assertEquals("hello world", toTimePoint("hello world").getTime());
        assertEquals("99 99", toTimePoint("99 99").getTime());
        assertEquals("March-99", toTimePoint("March-99").getTime());
        assertEquals("00:00", toTimePoint("00:00").getTime());
    }

    @Test
    public void threeWordDateTests() {
        // existing
        assertEquals(LocalDateTime.of(LocalDateTime.now().getYear(), 10, 12, 10, 30),
                toTimePoint("Oct 12 10:30").getTime());
        assertEquals(LocalDate.of(2020, 12, 12),
                toTimePoint("12/12 2020").getTime());
        assertEquals("12 34 5678",
                toTimePoint("12 34 5678").getTime());
        assertEquals(LocalDateTime.of(LocalDateTime.now().getYear(), 12, 13, 18, 0),
                toTimePoint("12/13 6PM").getTime());
        assertEquals(LocalDateTime.of(LocalDateTime.now().getYear(), 4, 23, 12, 34),
                toTimePoint("aPR-23/12:34").getTime());
        assertEquals(LocalDate.of(2025, 12, 1),
                toTimePoint("2025 December 01").getTime());
        assertEquals(LocalDateTime.of(LocalDateTime.now().getYear(), 1, 5, 0, 0),
                toTimePoint("Jan 5 12AM").getTime());
        assertEquals(LocalDateTime.of(LocalDateTime.now().getYear(), 7, 20, 23, 59),
                toTimePoint("Jul-20 23:59").getTime());
        assertEquals(LocalDate.of(1999, 1, 1), toTimePoint("01/01 1999").getTime());
        assertEquals("foo bar baz", toTimePoint("foo bar baz").getTime());
        assertEquals("13/13 2022", toTimePoint("13/13 2022").getTime());
        assertEquals(LocalDateTime.of(LocalDateTime.now().getYear(), 9, 9, 9, 9),
                toTimePoint("Sep 9 9:09").getTime());
        assertEquals("April 31 2023", toTimePoint("April 31 2023").getTime());
        assertEquals(LocalDate.of(2000, 2, 29), toTimePoint("2000 Feb 29").getTime());
        assertEquals("2100 Feb 29", toTimePoint("2100 Feb 29").getTime());
    }

    @Test
    public void fourWordDateTests() {
        // existing
        assertEquals(LocalDateTime.of(2027, 10, 12, 3, 0), toTimePoint("2027 Oct 12 3AM").getTime());
        assertEquals(LocalDateTime.of(2030, 1, 1, 0, 0), toTimePoint("2030 Jan 01 12AM").getTime());
        assertEquals(LocalDateTime.of(1995, 12, 31, 23, 59), toTimePoint("1995 Dec 31 23:59").getTime());
        assertEquals("abcd ef gh ij", toTimePoint("abcd ef gh ij").getTime());
        assertEquals("9999 99 99 99", toTimePoint("9999 99 99 99").getTime());
        assertEquals("year month day time", toTimePoint("year month day time").getTime());
        assertEquals("2020 Apr 31 10AM", toTimePoint("2020 Apr 31 10AM").getTime());
        assertEquals(LocalDateTime.of(600, 5, 4, 20, 20), toTimePoint("2020 04 05 0600").getTime());
        assertEquals("2024 Feb 29 26:00", toTimePoint("2024 Feb 29 26:00").getTime());
        assertEquals("1 2 3 4", toTimePoint("1 2 3 4").getTime());
        assertEquals(LocalDateTime.of(2016, 10, 10, 20, 30), toTimePoint("2016 oct 10 8:30PM").getTime());
        assertEquals("2025 May 10 noon", toTimePoint("2025 May 10 noon").getTime());
    }
}
