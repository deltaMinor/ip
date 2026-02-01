package her.m35;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class TimePointTest {
    @Test
    public void isSameDayAsTests() {
        TimePoint t1 = new TimePoint(LocalDate.of(2024, 5, 10));
        TimePoint t2 = new TimePoint(LocalDate.of(2024, 5, 10));
        assertTrue(t1.isSameDayAs(t2));
        TimePoint t3 = new TimePoint(LocalDate.of(2024, 5, 10));
        TimePoint t4 = new TimePoint(LocalDate.of(2024, 5, 11));
        assertFalse(t3.isSameDayAs(t4));
        TimePoint t5 = new TimePoint(LocalDate.of(2025, 10, 10));
        TimePoint t6 = new TimePoint(LocalDateTime.of(2025, 10, 10, 12, 34));
        assertTrue(t5.isSameDayAs(t6));
        TimePoint t7 = new TimePoint(LocalDateTime.of(2026, 10, 10, 10, 0));
        TimePoint t8 = new TimePoint(LocalDateTime.of(2026, 10, 11, 0, 0));
        assertFalse(t7.isSameDayAs(t8));
        TimePoint t9 = new TimePoint("2024 Oct 10");
        TimePoint t10 = new TimePoint(LocalDate.of(2024, 10, 10));
        assertFalse(t9.isSameDayAs(t10));
    }

    @Test
    public void isAfterTests() {
        TimePoint t1 = new TimePoint(LocalDate.of(2000, 1, 2));
        TimePoint t2 = new TimePoint(LocalDate.of(2000, 1, 1));
        assertTrue(t1.isAfter(t2));
        TimePoint t3 = new TimePoint(LocalDate.of(2000, 1, 1));
        TimePoint t4 = new TimePoint(LocalDate.of(2000, 1, 1));
        assertFalse(t3.isAfter(t4));
        TimePoint t5 = new TimePoint(LocalDateTime.of(2000, 1, 2, 0, 0));
        TimePoint t6 = new TimePoint(LocalDate.of(2000, 1, 1));
        assertTrue(t5.isAfter(t6));
        TimePoint t7 = new TimePoint(LocalDate.of(2024, 5, 2));
        TimePoint t8 = new TimePoint(LocalDateTime.of(2024, 5, 1, 23, 34));
        assertTrue(t7.isAfter(t8));
        TimePoint t9 = new TimePoint("A");
        TimePoint t10 = new TimePoint("B");
        assertFalse(t9.isAfter(t10));
    }


    @Test
    public void isBeforeTests() {
        TimePoint t1 = new TimePoint(LocalDate.of(2000, 5, 1));
        TimePoint t2 = new TimePoint(LocalDate.of(2000, 6, 1));
        assertTrue(t1.isBefore(t2));
        TimePoint t3 = new TimePoint(LocalDate.of(2000, 6, 1));
        TimePoint t4 = new TimePoint(LocalDate.of(2000, 5, 1));
        assertFalse(t3.isBefore(t4));
        TimePoint t5 = new TimePoint(LocalDateTime.of(2000, 5, 1, 0, 0));
        TimePoint t6 = new TimePoint(LocalDateTime.of(2000, 5, 1, 23, 59));
        assertTrue(t5.isBefore(t6));
        TimePoint t7 = new TimePoint(LocalDate.of(2000, 5, 1));
        TimePoint t8 = new TimePoint(LocalDateTime.of(2000, 5, 2, 0, 0));
        assertTrue(t7.isBefore(t8));
        TimePoint t9 = new TimePoint("abc");
        TimePoint t10 = new TimePoint("def");
        assertFalse(t9.isBefore(t10));
    }

    @Test
    public void equalsTests() {
        TimePoint t1 = new TimePoint(LocalDate.of(2024, 1, 1));
        assertTrue(t1.equals(t1));
        TimePoint t2 = new TimePoint(LocalDate.of(2024, 1, 1));
        assertFalse(t2.equals(null));
        TimePoint t3 = new TimePoint(LocalDate.of(2024, 1, 1));
        assertFalse(t3.equals("2024-01-01"));
        TimePoint t4 = new TimePoint(LocalDate.of(2024, 1, 1));
        TimePoint t5 = new TimePoint(LocalDate.of(2024, 1, 1));
        assertTrue(t4.equals(t5));
        TimePoint t6 = new TimePoint(LocalDate.of(2024, 1, 1));
        TimePoint t7 = new TimePoint(LocalDate.of(2024, 1, 2));
        assertFalse(t6.equals(t7));
        TimePoint t8 = new TimePoint(LocalDateTime.of(2024, 1, 1, 10, 0));
        TimePoint t9 = new TimePoint(LocalDateTime.of(2024, 1, 1, 10, 0));
        assertTrue(t8.equals(t9));
        TimePoint t10 = new TimePoint(LocalDate.of(2024, 1, 1));
        TimePoint t11 = new TimePoint(LocalDateTime.of(2024, 1, 1, 0, 0));
        assertFalse(t10.equals(t11));
        TimePoint t12 = new TimePoint("Oct 10");
        TimePoint t13 = new TimePoint("Oct 10");
        assertTrue(t12.equals(t13));
        TimePoint t14 = new TimePoint("Oct 10");
        TimePoint t15 = new TimePoint("Oct 11");
        assertFalse(t14.equals(t15));
    }
}
