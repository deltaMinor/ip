package her.m35.command;

import her.m35.Storage;
import her.m35.TaskList;
import her.m35.Ui;

/** Command to display all valid commands to the user. */
public class HelpCommand extends Command {

    /** List of commands available to the user. */
    private static final String HELP_OUTPUT = "Here are the commands you can use:\n"
            + "list: Show your current tasklist\n"
            + "todo [task name]: Save a new task with name [task name]\n"
            + "deadline [task name] /by [date]: Save a new task with a deadline of [date]\n"
            + "event [task name] /from [start date] /to [end date]: Save an event with the provided dates\n"
            + "mark [index]: Mark the task with index [index] as done\n"
            + "unmark [index]: Unmark the task with index [index] as undone\n"
            + "delete [index]: Delete the task at index [index]\n"
            + "clear: Empty your tasklist\n"
            + "find [keyword]: Show all tasks containing [keyword]\n"
            + "find /contains [keyword]: Show all tasks containing [keyword]\n"
            + "find /on [date]: Show all tasks with dates on [date]\n"
            + "find /after [date]: Show all tasks with dates after [date]\n"
            + "find /before [date]: Show all tasks with dates before [date]\n"
            + "find /done: Show all tasks that have been marked as done\n"
            + "find /todo: Show all tasks that are not marked as done\n"
            + "find /type [type]: Show all tasks that are of [type] (todo/deadline/event)\n"
            + "*Note: You can combine different find commands together:\n"
            + "*Example: find [keyword] /on [date]: Show all tasks containing [keyword] and on [date]\n"
            + "*Example: find /before [date1] /after [date2] /contains [keyword]: Show all tasks containing\n"
            + "*           [keyword], before [date1] and after [date2]\n"
            + "bye: Exit this program\n"
            + "help: See this list of commands again";

    /** Constructs a HelpCommand object. */
    public HelpCommand() {

    }

    /**
     * {@inheritDoc}
     *
     * Displays the saved help message to the user through the Ui.
     */
    @Override
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        ui.printMessage(HELP_OUTPUT);
    }
}
