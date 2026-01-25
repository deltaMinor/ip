public class DeadlineTask extends Task{
    private final String name;
    private final String date;

    public DeadlineTask(String name, String date){
        super(name + " (by: " + date + ")", Type.DEADLINE);
        this.name = name;
        this.date = date;
    }

    public String[] getData() {
        return new String[] {getType(), name, date};
    }
}
