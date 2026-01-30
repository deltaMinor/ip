import java.io.IOException;

import java.util.Scanner;
import java.util.ArrayList;

/**
 * Entry point and controller class for the HERM35 chatbot.
 */
public class HERM35 {
    /** Storage object used to store task list. */
    private Storage storage;

    /** Storage object used to load the help text. */
    private static final Storage COMMANDLIST = new Storage("help", ".txt");

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
    }
    /**
     * Program running entry point.
     */
    public void run() {
        Scanner input = new Scanner(System.in);
        String introduction = "Hey! I'm " + NAME + "!\nWhat can I do for you?";
        String helpOutput = "";
        String[] commandListLines = {};
        try {
            commandListLines = COMMANDLIST.read();
        } catch (IOException e) {
            System.out.println("Error: unable to read command list.\n");
            e.printStackTrace();
        }
        for (int i = 0; i < commandListLines.length; i++) {
            helpOutput += commandListLines[i] + "\n";
        }
        ui.printMessage(introduction);
        while (input.hasNextLine()) {
            String[] command = input.nextLine().split(" ", 2);
            switch (command[0]) {
                case "mark":
                    if (command.length < 2) {
                        ui.printMessage("Task index not given.");
                        break;
                    }
                    if (Parser.isInteger(command[1])) {
                        int taskIndex = Integer.parseInt(command[1]) - 1;
                        if (taskIndex >= 0 && taskIndex < taskList.size()) {
                            markTask(taskIndex, true);
                            ui.printMessage("Sure, I've marked this task as done:\n" + taskList.get(taskIndex));
                            break;
                        }
                    }
                    ui.printMessage(String.format(
                            "Please enter a number between 1 and %d to mark that task.", taskList.size()));
                    break;
                case "unmark":
                    if (command.length < 2) {
                        ui.printMessage("Task index not given.");
                        break;
                    }
                    if (Parser.isInteger(command[1])) {
                        int taskIndex = Integer.parseInt(command[1]) - 1;
                        if (taskIndex >= 0 && taskIndex < taskList.size()) {
                            markTask(taskIndex, false);
                            ui.printMessage("OK, I've marked this task as not done:\n"
                                    + taskList.get(taskIndex));
                            break;
                        }
                    }
                    ui.printMessage(String.format(
                            "Please enter a number between 1 and %d to mark that task.", taskList.size()));
                    break;
                case "list":
                    if (command.length > 1) {
                        ui.printMessage("Unknown command, please try again. (Did you mean \"list\"?)");
                    } else {
                        ui.printMessage(taskList.toString());
                    }
                    break;
                case "bye":
                    if (command.length > 1) {
                        ui.printMessage("Unknown command, please try again. (Did you mean \"bye\"?)");
                        break;
                    } else {
                        exit();
                        return;
                    }
                case "delete":
                    if (command.length < 2) {
                        ui.printMessage("Task name not given.");
                        break;
                    }
                    if (Parser.isInteger(command[1])) {
                        int taskIndex = Integer.parseInt(command[1]) - 1;
                        if (taskIndex >= 0 && taskIndex < taskList.size()) {
                            String taskDeletedMessage = "Got it, I'm deleting this task:\n"
                                    + taskList.get(taskIndex) + "\n";
                            deleteTaskData(taskIndex);
                            taskDeletedMessage += taskList.getCurrentTaskCountMessage();
                            ui.printMessage(taskDeletedMessage);
                            break;
                        }
                    }
                    ui.printMessage(String.format(
                            "Please enter a number between 1 and %d to delete that task.", taskList.size()));
                    break;
                case "clear":
                    if (command.length > 1) {
                        ui.printMessage("Unknown command, please try again. (Did you mean \"clear\"?)");
                    } else {
                        clearTaskData();
                        ui.printMessage("Alright, I have emptied the task list.\n" + taskList.getCurrentTaskCountMessage());
                    }
                    break;
                case "todo":
                    if (command.length < 2) {
                        ui.printMessage("Task name not given.");
                    } else {
                        insertTaskData(new ToDoTask(command[1]));
                        printAddedTaskMessage(taskList.size() - 1);
                    }
                    break;
                case "deadline":
                    if (command.length < 2) {
                        ui.printMessage("Task name not given.");
                        break;
                    }
                    String[] deadlineTask = command[1].split(" /by ", 2);
                    if (deadlineTask.length < 2) {
                        ui.printMessage("Please state the deadline, denoted with \" /by \"");
                        break;
                    }
                    insertTaskData(
                            new DeadlineTask(deadlineTask[0], Parser.toDate(deadlineTask[1])));
                    printAddedTaskMessage(taskList.size() - 1);
                    break;
                case "event":
                    if (command.length < 2) {
                        ui.printMessage("Task name not given.");
                        break;
                    }
                    String[] eventTask = command[1].split(" /from ", 2);
                    if (eventTask.length < 2) {
                        ui.printMessage("Please state when the event begins, denoted with \" /from \"");
                        break;
                    }
                    String[] eventPeriod = eventTask[1].split(" /to ", 2);
                    if (eventPeriod.length < 2) {
                        ui.printMessage("Please state when the event ends, denoted with \" /to \"");
                        break;
                    }
                    insertTaskData(
                            new EventTask(
                                    eventTask[0],
                                    Parser.toDate(eventPeriod[0]),
                                    Parser.toDate(eventPeriod[1])));
                    printAddedTaskMessage(taskList.size() - 1);
                    break;
                case "find":
                    if (command.length < 2) {
                        ui.printMessage("Search prompt not given.");
                    }
                    ArrayList<Task> filteredTaskList = new ArrayList<Task>();
                    if (command[1] == "/done") {
                        ui.printMessage(taskList.outputFilteredList(TaskList.FilterCondition.IS_MARKED));
                        break;
                    }
                    if (command[1] == "/todo") {
                        ui.printMessage(taskList.outputFilteredList(TaskList.FilterCondition.IS_UNMARKED));
                    }
                    if (command[1].contains("/on")) {
                        ui.printMessage(
                                taskList.outputFilteredList(
                                        TaskList.FilterCondition.ON_DATE,
                                        command[1].replace("/on ", "")));
                        break;
                    }
                    if (command[1].contains("/before")) {
                        ui.printMessage(
                                taskList.outputFilteredList(
                                            TaskList.FilterCondition.BEFORE,
                                        command[1].replace("/before ", "")));
                        break;
                    }
                    if (command[1].contains("/after")) {
                        ui.printMessage(
                                taskList.outputFilteredList(
                                        TaskList.FilterCondition.AFTER, command[1].replace("/after ", "")));
                        break;
                    }
                    ui.printMessage(
                            taskList.outputFilteredList(TaskList.FilterCondition.KEYWORD, command[1]));
                    break;
                case "help":
                    ui.printMessage(helpOutput);
                    break;
                default:
                    ui.printMessage("Unknown command, please try again.");
            }
        }
    }

    public static void main(String[] args) {
        new HERM35("tasklist").run();
    }

    /**
     * Prints a formatted confirmation message for a newly added task.
     *
     * @param taskIndex Index of the newly added task.
     */
    public void printAddedTaskMessage(int taskIndex) {
        String message = "The following task has been added:\n\t"
                + taskList.get(taskIndex) + "\n"
                + taskList.getCurrentTaskCountMessage();
        ui.printMessage(message);
    }

    /**
     * Adds a task to the in-memory task list and saves it to hard disk.
     *
     * @param task Task to be added.
     */
    public void insertTaskData(Task task) {
        taskList.add(task);
        try {
            storage.insert(task.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a task from the in-memory task list and hard disk by index.
     *
     * @param taskIndex index of the task to delete.
     */
    public void deleteTaskData(int taskIndex) {
        taskList.delete(taskIndex);
        try {
            storage.delete(taskIndex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clears all tasks from memory and hard disk.
     */
    public void clearTaskData() {
        taskList.clear();
        try {
            storage.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Edits an existing task in hard disk.
     *
     * @param taskIndex Index of the task to edit.
     * @param task      Updated task object.
     */
    public void editTaskData(int taskIndex, Task task) {
        try {
            storage.edit(taskIndex, task.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing task's isDone variable.
     *
     * @param taskIndex Index of the task to update.
     * @param isDone New isDone value of task.
     */

    public void markTask(int taskIndex, boolean isDone) {
        taskList.markTask(taskIndex, isDone);
        editTaskData(taskIndex, taskList.get(taskIndex));
    }

    /**
     * Exit procedure of the chatbot.
     */
    public void exit() {
        ui.printMessage("Bye, see you later!");
    }
}