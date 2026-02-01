package her.m35.task;

/**
 * Represents a simple to-do task.
 *
 * A task.ToDoTask contains only a task name and a completion status.
 */
public class ToDoTask extends Task {

    /** Viable names to refer to a to do task. */
    public static final String[] NAMES = {"TODO", "TO DO"};

    /** Name of the to-do task. */
    private final String name;

    /**
     * Creates a new uncompleted to-do task.
     *
     * @param name Description of the to-do task.
     */
    public ToDoTask(String name) {
        super(name, Type.TODO);
        this.name = name;
    }

    /**
     * Creates a new to-do task with a specified completion status.
     *
     * @param name Name of the to-do task.
     * @param isDone Completion status of the task.
     */
    public ToDoTask(String name, Boolean isDone) {
        super(name, Type.TODO, isDone);
        this.name = name;
    }

    @Override
    public String[] getData() {
        return new String[] {getTypeIcon(), getDoneIcon(), name};
    }
}
