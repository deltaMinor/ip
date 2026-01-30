/**
 * Command that bids the user farewell and indicates for the program to exit.
 */
public class ExitCommand extends Command {
    /** Constructs an ExitCommand object. */
    public ExitCommand() {

    }

    @Override
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        ui.printMessage("Bye, see you later!");
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
