import java.util.Scanner;

public class HERM35 {

    private static String command;
    private static final String LINE_SEPARATOR = "-----------------------";

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String name = "HERM35";
        String introduction = "Hey! I'm " + name + "!\nWhat can I do for you?";
        printMessage(introduction);
        while (input.hasNextLine()) {
            command = input.nextLine();
            if (command.equals("bye")) {
                exit();
                break;
            } else {
                printMessage(command);
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
