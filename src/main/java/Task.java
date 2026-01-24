public class Task {
    private Boolean done;
    private String name;
    private char typeIcon = ' ';

    public Task(String name) {
        done = false;
        this.name = name;
    }

    public Task(String name, char typeIcon) {
        done = false;
        this.name = name;
        this.typeIcon = typeIcon;
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
        return "[" + typeIcon + "][" + getDoneIcon() + "] " + getName();
    }
}
