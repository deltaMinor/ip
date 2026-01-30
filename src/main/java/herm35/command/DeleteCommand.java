package herm35.command;

import herm35.Parser;
import herm35.Storage;
import herm35.TaskList;
import herm35.Ui;

import java.io.IOException;

public class DeleteCommand extends Command {
    private String indexString;
    public DeleteCommand(String indexString) {
        this.indexString = indexString;
    }

    @Override
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        if (Parser.isInteger(indexString)) {
            int taskIndex = Integer.parseInt(indexString) - 1;
            if (taskIndex >= 0 && taskIndex < taskList.size()) {
                String taskDeletedMessage = "Got it, I'm deleting this task:\n"
                        + taskList.get(taskIndex) + "\n";
                taskList.delete(taskIndex);
                try {
                    storage.delete(taskIndex);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                taskDeletedMessage += taskList.getCurrentTaskCountMessage();
                ui.printMessage(taskDeletedMessage);
                return;
            }
        }
        ui.printMessage(String.format(
                "Please enter a number between 1 and %d to delete that task.", taskList.size()));
    }
}
