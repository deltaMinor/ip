package her.m35;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Custom time object that can represent a point in time using either string, LocalDate or LocalDateTime
 */

public class TimePoint {

    /** Enums to indicate the class in which the time is stored in this TimePoint. */
    public enum Format {
        STRING,
        LOCAL_DATE,
        LOCAL_DATE_TIME
    }

    /** Strings for months of the year. */
    public static final String[] MONTHS = {
        "JANUARY",
        "FEBRUARY",
        "MARCH",
        "APRIL",
        "MAY",
        "JUNE",
        "JULY",
        "AUGUST",
        "SEPTEMBER",
        "OCTOBER",
        "NOVEMBER",
        "DECEMBER"
    };

    /** Strings for abbreviations of months of the year in full-uppercase. */
    public static final String[] MTHS = {
        "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

    /** Strings for abbreviations of months of the year for toString() output. */
    private static final String[] Mths = {
        "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    /** Point in time being stored by the object, only one of these variables can have a non-null value. */
    private final String timeString;
    private final LocalDate localDate;
    private final LocalDateTime localDateTime;

    /** The format in which the time is stored. */
    private final Format format;

    /**
     * Creates a new TimePoint with the time in string format.
     *
     * @param time String that represents a time.
     */
    public TimePoint(String time) {
        this.timeString = time;
        this.localDate = null;
        this.localDateTime = null;
        this.format = Format.STRING;
    }

    /**
     * Creates a new TimePoint with the time in LocalDate format.
     *
     * @param localDate LocalDate that represents a time.
     */
    public TimePoint(LocalDate localDate) {
        this.timeString = null;
        this.localDate = localDate;
        this.localDateTime = null;
        this.format = Format.LOCAL_DATE;
    }

    /**
     * Creates a new TimePoint with the time in LocalDateTime format.
     *
     * @param localDateTime LocalDateTime that represents a time.
     */
    public TimePoint(LocalDateTime localDateTime) {
        this.timeString = null;
        this.localDate = null;
        this.localDateTime = localDateTime;
        this.format = Format.LOCAL_DATE_TIME;
    }

    public Format getFormat() {
        return this.format;
    }

    /**
     * Returns the time this TimePoint contains.
     *
     * @return The time this TimePoint contains that is not null.
     */
    public Object getTime() {
        switch (this.format) {
        case STRING:
            assert this.timeString != null;
            return this.timeString;
        case LOCAL_DATE:
            assert this.localDate != null;
            return this.localDate;
        case LOCAL_DATE_TIME:
            assert this.localDateTime != null;
            return this.localDateTime;
        default:
            return null;
        }
    }

    /**
     * Returns the day of the month this TimePoint contains, if it is available.
     *
     * @return Integer that represents the day of the month if it is available, else -1.
     */
    public int getDayOfMonth() {
        switch (this.format) {
        case LOCAL_DATE:
            assert this.localDate != null;
            return this.localDate.getDayOfMonth();
        case LOCAL_DATE_TIME:
            assert this.localDateTime != null;
            return this.localDateTime.toLocalDate().getDayOfMonth();
        default:
            return -1;
        }
    }

    /**
     * Returns the month this TimePoint contains, if it is available.
     *
     * @return Integer that represents the month if it is available, else -1.
     */
    public int getMonth() {
        switch (this.format) {
        case LOCAL_DATE:
            assert this.localDate != null;
            return this.localDate.getMonthValue();
        case LOCAL_DATE_TIME:
            assert this.localDateTime != null;
            return this.localDateTime.toLocalDate().getMonthValue();
        default:
            return -1;
        }
    }

    /**
     * Returns the year this TimePoint contains, if it is available.
     *
     * @return Integer that represents the year if it is available, else -1.
     */
    public int getYear() {
        switch (this.format) {
        case LOCAL_DATE:
            assert this.localDate != null;
            return this.localDate.getYear();
        case LOCAL_DATE_TIME:
            assert this.localDateTime != null;
            return this.localDateTime.toLocalDate().getYear();
        default:
            return -1;
        }
    }

    /**
     * Compares against another TimePoint if they are on the same day.
     * Both TimePoints being compared cannot contain their date as a string.
     *
     * @param other Other TimePoint to compare against.
     * @return True only if both TimePoints are on the same day, given they are not stored as a string.
     */
    public boolean isSameDayAs(TimePoint other) {
        LocalDate thisLocalDate;
        switch (this.format) {
        case LOCAL_DATE:
            assert this.localDate != null;
            thisLocalDate = this.localDate;
            break;
        case LOCAL_DATE_TIME:
            assert this.localDateTime != null;
            thisLocalDate = this.localDateTime.toLocalDate();
            break;
        default:
            return false;
        }
        LocalDate otherLocalDate;
        switch (other.format) {
        case LOCAL_DATE:
            assert other.localDate != null;
            otherLocalDate = (LocalDate) other.getTime();
            break;
        case LOCAL_DATE_TIME:
            assert other.localDateTime != null;
            otherLocalDate = ((LocalDateTime) other.getTime()).toLocalDate();
            break;
        default:
            return false;
        }
        return thisLocalDate.isEqual(otherLocalDate);
    }

    /**
     * Compares against another TimePoint to check if this TimePoint is after it.
     * Both TimePoints being compared cannot contain their date as a string.
     *
     * @param other Other TimePoint to compare against
     * @return True only if this TimePoint is after other, given they are not stored as a string.
     */
    public boolean isAfter(TimePoint other) {
        if (this.format == Format.STRING && other.getFormat() == Format.STRING) {
            return false;
        }
        if (this.format == Format.LOCAL_DATE) {
            assert this.localDate != null;
            if (other.getFormat() == Format.LOCAL_DATE) {
                assert other.localDate != null;
                return this.localDate.isAfter((LocalDate) other.getTime());
            } else {
                assert other.localDateTime != null;
                return this.localDate.isAfter(((LocalDateTime) other.getTime()).toLocalDate());
            }
        } else {
            assert this.localDateTime != null;
            if (other.getFormat() == Format.LOCAL_DATE) {
                assert other.localDate != null;
                return this.localDateTime.toLocalDate().isAfter((LocalDate) other.getTime());
            } else {
                assert other.localDateTime != null;
                return this.localDateTime.isAfter((LocalDateTime) other.getTime());
            }
        }
    }

    /**
     * Compares against another TimePoint to check if this TimePoint is before it.
     * Both TimePoints being compared cannot contain their date as a string.
     *
     * @param other Other TimePoint to compare against
     * @return True only if this TimePoint is before other, given they are not stored as a string.
     */
    public boolean isBefore(TimePoint other) {
        if (this.format == Format.STRING && other.getFormat() == Format.STRING) {
            return false;
        }
        if (this.format == Format.LOCAL_DATE) {
            assert this.localDate != null;
            if (other.getFormat() == Format.LOCAL_DATE) {
                assert other.localDate != null;
                return this.localDate.isBefore((LocalDate) other.getTime());
            } else {
                assert other.localDateTime != null;
                return this.localDate.isBefore(((LocalDateTime) other.getTime()).toLocalDate());
            }
        } else {
            assert this.localDateTime != null;
            if (other.getFormat() == Format.LOCAL_DATE) {
                assert other.localDate != null;
                return this.localDateTime.toLocalDate().isBefore((LocalDate) other.getTime());
            } else {
                assert other.localDateTime != null;
                return this.localDateTime.isBefore((LocalDateTime) other.getTime());
            }
        }
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        TimePoint otherTimePoint = (TimePoint) other;
        if (this.format != otherTimePoint.format) {
            return false;
        }
        switch (this.format) {
        case STRING:
            assert this.timeString != null;
            return this.timeString.equals(otherTimePoint.timeString);
        case LOCAL_DATE:
            assert this.localDate != null;
            return this.localDate.equals(otherTimePoint.localDate);
        case LOCAL_DATE_TIME:
            assert this.localDateTime != null;
            return this.localDateTime.equals(otherTimePoint.localDateTime);
        default:
            return false;
        }
    }

    @Override
    public String toString() {
        switch (format) {
        case STRING:
            return timeString;
        case LOCAL_DATE:
            assert this.localDate != null;
            return Mths[localDate.getMonthValue() - 1]
                    + " " + localDate.getDayOfMonth()
                    + " " + localDate.getYear();
        case LOCAL_DATE_TIME:
            assert this.localDateTime != null;
            return localDateTime.getHour()
                    + ":" + (localDateTime.getMinute() < 10 ? "0" : "") + localDateTime.getMinute()
                    + " " + Mths[localDateTime.getMonthValue() - 1]
                    + " " + localDateTime.getDayOfMonth()
                    + " " + localDateTime.getYear();
        default:
            return null;
        }
    }
}
