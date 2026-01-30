import java.io.IOException;

import java.util.Scanner;
import java.util.ArrayList;

/**
 * Entry point and controller class for the HERM35 chatbot.
 */
public class HERM35 {
    /** Storage object used to store task list. */
    private Storage storage;

    /** List of tasks currently managed by the chatbot. */
    private TaskList taskList;

    /** Name of the chatbot. */
    private static final String NAME = "HERM35";

    /** UI to deal with interactions with the user. */
    private Ui ui;

    public HERM35(String fileName) {
        ui = new Ui();
        storage = new Storage(fileName);
        try {
            taskList = new TaskList(storage.read());
        } catch (IOException e) {
            e.printStackTrace();
            taskList = new TaskList();
        }
        Parser.setup();
    }
    /**
     * Program running entry point.
     */
    public void run() {
        String introduction = "Hey! I'm " + NAME + "!\nWhat can I do for you?";

        ui.printMessage(introduction);
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

    public static void main(String[] args) {
        new HERM35("tasklist").run();
    }
}