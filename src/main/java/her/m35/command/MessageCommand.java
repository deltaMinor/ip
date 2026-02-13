package her.m35.command;

import her.m35.Storage;
import her.m35.TaskList;
import her.m35.Ui;

/**
 * Command that displays a string to the user as a formatted message.
 */
public class MessageCommand extends Command {
    /** String to be displayed to the user. */
    private final String[] message;

    /**
     * Constructs a MessageCommand with the given message.
     *
     * @param message Message to be displayed.
     */
    public MessageCommand(String... message) {
        this.message = message;
    }

    /**
     * {@inheritDoc}
     *
     * Displays message to the user through the Ui.
     */
    @Override
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        assert message != null;
        ui.printMessage(message);
    }
}
