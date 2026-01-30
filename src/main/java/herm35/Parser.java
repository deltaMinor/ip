package herm35;

import herm35.task.DeadlineTask;
import herm35.task.EventTask;
import herm35.task.ToDoTask;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Handles parsing of user input into information for the chatbot.
 *
 * Contains methods to convert strings into other variable types such as integer and date.
 */

public class Parser {

    /**
     * Enumeration of parameters needed for creation of a TimePoint object.
     */
    private enum TimeParameters {
        YEAR,
        MONTH,
        DAY,
        TIME
    }

    /**
     * Set up procedure such ensure every Command class has its required information.
     */
    public static void setup() {
        HelpCommand.setup();
    }

    /**
     * Parses user input and outputs the corresponding Command.
     *
     * @param input User input to be parsed
     * @return Command which corresponds to the given user input.
     */
    public static Command parse(String input) {
        String[] tokens = input.split(" ", 2);
        switch (tokens[0]) {
            case "mark":
                if (tokens.length < 2) {
                    return new MessageCommand("Task index not given.");
                }
                return new MarkCommand(tokens[1], true);
            case "unmark":
                if (tokens.length < 2) {
                    return new MessageCommand("Task index not given.");
                }
                return new MarkCommand(tokens[1], false);
            case "list":
                if (tokens.length > 1) {
                    return new MessageCommand("Unknown command, please try again. (Did you mean \"list\"?)");
                } else {
                    return new ListCommand();
                }
            case "bye":
                if (tokens.length > 1) {
                    return new MessageCommand("Unknown command, please try again. (Did you mean \"bye\"?)");
                } else {
                    return new ExitCommand();
                }
            case "delete":
                if (tokens.length < 2) {
                    return new MessageCommand("Task name not given.");
                }
                return new DeleteCommand(tokens[1]);
            case "clear":
                if (tokens.length > 1) {
                    return new MessageCommand("Unknown command, please try again. (Did you mean \"clear\"?)");
                } else {
                    return new ClearCommand();
                }
            case "todo":
                if (tokens.length < 2) {
                    return new MessageCommand("Task name not given.");
                } else {
                    return new AddTaskCommand(new ToDoTask(tokens[1]));
                }
            case "deadline":
                if (tokens.length < 2) {
                    return new MessageCommand("Task name not given.");
                }
                String[] deadlineTask = tokens[1].split(" /by ", 2);
                if (deadlineTask.length < 2) {
                    return new MessageCommand("Please state the deadline, denoted with \" /by \".");
                }
                return new AddTaskCommand(new DeadlineTask(deadlineTask[0], Parser.toDate(deadlineTask[1])));
            case "event":
                if (tokens.length < 2) {
                    return new MessageCommand("Task name not given.");
                }
                String[] eventTask = tokens[1].split(" /from ", 2);
                if (eventTask.length < 2) {
                    return new MessageCommand(
                            "Please state when the event begins, denoted with \" /from \".");
                }
                String[] eventPeriod = eventTask[1].split(" /to ", 2);
                if (eventPeriod.length < 2) {
                    return new MessageCommand("Please state when the event ends, denoted with \" /to \".");
                }
                return new AddTaskCommand(
                        new EventTask(
                                eventTask[0],
                                Parser.toDate(eventPeriod[0]),
                                Parser.toDate(eventPeriod[1])));
            case "find":
                if (tokens.length < 2) {
                    return new MessageCommand("Search prompt not given.");
                }
                return new FindCommand(tokens[1]);
            case "help":
                return new HelpCommand();
            default:
                return new MessageCommand("Unknown command, please try again.");
        }
    }

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

