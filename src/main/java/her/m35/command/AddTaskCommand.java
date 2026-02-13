package her.m35.command;

import java.io.IOException;

import her.m35.Storage;
import her.m35.TaskList;
import her.m35.Ui;
import her.m35.task.Task;

/**
 * Command that adds a new task to the task list.
 */
public class AddTaskCommand extends Command {

    /** The task to be added to the task list. */
    private final Task task;

    /**
     * Constructs an AddTaskCommand with the specified task.
     *
     * @param task Task to be added.
     */
    public AddTaskCommand(Task task) {
        this.task = task;
    }

    /**
     * {@inheritDoc}
     *
     * Adds the specified task to the provided TaskList, saves the task to Storage, and displays a confirmation
     * message via the Ui.
     */
    @Override
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        taskList.add(task);
        try {
            storage.insert(task.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ui.printMessage(
                "The following task has been added:\n  ",
                taskList.get(taskList.size() - 1).toString() + " ",
                taskList.get(taskList.size() - 1).getTagsDescription(),
                "\n",
                taskList.getCurrentTaskCountMessage());
    }
}
