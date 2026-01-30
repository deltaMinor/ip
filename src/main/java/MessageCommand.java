public class MessageCommand extends Command {
    private String message;
    public MessageCommand(String message) {
        this.message = message;
    }

    @Override
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        ui.printMessage(message);
    }
}
