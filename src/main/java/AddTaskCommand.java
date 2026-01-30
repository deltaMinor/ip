import java.io.IOException;

public class AddTaskCommand extends Command {
    private Task task;
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
