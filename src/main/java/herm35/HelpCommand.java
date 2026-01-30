package herm35;

import java.io.IOException;

/** herm35.Command to display all valid commands to the user. */
public class HelpCommand extends Command {
    /** herm35.Storage object used to load the help text. */
    private static final Storage COMMANDLIST = new Storage("./assets/help.txt");

    /** Paragraph to be shown to the user. */
    private static String helpOutput = "";

    /**
     * Loads the command list to initialize the help output paragraph.
     * Should be run once before HERM35 starts.
     */
    public static void setup() {
        helpOutput = "";
        String[] commandListLines = {};
        try {
            commandListLines = COMMANDLIST.read();
        } catch (IOException e) {
            System.out.println("Error: unable to read command list.\n");
            e.printStackTrace();
        }
        for (int i = 0; i < commandListLines.length; i++) {
            helpOutput += commandListLines[i] + "\n";
        }
    }

    /** Constructs a herm35.HelpCommand object. */
    public HelpCommand() {

    }

    @Override
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        ui.printMessage(helpOutput);
    }
}
