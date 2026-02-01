package her.m35.command;

import java.io.IOException;

import her.m35.Storage;
import her.m35.TaskList;
import her.m35.Ui;

/** Command that clears the task list. */
public class ClearCommand extends Command {
    /** Constructs a ClearCommand object. */
    public ClearCommand() {

    }

    /**
     * {@inheritDoc}
     *
     * Deletes every task in the provided TaskList and Storage, and informs the viewer via the Ui.
     */
    @Override
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        taskList.clear();
        try {
            storage.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ui.printMessage("Alright, I have emptied the task list.\n" + taskList.getCurrentTaskCountMessage());
    }
}
