import java.util.Scanner;

public class HERM35 {

    private static String command;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String name = "HERM35";
        System.out.println("Hey! I'm " + name + "!");
        System.out.println("What can I do for you?");
        while (input.hasNextLine()) {
            command = input.nextLine();
            if (command.equals("bye")) {
                exit();
            } else {
                printMessage(command);
            }
        }
    }

    public static void printMessage(String message) {
        System.out.println("-----------------------");
        System.out.println(message);
        System.out.println("-----------------------");
    }

    public static void exit() {
        printMessage("Bye, see you later!");
    }
}
