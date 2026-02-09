package her.m35.task;

import java.util.ArrayList;
import java.util.Arrays;

import her.m35.parser.TimePointParser;

/**
 * Represents a task with a description, completion and type.
 */
public abstract class Task {

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

    /** Tags attached to this task. */
    private final ArrayList<String> tags;

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
        this.tags = new ArrayList<>();
    }

    /**
     * Creates a new task with a list of tags.
     * @param description Description of the task.
     * @param type        Type of the task.
     * @param tags        Tags that are to be attached to the task.
     */
    public Task(String description, Type type, String[] tags) {
        isDone = false;
        this.description = description;
        this.type = type;
        this.tags = new ArrayList<>(Arrays.asList(tags));
    }

    /**
     * Creates a new task with an explicit completion status.
     *
     * @param description Description of the task.
     * @param type        Type of the task.
     * @param isDone      Completion status of the task.
     */
    public Task(String description, Type type, boolean isDone) {
        this.description = description;
        this.type = type;
        this.isDone = isDone;
        this.tags = new ArrayList<>();
    }

    /**
     * Creates a new task with an explicit completion status and a list of tags.
     *
     * @param description Description of the task.
     * @param type        Type of the task.
     * @param tags        Tags that are to be attached to the task.
     * @param isDone      Completion status of the task.
     */
    public Task(String description, Type type, String[] tags, boolean isDone) {
        this.description = description;
        this.type = type;
        this.tags = new ArrayList<>(Arrays.asList(tags));
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

    public boolean getIsDone() {
        return isDone;
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

    public String getTagsDescription() {
        if (tags.isEmpty()) {
            return "";
        }
        return "#" + String.join(", #", tags);
    }

    public String getTags() {
        return String.join("/", tags);
    }

    /**
     * Adds a tag to the list of tags.
     * @param tag Tag to be added.
     */
    public void addTag(String tag) {
        tags.add(tag);
    }

    /**
     * Removes a tag from the list of tags.
     * @param tag Tag to be removed.
     */
    public void removeTag(String tag) {
        tags.remove(tag);
    }

    /**
     * Checks if this task has a certain tag.
     * @param tag Tag to check for.
     * @return True only if this tasks contains the given tag.
     */
    public boolean hasTag(String tag) {
        for (String t : tags) {
            if (t.equals(tag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Clears all tags from the task.
     */
    public void clearTags() {
        tags.clear();
    }

    public Type getType() {
        return type;
    }

    /**
     * Returns a single-character code representing the task type.
     *
     * @return "T" for TODO, "D" for DEADLINE, "E" for EVENT.
     */
    @SuppressWarnings("checkstyle:Indentation")
    public String getTypeIcon() {
        return switch (type) {
        case TODO -> "T";
        case DEADLINE -> "D";
        case EVENT -> "E";
        default -> "";
        };
    }

    /**
     * Converts this task into an array of strings.
     *
     * @return Array of strings representing the task's fields.
     */
    public abstract String[] getData();

    /**
     * Reconstructs a Task object from stored data.
     *
     * @param data Array of stored task fields in string format.
     * @return Corresponding Task instance, or null if the data is invalid.
     */
    public static Task dataToTask(String[] data) {
        switch (data[0]) {
        case "T":
            if (data.length == 3) {
                return new ToDoTask(data[2], data[1].equals("X"));
            }
            return new ToDoTask(data[2], data[3].split("/"), data[1].equals("X"));
        case "D":
            if (data.length == 4) {
                return new DeadlineTask(
                        data[2], TimePointParser.toDate(data[3]), data[1].equals("X"));
            }
            return new DeadlineTask(
                    data[2], TimePointParser.toDate(data[3]), data[4].split("/"), data[1].equals("X"));
        case "E":
            if (data.length == 5) {
                return new EventTask(
                        data[2],
                        TimePointParser.toDate(data[3]),
                        TimePointParser.toDate(data[4]),
                        data[1].equals("X"));
            }
            return new EventTask(
                    data[2],
                    TimePointParser.toDate(data[3]),
                    TimePointParser.toDate(data[4]),
                    data[5].split("/"),
                    data[1].equals("X"));
        default:
            return null;
        }
    }

    @Override
    public String toString() {
        return "[" + getTypeIcon() + "][" + getDoneIcon() + "] " + getDescription() + " " + getTagsDescription();
    }
}
