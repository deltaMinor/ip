package herm35.command;

import herm35.Storage;
import herm35.TaskList;
import herm35.Ui;

/**
 * Command that lists every task that is stored and their status.
 */
public class ListCommand extends Command {
    /** Constructs an ListCommand object. */
    public ListCommand() {

    }

    /**
     * @inheritDoc
     *
     * Displays every task of the given TaskList to the user through the Ui.
     */
    @Override
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        ui.printMessage(taskList.toString());
    }
}
