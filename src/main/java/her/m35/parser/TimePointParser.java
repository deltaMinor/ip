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
    /**
     * Converts a string into a TimePoint object.
     *
     * @param timeString String that represents time to be converted.
     * @return Time as a TimePoint object.
     */
    public static TimePoint toDate(String timeString) {
        if (timeString == null) {
            return null;
        }

        String timeStringCopy = timeString.trim().toUpperCase();
        timeStringCopy = timeStringCopy.replace("TODAY", dateToString(LocalDate.now()));
        timeStringCopy = timeStringCopy.replace("TDY", dateToString(LocalDate.now()));
        String tomorrowDateString = dateToString(LocalDate.now().plusDays(1));
        timeStringCopy = timeStringCopy.replace("TOMORROW", tomorrowDateString);
        timeStringCopy = timeStringCopy.replace("TMRW", tomorrowDateString);
        timeStringCopy = timeStringCopy.replace("TMR", tomorrowDateString);
        int year;
        int time;
        String[] tokens = timeStringCopy.split("[/ \\-]");
        LocalDate date;
        LocalDateTime dateTime;
        switch (tokens.length) {
        case 2:
            year = LocalDate.now().getYear();
            date = tryCreateDate(toDay(tokens[0]), toMonth(tokens[1]), year);
            if (date != null) {
                return new TimePoint(date);
            }
            date = tryCreateDate(toDay(tokens[1]), toMonth(tokens[0]), year);
            if (date != null) {
                return new TimePoint(date);
            }
            return new TimePoint(timeString);
        case 3:
            if (tokens[0].contains(":") || tokens[0].contains("AM") || tokens[0].contains("PM")) {
                time = toHourMinuteTime(tokens[0]);
                if (time == -1) {
                    return new TimePoint(timeString);
                }
                year = LocalDate.now().getYear();
                dateTime = tryCreateDateTime(time, toDay(tokens[1]), toMonth(tokens[2]), year);
                if (dateTime != null) {
                    return new TimePoint(dateTime);
                }
                dateTime = tryCreateDateTime(time, toDay(tokens[2]), toMonth(tokens[1]), year);
                if (dateTime != null) {
                    return new TimePoint(dateTime);
                }
                return new TimePoint(timeString);
            }
            if (tokens[2].contains(":") || tokens[2].contains("AM") || tokens[2].contains("PM")) {
                time = toHourMinuteTime(tokens[2]);
                if (time == -1) {
                    return new TimePoint(timeString);
                }
                year = LocalDate.now().getYear();
                dateTime = tryCreateDateTime(time, toDay(tokens[0]), toMonth(tokens[1]), year);
                if (dateTime != null) {
                    return new TimePoint(dateTime);
                }
                dateTime = tryCreateDateTime(time, toDay(tokens[1]), toMonth(tokens[0]), year);
                if (dateTime != null) {
                    return new TimePoint(dateTime);
                }
                return new TimePoint(timeString);
            }
            date = threeStringstoLocalDate(new String[]{tokens[0], tokens[1], tokens[2]});
            if (date != null) {
                return new TimePoint(date);
            }
            return new TimePoint(timeString);
        case 4:
            if (tokens[0].contains(":") || tokens[0].contains("AM") || tokens[0].contains("PM")
                    || (tokens[0].length() == 4 && tokens[1].length() == 4)) {
                time = toHourMinuteTime(tokens[0]);
                if (time == -1) {
                    return new TimePoint(timeString);
                }
                date = threeStringstoLocalDate(new String[]{tokens[1], tokens[2], tokens[3]});
                if (date != null) {
                    return new TimePoint(
                            LocalDateTime.of(
                                    date.getYear(), date.getMonth(), date.getDayOfMonth(), time / 100, time % 100));
                }
                return new TimePoint(timeString);
            }
            if (tokens[3].contains(":") || tokens[3].contains("AM") || tokens[3].contains("PM")
                    || (tokens[2].length() == 4 && tokens[3].length() == 4)) {
                time = toHourMinuteTime(tokens[3]);
                if (time == -1) {
                    return new TimePoint(timeString);
                }
                date = threeStringstoLocalDate(new String[]{tokens[0], tokens[1], tokens[2]});
                if (date != null) {
                    return new TimePoint(
                            LocalDateTime.of(
                                    date.getYear(), date.getMonth(), date.getDayOfMonth(), time / 100, time % 100));
                }
                return new TimePoint(timeString);
            }
            if (tokens[0].length() == 4 && tokens[3].length() == 4) {
                time = toHourMinuteTime(tokens[0]);
                int yearPos = 3;
                if (time == -1) {
                    time = toHourMinuteTime(tokens[3]);
                    yearPos = 0;
                    if (time == -1) {
                        return new TimePoint(timeString);
                    }
                }
                date = threeStringstoLocalDate(new String[]{tokens[1], tokens[2], tokens[yearPos]});
                if (date != null) {
                    return new TimePoint(
                            LocalDateTime.of(
                                    date.getYear(), date.getMonth(), date.getDayOfMonth(), time / 100, time % 100));
                }
                return new TimePoint(timeString);
            }
            return new TimePoint(timeString);
        default:
            return new TimePoint(timeString);
        }
    }

    /**
     * Helper function which converts a LocalDate to a string which can be parsed by the TimePointParser toDate method.
     *
     * @param date Date to be converted to a string.
     * @return String representation of the given date.
     */
    private static String dateToString(LocalDate date) {
        return String.format(
                "%d-%s-%d", date.getDayOfMonth(), TimePoint.MTHS[date.getMonthValue() - 1], date.getYear());
    }

    /**
     * Helper function to reorder 3 numbers in way that makes sense as a date.
     *
     * @param a 1st number.
     * @param b 2nd number.
     * @param c 3rd number.
     * @return The three numbers reordered dd/mm/yyyy format, if they were given in a valid order, otherwise null.
     */
    private static int[] reorderForDate(int a, int b, int c) {
        if (tryCreateDate(a, b, c) != null) {
            return new int[]{a, b, c};
        }
        if (tryCreateDate(b, c, a) != null) {
            return new int[]{b, c, a};
        }
        if (tryCreateDate(b, a, c) != null) {
            return new int[]{b, a, c};
        }
        if (tryCreateDate(c, b, a) != null) {
            return new int[]{c, b, a};
        }
        return null;
    }

    /**
     * Helper function to convert an array of 3 strings to a LocalDate.
     *
     * @param strings Array of 3 strings.
     * @return A LocalDate corresponding to strings if they can form a valid date, else null.
     */
    private static LocalDate threeStringstoLocalDate(String[] strings) {
        if (Parser.isIntegerArray(strings)) {
            int[] dateOrder = reorderForDate(
                    Integer.parseInt(strings[0]),
                    Integer.parseInt(strings[1]),
                    Integer.parseInt(strings[2]));
            if (dateOrder != null) {
                return LocalDate.of(dateOrder[2], dateOrder[1], dateOrder[0]);
            }
        } else {
            int monthIndex;
            for (monthIndex = 0; monthIndex < strings.length; monthIndex++) {
                if (isMonthWord(strings[monthIndex])) {
                    break;
                }
            }
            switch (monthIndex) {
            case 0:
                if (Parser.isInteger(strings[1]) && Parser.isInteger(strings[2])) {
                    return tryCreateDate(
                            Integer.parseInt(strings[1]), toMonth(strings[0]), Integer.parseInt(strings[2]));
                }
                break;
            case 1:
                if (Parser.isInteger(strings[0]) && Parser.isInteger(strings[2])) {
                    LocalDate date = tryCreateDate(Integer.parseInt(strings[0]),
                            toMonth(strings[1]),
                            Integer.parseInt(strings[2]));
                    if (date != null) {
                        return date;
                    }
                    return tryCreateDate(Integer.parseInt(strings[2]),
                            toMonth(strings[1]),
                            Integer.parseInt(strings[0]));
                }
                break;
            case 2:
                if (Parser.isInteger(strings[0]) && Parser.isInteger(strings[1])) {
                    return tryCreateDate(
                            Integer.parseInt(strings[1]), toMonth(strings[2]), Integer.parseInt(strings[0]));
                }
                break;
            default:
                return null;
            }
        }
        return null;
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
        if (Parser.isInteger(timeString)) {
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
     * Checks if a word represents a month.
     *
     * @param word Word that may represent a month.
     * @return True if word represents a month, otherwise false.
     */
    private static boolean isMonthWord(String word) {
        for (int i = 0; i < 12; i++) {
            if (TimePoint.MONTHS[i].equals(word) || TimePoint.MTHS[i].equals(word)) {
                return true;
            }
        }
        return false;
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
     * Converts a string to a day of the month.
     *
     * @param day String which potentially represents a day of the month.
     * @return The day represented in integer value if it is valid, else -1.
     */
    public static int toDay(String day) {
        if (day == null) {
            return -1;
        }
        if (Parser.isInteger(day)) {
            int dayInt = Integer.parseInt(day);
            if (dayInt >= 1 && dayInt <= 31) {
                return dayInt;
            } else {
                return -1;
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
