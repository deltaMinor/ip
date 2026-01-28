public class ToDoTask extends Task {
    private final String name;

    public ToDoTask(String name) {
        super(name, Type.TODO);
        this.name = name;
    }

    public ToDoTask(String name, Boolean done) {
        super(name, Type.TODO, done);
        this.name = name;
    }

    public String[] getData() {
        return new String[] {getType(), getDoneIcon(), name};
    }
}
