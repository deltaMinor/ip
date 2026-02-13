package her.m35.command;

import java.io.IOException;

import her.m35.Storage;
import her.m35.TaskList;
import her.m35.Ui;
import her.m35.parser.Parser;

/**
 * Command that marks an existing task as done or undone, depending on input.
 */
public class MarkCommand extends Command {
    /** Task to be marked. */
    private final String indexString;

    /** New isDone value of the task. */
    private final boolean newStatus;

    /**
     * Constructs a MarkCommand with the specified task index and new isDone status.
     *
     * @param indexString Index of task to be changed.
     * @param newStatus New isDone status for the task.
     */
    public MarkCommand(String indexString, boolean newStatus) {
        this.indexString = indexString;
        this.newStatus = newStatus;
    }

    /**
     * {@inheritDoc}
     *
     * If indexString represents a valid task index, changes the isDone value of the specified task from the
     * provided TaskList to newStatus, saves the change to Storage, and displays a confirmation message via
     * the Ui.
     * If indexString is invalid, displays an error message to the user informing them of the valid task
     * indexes.
     */
    @Override
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        if (Parser.isInteger(indexString)) {
            int taskIndex = Integer.parseInt(indexString) - 1;
            if (taskIndex >= 0 && taskIndex < taskList.size()) {
                taskList.markTask(taskIndex, newStatus);
                String storageError = "";
                try {
                    storage.edit(taskIndex, taskList.get(taskIndex).getData());
                } catch (IOException e) {
                    storageError = "Error: Unable to save changes to storage!\n";
                }
                ui.printMessage(
                        storageError,
                        "Sure, I've marked this task as done:\n",
                        taskList.get(taskIndex).toString() + " ",
                        taskList.get(taskIndex).getTagsDescription());
                return;
            }
        }
        ui.printMessage(String.format(
                "Error: Please enter a number between 1 and %d to mark that task.", taskList.size()));
    }
}
