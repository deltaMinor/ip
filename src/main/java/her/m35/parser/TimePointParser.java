package her.m35.parser;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import her.m35.TimePoint;



/**
 * Handles parsing of user input into TimePoint objects specifically.
 * Contains methods to convert strings into TimePoint objects and checking validity of conversion.
 */
public class TimePointParser {
    private enum TimeParameter { YEAR, MONTH, DAY, TIME }

    private static final String[] VALID_TIME_FORMATS = {
        "D/M", "M/D",
        "D/M/Y", "M/D/Y", "Y/M/D", "Y/D/M", "T/D/M", "T/M/D", "D/M/T", "M/D/T",
        "T/D/M/Y", "T/M/D/Y", "T/Y/M/D", "T/Y/D/M", "D/M/Y/T", "M/D/Y/T", "Y/M/D/T", "Y/D/M/T",
    };

    private static final char[] VALID_SEPARATOR_WILDCARDS = {'/', ' ', '-', '\\'};

    /**
     * Converts a string into a TimePoint object.
     *
     * @param timeString String that represents time to be converted.
     * @return Time as a TimePoint object.
     */
    public static TimePoint toTimePoint(String timeString) {
        if (timeString == null || timeString.isEmpty()) {
            return null;
        }
        String timeStringCopy = timeString.trim().toUpperCase();
        timeStringCopy = timeStringCopy.replace("TODAY", dateToString(LocalDate.now()));
        timeStringCopy = timeStringCopy.replace("TDY", dateToString(LocalDate.now()));
        String tomorrowDateString = dateToString(LocalDate.now().plusDays(1));
        timeStringCopy = timeStringCopy.replace("TOMORROW", tomorrowDateString);
        timeStringCopy = timeStringCopy.replace("TMRW", tomorrowDateString);
        timeStringCopy = timeStringCopy.replace("TMR", tomorrowDateString);

        TimePoint result;
        for (String format : VALID_TIME_FORMATS) {
            result = toTimePoint(timeStringCopy, format);
            if (result != null) {
                return result;
            }
        }
        return new TimePoint(timeString);
    }

    /**
     * Converts a string into a TimePoint object with a given format.
     *
     * @param timeString String that represents time to be converted.
     * @param format Format for timeString to be parsed in.
     * @return Time as a TimePoint object.
     */
    private static TimePoint toTimePoint(String timeString, String format) {
        assert(timeString != null && !timeString.isEmpty());
        assert(format != null && !format.isEmpty());
        int day = -1;
        int month = -1;
        int year = -1;
        int time = -1;
        if (!format.contains("Y")) {
            year = LocalDate.now().getYear();
        }
        TimeParameter[] parameters = splitTimePointFormatString(format);
        assert(parameters != null);
        int parameterCount = parameters.length;
        String[] parameterStrings = new String[parameterCount];
        String remainder = timeString;
        for (int i = 0; i < parameterCount - 1; i++) {
            int separatorIndex = indexOfWildcard(remainder);
            if (separatorIndex == -1) {
                return null;
            }
            parameterStrings[i] = remainder.substring(0, separatorIndex);
            remainder = remainder.substring(separatorIndex + 1);
        }
        parameterStrings[parameterCount - 1] = remainder;
        for (int i = 0; i < parameterCount; i++) {
            switch (parameters[i]) {
            case YEAR:
                if (Parser.isInteger(parameterStrings[i]) && parameterStrings[i].length() == 4) {
                    year = Integer.parseInt(parameterStrings[i]);
                }
                if (year == -1) {
                    return null;
                }
                break;
            case MONTH:
                month = toMonth(parameterStrings[i]);
                if (month == -1) {
                    return null;
                }
                break;
            case DAY:
                if (Parser.isInteger(parameterStrings[i])) {
                    day = Integer.parseInt(parameterStrings[i]);
                }
                if (day == -1) {
                    return null;
                }
                break;
            case TIME:
                time = toHourMinuteTime(parameterStrings[i]);
                if (time == -1) {
                    return null;
                }
                break;
            default:
                return null;
            }
        }
        if (time == -1) {
            LocalDate date = tryCreateDate(day, month, year);
            if (date != null) {
                return new TimePoint(date);
            }
            return null;
        }
        LocalDateTime dateTime = tryCreateDateTime(time, day, month, year);
        if (dateTime != null) {
            return new TimePoint(dateTime);
        }
        return null;
    }

    /**
     * Helper function to find the index of the first wildcard time separator in regex "[/ \\-]"
     * @param string String to find wildcard separator symbol in.
     * @return Index of wildcard separator symbol/
     */
    private static int indexOfWildcard(String string) {
        int index = string.length() + 1;
        for (char c : VALID_SEPARATOR_WILDCARDS) {
            int indexOf = string.indexOf(c);
            if (indexOf < index && indexOf != -1) {
                index = indexOf;
            }
        }
        if (index < string.length()) {
            return index;
        }
        return -1;
    }

