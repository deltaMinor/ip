package her.m35.command;

import java.util.ArrayList;

import her.m35.Storage;
import her.m35.TaskList;
import her.m35.Ui;

/**
 * Command that lists every task that is stored and their status.
 */
public class ListTagsCommand extends Command {
    /** Constructs an ListCommand object. */
    public ListTagsCommand() {

    }

    /**
     * {@inheritDoc}
     *
     * Displays every tag, and the number of tasks containing it, in the given TaskList to the user through the Ui.
     */
    @Override
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        if (taskList.getTags().isEmpty()) {
            ui.printMessage("There are no tags! Add some now!");
            return;
        }
        ArrayList<String> output = new ArrayList<>();
        for (String tag : taskList.getTags().keySet()) {
            output.add("#" + tag);
            output.add(": " + taskList.getTags().get(tag) + "\n");
        }
        ui.printMessage(output.toArray(new String[0]));
    }
}
