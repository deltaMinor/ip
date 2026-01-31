package herm35.command;

import herm35.Storage;
import herm35.TaskList;
import herm35.Ui;

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

    /**
     * @inheritDoc
     *
     * Parses findPrompt to determine the criterion used to filter the task list, then displays it if there
     * are tasks corresponding to the prompt with the Ui. If there are no tasks corresponding to the given
     * prompt, the user is informed via the Ui.
     */
    @Override
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        if (findPrompt.equals("/done")) {
            ui.printMessage(taskList.outputFilteredList(TaskList.FilterCondition.IS_MARKED));
            return;
        }
        if (findPrompt.equals("/todo")) {
            ui.printMessage(taskList.outputFilteredList(TaskList.FilterCondition.IS_UNMARKED));
            return;
        }
        if (findPrompt.contains("/on")) {
            ui.printMessage(
                    taskList.outputFilteredList(
                            TaskList.FilterCondition.ON_DATE,
                            findPrompt.replace("/on ", "")));
            return;
        }
        if (findPrompt.contains("/before")) {
            ui.printMessage(
                    taskList.outputFilteredList(
                            TaskList.FilterCondition.BEFORE,
                            findPrompt.replace("/before ", "")));
            return;
        }
        if (findPrompt.contains("/after")) {
            ui.printMessage(
                    taskList.outputFilteredList(
                            TaskList.FilterCondition.AFTER, findPrompt.replace("/after ", "")));
            return;
        }
        if (findPrompt.contains("/type")) {
            ui.printMessage(
                    taskList.outputFilteredList(
                            TaskList.FilterCondition.OF_TYPE, findPrompt.replace("/type ", "")));
            return;
        }
        ui.printMessage(
                taskList.outputFilteredList(TaskList.FilterCondition.KEYWORD, findPrompt));
    }
}