        int day = -1, month = -1, year = -1, time = -1;
        String[] tokens = timeString.split("[/ \\-]");
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = tokens[i].toUpperCase();
        }
        LocalDate tempLocalDate = null;
        switch (tokens.length) {
            case 2:
                year = LocalDate.now().getYear();
                if (isValidDate(toDay(tokens[0]), toMonth(tokens[1]), year)) {
                    month = toMonth(tokens[1]);
                    day = toDay(tokens[0]);
                    year = LocalDate.now().getYear();
                    break;
                }
                if (isValidDate(toDay(tokens[1]), toMonth(tokens[0]), year)) {
                    month = toMonth(tokens[0]);
                    day = toDay(tokens[1]);
                    year = LocalDate.now().getYear();
                }
                break;
            case 3:
                if (tokens[0].contains(":") || tokens[0].contains("AM") || tokens[0].contains("PM")) {
                    time = toHourMinuteTime(tokens[0]);
                    year = LocalDate.now().getYear();
                    if (isValidDate(toDay(tokens[1]), toMonth(tokens[2]), year)) {
                        month = toMonth(tokens[2]);
                        day = toDay(tokens[1]);
                        break;
                    }
                    if (isValidDate(toDay(tokens[2]), toMonth(tokens[1]), year)) {
                        month = toMonth(tokens[1]);
                        day = toDay(tokens[2]);
                    }
                    break;
                }
                if (tokens[2].contains(":") || tokens[2].contains("AM") || tokens[2].contains("PM")) {
                    time = toHourMinuteTime(tokens[2]);
                    year = LocalDate.now().getYear();
                    if (isValidDate(toDay(tokens[0]), toMonth(tokens[1]), year)) {
                        month = toMonth(tokens[1]);
                        day = toDay(tokens[0]);
                        year = LocalDate.now().getYear();
                        break;
                    }
                    if (isValidDate(toDay(tokens[1]), toMonth(tokens[0]), year)) {
                        month = toMonth(tokens[0]);
                        day = toDay(tokens[1]);
                        year = LocalDate.now().getYear();
                    }
                    break;
                }
                tempLocalDate = threeStringstoLocalDate(new String[] {tokens[0], tokens[1], tokens[2]});
                if (tempLocalDate != null) {
                    day = tempLocalDate.getDayOfMonth();
                    month = tempLocalDate.getMonthValue();
                    year = tempLocalDate.getYear();
                }
                break;
            case 4:
                if (tokens[0].contains(":") || tokens[0].contains("AM") || tokens[0].contains("PM")
                        || (tokens[0].length() == 4 && tokens[1].length() == 4)) {
                    time = toHourMinuteTime(tokens[0]);
                    tempLocalDate = threeStringstoLocalDate(new String[] {tokens[1], tokens[2], tokens[3]});
                    if (tempLocalDate != null) {
                        day = tempLocalDate.getDayOfMonth();
                        month = tempLocalDate.getMonthValue();
                        year = tempLocalDate.getYear();
                    }
                    break;
                }
                if (tokens[3].contains(":") || tokens[3].contains("AM") || tokens[3].contains("PM")
                        || (tokens[2].length() == 4 && tokens[3].length() == 4)) {
                    time = toHourMinuteTime(tokens[3]);
                    tempLocalDate = threeStringstoLocalDate(new String[] {tokens[0], tokens[1], tokens[2]});
                    if (tempLocalDate != null) {
                        day = tempLocalDate.getDayOfMonth();
                        month = tempLocalDate.getMonthValue();
                        year = tempLocalDate.getYear();
                    }
                    break;
                }
                if (tokens[0].length() == 4 && tokens[3].length() == 4) {
                    time = toHourMinuteTime(tokens[0]);
                    tempLocalDate = threeStringstoLocalDate(new String[] {tokens[1], tokens[2], tokens[3]});
                    if (tempLocalDate != null) {
                        day = tempLocalDate.getDayOfMonth();
                        month = tempLocalDate.getMonthValue();
                        year = tempLocalDate.getYear();
                    }
                    break;
                }
            default:
                return new TimePoint(timeString);
        }

        if(month == -1 || day == -1 || year == -1) {
            return new TimePoint(timeString);
        }

        if(time == -1) {
            if(isValidDate(day, month, year)) {
                return new TimePoint(LocalDate.of(year, month, day));
            }
            return new TimePoint(timeString);
        } else {
            LocalDateTime localDateTime;
            try {
                localDateTime = LocalDateTime.of(year, month, day, time / 100, time % 100);
            } catch (DateTimeException e) {
                return new TimePoint(timeString);
            }
            return new TimePoint(localDateTime);
        }
    }

    /**
     * Check if an order of time parameters is valid.
     *
     * @param timeParameters Order of time parameters in array format.
     * @return Validity of order of time parameters in timeParameters.
     */
    private static boolean isValidTimeParameterOrder(TimeParameters[] timeParameters) {
        if (timeParameters.length != 4) {
            return false;
        }

        int monthPosition = -1;
        int dayPosition = -1;
        for(int i = 0; i < 4; i++) {
            if (timeParameters[i] == TimeParameters.MONTH) {
                monthPosition = i;
            }
            if (timeParameters[i] == TimeParameters.DAY) {
                dayPosition = i;
            }
        }
        if (Math.abs(monthPosition - dayPosition) != 1) {
            return false;
        }
        if (timeParameters[3] == TimeParameters.YEAR && timeParameters[2] == TimeParameters.TIME
                || timeParameters[0] == TimeParameters.YEAR && timeParameters[1] == TimeParameters.TIME) {
            return false;
        }
        return true;
    }

    /**
     * Helper function to reorder 3 numbers in way that makes sense as a date.
     *
     * @param a 1st number.
     * @param b 2nd number.
     * @param c 3rd number.
     * @return The three numbers reordered dd/mm/yyyy format, if they were given in a valid order,
     * otherwise null.
     */
    private static int[] reorderForDate(int a, int b, int c) {
        if (isValidDate(a, b, c)) {
            return new int[]{a, b, c};
        }
        if (isValidDate(b, c, a)) {
            return new int[]{b, c, a};
        }
        if (isValidDate(b, a, c)) {
            return new int[]{b, a, c};
        }
        if (isValidDate(c, b, a)) {
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
        if (isIntegerArray(strings)) {
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
                    if (isValidDate(
                            Integer.parseInt(strings[1]),
                            toMonth(strings[0]),
                            Integer.parseInt(strings[2]))) {
                        return LocalDate.of(
                                Integer.parseInt(strings[2]),
                                toMonth(strings[0]),
                                Integer.parseInt(strings[1]));
                    }
                    break;
                case 1:
                    if (isValidDate(
                            Integer.parseInt(strings[0]),
                            toMonth(strings[1]),
                            Integer.parseInt(strings[2]))) {
                        return LocalDate.of(
                                Integer.parseInt(strings[2]),
                                toMonth(strings[1]),
                                Integer.parseInt(strings[0]));
                    }
                    if (isValidDate(
                            Integer.parseInt(strings[2]),
                            toMonth(strings[1]),
                            Integer.parseInt(strings[0]))) {
                        return LocalDate.of(
                                Integer.parseInt(strings[0]),
                                toMonth(strings[1]),
                                Integer.parseInt(strings[2]));
                    }
                    break;
                case 2:
                    if (isValidDate(
                            Integer.parseInt(strings[1]),
                            toMonth(strings[2]),
                            Integer.parseInt(strings[0]))) {
                        return LocalDate.of(
                                Integer.parseInt(strings[0]),
                                toMonth(strings[2]),
                                Integer.parseInt(strings[1]));
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
     * @param time String to convert to a time value.
     * @return Converted integer value which represents the time, if valid, else -1.
     */
    public static int toHourMinuteTime(String time) {
        if (time == null) {
            return -1;
        }
        if (isInteger(time)) {
            int timeInt = Integer.parseInt(time);
            int hour = timeInt / 100;
            int minute = timeInt % 100;
            if (hour >= 0 && hour <= 23 && minute >= 0 && minute <= 59) {
                return hour * 100 + minute;
            } else {
                return -1;
            }
        }
        if (time.contains(":")) {
            String[] tokens = time.split(":");
            if (isIntegerArray(tokens) && tokens.length == 2) {
                int hour = Integer.parseInt(tokens[0]);
                int minute = Integer.parseInt(tokens[1]);
                if (hour >= 0 && hour <= 23 && minute >= 0 && minute <= 59) {
                    return hour * 100 + minute;
                }
            }
        }
        if (time.contains("AM")) {
            String trimmedTime = time.replace("AM", "");
            if (isInteger(trimmedTime)) {
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
        if (time.contains("PM")) {
            String trimmedTime = time.replace("PM", "");
            if (isInteger(trimmedTime)) {
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
        if (isInteger(month)) {
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
        if (isInteger(day)) {
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
     * Checks whether a given string can be parsed as an integer.
     *
     * @param str String to be checked.
     * @return true if the string represents an integer, false otherwise.
     */
    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        try {
            int i = Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Checks whether all strings in a given string array can all be parsed as an integer.
     *
     * @param strings Array of strings to be checked.
     * @return true only if all strings in string represents an integer.
     */
    public static boolean isIntegerArray(String[] strings) {
        for (String string : strings) {
            if (!isInteger(string)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if given day, month and year form a valid date.
     *
     * @param day Day.
     * @param month Month.
     * @param year Year.
     * @return Validity of date provided.
     */
    private static boolean isValidDate(int day, int month, int year) {
        try {
            LocalDate.of(year, month, day);
            return true;
        } catch (DateTimeException e) {
            return false;
        }
    }
}
