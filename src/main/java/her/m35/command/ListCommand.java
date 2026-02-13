package her.m35.command;

import her.m35.Storage;
import her.m35.TaskList;
import her.m35.Ui;

/**
 * Command that lists every task that is stored and their status.
 */
public class ListCommand extends Command {
    /** Constructs an ListCommand object. */
    public ListCommand() {

    }

    /**
     * {@inheritDoc}
     *
     * Displays every task of the given TaskList to the user through the Ui.
     */
    @Override
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        ui.printMessage(taskList.getFormattedTaskList());
    }
}
