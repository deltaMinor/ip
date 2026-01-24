import java.util.Scanner;

public class HERM35 {

    private static String command;
    private static final String LINE_SEPARATOR = "-----------------------";

    private static String[] taskList = new String[100];
    private static int taskListCount = 0;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String name = "HERM35";
        String introduction = "Hey! I'm " + name + "!\nWhat can I do for you?";
        printMessage(introduction);
        while (input.hasNextLine()) {
            command = input.nextLine();
            switch(command) {
                case "list":
                    String listOutput = "";
                    for (int i = 0; i < taskListCount; i++) {
                        listOutput += String.valueOf(i+1) + ". " + taskList[i] + "\n";
                    }
                    printMessage(listOutput);
                    break;
                case "bye":
                    exit();
                    return;
                default:
                    taskList[taskListCount] = command;
                    printMessage("added: " + taskList[taskListCount]);
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
}
