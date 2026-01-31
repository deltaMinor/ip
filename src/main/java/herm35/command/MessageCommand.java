package herm35.command;

import herm35.Storage;
import herm35.TaskList;
import herm35.Ui;

/**
 * Command that displays a string to the user as a formatted message.
 */
public class MessageCommand extends Command {
    /** String to be displayed to the user. */
    private String message;

    /**
     * Constructs a MessageCommand with the given message.
     *
     * @param message Message to be displayed.
     */
    public MessageCommand(String message) {
        this.message = message;
    }

    /**
     * @inheritDoc
     *
     * Displays message to the user through the Ui.
     */
    @Override
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        ui.printMessage(message);
    }
}
