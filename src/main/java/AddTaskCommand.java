import java.io.IOException;

/**
 * Command that adds a new task to the task list.
 */
public class AddTaskCommand extends Command {

    /** The task to be added to the task list. */
    private Task task;

    /**
     * Constructs an AddTaskCommand with the specified task.
     *
     * @param task Task to be added.
     */
    public AddTaskCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        taskList.add(task);
        try {
            storage.insert(task.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String message = "The following task has been added:\n\t"
                + taskList.get(taskList.size() - 1) + "\n"
                + taskList.getCurrentTaskCountMessage();
        ui.printMessage(message);
    }
}
