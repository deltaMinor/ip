public abstract class Command {

    abstract void execute(TaskList taskList, Storage storage, Ui ui);

    public boolean isExit() {
        return false;
    }
}