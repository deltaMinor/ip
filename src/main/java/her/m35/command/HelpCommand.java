package her.m35.command;

import her.m35.Storage;
import her.m35.TaskList;
import her.m35.Ui;

/** Command to display all valid commands to the user. */
public class HelpCommand extends Command {

    /** List of commands available to the user. */
    private static final String HELP_OUTPUT = """
            Here are the commands you can use:
            list: Show your current tasklist
            todo [task name]: Save a new task with name [task name]
            deadline [task name] /by [date]: Save a new task with a deadline of [date]
            event [task name] /from [start date] /to [end date]: Save an event with the provided dates
            *At the end of a task, you may write #tag to tag it. You may use multiple alphanumeric tags at once.
            mark [index]: Mark the task with index [index] as done
            unmark [index]: Unmark the task with index [index] as undone
            delete [index]: Delete the task at index [index]
            clear: Empty your tasklist
            tag [index] #[tag name]: Tag task with index [index] with tag [tag name]
            untag [index]: Remove all tags from task with index [index]
            untag [index] #[tag name]: Remove tag [tag name] from task with index [index]
            tags: View all tags
            find [keyword]: Show all tasks containing [keyword]
            find /contains [keyword]: Show all tasks containing [keyword]
            find /on [date]: Show all tasks with dates on [date]
            find /after [date]: Show all tasks with dates after [date]
            find /before [date]: Show all tasks with dates before [date]
            find /done: Show all tasks that have been marked as done
            find /todo: Show all tasks that are not marked as done
            find /type [type]: Show all tasks that are of [type] (todo/deadline/event)
            find /tag [tag name]: Show all tasks with tag [tag name]
            *Note: You can combine different find commands together:
            *Example: find [keyword] /on [date]: Show all tasks containing [keyword] and on [date]
            *Example: find /before [date1] /after [date2] /contains [keyword]: Show all tasks containing
            *           [keyword], before [date1] and after [date2]
            show tags: display tasks with tags
            hide tags: display tasks without tags
            bye: Exit this program
            help: See this list of commands again""";

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
