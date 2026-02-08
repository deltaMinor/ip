package her.m35;

import java.io.IOException;

import her.m35.command.Command;
import her.m35.command.MessageCommand;
import her.m35.parser.Parser;

/**
 * Entry point and controller class for the HERM35 chatbot.
 */
public class Herm35 {
    /** Name of the chatbot. */
    private static final String NAME = "HERM35";

    /** Storage object used to store task list. */
    private Storage storage;

    /** List of tasks currently managed by the chatbot. */
    private TaskList taskList;

    /** UI to deal with interactions with the user. */
    private Ui ui;

    /** Opening introduction to the user. */
    private String openingLines = "Hey! I'm " + NAME + "!\nWhat can I do for you?";

    /** Boolean for whether the HERM35 program should be exiting. */
    private boolean isExit = false;

    /**
     * Constructs a HERM35 chatbot instance.
     * Initializes the UI, storage, and task list. If previously saved data
     * cannot be read from storage, an empty task list is created instead.
     * The command parser is also set up during construction.
     *
     * @param fileName The name of the file which stores the task list.
     */
    public Herm35(String fileName) {
        ui = new Ui();
        try {
            storage = new Storage(fileName);
        } catch (IOException e) {
            openingLines += "\nError: " + e.getMessage() + "\n Unable to open file: " + fileName + "for storage.";
        }
        try {
            taskList = new TaskList(storage.read());
        } catch (IOException e) {
            openingLines += "\nError: " + e.getMessage() + "\n Unable to read task list, creating blank task list.";
            taskList = new TaskList();
        }
    }

    /**
     * Construct a new Herm35 instance with the default parameters.
     */
    public Herm35() {
        this("tasklist.csv");
    }

    /**
     * Runs the main interaction loop of the chatbot.
     * Displays an introduction message, then repeatedly reads user input, parses it into a Command,
     * and executes it. The loop terminates when an executed command signals that the program should exit.
     * Any exceptions thrown during command parsing or execution are caught and their messages
     * displayed to the user.
     */
    public void run() {
        new MessageCommand(openingLines).execute(taskList, storage, ui);
        isExit = false;
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
     * Creates a HERM35 chatbot instance using tasklist.csv as the storage file and starts the chatbot by
     * invoking run().
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        new Herm35("tasklist.csv").run();
    }

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        try {
            Command c = Parser.parse(input);
            c.execute(taskList, storage, ui);
            isExit = c.isExit();
        } catch (Exception e) {
            ui.printMessage(e.getMessage());
        }
        return ui.getResponse();
    }

    public boolean getIsExit() {
        return isExit;
    }
}
