import java.io.IOException;

/** Command that clears the task list. */
public class ClearCommand extends Command {
    /** Constructs a ClearCommand object. */
    public ClearCommand() {

    }

    @Override
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        taskList.clear();
        try {
            storage.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ui.printMessage("Alright, I have emptied the task list.\n" + taskList.getCurrentTaskCountMessage());
    }
}
