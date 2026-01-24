public class Task {
    private Boolean done;
    private String name;

    public Task(String name) {
        done = false;
        this.name = name;
    }

    public void mark(Boolean done) {
        this.done = done;
    }

    public String getDoneIcon() {
        return done ? "X" : " ";
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "[" + getDoneIcon() + "] " + getName();
    }
}
