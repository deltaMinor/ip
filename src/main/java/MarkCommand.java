import java.io.IOException;

public class MarkCommand extends Command {
    private String indexString;
    private boolean newStatus;

    public MarkCommand(String indexString, boolean newStatus) {
        this.indexString = indexString;
        this.newStatus = newStatus;
    }

    @Override
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        if (Parser.isInteger(indexString)) {
            int taskIndex = Integer.parseInt(indexString) - 1;
            if (taskIndex >= 0 && taskIndex < taskList.size()) {
                taskList.markTask(taskIndex, newStatus);
                try {
                    storage.edit(taskIndex, taskList.get(taskIndex).getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ui.printMessage("Sure, I've marked this task as done:\n" + taskList.get(taskIndex));
                return;
            }
        }
        ui.printMessage(String.format(
                "Please enter a number between 1 and %d to mark that task.", taskList.size()));
    }
}
