import java.util.Scanner;

public class HERM35 {

    private static String command;
    private static final String LINE_SEPARATOR = "-----------------------";

    private static Task[] taskList = new Task[100];
    private static int taskListCount = 0;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String name = "HERM35";
        String introduction = "Hey! I'm " + name + "!\nWhat can I do for you?";
        printMessage(introduction);
        while (input.hasNextLine()) {
            command = input.nextLine();
            String words[] = command.split(" ");
            if (words.length == 2) {
                if (isInteger(words[1])) {
                    int taskIndex = Integer.parseInt(words[1]) - 1;
                    if (taskIndex < taskListCount && taskIndex >= 0) {
                        if (words[0].equals("mark")) {
                            taskList[taskIndex].mark(true);
                            printMessage("Sure, I've marked this task as done:\n" + taskList[taskIndex]);
                            continue;
                        } else if (words[0].equals("unmark")) {
                            taskList[taskIndex].mark(false);
                            printMessage("OK, I've marked this task as not done:\n" + taskList[taskIndex]);
                            continue;
                        }
                    }
                }
            }
            switch(command) {
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
                default:
                    taskList[taskListCount] = new Task(command);
                    printMessage("added: " + taskList[taskListCount].getName());
                    taskListCount++;
                    break;
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
