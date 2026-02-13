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

    /**
     * {@inheritDoc}
     *
     * If indexString represents a valid task index, removes all tags from the specified task from the provided
     * TaskList, saves the change to Storage, and displays a confirmation message via the Ui.
     * If indexString is invalid, displays an error message to the user informing them of the valid task
     * indexes.
     */
    @Override
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        if (Parser.isInteger(indexString)) {
            int taskIndex = Integer.parseInt(indexString) - 1;
            if (taskIndex >= 0 && taskIndex < taskList.size()) {
                taskList.get(taskIndex).clearTags();
                String storageError = "";
                try {
                    storage.edit(taskIndex, taskList.get(taskIndex).getData());
                } catch (IOException e) {
                    storageError = "Error: Unable to save changes to storage!\n";
                }
                ui.printMessage(
                        storageError,
                        "The following task has all its tags removed:\n",
                        taskList.get(taskIndex).toString());
                return;
            }
        }
        ui.printMessage(String.format(
                "Error: Please enter a number between 1 and %d to remove that task's tags.", taskList.size()));
    }
}
