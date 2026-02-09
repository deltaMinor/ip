package her.m35.task;

import her.m35.TimePoint;

/**
 * Represents a task that must be completed by a specific time.
 */
public class DeadlineTask extends Task {

    /** Viable names to refer to a DEADLINE task. */
    public static final String[] NAMES = {"DEADLINE", "DEAD LINE", "DEAD"};

    /** Name and deadline of the task. */
    private final String name;
    private final TimePoint byDate;

    /**
     * Creates a new uncompleted deadline task.
     *
     * @param name Description of the task.
     * @param date Deadline of the task.
     */
    public DeadlineTask(String name, TimePoint date) {
        super(name + " (by: " + date + ")", Type.DEADLINE);
        this.name = name;
        this.byDate = date;
    }

    /**
     * Creates a new uncompleted deadline task with a list of tags/
     *
     * @param name Description of the task.
     * @param date Deadline of the task.
     * @param tags Tags to be attached to the task.
     */
    public DeadlineTask(String name, TimePoint date, String[] tags) {
        super(name + " (by: " + date + ")", Type.DEADLINE, tags);
        this.name = name;
        this.byDate = date;
    }

    /**
     * Creates a new deadline task with a specified completion status.
     *
     * @param name Description of the task.
     * @param date Deadline of the task.
     * @param isDone Completion status of the task.
     */
    public DeadlineTask(String name, TimePoint date, Boolean isDone) {
        super(name + " (by: " + date + ")", Type.DEADLINE, isDone);
        this.name = name;
        this.byDate = date;
    }

    /**
     * Creates a new deadline task with a specified completion status and a list of tags.
     *
     * @param name Description of the task.
     * @param date Deadline of the task.
     * @param tags Tags to be attached to the task.
     * @param isDone Completion status of the task.
     */
    public DeadlineTask(String name, TimePoint date, String[] tags, Boolean isDone) {
        super(name + " (by: " + date + ")", Type.DEADLINE, tags, isDone);
        this.name = name;
        this.byDate = date;
    }

    public TimePoint getByDate() {
        return byDate;
    }

    @Override
    public String[] getData() {
        return new String[] {getTypeIcon(), getDoneIcon(), name, byDate.toString(), getTagsData()};
    }
}
