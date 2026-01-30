package herm35;

import herm35.task.Task;
import herm35.task.ToDoTask;
import herm35.task.DeadlineTask;
import herm35.task.EventTask;

import java.util.ArrayList;

public class TaskList {
    public enum FilterCondition {
        IS_MARKED,
        IS_UNMARKED,
        KEYWORD,
        ON_DATE,
        BEFORE,
        AFTER,
        OF_TYPE
    }

    /** Maximum number of tasks allowed. */
    public static final int TASK_LIMIT = 100;

    /** List of tasks being stored. */
    private final ArrayList<Task> taskList;

    public TaskList() {
        taskList = new ArrayList<>();
    }

    public TaskList(String[] taskData) {
        taskList = new ArrayList<>();
        if (taskData.length > 0) {
            taskList.clear();
            for (int i = 0; i < taskData.length; i++) {
                Task newTask = Task.dataToTask(taskData[i].split(","));
                if (newTask != null) {
                    taskList.add(newTask);
                }
            }
        }
    }

    /**
     * Returns a message indicating the current number of tasks stored.
     *
     * @return Formatted task count message string.
     */
    public String getCurrentTaskCountMessage() {
        return "You now have " + taskList.size() + "/" + TASK_LIMIT + " tasks.";
    }

    /**
     * Get the task at a given index.
     *
     * @param taskIndex Index of specified task to get.
     * @return Task corresponding to given task, if available, else null/
     */
    public Task get(int taskIndex) {
        if (taskIndex < 0 || taskIndex >= taskList.size()) {
            return null;
        }
        return taskList.get(taskIndex);
    }

    /**
     * Get the size of the task list.
     * @return Size of the task list.
     */
    public int size() {
        return taskList.size();
    }

    /**
     * Adds a task.
     *
     * @param task Task to be added.
     */
    public void add(Task task) {
        taskList.add(task);
    }

    /**
     * Deletes a task.
     *
     * @param taskIndex index of the task to delete.
     */
    public void delete(int taskIndex) {
        taskList.remove(taskIndex);
    }

    /**
     * Clears all tasks.
     */
    public void clear() {
        taskList.clear();
    }

    /**
     * Updates an existing task's isDone variable.
     *
     * @param taskIndex Index of the task to update.
     * @param isDone New isDone value of task.
     */

    public void markTask(int taskIndex, boolean isDone) {
        taskList.get(taskIndex).mark(isDone);
    }

    /**
     * Convert a given filtered task list to a string sequence that can be printed as a message.
     * The tasks will be indexed with their position in the memory task list.
     *
     * @param filteredTaskList Task list to be converted into a message.
     * @param noTasksMessage Output message if there are no tasks in the task list.
     * @return String sequence that can be printed as a message.
     */
    public String filteredTaskListToMessage(ArrayList<Task> filteredTaskList, String noTasksMessage) {
        String listOutput = "";
        if (!filteredTaskList.isEmpty()) {
            int filteredTaskListIndex = 0;
            for (int i = 0; i < taskList.size(); i++) {
                if (filteredTaskList.get(filteredTaskListIndex) == taskList.get(i)) {
                    listOutput += i + 1 + "." + taskList.get(i).toString() + "\n";
                    filteredTaskListIndex++;
                    if (filteredTaskListIndex == filteredTaskList.size()) {
                        return listOutput;
                    }
                }
            }
        } else {
            return noTasksMessage;
        }
        return listOutput;
    }

    public String outputFilteredList(FilterCondition filterCondition) {
        ArrayList<Task> filteredTaskList = new ArrayList<Task>();
        switch (filterCondition) {
            case IS_MARKED:
                for (int i = 0; i < taskList.size(); i++) {
                    if (taskList.get(i).getIsDone()) {
                        filteredTaskList.add(taskList.get(i));
                    }
                }
                return filteredTaskListToMessage(filteredTaskList,"There are no completed tasks.");
            case IS_UNMARKED:
                for (int i = 0; i < taskList.size(); i++) {
                    if (!taskList.get(i).getIsDone()) {
                        filteredTaskList.add(taskList.get(i));
                    }
                }
                return filteredTaskListToMessage(filteredTaskList,"There are no uncompleted tasks.");
            case KEYWORD:
                return "Search prompt not given.";
            default:
                return "Date not given.";
        }
    }

