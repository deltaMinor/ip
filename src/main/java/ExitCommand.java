public class ExitCommand extends Command {
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
