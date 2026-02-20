package her.m35;

import java.util.ArrayList;
import java.util.HashMap;

import her.m35.parser.TimePointParser;
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
        ERROR_CONDITION,
        IS_MARKED,
        IS_UNMARKED,
        KEYWORD,
        TAG,
        ON_DATE,
        BEFORE,
        AFTER,
        OF_TYPE
    }

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
        for (String taskString : taskData) {
            Task newTask = Task.dataToTask(taskString.split(","));
            if (newTask != null) {
                taskList.add(newTask);
            }
        }
    }

    /**
     * Returns a message indicating the current number of tasks stored.
     *
     * @return Formatted task count message string.
     */
    public String getCurrentTaskCountMessage() {
        return "You now have " + taskList.size() + " tasks.";
    }

    /**
     * Returns the task at a given index.
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
     * Returns the size of the task list.
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
     * Converts a given filtered task list to a string sequence that can be printed as a message.
     * The tasks will be indexed with their position in the memory task list.
     *
     * @param filteredTaskList Task list to be converted into a message.
     * @return String sequence that can be printed as a message.
     */
    public String[] filteredTaskListToMessage(ArrayList<Task> filteredTaskList) {
        ArrayList<String> listOutput = new ArrayList<>();
        int filteredTaskListIndex = 0;
        for (int i = 0; i < taskList.size(); i++) {
            if (filteredTaskList.get(filteredTaskListIndex) == taskList.get(i)) {
                listOutput.add((i + 1) + ".");
                listOutput.add(taskList.get(i).toString() + " ");
                listOutput.add(taskList.get(i).getTagsDescription());
                listOutput.add("\n");
                filteredTaskListIndex++;
                if (filteredTaskListIndex == filteredTaskList.size()) {
                    listOutput.remove(listOutput.size() - 1);
                    return listOutput.toArray(new String[0]);
                }
            }
        }
        return null;
    }

    /**
     * Applies a sequence of filter conditions to the task list and returns the formatted result.
     *
     * @param filterConditions Array of filter conditions to apply.
     * @param keywords Keywords corresponding to each filter condition.
     * @return Filtered task list formatted as a printable message.
     */
    public String[] outputFilteredList(FilterCondition[] filterConditions, String[] keywords) {
        ArrayList<Task> filteredTaskList = new ArrayList<>(taskList);
        if (filterConditions.length == 0) {
            return new String[] {"Your task list is empty!"};
        }
        for (int i = 0; i < filterConditions.length; i++) {
            String noTasksMessage;
            switch (filterConditions[i]) {
            case IS_MARKED:
                noTasksMessage = "There are no completed tasks.";
                filteredTaskList.removeIf(task -> !task.getIsDone());
                break;
            case IS_UNMARKED:
                noTasksMessage = "There are no uncompleted tasks.";
                filteredTaskList.removeIf(Task::getIsDone);
                break;
            case KEYWORD:
                String keyword = keywords[i].toLowerCase();
                noTasksMessage = String.format("There are no tasks containing \"%s\".", keywords[i]);
                filteredTaskList.removeIf(task -> !task.containsWord(keyword));
                break;
            case TAG:
                String tag = keywords[i];
                noTasksMessage = String.format("There are no tasks containing tag #%s.", tag);
                filteredTaskList.removeIf(task -> !task.hasTag(tag));
                break;
            case ON_DATE:
                TimePoint onTimePoint = TimePointParser.toTimePoint(keywords[i]);
                noTasksMessage = String.format("There are no tasks occurring on %s.", onTimePoint);
                if (onTimePoint.getFormat() == TimePoint.Format.STRING) {
                    return new String[]{"Invalid on date. Recommended format: DD/MM/YYYY"};
                }
                filteredTaskList.removeIf(task -> !task.isOnDate(onTimePoint));
                break;
            case BEFORE:
                TimePoint beforeTimePoint = TimePointParser.toTimePoint(keywords[i]);
                noTasksMessage = String.format("There are no tasks occurring before %s.", beforeTimePoint);
                if (beforeTimePoint.getFormat() == TimePoint.Format.STRING) {
                    return new String[]{"Invalid before date. Recommended format: DD/MM/YYYY"};
                }
                filteredTaskList.removeIf(task -> !task.isBeforeDate(beforeTimePoint));
                break;
            case AFTER:
                TimePoint afterTimePoint = TimePointParser.toTimePoint(keywords[i]);
                noTasksMessage = String.format("There are no tasks occurring after %s.", afterTimePoint);
                if (afterTimePoint.getFormat() == TimePoint.Format.STRING) {
                    return new String[]{"Invalid after date. Recommended format: DD/MM/YYYY"};
                }
                filteredTaskList.removeIf(task -> !task.isAfterDate(afterTimePoint));
                break;
            case OF_TYPE:
                Task.Type targetTaskType = normalizeByTaskName(keywords[i]);
                noTasksMessage = "There are no tasks of type " + keywords[i];
                if (targetTaskType == null) {
                    return new String[]{noTasksMessage};
                }
                filteredTaskList.removeIf(task -> task.getType() != targetTaskType);
                break;
            default:
                return new String[]{"Error: Invalid filter command: " + keywords[i]};
            }
            if (filteredTaskList.isEmpty()) {
                return new String[]{noTasksMessage};
            }
        }
        return filteredTaskListToMessage(filteredTaskList);
    }

    /**
     * Helper function to normalize the keyword for filter by task type command.
     * @param keyword Word to be normalized.
     * @return keyword with its internal words normalized.
     */
    private Task.Type normalizeByTaskName(String keyword) {
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
        return switch (normalisedKeyword) {
        case "T" -> Task.Type.TODO;
        case "D" -> Task.Type.DEADLINE;
        case "E" -> Task.Type.EVENT;
        default -> null;
        };
    }

    /**
     * Returns a hashmap which contains every tag with a count of how many tasks have that tag.
     * @return a hashmap which contains every tag with a count of how many tasks have that tag.
     */
    public HashMap<String, Integer> getTags() {
        HashMap<String, Integer> tags = new HashMap<>();
        for (Task task : taskList) {
            for (String tag : task.getTags()) {
                tags.put(tag, tags.getOrDefault(tag, 0) + 1);
            }
        }
        return tags;
    }

    public String[] getFormattedTaskList() {
        if (taskList.isEmpty()) {
            return new String[] {"Your task list is empty!"};
        }
        String[] formattedTaskList = new String[taskList.size() * 3];
        formattedTaskList[0] = "1.";
        formattedTaskList[1] = taskList.get(0).toString() + " ";
        formattedTaskList[2] = taskList.get(0).getTagsDescription();
        for (int i = 1; i < taskList.size(); i++) {
            formattedTaskList[i * 3] = "\n" + (i + 1) + ".";
            formattedTaskList[i * 3 + 1] = taskList.get(i).toString() + " ";
            formattedTaskList[i * 3 + 2] = taskList.get(i).getTagsDescription();
        }
        return formattedTaskList;
    }

    @Override
    public String toString() {
        return Ui.listToMessage(taskList, "Your task list is empty!");
    }
}
