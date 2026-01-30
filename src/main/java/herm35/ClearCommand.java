package herm35;

import java.io.IOException;

/** herm35.Command that clears the task list. */
public class ClearCommand extends Command {
    /** Constructs a herm35.ClearCommand object. */
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
