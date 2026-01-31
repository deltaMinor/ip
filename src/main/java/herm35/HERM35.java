package herm35;

import herm35.command.MessageCommand;
import herm35.command.Command;

import java.io.IOException;

/**
 * Entry point and controller class for the HERM35 chatbot.
 */
public class HERM35 {
    /** Storage object used to store task list. */
    private Storage storage;

    /** Availability of storage for task list saving. */
    private boolean isStorageAvailable;

    /** List of tasks currently managed by the chatbot. */
    private TaskList taskList;

    /** Name of the chatbot. */
    private static final String NAME = "HERM35";

    /** UI to deal with interactions with the user. */
    private Ui ui;

    /** Opening introduction to the user. */
    private String openingLines = "Hey! I'm " + NAME + "!\nWhat can I do for you?";

    /**
     * Constructs a HERM35 chatbot instance.
     *
     * Initializes the UI, storage, and task list. If previously saved data
     * cannot be read from storage, an empty task list is created instead.
     * The command parser is also set up during construction.
     *
     * @param fileName The name of the file which stores the task list.
     */
    public HERM35(String fileName) {
        ui = new Ui();
        try {
            storage = new Storage(fileName);
            isStorageAvailable = true;
        } catch (IOException e) {
            isStorageAvailable = false;
            openingLines += "\nError: " + e.getMessage() + "\n Unable to open file: " + fileName + "for storage.";
        }
        try {
            taskList = new TaskList(storage.read());
        } catch (IOException e) {
            openingLines += "\nError: " + e.getMessage() + "\n Unable to read task list, creating blank task list.";
            taskList = new TaskList();
        }
        String parserErrorMessage = Parser.setup();
        if (!parserErrorMessage.equals("")) {
            openingLines += "\n" + Parser.setup();
        }
    }

    /**
     * Runs the main interaction loop of the chatbot.
     *
     * Displays an introduction message, then repeatedly reads user input, parses it into a Command,
     * and executes it. The loop terminates when an executed command signals that the program should exit.
     * Any exceptions thrown during command parsing or execution are caught and their messages
     * displayed to the user.
     */
    public void run() {
        new MessageCommand(openingLines).execute(taskList, storage, ui);
        boolean isExit = false;
        while (!isExit) {
            try {
                Command c = Parser.parse(ui.nextLine());
                c.execute(taskList, storage, ui);
                isExit = c.isExit();
            } catch (Exception e) {
                ui.printMessage(e.getMessage());
            }
        }
    }

    /**
     * Main entry point of the application.
     *
     * Creates a HERM35 chatbot instance using tasklist.csv as the storage file and starts the chatbot by
     * invoking run().
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        new HERM35("data/tasklist.csv").run();
    }
}
