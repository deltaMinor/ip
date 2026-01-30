import java.io.IOException;

/**
 * Command that marks an existing task as done or undone, depending on input.
 */
public class MarkCommand extends Command {
    /** Task to be marked. */
    private String indexString;

    /** New isDone value of the task. */
    private boolean newStatus;

    /**
     * Constructs a MarkCommand with the specified task index and new isDone status.
     *
     * @param indexString Index of task to be changed.
     * @param newStatus New isDone status for the task.
     */
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
