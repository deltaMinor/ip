import java.io.IOException;

import java.util.Scanner;
import java.util.ArrayList;

/**
 * Entry point and controller class for the HERM35 chatbot.
 */
public class HERM35 {

    /** Separator string used when printing messages. */
    private static final String LINE_SEPARATOR = "-----------------------";

    /** Filename of task data storage. */
    private static final String TASKLIST_FILE_NAME = "tasklist";

    /** Storage object used to store task list. */
    private static final Storage TASKLIST_STORAGE = new Storage(TASKLIST_FILE_NAME);

    /** Storage object used to load the help text. */
    private static final Storage COMMANDLIST = new Storage("help", ".txt");

    /** Maximum number of tasks allowed. */
    public static final int TASK_LIMIT = 100;

    /** List of tasks currently managed by the chatbot. */
    private static final ArrayList<Task> taskList = new ArrayList<Task>();

    /** Name of the chatbot. */
    private static final String NAME = "HERM35";

    /**
     * Program entry point.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String introduction = "Hey! I'm " + NAME + "!\nWhat can I do for you?";
        readTaskData();
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
        printMessage(introduction);
        while (input.hasNextLine()) {
            String[] command = input.nextLine().split(" ", 2);
            switch (command[0]) {
                case "mark":
                    if (command.length < 2) {
                        printMessage("Task index not given.");
                        break;
                    }
                    if (Parser.isInteger(command[1])) {
                        int taskIndex = Integer.parseInt(command[1]) - 1;
                        if (taskIndex >= 0 && taskIndex < taskList.size()) {
                            markTask(taskIndex, true);
                            printMessage("Sure, I've marked this task as done:\n" + taskList.get(taskIndex));
                            break;
                        }
                    }
                    printMessage(String.format(
                            "Please enter a number between 1 and %d to mark that task.", taskList.size()));
                    break;
                case "unmark":
                    if (command.length < 2) {
                        printMessage("Task index not given.");
                        break;
                    }
                    if (Parser.isInteger(command[1])) {
                        int taskIndex = Integer.parseInt(command[1]) - 1;
                        if (taskIndex >= 0 && taskIndex < taskList.size()) {
                            markTask(taskIndex, false);
                            printMessage("OK, I've marked this task as not done:\n"
                                    + taskList.get(taskIndex));
                            break;
                        }
                    }
                    printMessage(String.format(
                            "Please enter a number between 1 and %d to mark that task.", taskList.size()));
                    break;
                case "list":
                    if (command.length > 1) {
                        printMessage("Unknown command, please try again. (Did you mean \"list\"?)");
                    } else {
                        String listOutput = "";
                        if (taskList.size() > 0) {
                            for (int i = 0; i < taskList.size(); i++) {
                                listOutput += i + 1 + "." + taskList.get(i) + "\n";
                            }
                        } else {
                            listOutput = "Your task list is empty!";
                        }
                        printMessage(listOutput);
                    }
                    break;
                case "bye":
                    if (command.length > 1) {
                        printMessage("Unknown command, please try again. (Did you mean \"list\"?)");
                        break;
                    } else {
                        exit();
                        return;
                    }
                case "delete":
                    if (command.length < 2) {
                        printMessage("Task name not given.");
                        break;
                    }
                    if (Parser.isInteger(command[1])) {
                        int taskIndex = Integer.parseInt(command[1]) - 1;
                        if (taskIndex >= 0 && taskIndex < taskList.size()) {
                            String taskDeletedMessage = "Got it, I'm deleting this task:\n"
                                    + taskList.get(taskIndex) + "\n";
                            deleteTaskData(taskIndex);
                            taskDeletedMessage += getCurrentTaskCountMessage();
                            printMessage(taskDeletedMessage);
                            break;
                        }
                    }
                    printMessage(String.format(
                            "Please enter a number between 1 and %d to delete that task.", taskList.size()));
                    break;
                case "clear":
                    if (command.length > 1) {
                        printMessage("Unknown command, please try again. (Did you mean \"clear\"?");
                    } else {
                        clearTaskData();
                        printMessage("Alright, I have emptied the task list.\n" + getCurrentTaskCountMessage());
                    }
                    break;
                case "todo":
                    if (command.length < 2) {
                        printMessage("Task name not given.");
                    } else {
                        insertTaskData(new ToDoTask(command[1]));
                        printAddedTaskMessage(taskList.size() - 1);
                    }
                    break;
                case "deadline":
                    if (command.length < 2) {
                        printMessage("Task name not given.");
                        break;
                    }
                    String[] deadlineTask = command[1].split(" /by ", 2);
                    if (deadlineTask.length < 2) {
                        printMessage("Please state the deadline, denoted with \" /by \"");
                        break;
                    }
                    insertTaskData(
                            new DeadlineTask(deadlineTask[0], Parser.toDate(deadlineTask[1])));
                    printAddedTaskMessage(taskList.size() - 1);
                    break;
                case "event":
                    if (command.length < 2) {
                        printMessage("Task name not given.");
                        break;
                    }
                    String[] eventTask = command[1].split(" /from ", 2);
                    if (eventTask.length < 2) {
                        printMessage("Please state when the event begins, denoted with \" /from \"");
                        break;
                    }
                    String[] eventPeriod = eventTask[1].split(" /to ", 2);
                    if (eventPeriod.length < 2) {
                        printMessage("Please state when the event ends, denoted with \" /to \"");
                        break;
                    }
                    insertTaskData(
                            new EventTask(
                                    eventTask[0],
                                    Parser.toDate(eventPeriod[0]),
                                    Parser.toDate(eventPeriod[1])));
                    printAddedTaskMessage(taskList.size() - 1);
                    break;
                case "help":
                    printMessage(helpOutput);
                    break;
                default:
                    printMessage("Unknown command, please try again.");
            }
        }
    }

    /**
     * Prints a formatted confirmation message for a newly added task.
     *
     * @param taskIndex Index of the newly added task.
     */
    public static void printAddedTaskMessage(int taskIndex) {
        String message = "The following task has been added:\n\t"
                + taskList.get(taskIndex) + "\n"
                + getCurrentTaskCountMessage();
        printMessage(message);
    }

