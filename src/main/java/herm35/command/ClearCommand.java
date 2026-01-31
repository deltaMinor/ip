package herm35.command;

import herm35.Storage;
import herm35.TaskList;
import herm35.Ui;

import java.io.IOException;

/** Command that clears the task list. */
public class ClearCommand extends Command {
    /** Constructs a ClearCommand object. */
    public ClearCommand() {

    }

    /**
     * @inheritDoc
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
