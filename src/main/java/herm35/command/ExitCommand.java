package herm35.command;

import herm35.Storage;
import herm35.TaskList;
import herm35.Ui;

/**
 * Command that bids the user farewell and indicates for the program to exit.
 */
public class ExitCommand extends Command {
    /** Constructs an ExitCommand object. */
    public ExitCommand() {

    }

    /**
     * @inheritDoc
     *
     * Bids the user farewell via the given Ui.
     */
    @Override
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        ui.printMessage("Bye, see you later!");
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