    /**
     * Returns a message indicating the current number of tasks stored.
     *
     * @return Formatted task count message string.
     */
    public static String getCurrentTaskCountMessage() {
        return "You now have " + taskList.size() + "/" + TASK_LIMIT + " tasks.";
    }

    /**
     * Prints the message given in formatting.
     *
     * @param message Message to be printed.
     */
    public static void printMessage(String message) {
        printLine(LINE_SEPARATOR);
        Scanner reader = new Scanner(message);
        while (reader.hasNextLine()) {
            printLine(reader.nextLine());
        }
        printLine(LINE_SEPARATOR);
    }

    /**
     * Prints a single line with indentation.
     *
     * @param line Line to be printed.
     */
    public static void printLine(String line) {
        System.out.println("\t" + line);
    }

    /**
     * Adds a task to the in-memory task list and saves it to hard disk.
     *
     * @param task Task to be added.
     */
    public static void insertTaskData(Task task) {
        taskList.add(task);
        try {
            TASKLIST_STORAGE.insert(task.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a task from the in-memory task list and hard disk by index.
     *
     * @param taskIndex index of the task to delete.
     */
    public static void deleteTaskData(int taskIndex) {
        taskList.remove(taskIndex);
        try {
            TASKLIST_STORAGE.delete(taskIndex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clears all tasks from memory and hard disk.
     */
    public static void clearTaskData() {
        taskList.clear();
        try {
            TASKLIST_STORAGE.clear();
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
    public static void editTaskData(int taskIndex, Task task) {
        try {
            TASKLIST_STORAGE.edit(taskIndex, task.getData());
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

    public static void markTask(int taskIndex, boolean isDone) {
        taskList.get(taskIndex).mark(isDone);
        editTaskData(taskIndex, taskList.get(taskIndex));
    }

    /**
     * Loads task data from hard disk into memory.
     */
    public static void readTaskData() {
        String[] lines = {};
        try {
            lines = TASKLIST_STORAGE.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (lines.length > 0) {
            taskList.clear();
            for (int i = 0; i < lines.length; i++) {
                Task newTask = Task.dataToTask(lines[i].split(","));
                if (newTask != null) {
                    taskList.add(newTask);
                }
            }
        }
    }

    /**
     * Exit procedure of the chatbot.
     */
    public static void exit() {
        printMessage("Bye, see you later!");
    }
}