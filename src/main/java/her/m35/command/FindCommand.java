package her.m35.command;

import java.util.ArrayList;

import her.m35.Storage;
import her.m35.TaskList;
import her.m35.Ui;

/** Command to display all tasks that fit a search prompt. */
public class FindCommand extends Command {

    /** Prompt string used to filter the task list. */
    private String findPrompt;

    /**
     * Constructs a FindCommand with the specified prompt.
     *
     * @param findPrompt Prompt string used to filter the task list.
     */
    public FindCommand(String findPrompt) {
        this.findPrompt = findPrompt;
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
        while (!findPrompt.isEmpty()) {
            if (findPrompt.startsWith("/done ") || findPrompt.equals("/done")) {
                filterConditions.add(TaskList.FilterCondition.IS_MARKED);
                findPrompt = findPrompt.replaceFirst("/done", "").trim();
                keywords.add("");
                continue;
            }
            if (findPrompt.startsWith("/todo ") || findPrompt.equals("/todo")) {
                filterConditions.add(TaskList.FilterCondition.IS_UNMARKED);
                findPrompt = findPrompt.replaceFirst("/todo", "").trim();
                keywords.add("");
                continue;
            }
            if (findPrompt.startsWith("/on ")) {
                filterConditions.add(TaskList.FilterCondition.ON_DATE);
                findPrompt = findPrompt.replaceFirst("/on ", "");
                int endIndex = findPrompt.indexOf("/");
                if (endIndex != -1) {
                    String newKeyword = findPrompt.substring(0, endIndex).trim();
                    keywords.add(newKeyword);
                    findPrompt = findPrompt.replaceFirst(newKeyword, "").trim();
                } else {
                    keywords.add(findPrompt.trim());
                    findPrompt = "";
                }
                continue;
            }
            if (findPrompt.startsWith("/before ")) {
                filterConditions.add(TaskList.FilterCondition.BEFORE);
                findPrompt = findPrompt.replaceFirst("/before ", "");
                int endIndex = findPrompt.indexOf("/");
                if (endIndex != -1) {
                    String newKeyword = findPrompt.substring(0, endIndex).trim();
                    keywords.add(newKeyword);
                    findPrompt = findPrompt.replaceFirst(newKeyword, "").trim();
                } else {
                    keywords.add(findPrompt.trim());
                    findPrompt = "";
                }
                continue;
            }
            if (findPrompt.startsWith("/after ")) {
                filterConditions.add(TaskList.FilterCondition.AFTER);
                findPrompt = findPrompt.replaceFirst("/after ", "");
                int endIndex = findPrompt.indexOf("/");
                if (endIndex != -1) {
                    String newKeyword = findPrompt.substring(0, endIndex).trim();
                    keywords.add(newKeyword);
                    findPrompt = findPrompt.replaceFirst(newKeyword, "").trim();
                } else {
                    keywords.add(findPrompt.trim());
                    findPrompt = "";
                }
                continue;
            }
            if (findPrompt.startsWith("/type ")) {
                filterConditions.add(TaskList.FilterCondition.OF_TYPE);
                findPrompt = findPrompt.replaceFirst("/type ", "");
                int endIndex = findPrompt.indexOf("/");
                if (endIndex != -1) {
                    String newKeyword = findPrompt.substring(0, endIndex).trim();
                    keywords.add(newKeyword);
                    findPrompt = findPrompt.replaceFirst(newKeyword, "").trim();
                } else {
                    keywords.add(findPrompt.trim());
                    findPrompt = "";
                }
                continue;
            }
            if (findPrompt.startsWith("/contains ")) {
                filterConditions.add(TaskList.FilterCondition.KEYWORD);
                findPrompt = findPrompt.replaceFirst("/contains ", "");
                int endIndex = findPrompt.indexOf("/");
                if (endIndex != -1) {
                    String newKeyword = findPrompt.substring(0, endIndex).trim();
                    keywords.add(newKeyword);
                    findPrompt = findPrompt.replaceFirst(newKeyword, "").trim();
                } else {
                    keywords.add(findPrompt.trim());
                    findPrompt = "";
                }
                continue;
            }
            filterConditions.add(TaskList.FilterCondition.KEYWORD);
            int endIndex = findPrompt.indexOf("/");
            if (endIndex != -1) {
                String newKeyword = findPrompt.substring(0, endIndex).trim();
                keywords.add(newKeyword);
                findPrompt = findPrompt.replaceFirst(newKeyword, "").trim();
            } else {
                keywords.add(findPrompt.trim());
                findPrompt = "";
            }
        }
        ui.printMessage(
                taskList.outputFilteredList(
                        filterConditions.toArray(new TaskList.FilterCondition[filterConditions.size()]),
                        keywords.toArray(new String[keywords.size()])));
    }
}
