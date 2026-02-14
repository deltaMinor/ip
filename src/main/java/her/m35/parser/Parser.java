package her.m35.parser;

import java.util.ArrayList;
import java.util.Arrays;

import her.m35.TaskList;
import her.m35.command.AddTaskCommand;
import her.m35.command.ClearCommand;
import her.m35.command.ClearTagsCommand;
import her.m35.command.Command;
import her.m35.command.DeleteCommand;
import her.m35.command.ExitCommand;
import her.m35.command.FindCommand;
import her.m35.command.HelpCommand;
import her.m35.command.ListCommand;
import her.m35.command.ListTagsCommand;
import her.m35.command.MarkCommand;
import her.m35.command.MessageCommand;
import her.m35.command.QuoteCommand;
import her.m35.command.SetTagVisibilityCommand;
import her.m35.command.TagCommand;
import her.m35.command.UntagCommand;
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
        if (input.equals("show tags")) {
            return new SetTagVisibilityCommand(true);
        }
        if (input.equals("hide tags")) {
            return new SetTagVisibilityCommand(false);
        }
        if (input.equals("quote")) {
            return new QuoteCommand();
        }
        String[] tokens = input.split(" ", 2);
        switch (tokens[0]) {
        case "mark":
            if (tokens.length < 2) {
                return new MessageCommand("Error: Task index not given.");
            }
            return new MarkCommand(tokens[1], true);
        case "unmark":
            if (tokens.length < 2) {
                return new MessageCommand("Error: Task index not given.");
            }
            return new MarkCommand(tokens[1], false);
        case "list":
            if (tokens.length > 1) {
                return new MessageCommand("Error: Unknown command, please try again. (Did you mean \"list\"?)");
            } else {
                return new ListCommand();
            }
        case "bye":
            if (tokens.length > 1) {
                return new MessageCommand("Error: Unknown command, please try again. (Did you mean \"bye\"?)");
            } else {
                return new ExitCommand();
            }
        case "delete":
            if (tokens.length < 2) {
                return new MessageCommand("Error: Task name not given.");
            }
            return new DeleteCommand(tokens[1]);
        case "clear":
            if (tokens.length > 1) {
                return new MessageCommand("Error: Unknown command, please try again. (Did you mean \"clear\"?)");
            } else {
                return new ClearCommand();
            }
        case "todo":
            if (tokens.length < 2) {
                return new MessageCommand("Error: Task name not given.");
            }
            String[] todoTaskTokens = tokens[1].split(" #");
            if (todoTaskTokens.length == 1) {
                return new AddTaskCommand(new ToDoTask(tokens[1]));
            }
            for (int i = 1; i < todoTaskTokens.length; i++) {
                if (!todoTaskTokens[i].matches("[a-zA-Z0-9]+")) {
                    return new MessageCommand(
                            String.format("Error: Tags need to be strictly alphanumeric. (%s)", todoTaskTokens[i]));
                }
            }
            return new AddTaskCommand(
                    new ToDoTask(
                            todoTaskTokens[0],
                            Arrays.copyOfRange(todoTaskTokens, 1, todoTaskTokens.length)));
        case "deadline":
            if (tokens.length < 2) {
                return new MessageCommand("Error: Task name not given.");
            }
            String[] deadlineTaskTokens = tokens[1].split(" /by ", 2);
            if (deadlineTaskTokens.length < 2) {
                return new MessageCommand("Error: Please state the deadline, denoted with \" /by \".");
            }
            String[] deadlineTagTokens = deadlineTaskTokens[1].split(" #");
            if (deadlineTagTokens.length == 1) {
                return new AddTaskCommand(
                        new DeadlineTask(deadlineTaskTokens[0], TimePointParser.toTimePoint(deadlineTaskTokens[1])));
            }
            for (int i = 1; i < deadlineTagTokens.length; i++) {
                if (!deadlineTagTokens[i].matches("[a-zA-Z0-9]+")) {
                    return new MessageCommand(
                            String.format("Error: Tags need to be strictly alphanumeric. (%s)", deadlineTagTokens[i]));
                }
            }
            return new AddTaskCommand(
                    new DeadlineTask(
                            deadlineTaskTokens[0],
                            TimePointParser.toTimePoint(deadlineTagTokens[0]),
                            Arrays.copyOfRange(deadlineTagTokens, 1, deadlineTagTokens.length)));
        case "event":
            if (tokens.length < 2) {
                return new MessageCommand("Error: Task name not given.");
            }
            String[] eventTaskTokens = tokens[1].split(" /from ", 2);
            if (eventTaskTokens.length < 2) {
                return new MessageCommand(
                        "Error: Please state when the event begins, denoted with \" /from \".");
            }
            String[] eventPeriodTokens = eventTaskTokens[1].split(" /to ", 2);
            if (eventPeriodTokens.length < 2) {
                return new MessageCommand("Error: Please state when the event ends, denoted with \" /to \".");
            }
            String[] eventTagTokens = eventPeriodTokens[1].split(" #");
            if (eventTagTokens.length == 1) {
                return new AddTaskCommand(
                        new EventTask(
                                eventTaskTokens[0],
                                TimePointParser.toTimePoint(eventPeriodTokens[0]),
                                TimePointParser.toTimePoint(eventPeriodTokens[1])));
            }
            for (int i = 1; i < eventTagTokens.length; i++) {
                if (!eventTagTokens[i].matches("[a-zA-Z0-9]+")) {
                    return new MessageCommand(
                            String.format("Error: Tags need to be strictly alphanumeric. (%s)", eventTagTokens[i]));
                }
            }
            return new AddTaskCommand(
                    new EventTask(
                            eventTaskTokens[0],
                            TimePointParser.toTimePoint(eventPeriodTokens[0]),
                            TimePointParser.toTimePoint(eventTagTokens[0]),
                            Arrays.copyOfRange(eventTagTokens, 1, eventTagTokens.length)));
        case "tag":
            if (tokens.length < 2) {
                return new MessageCommand("Error: Task index not given.");
            }
            String[] tagTokens = tokens[1].split(" ");
            if (tagTokens.length == 1) {
                return new MessageCommand("Error: Tags not given.");
            }
            String[] tags = new String[tagTokens.length - 1];
            for (int i = 1; i < tagTokens.length; i++) {
                if (!tagTokens[i].startsWith("#")) {
                    return new MessageCommand("Error: Notate tags with # sign.");
                }
                String tag = tagTokens[i].substring(1);
                if (!tag.matches("[a-zA-Z0-9]+")) {
                    return new MessageCommand(String.format("Error: Tags need to be strictly alphanumeric. (%s)", tag));
                }
                tags[i - 1] = tag;
            }
            return new TagCommand(tagTokens[0], tags);
        case "untag":
            if (tokens.length < 2) {
                return new MessageCommand("Error: Task index not given.");
            }
            String[] untagTokens = tokens[1].split(" ");
            if (untagTokens.length == 1) {
                return new ClearTagsCommand(untagTokens[0]);
            }
            String[] untags = new String[untagTokens.length - 1];
            for (int i = 1; i < untagTokens.length; i++) {
                if (!untagTokens[i].startsWith("#")) {
                    return new MessageCommand("Error: Notate tags with # sign.");
                }
                String tag = untagTokens[i].substring(1);
                if (!tag.matches("[a-zA-Z0-9]+")) {
                    return new MessageCommand(String.format("Error: Tags need to be strictly alphanumeric. (%s)", tag));
                }
                untags[i - 1] = tag;
            }
            return new UntagCommand(untagTokens[0], untags);
        case "tags":
            if (tokens.length > 1) {
                return new MessageCommand("Error: Unknown command, please try again. (Did you mean \"tags\"?)");
            }
            return new ListTagsCommand();
        case "find":
            if (tokens.length < 2) {
                return new MessageCommand("Error: Search prompt not given.");
            }
            return new FindCommand(tokens[1]);
        case "help":
            if (tokens.length < 2) {
                return new HelpCommand(HelpCommand.Section.BASIC_HELP);
            }
            return switch (tokens[1]) {
            case "edit" -> new HelpCommand(HelpCommand.Section.EDITING_TASK);
            case "find" -> new HelpCommand(HelpCommand.Section.FINDING_TASKS);
            case "customisation" -> new HelpCommand(HelpCommand.Section.CUSTOMISATION);
            default -> new HelpCommand(HelpCommand.Section.ERROR_COMMAND);
            };
        default:
            return new MessageCommand(
                    "Error: Unknown command, please try again.\nType \"help\" to see the available commands.");
        }
    }

    /**
     * Parses a prompt into ArrayLists of filter conditions and keywords for the FindCommand.
     *
     * @param findPrompt Prompt to be parsed.
     * @param filterConditions ArrayList to store filter conditions.
     * @param keywords ArrayList to store keywords.
     * @throws Exception Indicates error in the given prompt.
     */
    public static void parseFindPrompt(
            String findPrompt,
            ArrayList<TaskList.FilterCondition> filterConditions,
            ArrayList<String> keywords) throws Exception {
        filterConditions.clear();
        keywords.clear();
        String remainingPrompt = findPrompt;
        while (!remainingPrompt.isEmpty()) {
            if (remainingPrompt.startsWith("/")) {
                String[] promptTokens = remainingPrompt.split(" ", 2);
                String commandWord = promptTokens[0].replaceFirst("/", "");
                if (promptTokens.length > 1) {
                    remainingPrompt = promptTokens[1];
                } else {
                    remainingPrompt = "";
                }
                switch (commandWord) {
                case "done":
                    filterConditions.add(TaskList.FilterCondition.IS_MARKED);
                    keywords.add("");
                    break;
                case "todo":
                    filterConditions.add(TaskList.FilterCondition.IS_UNMARKED);
                    keywords.add("");
                    break;
                case "on":
                    filterConditions.add(TaskList.FilterCondition.ON_DATE);
                    remainingPrompt = addNextKeyword(remainingPrompt, keywords);
                    break;
                case "before":
                    filterConditions.add(TaskList.FilterCondition.BEFORE);
                    remainingPrompt = addNextKeyword(remainingPrompt, keywords);
                    break;
                case "after":
                    filterConditions.add(TaskList.FilterCondition.AFTER);
                    remainingPrompt = addNextKeyword(remainingPrompt, keywords);
                    break;
                case "type":
                    filterConditions.add(TaskList.FilterCondition.OF_TYPE);
                    remainingPrompt = addNextKeyword(remainingPrompt, keywords);
                    break;
                case "contains":
                    filterConditions.add(TaskList.FilterCondition.KEYWORD);
                    remainingPrompt = addNextKeyword(remainingPrompt, keywords);
                    break;
                case "tag":
                    int endIndex = remainingPrompt.indexOf("/");
                    String tagsString;
                    if (endIndex != -1) {
                        tagsString = remainingPrompt.substring(0, endIndex).trim();
                        remainingPrompt = remainingPrompt.replaceFirst(tagsString, "").trim();
                    } else {
                        tagsString = remainingPrompt.trim();
                        remainingPrompt = "";
                    }
                    String[] tags = tagsString.split(" ");
                    for (String tag : tags) {
                        if (!tag.startsWith("#")) {
                            throw new Exception("Error: Notate tags with a # sign.");
                        }
                        String keyword = tag.substring(1);
                        if (!keyword.matches("[a-zA-Z0-9]+")) {
                            throw new Exception(
                                    String.format("Error: Tags need to be strictly alphanumeric. (%s)", keyword));
                        }
                        filterConditions.add(TaskList.FilterCondition.TAG);
                        keywords.add(keyword);
                    }
                    break;
                default:
                    filterConditions.add(TaskList.FilterCondition.ERROR_CONDITION);
                    keywords.add(commandWord);
                    break;
                }
            } else {
                filterConditions.add(TaskList.FilterCondition.KEYWORD);
                remainingPrompt = addNextKeyword(remainingPrompt, keywords);
            }
        }
    }

    /**
     * Helper function to add split a prompt string into the next keyword and the following command, then add the
     * keyword to the list of keywords.
     *
     * @param remainingPrompt Prompt string to be processed
     * @param keywords List of keywords to add the next keyword to.
     * @return Remainder prompt, containing the next segment of commands, if any.
     */
    private static String addNextKeyword(String remainingPrompt, ArrayList<String> keywords) {
        int endIndex = remainingPrompt.indexOf("/");
        if (endIndex != -1) {
            String newKeyword = remainingPrompt.substring(0, endIndex).trim();
            keywords.add(newKeyword);
            return remainingPrompt.replaceFirst(newKeyword, "").trim();
        } else {
            keywords.add(remainingPrompt.trim());
            return "";
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
