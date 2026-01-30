import java.util.ArrayList;
import java.util.Scanner;

public class Ui {

    /** Separator string used when printing messages. */
    private static final String LINE_SEPARATOR = "-----------------------";

    /** Scanner to receive user input. */
    private Scanner input;

    public Ui() {
        input = new Scanner(System.in);
    }

    public boolean hasNextLine() {
        return input.hasNextLine();
    }

    public String nextLine() {
        return input.nextLine();
    }
    /**
     * Prints a single line with indentation.
     *
     * @param line Line to be printed.
     */
    public void printLine(String line) {
        System.out.println("\t" + line);
    }

    /**
     * Prints the message given in formatting.
     *
     * @param message Message to be printed.
     */
    public void printMessage(String message) {
        printLine(LINE_SEPARATOR);
        Scanner reader = new Scanner(message);
        while (reader.hasNextLine()) {
            printLine(reader.nextLine());
        }
        printLine(LINE_SEPARATOR);
    }

    /**
     * Convert a given list to a string sequence that can be printed as a message.
     *
     * @param list List to be converted into a message.
     * @param emptyListMessage Output message if there are no items in the task list.
     * @return String sequence that can be printed as a message.
     */
    public static String listToMessage(ArrayList<? extends Object> list, String emptyListMessage) {
        String listOutput = "";
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                listOutput += i + 1 + "." + list.get(i).toString() + "\n";
            }
        } else {
            return emptyListMessage;
        }
        return listOutput;
    }
}
