import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Custom time object that can represent a point in time using either string, LocalDate or LocalDateTime
 */

public class TimePoint {

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
    public static final String[] MTHS = {
            "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
    public static final String[] Mths = {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    /** Point in time being stored by the object, only one of these variables can have a non-null value. */
    private final String timeString;
    private final LocalDate localDate;
    private final LocalDateTime localDateTime;

    /** The format in which the time is stored. */
    private final Format format;

    public TimePoint(String time) {
        this.timeString = time;
        this.localDate = null;
        this.localDateTime = null;
        this.format = Format.STRING;
    }

    public TimePoint(LocalDate localDate) {
        this.timeString = null;
        this.localDate = localDate;
        this.localDateTime = null;
        this.format = Format.LOCAL_DATE;
    }

    public TimePoint(LocalDateTime localDateTime) {
        this.timeString = null;
        this.localDate = null;
        this.localDateTime = localDateTime;
        this.format = Format.LOCAL_DATE_TIME;
    }

    public Format getFormat() {
        return this.format;
    }

    public Object getTime() {
        switch (this.format) {
            case STRING:
                return this.timeString;
            case LOCAL_DATE:
                return this.localDate;
            case LOCAL_DATE_TIME:
                return this.localDateTime;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        switch (format) {
            case STRING:
                return timeString;
            case LOCAL_DATE:
                return Mths[localDate.getMonthValue() - 1]
                        + " " + localDate.getDayOfMonth()
                        + " " + localDate.getYear();
            case LOCAL_DATE_TIME:
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
