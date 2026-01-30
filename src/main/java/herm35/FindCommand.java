package herm35;

/** Command to display all tasks that fit a search prompt. */
public class FindCommand extends Command {

    /** Prompt string used to filter the task list. */
    String findPrompt;

    /**
     * Constructs a FindCommand with the specified prompt.
     *
     * @param findPrompt Prompt string used to filter the task list.
     */
    public FindCommand(String findPrompt) {
        this.findPrompt = findPrompt;
    }

    @Override
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        if (findPrompt == "/done") {
            ui.printMessage(taskList.outputFilteredList(TaskList.FilterCondition.IS_MARKED));
        }
        if (findPrompt == "/todo") {
            ui.printMessage(taskList.outputFilteredList(TaskList.FilterCondition.IS_UNMARKED));
        }
        if (findPrompt.contains("/on")) {
            ui.printMessage(
                    taskList.outputFilteredList(
                            TaskList.FilterCondition.ON_DATE,
                            findPrompt.replace("/on ", "")));
        }
        if (findPrompt.contains("/before")) {
            ui.printMessage(
                    taskList.outputFilteredList(
                            TaskList.FilterCondition.BEFORE,
                            findPrompt.replace("/before ", "")));
        }
        if (findPrompt.contains("/after")) {
            ui.printMessage(
                    taskList.outputFilteredList(
                            TaskList.FilterCondition.AFTER, findPrompt.replace("/after ", "")));
        }
        ui.printMessage(
                taskList.outputFilteredList(TaskList.FilterCondition.KEYWORD, findPrompt));
    }
}
