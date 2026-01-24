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

    public String toString() {
        return "[" + (done ? "X" : " ") + "]" + name;
    }
}
