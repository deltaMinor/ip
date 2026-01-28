abstract public class Task {
    public enum Type {
        TODO,
        DEADLINE,
        EVENT
    }

    private Boolean done;
    private final String description;
    private final Type type;

    public Task(String description, Type type) {
        done = false;
        this.description = description;
        this.type = type;
    }

    public Task(String description, Type type, Boolean done) {
        this.description = description;
        this.type = type;
        this.done = done;
    }

    public void mark(Boolean done) {
        this.done = done;
    }

    public String getDoneIcon() {
        return done ? "X" : " ";
    }

    public String getDescription() {
        return description;
    }

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

    abstract public String[] getData();

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
