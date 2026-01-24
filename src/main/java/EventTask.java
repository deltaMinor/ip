public class EventTask extends Task{
    public EventTask(String name, String from, String to){
        super(name + " (from: " + from + " to: " + to + ")", 'E');
    }
}