    public String outputFilteredList(FilterCondition filterCondition, String keyword) {
        ArrayList<Task> filteredTaskList = new ArrayList<Task>();
        String noTasksMessage = "";
        switch (filterCondition) {
            case KEYWORD:
                for (int i = 0; i < taskList.size(); i++) {
                    if (taskList.get(i).toString().toLowerCase().contains(keyword.toLowerCase())) {
                        filteredTaskList.add(taskList.get(i));
                    }
                }
                return filteredTaskListToMessage(
                        filteredTaskList, String.format("There are no tasks containing \"%s\".", keyword));
            case ON_DATE:
                TimePoint onTimePoint = Parser.toDate(keyword);
                noTasksMessage = String.format("There are no tasks occurring on %s.", onTimePoint);
                switch (onTimePoint.getFormat()) {
                    case LOCAL_DATE:
                        for (Task task : taskList) {
                            switch (task.getType()) {
                                case DEADLINE:
                                    if (onTimePoint.isSameDayAs(((DeadlineTask) task).getByDate())) {
                                        filteredTaskList.add(task);
                                    }
                                    break;
                                case EVENT:
                                    TimePoint fromDate = ((EventTask) task).getFromDate();
                                    TimePoint toDate = ((EventTask) task).getToDate();
                                    if (onTimePoint.isAfter(fromDate)
                                            && onTimePoint.isBefore(toDate)) {
                                        filteredTaskList.add(task);
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                        break;
                    case LOCAL_DATE_TIME:
                        for (Task task : taskList) {
                            switch (task.getType()) {
                                case DEADLINE:
                                    if (onTimePoint.equals(((DeadlineTask) task).getByDate())) {
                                        filteredTaskList.add(task);
                                    }
                                    break;
                                case EVENT:
                                    TimePoint fromDate = ((EventTask) task).getFromDate();
                                    TimePoint toDate = ((EventTask) task).getToDate();
                                    if (onTimePoint.isAfter(fromDate)
                                            && onTimePoint.isBefore(toDate)) {
                                        filteredTaskList.add(task);
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                        break;
                    default:
                        noTasksMessage = "Invalid on date. Recommended format: DD/MM/YYYY";
                }
                return filteredTaskListToMessage(filteredTaskList, noTasksMessage);
            case BEFORE:
                TimePoint beforeTimePoint = Parser.toDate(keyword);
                noTasksMessage = String.format(
                        "There are no tasks occurring before %s.", beforeTimePoint);
                switch (beforeTimePoint.getFormat()) {
                    case STRING:
                        noTasksMessage = "Invalid before date. Recommended format: DD/MM/YYYY";
                        break;
                    default:
                        for (Task task : taskList) {
                            switch (task.getType()) {
                                case DEADLINE:
                                    if (beforeTimePoint.isAfter(((DeadlineTask) task).getByDate())) {
                                        filteredTaskList.add(task);
                                    }
                                    break;
                                case EVENT:
                                    if (beforeTimePoint.isAfter(((EventTask) task).getFromDate())) {
                                        filteredTaskList.add(task);
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                        break;
                }
                return filteredTaskListToMessage(filteredTaskList, noTasksMessage);
            case AFTER:
                TimePoint afterTimePoint = Parser.toDate(keyword);
                noTasksMessage = String.format(
                        "There are no tasks occurring after %s.", afterTimePoint);
                switch (afterTimePoint.getFormat()) {
                    case STRING:
                        noTasksMessage = "Invalid after date. Recommended format: DD/MM/YYYY";
                        break;
                    default:
                        for (Task task : taskList) {
                            switch (task.getType()) {
                                case DEADLINE:
                                    if (afterTimePoint.isBefore(((DeadlineTask) task).getByDate())) {
                                        filteredTaskList.add(task);
                                    }
                                    break;
                                case EVENT:
                                    if (afterTimePoint.isBefore(((EventTask) task).getFromDate())) {
                                        filteredTaskList.add(task);
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                        break;
                }
                return filteredTaskListToMessage(filteredTaskList, noTasksMessage);
            case OF_TYPE:
                String normalisedKeyword = keyword.toUpperCase();
                for (String toDoName : ToDoTask.NAMES) {
                    normalisedKeyword = normalisedKeyword.replace(toDoName, "T");
                }
                for (String deadlineName : DeadlineTask.NAMES) {
                    normalisedKeyword = normalisedKeyword.replace(deadlineName, "D");
                }
                for (String eventName : EventTask.NAMES) {
                    normalisedKeyword = normalisedKeyword.replace(eventName, "E");
                }
                String noTasksOfTypeMessage = "There are no tasks of type " + normalisedKeyword;
                switch (normalisedKeyword) {
                    case "T":
                        for (Task task : taskList) {
                            if (task.getType() == Task.Type.TODO) {
                                filteredTaskList.add(task);
                            }
                        }
                        break;
                    case "D":
                        for (Task task : taskList) {
                            if (task.getType() == Task.Type.DEADLINE) {
                                filteredTaskList.add(task);
                            }
                        }
                        break;
                    case "E":
                        for (Task task : taskList) {
                            if (task.getType() == Task.Type.EVENT) {
                                filteredTaskList.add(task);
                            }
                        }
                        break;
                    default:
                        break;
                }
                return filteredTaskListToMessage(filteredTaskList, noTasksOfTypeMessage);
            default:
                return "Invalid filter command.";
        }
    }

    @Override
    public String toString() {
        return Ui.listToMessage(taskList, "Your task list is empty!");
    }
}
