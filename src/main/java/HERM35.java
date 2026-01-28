import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

public class HERM35 {

    private static final String LINE_SEPARATOR = "-----------------------";
    private static final String TASKLIST_FILE_NAME = "tasklist";
    private static final Storage TASKLIST_STORAGE = new Storage(TASKLIST_FILE_NAME);;

    public static final int TASK_LIMIT = 100;
    private static final ArrayList<Task> taskList = new ArrayList<Task>();

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String name = "HERM35";
        String introduction = "Hey! I'm " + name + "!\nWhat can I do for you?";
        readTaskData();
        printMessage(introduction);
        while (input.hasNextLine()) {
            String[] command = input.nextLine().split(" ", 2);
            switch (command[0]) {
                case "mark":
                    if (command.length < 2) {
                        printMessage("Task index not given.");
                        break;
                    }
                    if (isInteger(command[1])) {
                        int taskIndex = Integer.parseInt(command[1]) - 1;
                        if (taskIndex >= 0 && taskIndex < taskList.size()) {
                            taskList.get(taskIndex).mark(true);
                            editTaskData(taskIndex, taskList.get(taskIndex));
                            printMessage("Sure, I've marked this task as done:\n" + taskList.get(taskIndex));
                            break;
                        }
                    }
                    printMessage(String.format(
                            "Please enter a number between 1 and %d to mark that task.", taskList.size()));
                    break;
                case "unmark":
                    if (command.length < 2) {
                        printMessage("Task index not given.");
                        break;
                    }
                    if (isInteger(command[1])) {
                        int taskIndex = Integer.parseInt(command[1]) - 1;
                        if (taskIndex >= 0 && taskIndex < taskList.size()) {
                            taskList.get(taskIndex).mark(false);
                            editTaskData(taskIndex, taskList.get(taskIndex));
                            printMessage("OK, I've marked this task as not done:\n"
                                    + taskList.get(taskIndex));
                            break;
                        }
                    }
                    printMessage(String.format(
                            "Please enter a number between 1 and %d to mark that task.", taskList.size()));
                    break;
                case "list":
                    if (command.length > 1) {
                        printMessage("Unknown command, please try again. (Did you mean \"list\"?");
                    } else {
                        String listOutput = "";
                        if (taskList.size() > 0) {
                            for (int i = 0; i < taskList.size(); i++) {
                                listOutput += i + 1 + "." + taskList.get(i) + "\n";
                            }
                        } else {
                            listOutput = "Your task list is empty!";
                        }
                        printMessage(listOutput);
                    }
                    break;
                case "bye":
                    exit();
                    return;
                case "delete":
                    if (command.length < 2) {
                        printMessage("Task name not given.");
                        break;
                    }
                    if (isInteger(command[1])) {
                        int taskIndex = Integer.parseInt(command[1]) - 1;
                        if (taskIndex >= 0 && taskIndex < taskList.size()) {
                            String taskDeletedMessage = "Got it, I'm deleting this task:\n"
                                    + taskList.remove(taskIndex) + "\n";
                            taskDeletedMessage += getCurrentTaskCountMessage();
                            printMessage(taskDeletedMessage);
                            deleteTaskData(taskIndex);
                            break;
                        }
                    }
                    printMessage(String.format(
                            "Please enter a number between 1 and %d to delete that task.", taskList.size()));
                    break;
                case "clear":
                    if (command.length > 1) {
                        printMessage("Unknown command, please try again. (Did you mean \"clear\"?");
                    } else {
                        clearTaskData();
                        printMessage("Alright, I have emptied the task list.\n" + getCurrentTaskCountMessage());
                    }
                    break;
                case "todo":
                    if (command.length < 2) {
                        printMessage("Task name not given.");
                    } else {
                        insertTaskData(new ToDoTask(command[1]));
                        printAddedTaskMessage(taskList.size() - 1);
                    }
                    break;
                case "deadline":
                    if (command.length < 2) {
                        printMessage("Task name not given.");
                        break;
                    }
                    String[] deadlineTask = command[1].split(" /by ", 2);
                    if (deadlineTask.length < 2) {
                        printMessage("Please state the deadline, denoted with \" /by \"");
                        break;
                    }
                    insertTaskData(new DeadlineTask(deadlineTask[0], deadlineTask[1]));
                    printAddedTaskMessage(taskList.size() - 1);
                    break;
                case "event":
                    if (command.length < 2) {
                        printMessage("Task name not given.");
                        break;
                    }
                    String[] eventTask = command[1].split(" /from ", 2);
                    if (eventTask.length < 2) {
                        printMessage("Please state when the event begins, denoted with \" /from \"");
                        break;
                    }
                    String[] eventPeriod = eventTask[1].split(" /to ", 2);
                    if (eventPeriod.length < 2) {
                        printMessage("Please state when the event ends, denoted with \" /to \"");
                        break;
                    }
                    insertTaskData(new EventTask(eventTask[0], eventPeriod[0], eventPeriod[1]));
                    printAddedTaskMessage(taskList.size() - 1);
                    break;
                default:
                    printMessage("Unknown command, please try again.");
            }
        }
    }

    public static void printAddedTaskMessage(int taskIndex) {
        String message = "The following task has been added:\n\t"
                + taskList.get(taskIndex) + "\n"
                + getCurrentTaskCountMessage();
        printMessage(message);
    }

    public static String getCurrentTaskCountMessage() {
        return "You now have " + taskList.size() + "/" + TASK_LIMIT + " tasks.";
    }

    public static void printMessage(String message) {
        printLine(LINE_SEPARATOR);
        Scanner reader = new Scanner(message);
        while (reader.hasNextLine()) {
            printLine(reader.nextLine());
        }
        printLine(LINE_SEPARATOR);
    }

    public static void printLine(String line) {
        System.out.println("\t" + line);
    }

    public static void insertTaskData(Task task) {
        taskList.add(task);
        try {
            TASKLIST_STORAGE.insert(task.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteTaskData(int taskIndex) {
        try {
            TASKLIST_STORAGE.delete(taskIndex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearTaskData() {
        taskList.clear();
        try {
            TASKLIST_STORAGE.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void editTaskData(int taskIndex, Task task) {
        try {
            TASKLIST_STORAGE.edit(taskIndex, task.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readTaskData() {
        String[] lines = {};
        try {
            lines = TASKLIST_STORAGE.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (lines.length > 0) {
            taskList.clear();
            for (int i = 0; i < lines.length; i++) {
                Task newTask = Task.dataToTask(lines[i].split(","));
                if (newTask != null) {
                    taskList.add(newTask);
                }
            }
        }
    }

    public static void exit() {
        printMessage("Bye, see you later!");
    }

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        try {
            double i = Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
