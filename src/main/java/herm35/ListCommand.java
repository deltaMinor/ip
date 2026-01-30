package herm35;

/**
 * herm35.Command that lists every task that is stored and their status.
 */
public class ListCommand extends Command {
    /** Constructs an herm35.ListCommand object. */
    public ListCommand() {

    }

    @Override
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        ui.printMessage(taskList.toString());
    }
}
