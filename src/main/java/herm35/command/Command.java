package herm35.command;

import herm35.TaskList;
import herm35.Storage;
import herm35.Ui;

/**
 * Represents an executable command in the chatbot.
 *
 * Each Command encapsulates a specific user instruction and defines how it should be executed using the
 * task list, storage, and ui. Subclasses should implement execute(TaskList, Storage, Ui) and may
 * override isExit() to signal termination of the program.
 */
public abstract class Command {

    /**
     * Executes the command.
     *
     * Implementations define the behavior of the command and may modify the task list, interact with
     * storage, and communicate with the user through the UI.
     *
     * @param taskList Task list to be operated on.
     * @param storage Storage used for saving data.
     * @param ui User interface for input and output.
     */
    public abstract void execute(TaskList taskList, Storage storage, Ui ui);

    /**
     * Indicates whether this command should cause the application to exit.
     *
     * By default, commands do not terminate the program. Exit-related commands should override this method
     * to return true.
     *
     * @return true if the application should exit after executing this command, false otherwise.
     */
    public boolean isExit() {
        return false;
    }
}
