import java.io.IOException;

public class HelpCommand extends Command {
    /** Storage object used to load the help text. */
    private static final Storage COMMANDLIST = new Storage("help", ".txt");

    private static String helpOutput = "";

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

    public HelpCommand() {

    }

    @Override
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        ui.printMessage(helpOutput);
    }
}
