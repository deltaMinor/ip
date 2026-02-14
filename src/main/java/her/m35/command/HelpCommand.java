package her.m35.command;

import her.m35.Storage;
import her.m35.TaskList;
import her.m35.Ui;

/** Command to display all valid commands to the user. */
public class HelpCommand extends Command {

    /** Enum for the possible help messages the Help command may output. */
    public enum Section {
        BASIC_HELP,
        EDITING_TASK,
        FINDING_TASKS,
        CUSTOMISATION,
        ERROR_COMMAND
    }

    /** List of basic HERM35 commands. */
    private static final String[] BASIC_HELP_TEXT = {
        "With HERM35, you can keep track of all your tasks, including their completion status and deadlines.",
        "The following are some commands you can use to get started:\n",
        "$ -list: Show your current tasklist\n",
        "$ -todo [task name]: Save a new task with name [task name]\n",
        "$ -deadline [task name] /by [date]: Save a new task with a deadline of [date]\n",
        "$ -event [task name] /from [start date] /to [end date]: Save an event with the provided dates\n",
        "$ -delete [index]: Delete the task at index [index]\n",
        "$ -clear: Empty your tasklist\n",
        "$ -bye: Exit this program\n",
        "At the end of a task, you may write ",
        "#[word]",
        " to tag it as [word]. ",
        "You may use multiple alphanumeric tags at once.\n\n",
        "To learn more, you can use one of the following commands:\n",
        "$ -help edit: Learn how to edit a task's tags or mark it as done or undone.\n",
        "$ -help find: Learn all the command words to filtering your task list to find a task.\n",
        "$ -help customisation: Learn the command words to customise your experience.\n\n",
        "I am more than just a task management bot however, you can even try the following command:\n",
        "$ -quote: Learn a quote about Hermes!"
    };

    /** List of HERM35 commands for marking or tagging an already existing task. */
    private static final String[] EDITING_TASK_TEXT = {
        "If you want to edit a task, here are the available commands you can use:\n",
        "$ -mark [index]: Mark the task with index [index] as done.\n",
        "$ -unmark [index]: Unmark the task with index [index] as undone.\n",
        "$ -tag [index] ",
        "#[tag name]",
        "$: Tag task with index [index] with tag [tag name].\n",
        "$ -untag [index]: Remove all tags from task with index [index]\n",
        "$ -untag [index] ",
        "#[tag name]",
        "$: Remove tag [tag name] from task with index [index]"
    };

    /** List of HERM35 commands for filtering the tasklist. */
    private static final String[] FINDING_TASKS_TEXT = {
        "The main command word for finding tasks is '",
        "find",
        "', but there are many other command words you can use in conjunction to improve your filters. ",
        "They should be notated with the / symbol.\n",
        "$ -find [keyword]: Show all tasks containing [keyword].\n",
        "$ -find /contains [keyword]: Show all tasks containing [keyword].\n",
        "$ -find /on [date]: Show all tasks with dates on [date].\n",
        "$ -find /after [date]: Show all tasks with dates after [date].\n",
        "$ -find /before [date]: Show all tasks with dates before [date].\n",
        "$ -find /done: Show all tasks that have been marked as done.\n",
        "$ -find /todo: Show all tasks that are not marked as done.\n",
        "$ -find /type [type]: Show all tasks that are of [type] (todo/deadline/event).\n",
        "$ -find /tag ",
        "#[tag name]",
        "$: Show all tasks with tag [tag name].\n",
        "You can combine different find commands together, for example:\n",
        "$find /before [date1] /after [date2] /contains [keyword] ",
        "Shows all tasks containing [keyword], before [date1] and after [date2].\n",
        "You can also use:\n",
        "$ -tags: Shows all tags being used and how many tasks each tag has."
    };

    /** List of HERM35 commands for customising the program. */
    private static final String[] CUSTOMISATIONS_TEXT = {
        "If you want to customise your experience, here are some commands:\n",
        "$ -show tags: display tasks with tags.\n",
        "$ -hide tags: display tasks without tags.",
    };

    /** List of HERM35 commands in the case of an erroneous help command. */
    private static final String[] ERROR_COMMAND_TEXT = {
        "Error: Whoops, I don't think I understood your command, but it certainly seems like you want help!\n",
        "Here are the appropriate commands for more help:\n",
        "$ -help: The basic list of commands to get you started.\n",
        "$ -help edit: Learn how to edit a task's tags or mark it as done or undone.\n",
        "$ -help find: Learn all the command words to filtering your task list to find a task.\n",
        "$ -help customisation: Learn the command words to customise your experience."
    };

    private final Section section;

    /** Constructs a HelpCommand object. */
    public HelpCommand(Section section) {
        this.section = section;
    }

    /**
     * {@inheritDoc}
     *
     * Displays the saved help message to the user through the Ui.
     */
    @Override
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        switch (this.section) {
        case BASIC_HELP:
            ui.printMessage(BASIC_HELP_TEXT);
            break;
        case EDITING_TASK:
            ui.printMessage(EDITING_TASK_TEXT);
            break;
        case FINDING_TASKS:
            ui.printMessage(FINDING_TASKS_TEXT);
            break;
        case CUSTOMISATION:
            ui.printMessage(CUSTOMISATIONS_TEXT);
            break;
        case ERROR_COMMAND:
            ui.printMessage(ERROR_COMMAND_TEXT);
            break;
        default:
            break;
        }
    }
}
