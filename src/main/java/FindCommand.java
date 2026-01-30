import java.util.ArrayList;

public class FindCommand extends Command {

    String findPrompt;
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
