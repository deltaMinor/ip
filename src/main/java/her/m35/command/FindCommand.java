package her.m35.command;

import java.util.ArrayList;

import her.m35.Storage;
import her.m35.TaskList;
import her.m35.Ui;

/** Command to display all tasks that fit a search prompt. */
public class FindCommand extends Command {

    /** Prompt string used to filter the task list. */
    private final String findPrompt;

    /**
     * Constructs a FindCommand with the specified prompt.
     *
     * @param findPrompt Prompt string used to filter the task list.
     */
    public FindCommand(String findPrompt) {
        this.findPrompt = findPrompt.trim();
    }

    /**
     * {@inheritDoc}
     *
     * Parses findPrompt to determine the criterion used to filter the task list, then displays it if there
     * are tasks corresponding to the prompt with the Ui. If there are no tasks corresponding to the given
     * prompt, the user is informed via the Ui.
     */
    @Override
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        ArrayList<TaskList.FilterCondition> filterConditions = new ArrayList<>();
        ArrayList<String> keywords = new ArrayList<>();
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
                            ui.printMessage("Error: Notate tags with a # sign.");
                            return;
                        }
                        String keyword = tag.substring(1);
                        if (!keyword.matches("[a-zA-Z0-9]+")) {
                            ui.printMessage(String.format("Error: Tags need to be strictly alphanumeric. (%s)", keyword));
                            return;
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
        ui.printMessage(
                taskList.outputFilteredList(
                        filterConditions.toArray(new TaskList.FilterCondition[0]), keywords.toArray(new String[0])));
    }

    private String addNextKeyword(String remainingPrompt, ArrayList<String> keywords) {
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
}
