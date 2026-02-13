package her.m35.command;

import java.io.IOException;

import her.m35.Storage;
import her.m35.TaskList;
import her.m35.Ui;
import her.m35.parser.Parser;

/**
 * Command that removes a list of tags from a task.
 */
public class UntagCommand extends Command {
    /** Index of task to have its tags removed. */
    private final String indexString;

    /** Tags to be removed from the task. */
    private final String[] tags;

    /**
     * Constructs an UntagCommand with the specified task index and tags.
     *
     * @param indexString String containing index of task to be amended.
     * @param tags Tags to be removed from the task.
     */
    public UntagCommand(String indexString, String[] tags) {
        this.indexString = indexString;
        this.tags = tags;
    }
    /**
     * {@inheritDoc}
     *
     * If indexString represents a valid task index, and the indicated task contains the given tags, removes the given
     * tags from the specified task from the provided TaskList, saves the change to Storage, and displays a
     * confirmation message via the Ui.
     * If indexString is invalid, displays an error message to the user informing them of the valid task
     * indexes.
     * If the task already does not contain a tag, displays an error message to the user informing them of the tag.
     */
    @Override
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        if (Parser.isInteger(indexString)) {
            int taskIndex = Integer.parseInt(indexString) - 1;
            if (taskIndex >= 0 && taskIndex < taskList.size()) {
                for (String tag : tags) {
                    if (!taskList.get(taskIndex).hasTag(tag)) {
                        ui.printMessage("This task already has tag ", "#" + tag, "!");
                        return;
                    }
                }
                for (String tag : tags) {
                    taskList.get(taskIndex).removeTag(tag);
                }
                try {
                    storage.edit(taskIndex, taskList.get(taskIndex).getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ui.printMessage(
                        "The following task has been updated:\n",
                        taskList.get(taskIndex).toString() + " ",
                        taskList.get(taskIndex).getTagsDescription());
                return;
            }
        }
        ui.printMessage(String.format(
                "Please enter a number between 1 and %d to untag that task.", taskList.size()));
    }
}
