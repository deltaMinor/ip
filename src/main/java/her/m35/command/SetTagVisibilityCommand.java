package her.m35.command;

import her.m35.Storage;
import her.m35.TaskList;
import her.m35.Ui;
import her.m35.task.Task;

/**
 * Command that sets the global tagsIsVisible variable to a given value.
 */
public class SetTagVisibilityCommand extends Command {
    /** Messages to be shown depending on the tagsIsVisible value. */
    private static final String SHOW_TAGS_MESSAGE = "Nice! Tags will be displayed alongside tasks from now on.";
    private static final String HIDE_TAGS_MESSAGE =
            "No problem, tags will no longer be shown alongside tasks going forward.";

    /** New value of tagsIsVisible. */
    private final boolean tagsIsVisible;

    /**
     * Constructs a SetTaskVisibilityCommand with the specified new tagsIsVisible value.
     * @param tagsIsVisible New value for tagsIsVisible.
     */
    public SetTagVisibilityCommand(boolean tagsIsVisible) {
        this.tagsIsVisible = tagsIsVisible;
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
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        Task.setTagsIsVisible(tagsIsVisible);
        if (tagsIsVisible) {
            ui.printMessage(SHOW_TAGS_MESSAGE);
        } else {
            ui.printMessage(HIDE_TAGS_MESSAGE);
        }
    }
}
