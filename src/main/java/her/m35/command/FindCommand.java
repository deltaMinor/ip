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
        ArrayList<TaskList.FilterCondition> filterConditions = new ArrayList<TaskList.FilterCondition>();
        ArrayList<String> keywords = new ArrayList<String>();
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
                int endIndex;
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
                    endIndex = remainingPrompt.indexOf("/");
                    if (endIndex != -1) {
                        String newKeyword = remainingPrompt.substring(0, endIndex).trim();
                        keywords.add(newKeyword);
                        remainingPrompt = remainingPrompt.replaceFirst(newKeyword, "").trim();
                    } else {
                        keywords.add(remainingPrompt.trim());
                        remainingPrompt = "";
                    }
                    break;
                case "before":
                    filterConditions.add(TaskList.FilterCondition.BEFORE);
                    endIndex = remainingPrompt.indexOf("/");
                    if (endIndex != -1) {
                        String newKeyword = remainingPrompt.substring(0, endIndex).trim();
                        keywords.add(newKeyword);
                        remainingPrompt = remainingPrompt.replaceFirst(newKeyword, "").trim();
                    } else {
                        keywords.add(remainingPrompt.trim());
                        remainingPrompt = "";
                    }
                    break;
                case "after":
                    filterConditions.add(TaskList.FilterCondition.AFTER);
                    endIndex = remainingPrompt.indexOf("/");
                    if (endIndex != -1) {
                        String newKeyword = remainingPrompt.substring(0, endIndex).trim();
                        keywords.add(newKeyword);
                        remainingPrompt = remainingPrompt.replaceFirst(newKeyword, "").trim();
                    } else {
                        keywords.add(remainingPrompt.trim());
                        remainingPrompt = "";
                    }
                    break;
                case "type":
                    filterConditions.add(TaskList.FilterCondition.OF_TYPE);
                    endIndex = remainingPrompt.indexOf("/");
                    if (endIndex != -1) {
                        String newKeyword = remainingPrompt.substring(0, endIndex).trim();
                        keywords.add(newKeyword);
                        remainingPrompt = remainingPrompt.replaceFirst(newKeyword, "").trim();
                    } else {
                        keywords.add(remainingPrompt.trim());
                        remainingPrompt = "";
                    }
                    break;
                case "contains":
                    filterConditions.add(TaskList.FilterCondition.KEYWORD);
                    endIndex = remainingPrompt.indexOf("/");
                    if (endIndex != -1) {
                        String newKeyword = remainingPrompt.substring(0, endIndex).trim();
                        keywords.add(newKeyword);
                        remainingPrompt = remainingPrompt.replaceFirst(newKeyword, "").trim();
                    } else {
                        keywords.add(remainingPrompt.trim());
                        remainingPrompt = "";
                    }
                    break;
                default:
                    filterConditions.add(TaskList.FilterCondition.ERROR_CONDITION);
                    keywords.add("");
                    break;
                }
            } else {
                filterConditions.add(TaskList.FilterCondition.KEYWORD);
                int endIndex = remainingPrompt.indexOf("/");
                if (endIndex != -1) {
                    String newKeyword = remainingPrompt.substring(0, endIndex).trim();
                    keywords.add(newKeyword);
                    remainingPrompt = remainingPrompt.replaceFirst(newKeyword, "").trim();
                } else {
                    keywords.add(remainingPrompt.trim());
                    remainingPrompt = "";
                }
            }
        }
        ui.printMessage(
                taskList.outputFilteredList(
                        filterConditions.toArray(new TaskList.FilterCondition[0]), keywords.toArray(new String[0])));
    }
}
