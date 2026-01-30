
/**
 * Represents a task that must be completed by a specific time.
 */
public class DeadlineTask extends Task{

    /** Name and deadline of the task. */
    private final String name;
    private final TimePoint date;

    /**
     * Creates a new uncompleted deadline task.
     *
     * @param name Description of the task.
     * @param date Deadline of the task.
     */
    public DeadlineTask(String name, TimePoint date){
        super(name + " (by: " + date + ")", Type.DEADLINE);
        this.name = name;
        this.date = date;
    }

    /**
     * Creates a new deadline task with a specified completion status.
     *
     * @param name Description of the task.
     * @param date Deadline of the task.
     * @param isDone Completion status of the task.
     */
    public DeadlineTask(String name, TimePoint date, Boolean isDone){
        super(name + " (by: " + date + ")", Type.DEADLINE, isDone);
        this.name = name;
        this.date = date;
    }

    @Override
    public String[] getData() {
        return new String[] {getType(), getDoneIcon(), name, date.toString()};
    }
}
