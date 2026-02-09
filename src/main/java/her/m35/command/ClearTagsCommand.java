package her.m35.command;

import java.io.IOException;

import her.m35.Storage;
import her.m35.TaskList;
import her.m35.Ui;
import her.m35.parser.Parser;

/**
 * Command that removes all tags from a task.
 */
public class ClearTagsCommand extends Command {
    /** Index of task to have its tags removed. */
    private final String indexString;

    /**
     * Constructs an ClearTagsCommand with the specified task index.
     *
     * @param indexString String containing index of task to be amended.
     */
    public ClearTagsCommand(String indexString) {
        this.indexString = indexString;
    }

    @Override
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        if (Parser.isInteger(indexString)) {
            int taskIndex = Integer.parseInt(indexString) - 1;
            if (taskIndex >= 0 && taskIndex < taskList.size()) {
                taskList.get(taskIndex).clearTags();
                try {
                    storage.edit(taskIndex, taskList.get(taskIndex).getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ui.printMessage("The following task has its tags removed:\n" + taskList.get(taskIndex));
                return;
            }
        }
        ui.printMessage(String.format(
                "Please enter a number between 1 and %d to remove that task's tags.", taskList.size()));
    }
}
