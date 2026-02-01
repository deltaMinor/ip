package her.m35.task;

import her.m35.TimePoint;

/**
 * Represents a task that begins and ends at specific times.
 */
public class EventTask extends Task {

    /** Viable names to refer to an event task. */
    public static final String[] NAMES = {"EVENT"};

    /** Name, start time and end time of the event. */
    private final String name;
    private final TimePoint fromDate;
    private final TimePoint toDate;

    /**
     * Creates a new uncompleted event task.
     *
     * @param name Name of the event.
     * @param from Start time of the event.
     * @param to   End time of the event.
     */
    public EventTask(String name, TimePoint from, TimePoint to) {
        super(name + " (from: " + from + " to: " + to + ")", Type.EVENT);
        this.name = name;
        this.fromDate = from;
        this.toDate = to;
    }

    /**
     * Creates a new event task with a specified completion status.
     *
     * @param name Name of the event.
     * @param from Start time of the event.
     * @param to   End time of the event.
     * @param isDone Completion status of the task.
     */
    public EventTask(String name, TimePoint from, TimePoint to, Boolean isDone) {
        super(name + " (from: " + from + " to: " + to + ")", Type.EVENT, isDone);
        this.name = name;
        this.fromDate = from;
        this.toDate = to;
    }

    public TimePoint getFromDate() {
        return fromDate;
    }

    public TimePoint getToDate() {
        return toDate;
    }

    @Override
    public String[] getData() {
        return new String[] {getTypeIcon(), getDoneIcon(), name, fromDate.toString(), toDate.toString()};
    }
}
