package her.m35.command;

import java.io.IOException;

import her.m35.Storage;
import her.m35.TaskList;
import her.m35.Ui;
import her.m35.parser.Parser;
import her.m35.task.Task;

/**
 * Deletes a task from the task list.
 */
public class DeleteCommand extends Command {
    /** String representing the index of the task to be deleted from the task list. */
    private final String indexString;

    /** Constructs a DeleteCommand object. */
    public DeleteCommand(String indexString) {
        this.indexString = indexString;
    }

    /**
     * {@inheritDoc}
     *
     * If indexString represents a valid task index, deletes the specified task from the provided TaskList,
     * saves the change to Storage, and displays a confirmation message via the Ui.
     * If indexString is invalid, displays an error message to the user informing them of the valid task
     * indexes.
     */
    @Override
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        if (Parser.isInteger(indexString)) {
            int taskIndex = Integer.parseInt(indexString) - 1;
            if (taskIndex >= 0 && taskIndex < taskList.size()) {
                Task deletedTask = taskList.get(taskIndex);
                String storageError = "";
                try {
                    storage.edit(taskIndex, taskList.get(taskIndex).getData());
                } catch (IOException e) {
                    storageError = "Error: Unable to save changes to storage!\n";
                }
                ui.printMessage(
                        storageError,
                        "Got it, I'm deleting this task:\n",
                        deletedTask.toString() + " ",
                        deletedTask.getTagsDescription(),
                        "\n" + taskList.getCurrentTaskCountMessage());
                return;
            }
        }
        ui.printMessage(String.format(
                "Error: Please enter a number between 1 and %d to delete that task.", taskList.size()));
    }
}