    /**
     * Converts a string into a TimeParameter array.
     *
     * @param format Format to convert into TimeParameter array.
     * @return format as a TimeParameter array.
     */
    private static TimeParameter[] splitTimePointFormatString(String format) {
        int length = format.length();
        assert(length % 2 == 1 && length >= 3 && length <= 7);
        for (int i = 1; i < length; i += 2) {
            assert(format.charAt(i) == '/');
        }
        int parameterCount = (format.length() + 1) / 2;
        TimeParameter[] parameters = new TimeParameter[parameterCount];
        for (int i = 0; i < parameterCount; i++) {
            switch (format.charAt(i * 2)) {
            case 'Y':
                parameters[i] = TimeParameter.YEAR;
                break;
            case 'M':
                parameters[i] = TimeParameter.MONTH;
                break;
            case 'D':
                parameters[i] = TimeParameter.DAY;
                break;
            case 'T':
                parameters[i] = TimeParameter.TIME;
                break;
            default:
                return null;
            }
        }
        return parameters;
    }

    /**
     * Helper function which converts a LocalDate to a string which can be parsed by the TimePointParser toTimePoint
     * method.
     *
     * @param date Date to be converted to a string.
     * @return String representation of the given date.
     */
    private static String dateToString(LocalDate date) {
        return String.format(
                "%d-%s-%d", date.getDayOfMonth(), TimePoint.MTHS[date.getMonthValue() - 1], date.getYear());
    }

    /**
     * Converts a string to a time value, in 24-hour format.
     *
     * @param timeString String to convert to a time value.
     * @return Converted integer value which represents the time, if valid, else -1.
     */
    public static int toHourMinuteTime(String timeString) {
        if (timeString == null) {
            return -1;
        }
        if (Parser.isInteger(timeString) && timeString.length() == 4) {
            int timeInt = Integer.parseInt(timeString);
            int hour = timeInt / 100;
            int minute = timeInt % 100;
            if (hour >= 0 && hour <= 23 && minute >= 0 && minute <= 59) {
                return hour * 100 + minute;
            } else {
                return -1;
            }
        }
        if (timeString.contains("AM")) {
            String trimmedTime = timeString.replace("AM", "");
            if (trimmedTime.contains(":")) {
                int twelveHourTime = toHourMinuteTime(trimmedTime);
                if (twelveHourTime < 100 || twelveHourTime >= 1300) {
                    return -1;
                }
                if (twelveHourTime > 1200) {
                    twelveHourTime -= 1200;
                }
                return twelveHourTime;
            }
            if (Parser.isInteger(trimmedTime)) {
                int hour = Integer.parseInt(trimmedTime);
                if (hour >= 1 && hour <= 11) {
                    return hour * 100;
                } else if (hour == 12) {
                    return 0;
                } else {
                    return -1;
                }
            }
        }
        if (timeString.contains("PM")) {
            String trimmedTime = timeString.replace("PM", "");
            if (trimmedTime.contains(":")) {
                int twelveHourTime = toHourMinuteTime(trimmedTime);
                if (twelveHourTime < 100 || twelveHourTime >= 1300) {
                    return -1;
                }
                if (twelveHourTime < 1200) {
                    twelveHourTime += 1200;
                }
                return twelveHourTime;
            }
            if (Parser.isInteger(trimmedTime)) {
                int hour = Integer.parseInt(trimmedTime);
                if (hour >= 1 && hour <= 11) {
                    return (hour + 12) * 100;
                } else if (hour == 12) {
                    return 1200;
                } else {
                    return -1;
                }
            }
        }
        if (timeString.contains(":")) {
            String[] tokens = timeString.split(":");
            if (Parser.isIntegerArray(tokens) && tokens.length == 2) {
                int hour = Integer.parseInt(tokens[0]);
                int minute = Integer.parseInt(tokens[1]);
                if (hour >= 0 && hour <= 23 && minute >= 0 && minute <= 59) {
                    return hour * 100 + minute;
                }
            }
        }
        return -1;
    }

    /**
     * Converts a string to its month value.
     *
     * @param month String which potentially represents a month.
     * @return The month represented in integer value if it is valid, else -1.
     */
    public static int toMonth(String month) {
        if (month == null) {
            return -1;
        }
        if (Parser.isInteger(month)) {
            int monthInt = Integer.parseInt(month);
            if (monthInt >= 1 && monthInt <= 12) {
                return monthInt;
            } else {
                return -1;
            }
        }
        for (int i = 1; i <= 12; i++) {
            if (TimePoint.MONTHS[i - 1].equals(month) || TimePoint.MTHS[i - 1].equals(month)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Attempts creating a LocalDate with the given day, month and year values.
     * Returns null if the given value is invalid.
     * @param day Integer value of day of the month.
     * @param month Integer value of month of the year.
     * @param year Integer value of the year.
     * @return A LocalDate corresponding to the given parameters if they form a valid date, else return null.
     */
    private static LocalDate tryCreateDate(int day, int month, int year) {
        try {
            return LocalDate.of(year, month, day);
        } catch (DateTimeException e) {
            return null;
        }
    }

    /**
     * Attempts creating a LocalDateTime with the given time, day, month and year values.
     * Returns null if the given value is invalid.
     * @param time Integer value of time of the day, in HH*100 + MM format.
     * @param day Integer value of day of the month.
     * @param month Integer value of month of the year.
     * @param year Integer value of the year.
     * @return A LocalDate corresponding to the given parameters if they form a valid date, else return null.
     */
    private static LocalDateTime tryCreateDateTime(int time, int day, int month, int year) {
        try {
            return LocalDateTime.of(year, month, day, time / 100, time % 100);
        } catch (DateTimeException e) {
            return null;
        }
    }
}
