package her.m35.command;

import java.io.IOException;

import her.m35.Storage;
import her.m35.TaskList;
import her.m35.Ui;
import her.m35.parser.Parser;

/**
 * Command that adds a list of tags to a task.
 */
public class TagCommand extends Command {
    /** Index of task to be given tags. */
    private final String indexString;

    /** Tags to be added to the task. */
    private final String[] tags;

    /**
     * Constructs a TagCommand with the specified task index and tags.
     *
     * @param indexString String containing index of task to be amended.
     * @param tags Tags to be added to the task.
     */
    public TagCommand(String indexString, String[] tags) {
        this.indexString = indexString;
        this.tags = tags;
    }
    /**
     * {@inheritDoc}
     *
     * If indexString represents a valid task index, and the indicated task does not already contain the given tags,
     * adds the tags to the specified task from the provided TaskList, saves the change to Storage, and displays a
     * confirmation message via the Ui.
     * If indexString is invalid, displays an error message to the user informing them of the valid task
     * indexes.
     * If the task already contains the tags, displays an error message to the user informing them of the tag.
     */
    @Override
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        if (Parser.isInteger(indexString)) {
            int taskIndex = Integer.parseInt(indexString) - 1;
            if (taskIndex >= 0 && taskIndex < taskList.size()) {
                for (String tag : tags) {
                    if (taskList.get(taskIndex).hasTag(tag)) {
                        ui.printMessage(String.format("This task already has tag #%s!", tag));
                        return;
                    }
                }
                for (String tag : tags) {
                    taskList.get(taskIndex).addTag(tag);
                }
                try {
                    storage.edit(taskIndex, taskList.get(taskIndex).getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ui.printMessage("The following task has been updated:\n", taskList.get(taskIndex).toString());
                return;
            }
        }
        ui.printMessage(String.format(
                "Please enter a number between 1 and %d to tag that task.", taskList.size()));
    }
}
