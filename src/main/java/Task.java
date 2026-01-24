public class Task {
    private Boolean done;
    private String description;
    private char typeIcon = ' ';

    public Task(String description) {
        done = false;
        this.description = description;
    }

    public Task(String description, char typeIcon) {
        done = false;
        this.description = description;
        this.typeIcon = typeIcon;
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

    @Override
    public String toString() {
        return "[" + typeIcon + "][" + getDoneIcon() + "] " + getDescription();
    }
}
