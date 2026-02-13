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
        String storageError = "";
        try {
            storage.clear();
        } catch (IOException e) {
            storageError = "Error: Unable to clear storage!\nCause: " + e.getMessage() + "\n";
        }
        ui.printMessage(
                storageError,
                "Alright, I have emptied the task list.\n" + taskList.getCurrentTaskCountMessage());
    }
}
