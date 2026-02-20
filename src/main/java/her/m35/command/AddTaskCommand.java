package her.m35.command;

import java.io.IOException;

import her.m35.Storage;
import her.m35.TaskList;
import her.m35.TimePoint;
import her.m35.Ui;
import her.m35.task.EventTask;
import her.m35.task.Task;

/**
 * Command that adds a new task to the task list.
 */
public class AddTaskCommand extends Command {

    /** The task to be added to the task list. */
    private final Task task;

    /**
     * Constructs an AddTaskCommand with the specified task.
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
        String storageError = "";
        try {
            storage.insert(task.getData());
        } catch (IOException e) {
            storageError = "Error: Unable to save task to storage!\nCause: " + e.getMessage() + "\n";
        }
        if (isInvalidEventTask(task)) {
            ui.printMessage(
                    storageError,
                    "Warning: Event ends before it begins.\n",
                    "I will trust your decision and add this task regardless:\n  ",
                    taskList.get(taskList.size() - 1).toString() + " ",
                    taskList.get(taskList.size() - 1).getTagsDescription() + "\n",
                    "If you change your mind, delete this task with the command \"delete " + taskList.size() + "\"\n",
                    taskList.getCurrentTaskCountMessage());
            return;
        }
        ui.printMessage(
                storageError,
                "The following task has been added:\n  ",
                taskList.get(taskList.size() - 1).toString() + " ",
                taskList.get(taskList.size() - 1).getTagsDescription() + "\n",
                taskList.getCurrentTaskCountMessage());
    }

    private boolean isInvalidEventTask(Task task) {
        if (task.getType() != Task.Type.EVENT) {
            return false;
        }
        TimePoint fromDate = ((EventTask) task).getFromDate();
        TimePoint toDate = ((EventTask) task).getToDate();
        assert (fromDate != null && toDate != null);
        return fromDate.isAfter(toDate);
    }
}
