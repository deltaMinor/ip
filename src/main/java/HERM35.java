import java.util.Scanner;

public class HERM35 {

    private static final String LINE_SEPARATOR = "-----------------------";

    private static Task[] taskList = new Task[100];
    private static int taskListCount = 0;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String name = "HERM35";
        String introduction = "Hey! I'm " + name + "!\nWhat can I do for you?";
        printMessage(introduction);
        while (input.hasNextLine()) {
            String command[] = input.nextLine().split(" ", 2);
            switch (command[0]) {
                case "mark":
                    if (isInteger(command[1])) {
                        int taskIndex = Integer.parseInt(command[1]) - 1;
                        if (taskIndex >= 0 && taskIndex < taskListCount) {
                            taskList[taskIndex].mark(true);
                            printMessage("Sure, I've marked this task as done:\n" + taskList[taskIndex]);
                            break;
                        }
                    }
                    printMessage(String.format(
                            "Please enter a number between 1 and %d to mark that task.", taskListCount));
                    break;
                case "unmark":
                    if (isInteger(command[1])) {
                        int taskIndex = Integer.parseInt(command[1]) - 1;
                        if (taskIndex >= 0 && taskIndex < taskListCount) {
                            taskList[taskIndex].mark(false);
                            printMessage("OK, I've marked this task as not done:\n" + taskList[taskIndex]);
                            break;
                        }
                    }
                    printMessage(String.format(
                            "Please enter a number between 1 and %d to mark that task.", taskListCount));
                    break;
                case "list":
                    String listOutput = "";
                    for (int i = 0; i < taskListCount; i++) {
                        listOutput += String.valueOf(i+1) + "." + taskList[i] + "\n";
                    }
                    printMessage(listOutput);
                    break;
                case "bye":
                    exit();
                    return;
                case "todo":
                    taskList[taskListCount] = new ToDoTask(command[1]);
                    printMessage("added: " + taskList[taskListCount].getName());
                    taskListCount++;
                    break;
                default:
                    printMessage("Unknown command, please try again.");
            }
        }
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
