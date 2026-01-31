package herm35.command;

import herm35.Storage;
import herm35.TaskList;
import herm35.Ui;

import java.io.IOException;

/** Command to display all valid commands to the user. */
public class HelpCommand extends Command {
    /** Storage object used to load the help text. */
    private static Storage COMMANDLIST = null;

    /** Paragraph to be shown to the user. */
    private static String helpOutput = "";

    /**
     * Loads the command list to initialize the help output paragraph.
     * Should be run once before HERM35 starts.
     */
    public static String setup() {
        String errorMessages = "";
        try {
            COMMANDLIST = new Storage("assets/help.txt");
        } catch (IOException e) {
            errorMessages += "Error: " + e.getMessage() + "\n";
        }
        helpOutput = "";
        String[] commandListLines = {};
        try {
            commandListLines = COMMANDLIST.read();
        } catch (IOException e) {
            errorMessages += "Error: " + e.getMessage() + "\n";
        }
        for (int i = 0; i < commandListLines.length; i++) {
            helpOutput += commandListLines[i] + "\n";
        }
        return errorMessages;
    }

    /** Constructs a HelpCommand object. */
    public HelpCommand() {

    }

    /**
     * @inheritDoc
     *
     * Displays the saved help message to the user through the Ui.
     */
    @Override
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        ui.printMessage(helpOutput);
    }
}
