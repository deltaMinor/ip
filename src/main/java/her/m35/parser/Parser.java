package her.m35.parser;

import java.util.Arrays;

import her.m35.command.AddTaskCommand;
import her.m35.command.ClearCommand;
import her.m35.command.Command;
import her.m35.command.DeleteCommand;
import her.m35.command.ExitCommand;
import her.m35.command.FindCommand;
import her.m35.command.HelpCommand;
import her.m35.command.ListCommand;
import her.m35.command.MarkCommand;
import her.m35.command.MessageCommand;
import her.m35.task.DeadlineTask;
import her.m35.task.EventTask;
import her.m35.task.ToDoTask;

/**
 * Handles parsing of user input into information for the chatbot.
 * Contains methods to convert strings into other variable types such as integer.
 */
public class Parser {
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
            }
            String[] todoTaskTokens = tokens[1].split(" #");
            if (todoTaskTokens.length == 1) {
                return new AddTaskCommand(new ToDoTask(tokens[1]));
            }
            for (int i = 1; i < todoTaskTokens.length; i++) {
                if (!todoTaskTokens[i].matches("[a-zA-Z0-9]+")) {
                    return new MessageCommand(
                            String.format("Tags need to be strictly alphanumeric. (%s)", todoTaskTokens[i]));
                }
            }
            return new AddTaskCommand(
                    new ToDoTask(
                            todoTaskTokens[0],
                            Arrays.copyOfRange(todoTaskTokens, 1, todoTaskTokens.length)));
        case "deadline":
            if (tokens.length < 2) {
                return new MessageCommand("Task name not given.");
            }
            String[] deadlineTaskTokens = tokens[1].split(" /by ", 2);
            if (deadlineTaskTokens.length < 2) {
                return new MessageCommand("Please state the deadline, denoted with \" /by \".");
            }
            String[] deadlineTagTokens = deadlineTaskTokens[1].split(" #");
            if (deadlineTagTokens.length == 1) {
                return new AddTaskCommand(
                        new DeadlineTask(deadlineTaskTokens[0], TimePointParser.toDate(deadlineTaskTokens[1])));
            }
            for (int i = 1; i < deadlineTagTokens.length; i++) {
                if (!deadlineTagTokens[i].matches("[a-zA-Z0-9]+")) {
                    return new MessageCommand(
                            String.format("Tags need to be strictly alphanumeric. (%s)", deadlineTagTokens[i]));
                }
            }
            return new AddTaskCommand(
                    new DeadlineTask(
                            deadlineTaskTokens[0],
                            TimePointParser.toDate(deadlineTagTokens[0]),
                            Arrays.copyOfRange(deadlineTagTokens, 1, deadlineTagTokens.length)));
        case "event":
            if (tokens.length < 2) {
                return new MessageCommand("Task name not given.");
            }
            String[] eventTaskTokens = tokens[1].split(" /from ", 2);
            if (eventTaskTokens.length < 2) {
                return new MessageCommand(
                        "Please state when the event begins, denoted with \" /from \".");
            }
            String[] eventPeriodTokens = eventTaskTokens[1].split(" /to ", 2);
            if (eventPeriodTokens.length < 2) {
                return new MessageCommand("Please state when the event ends, denoted with \" /to \".");
            }
            String[] eventTagTokens = eventPeriodTokens[1].split(" #");
            if (eventTagTokens.length == 1) {
                return new AddTaskCommand(
                        new EventTask(
                                eventTaskTokens[0],
                                TimePointParser.toDate(eventPeriodTokens[0]),
                                TimePointParser.toDate(eventPeriodTokens[1])));
            }
            for (int i = 1; i < eventTagTokens.length; i++) {
                if (!eventTagTokens[i].matches("[a-zA-Z0-9]+")) {
                    return new MessageCommand(
                            String.format("Tags need to be strictly alphanumeric. (%s)", eventTagTokens[i]));
                }
            }
            return new AddTaskCommand(
                    new EventTask(
                            eventTaskTokens[0],
                            TimePointParser.toDate(eventPeriodTokens[0]),
                            TimePointParser.toDate(eventTagTokens[0]),
                            Arrays.copyOfRange(eventTagTokens, 1, eventTagTokens.length)));
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
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
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
}
