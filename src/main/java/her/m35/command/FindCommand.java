package her.m35.command;

import java.util.ArrayList;

import her.m35.Storage;
import her.m35.TaskList;
import her.m35.Ui;
import her.m35.parser.Parser;

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
        try {
            Parser.parseFindPrompt(findPrompt, filterConditions, keywords);
        } catch (Exception e) {
            ui.printMessage(e.getMessage());
            return;
        }
        ui.printMessage(
                taskList.outputFilteredList(
                        filterConditions.toArray(new TaskList.FilterCondition[0]), keywords.toArray(new String[0])));
    }
}
