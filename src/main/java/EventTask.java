/**
 * Represents a task that begins and ends at specific times.
 */
public class EventTask extends Task {

    /** Name, start time and end time of the event. */
    private final String name;
    private final TimePoint from;
    private final TimePoint to;

    /**
     * Creates a new uncompleted event task.
     *
     * @param name Name of the event.
     * @param from Start time of the event.
     * @param to   End time of the event.
     */
    public EventTask(String name, TimePoint from, TimePoint to){
        super(name + " (from: " + from + " to: " + to + ")", Type.EVENT);
        this.name = name;
        this.from = from;
        this.to = to;
    }

    /**
     * Creates a new event task with a specified completion status.
     *
     * @param name Name of the event.
     * @param from Start time of the event.
     * @param to   End time of the event.
     * @param isDone Completion status of the task.
     */
    public EventTask(String name, TimePoint from, TimePoint to, Boolean isDone){
        super(name + " (from: " + from + " to: " + to + ")", Type.EVENT, isDone);
        this.name = name;
        this.from = from;
        this.to = to;
    }

    @Override
    public String[] getData() {
        return new String[] {getType(), getDoneIcon(), name, from.toString(), to.toString()};
    }
}