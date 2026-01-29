/**
 * Represents a task with a description, completion and type.
 */
abstract public class Task {

    /**
     * Enumeration of supported task types.
     */
    public enum Type {
        /** A simple task with only a name. */
        TODO,

        /** A task with a deadline. */
        DEADLINE,

        /** A task that begins and ends at given times. */
        EVENT
    }

    /** Indicates whether the task has been completed. */
    private Boolean isDone;

    /** Description of the task. */
    private final String description;

    /** Type of the task. */
    private final Type type;

    /**
     * Creates a new task.
     *
     * @param description Description of the task.
     * @param type        Type of the task.
     */
    public Task(String description, Type type) {
        isDone = false;
        this.description = description;
        this.type = type;
    }

    /**
     * Creates a new task with an explicit completion status.
     *
     * @param description Description of the task.
     * @param type        Type of the task.
     * @param isDone      Completion status of the task.
     */
    public Task(String description, Type type, Boolean isDone) {
        this.description = description;
        this.type = type;
        this.isDone = isDone;
    }

    /**
     * Marks the task as done or not done.
     *
     * @param isDone The completion status of the task.
     */
    public void mark(Boolean isDone) {
        this.isDone = isDone;
    }

    /**
     * Returns a character representing the value of isDone.
     *
     * @return "X" if isDone is true, " " otherwise.
     */
    public String getDoneIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns the task description.
     *
     * @return Description of the task.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns a single-character code representing the task type.
     *
     * @return "T" for TODO, "D" for DEADLINE, "E" for EVENT
     */
    public String getType() {
        switch (type) {
            case TODO:
                return "T";
            case DEADLINE:
                return "D";
            case EVENT:
                return "E";
        }
        return "";
    }

    /**
     * Converts this task into an array of strings.
     *
     * @return Array of strings representing the task's fields
     */
    abstract public String[] getData();

    /**
     * Reconstructs a Task object from stored data.
     *
     * @param data Array of stored task fields in string format
     * @return Corresponding Task instance, or null if the data is invalid
     */
    public static Task dataToTask(String[] data) {
        switch (data[0]) {
            case "T":
                return new ToDoTask(data[2], data[1].equals("X"));
            case "D":
                return new DeadlineTask(data[2], data[3], data[1].equals("X"));
            case "E":
                return new EventTask(data[2], data[3], data[4], data[1].equals("X"));
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return "[" + getType() + "][" + getDoneIcon() + "] " + getDescription();
    }
}
