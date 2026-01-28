public class DeadlineTask extends Task{
    private final String name;
    private final String date;

    public DeadlineTask(String name, String date){
        super(name + " (by: " + date + ")", Type.DEADLINE);
        this.name = name;
        this.date = date;
    }

    public DeadlineTask(String name, String date, Boolean done){
        super(name + " (by: " + date + ")", Type.DEADLINE, done);
        this.name = name;
        this.date = date;
    }

    public String[] getData() {
        return new String[] {getType(), getDoneIcon(), name, date};
    }
}
