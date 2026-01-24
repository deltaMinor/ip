public class DeadlineTask extends Task{
    public DeadlineTask(String name, String date){
        super(name + " (by: " + date + ")", 'D');
    }
}
