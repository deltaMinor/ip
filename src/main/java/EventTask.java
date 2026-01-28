public class EventTask extends Task{
    private final String name;
    private final String from;
    private final String to;

    public EventTask(String name, String from, String to){
        super(name + " (from: " + from + " to: " + to + ")", Type.EVENT);
        this.name = name;
        this.from = from;
        this.to = to;
    }

    public EventTask(String name, String from, String to, Boolean done){
        super(name + " (from: " + from + " to: " + to + ")", Type.EVENT, done);
        this.name = name;
        this.from = from;
        this.to = to;
    }

    public String[] getData() {
        return new String[] {getType(), getDoneIcon(), name, from, to};
    }
}
