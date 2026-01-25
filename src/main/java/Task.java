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

    @Override
    public String toString() {
        return "[" + getType() + "][" + getDoneIcon() + "] " + getDescription();
    }
}
