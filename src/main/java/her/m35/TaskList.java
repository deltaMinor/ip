package her.m35;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Predicate;

import her.m35.task.DeadlineTask;
import her.m35.task.EventTask;
import her.m35.task.Task;
import her.m35.task.ToDoTask;

/**
 * Encapsulates a list of tasks currently managed by the application.
 * Provides operations to add, delete, update, filter, and format tasks for display to the user.
 */
public class TaskList {

    /**
     * Enumeration of supported filter conditions for the find command.
     */
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

    /**
     * Constructs an empty task list.
     */
    public TaskList() {
        taskList = new ArrayList<>();
    }

    /**
     * Constructs a task list from stored task data.
     *
     * @param taskData Array of task data strings used to reconstruct tasks.
     */
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
     * @return Task corresponding to given task, if available, else null.
     */
    public Task get(int taskIndex) {
        if (taskIndex < 0 || taskIndex >= taskList.size()) {
            return null;
        }
        return taskList.get(taskIndex);
    }

    /**
     * Get the size of the task list.
     *
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

    /**
     * Applies a sequence of filter conditions to the task list and returns the formatted result.
     *
     * @param filterConditions Array of filter conditions to apply.
     * @param keywords Keywords corresponding to each filter condition.
     * @return Filtered task list formatted as a printable message.
     */
    public String outputFilteredList(FilterCondition[] filterConditions, String[] keywords) {
        ArrayList<Task> filteredTaskList = new ArrayList<Task>(taskList);
        if (filterConditions.length == 0) {
            return "Your task list is empty!";
        }
        String noTasksMessage = "";
        for (int i = 0; i < filterConditions.length; i++) {
            switch (filterConditions[i]) {
            case IS_MARKED:
                filterTasksByPredicate(filteredTaskList, (Task task) -> task.getIsDone());
                if (filterConditions.length == 0) {
                    return "There are no completed tasks.";
                }
                break;
            case IS_UNMARKED:
                filterTasksByPredicate(filteredTaskList, (Task task) -> !task.getIsDone());
                if (filterConditions.length == 0) {
                    return "There are no uncompleted tasks.";
                }
                break;
            case KEYWORD:
                String keyword = keywords[i].toLowerCase();
                filterTasksByPredicate(
                        filteredTaskList, (Task task) -> task.toString().toLowerCase().contains(keyword));
                if (filterConditions.length == 0) {
                    return String.format("There are no tasks containing \"%s\".", keywords[i]);
                }
                break;
            case ON_DATE:
                TimePoint onTimePoint = Parser.toDate(keywords[i]);
                noTasksMessage = String.format("There are no tasks occurring on %s.", onTimePoint);
                switch (onTimePoint.getFormat()) {
                case LOCAL_DATE:
                    Predicate<Task> onDatePredicate = task -> (task.getType() == Task.Type.DEADLINE)
                            ? onTimePoint.isSameDayAs(((DeadlineTask) task).getByDate())
                            : ((task.getType() == Task.Type.EVENT)
                            ? (onTimePoint.isAfter(((EventTask) task).getFromDate())
                            && onTimePoint.isBefore(((EventTask) task).getToDate()))
                            : false);
                    filterTasksByPredicate(filteredTaskList, onDatePredicate);
                    break;
                case LOCAL_DATE_TIME:
                    Predicate<Task> onDateTimePredicate = task -> (task.getType() == Task.Type.DEADLINE)
                            ? onTimePoint.equals(((DeadlineTask) task).getByDate())
                            : ((task.getType() == Task.Type.EVENT)
                            ? (onTimePoint.isAfter(((EventTask) task).getFromDate())
                            && onTimePoint.isBefore(((EventTask) task).getToDate()))
                            : false);
                    filterTasksByPredicate(filteredTaskList, onDateTimePredicate);
                    break;
                default:
                    return "Invalid on date. Recommended format: DD/MM/YYYY";
                }
                if (filterConditions.length == 0) {
                    return noTasksMessage;
                }
                break;
            case BEFORE:
                TimePoint beforeTimePoint = Parser.toDate(keywords[i]);
                noTasksMessage = String.format("There are no tasks occurring before %s.", beforeTimePoint);
                if (beforeTimePoint.getFormat() == TimePoint.Format.STRING) {
                    return "Invalid before date. Recommended format: DD/MM/YYYY";
                }
                Predicate<Task> beforeDatePredicate = task -> task.getType() == Task.Type.DEADLINE
                        ? beforeTimePoint.isAfter(((DeadlineTask) task).getByDate())
                        : task.getType() == Task.Type.EVENT
                        ? beforeTimePoint.isAfter(((EventTask) task).getToDate())
                        : false;
                filterTasksByPredicate(filteredTaskList, beforeDatePredicate);
                if (filterConditions.length == 0) {
                    return noTasksMessage;
                }
                break;
            case AFTER:
                TimePoint afterTimePoint = Parser.toDate(keywords[i]);
                noTasksMessage = String.format("There are no tasks occurring after %s.", afterTimePoint);
                if (afterTimePoint.getFormat() == TimePoint.Format.STRING) {
                    return "Invalid before date. Recommended format: DD/MM/YYYY";
                }
                Predicate<Task> afterDatePredicate = task -> task.getType() == Task.Type.DEADLINE
                        ? afterTimePoint.isBefore(((DeadlineTask) task).getByDate())
                        : task.getType() == Task.Type.EVENT
                        ? afterTimePoint.isBefore(((EventTask) task).getFromDate())
                        : false;
                filterTasksByPredicate(filteredTaskList, afterDatePredicate);
                if (filterConditions.length == 0) {
                    return noTasksMessage;
                }
                break;
            case OF_TYPE:
                String normalisedKeyword = keywords[i].toUpperCase();
                for (String toDoName : ToDoTask.NAMES) {
                    normalisedKeyword = normalisedKeyword.replace(toDoName, "T");
                }
                for (String deadlineName : DeadlineTask.NAMES) {
                    normalisedKeyword = normalisedKeyword.replace(deadlineName, "D");
                }
                for (String eventName : EventTask.NAMES) {
                    normalisedKeyword = normalisedKeyword.replace(eventName, "E");
                }
                String noTasksOfTypeMessage = "There are no tasks of type " + keywords[i];
                Task.Type targetTaskType;
                switch (normalisedKeyword) {
                case "T":
                    targetTaskType = Task.Type.TODO;
                    break;
                case "D":
                    targetTaskType = Task.Type.DEADLINE;
                    break;
                case "E":
                    targetTaskType = Task.Type.EVENT;
                    break;
                default:
                    return noTasksOfTypeMessage;
                }
                filterTasksByPredicate(filteredTaskList, (Task task) -> task.getType() == targetTaskType);
                if (filterConditions.length == 0) {
                    return noTasksOfTypeMessage;
                }
                break;
            default:
                return "Invalid filter command.";
            }
        }
        return filteredTaskListToMessage(filteredTaskList, "There are no tasks fitting your prompt.");
    }

    /**
     * Given an ArrayList of Tasks, filters it by Predicate filterCondition such that only tasks that pass the
     * predicate remain.
     *
     * @param taskArrayList ArrayList of tasks to be filtered.
     * @param filterCondition Predicate which each task must pass to remain in the array list.
     */
    public void filterTasksByPredicate(ArrayList<Task> taskArrayList, Predicate<Task> filterCondition) {
        Iterator<Task> taskListIterator = taskArrayList.iterator();
        while (taskListIterator.hasNext()) {
            Task task = taskListIterator.next();
            if (!filterCondition.test(task)) {
                taskListIterator.remove();
            }
        }
    }

    @Override
    public String toString() {
        return Ui.listToMessage(taskList, "Your task list is empty!");
    }
}
