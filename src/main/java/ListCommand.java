public class ListCommand extends Command {
    public ListCommand() {

    }

    @Override
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        ui.printMessage(taskList.toString());
    }
}
