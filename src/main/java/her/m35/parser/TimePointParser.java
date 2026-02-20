package her.m35.parser;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

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
     * @param timeString String that represents time to be converted.
     * @return Time as a TimePoint object.
     */
    public static TimePoint toTimePoint(String timeString) {
        if (timeString == null || timeString.isEmpty()) {
            return null;
        }
        String timeStringCopy = parseFlexibleTime(timeString);

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
     * @param timeString String that represents time to be converted.
     * @param format Format for timeString to be parsed in.
     * @return Time as a TimePoint object.
     */
    private static TimePoint toTimePoint(String timeString, String format) {
        assert(timeString != null && !timeString.isEmpty());
        assert(format != null && !format.isEmpty());
        TimeParameter[] parameters = splitTimePointFormatString(format);
        assert(parameters != null);
        int parameterCount = parameters.length;
        String[] parameterStrings = separateTimeString(timeString, parameterCount);
        if (parameterStrings == null) {
            return null;
        }
        TimeParametersBundle parsedParameters = parseParameters(parameterStrings, parameters);
        if (parsedParameters == null) {
            return null;
        }
        if (!format.contains("Y")) {
            parsedParameters.setYear(LocalDate.now().getYear());
        }
        if (parsedParameters.getTime() == -1) {
            LocalDate date = tryCreateDate(parsedParameters);
            if (date != null) {
                return new TimePoint(date);
            }
            return null;
        }
        LocalDateTime dateTime = tryCreateDateTime(parsedParameters);
        if (dateTime != null) {
            return new TimePoint(dateTime);
        }
        return null;
    }

    private static String parseFlexibleTime(String timeString) {
        String timeStringCopy = timeString.trim().toUpperCase();
        timeStringCopy = Parser.replaceStringWithArraySelection(
                timeStringCopy, new String[] {"TODAY", "TDY"}, dateToString(LocalDate.now()));
        String tomorrowDateString = dateToString(LocalDate.now().plusDays(1));
        timeStringCopy = Parser.replaceStringWithArraySelection(
                timeStringCopy, new String[] {"TOMORROW", "TMRW", "TMR"}, tomorrowDateString);
        String nextWeekDateString = dateToString(LocalDate.now().plusDays(7));
        timeStringCopy = Parser.replaceStringWithArraySelection(
                timeStringCopy, new String[] {"NEXT WEEK"}, nextWeekDateString);

        String mondayString = dateToString(LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)));
        timeStringCopy = Parser.replaceStringWithArraySelection(
                timeStringCopy, new String[] {"MONDAY", "MON"}, mondayString);
        String tuesdayString = dateToString(LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.TUESDAY)));
        timeStringCopy = Parser.replaceStringWithArraySelection(
                timeStringCopy, new String[] {"TUESDAY", "TUES", "TUE"}, tuesdayString);
        String wednesdayString = dateToString(LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY)));
        timeStringCopy = Parser.replaceStringWithArraySelection(
                timeStringCopy, new String[] {"WEDNESDAY", "WED"}, wednesdayString);
        String thursdayString = dateToString(LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.THURSDAY)));
        timeStringCopy = Parser.replaceStringWithArraySelection(
                timeStringCopy, new String[] {"THURSDAY", "THURS", "THUR", "THU"}, thursdayString);
        String fridayString = dateToString(LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.FRIDAY)));
        timeStringCopy = Parser.replaceStringWithArraySelection(
                timeStringCopy, new String[] {"FRIDAY", "FRI"}, fridayString);
        String saturdayString = dateToString(LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SATURDAY)));
        timeStringCopy = Parser.replaceStringWithArraySelection(
                timeStringCopy, new String[] {"SATURDAY", "SAT"}, saturdayString);
        String sundayString = dateToString(LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY)));
        timeStringCopy = Parser.replaceStringWithArraySelection(
                timeStringCopy, new String[] {"SUNDAY", "SUN"}, sundayString);
        return timeStringCopy;
    }

    private static TimeParametersBundle parseParameters(String[] parameterStrings, TimeParameter[] parameters) {
        assert(parameterStrings.length == parameters.length);
        int parameterCount = parameters.length;
        TimeParametersBundle parsedParameters = new TimeParametersBundle(-1, -1, -1, -1);
        for (int i = 0; i < parameterCount; i++) {
            switch (parameters[i]) {
            case YEAR:
                if (Parser.isInteger(parameterStrings[i]) && parameterStrings[i].length() == 4) {
                    parsedParameters.setYear(Integer.parseInt(parameterStrings[i]));
                }
                if (parsedParameters.getYear() == -1) {
                    return null;
                }
                break;
            case MONTH:
                parsedParameters.setMonth(toMonth(parameterStrings[i]));
                if (parsedParameters.getMonth() == -1) {
                    return null;
                }
                break;
            case DAY:
                if (Parser.isInteger(parameterStrings[i])) {
                    parsedParameters.setDay(Integer.parseInt(parameterStrings[i]));
                }
                if (parsedParameters.getDay() == -1) {
                    return null;
                }
                break;
            case TIME:
                parsedParameters.setTime(toHourMinuteTime(parameterStrings[i]));
                if (parsedParameters.getTime() == -1) {
                    return null;
                }
                break;
            default:
                return null;
            }
        }
        return parsedParameters;
    }

    private static class TimeParametersBundle {
        private int day;
        private int month;
        private int year;
        private int time;

        public TimeParametersBundle(int day, int month, int year, int time) {
            this.day = day;
            this.month = month;
            this.year = year;
            this.time = time;
        }

        public int getDay() {
            return day;
        }
        public int getMonth() {
            return month;
        }
        public int getYear() {
            return year;
        }
        public int getTime() {
            return time;
        }
        public void setDay(int day) {
            this.day = day;
        }
        public void setMonth(int month) {
            this.month = month;
        }
        public void setYear(int year) {
            this.year = year;
        }
        public void setTime(int time) {
            this.time = time;
        }
    }

    private static String[] separateTimeString(String timeString, int count) {
        String[] parameterStrings = new String[count];
        String remainder = timeString;
        for (int i = 0; i < count - 1; i++) {
            int separatorIndex = indexOfWildcard(remainder);
            if (separatorIndex == -1) {
                return null;
            }
            parameterStrings[i] = remainder.substring(0, separatorIndex);
            remainder = remainder.substring(separatorIndex + 1);
        }
        parameterStrings[count - 1] = remainder;
        return parameterStrings;
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
     * @param date Date to be converted to a string.
     * @return String representation of the given date.
     */
    private static String dateToString(LocalDate date) {
        return String.format(
                "%d-%s-%d", date.getDayOfMonth(), TimePoint.MTHS[date.getMonthValue() - 1], date.getYear());
    }

    /**
     * Converts a string to a time value, in 24-hour format.
     * @param timeString String to convert to a time value.
     * @return Converted integer value which represents the time, if valid, else -1.
     */
    public static int toHourMinuteTime(String timeString) {
        if (timeString == null) {
            return -1;
        }
        if (Parser.isInteger(timeString) && timeString.length() == 4) {
            return digitsToHourMinute(timeString);
        }
        if (timeString.contains("AM")) {
            return amToHourMinute(timeString);
        }
        if (timeString.contains("PM")) {
            return pmToHourMinute(timeString);
        }
        if (timeString.contains(":")) {
            return colonToHourMinute(timeString);
        }
        return -1;
    }

    private static int digitsToHourMinute(String timeString) {
        int timeInt = Integer.parseInt(timeString);
        int hour = timeInt / 100;
        int minute = timeInt % 100;
        if (hour >= 0 && hour <= 23 && minute >= 0 && minute <= 59) {
            return hour * 100 + minute;
        } else {
            return -1;
        }
    }

    private static int amToHourMinute(String timeString) {
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
        return -1;
    }

    private static int pmToHourMinute(String timeString) {
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
        return -1;
    }

    private static int colonToHourMinute(String timeString) {
        String[] tokens = timeString.split(":");
        if (Parser.isIntegerArray(tokens) && tokens.length == 2) {
            int hour = Integer.parseInt(tokens[0]);
            int minute = Integer.parseInt(tokens[1]);
            if (hour >= 0 && hour <= 23 && minute >= 0 && minute <= 59) {
                return hour * 100 + minute;
            }
        }
        return -1;
    }

    /**
     * Converts a string to its month value.
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

    private static LocalDate tryCreateDate(TimeParametersBundle bundle) {
        return tryCreateDate(bundle.getDay(), bundle.getMonth(), bundle.getYear());
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

    private static LocalDateTime tryCreateDateTime(TimeParametersBundle bundle) {
        return tryCreateDateTime(bundle.getTime(), bundle.getDay(), bundle.getMonth(), bundle.getYear());
    }
}
