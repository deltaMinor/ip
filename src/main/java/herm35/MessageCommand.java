package herm35;

/**
 * herm35.Command that displays a string to the user as a formatted message.
 */
public class MessageCommand extends Command {
    /** String to be displayed to the user. */
    private String message;

    /**
     * Constructs a herm35.MessageCommand with the given message.
     *
     * @param message Message to be displayed.
     */
    public MessageCommand(String message) {
        this.message = message;
    }

    @Override
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        ui.printMessage(message);
    }
}
