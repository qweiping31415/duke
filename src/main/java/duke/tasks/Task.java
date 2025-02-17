package duke.tasks;

import duke.core.TaskList;
import duke.errors.DukeAssertions;


/**
 * Represents a task in the application. A task has two private fields, the description of the task and
 * the state of completion of the task. The type of task is package-private and taskList is a
 * class attribute. The Task class provides the getters to type, description, completion status,
 * the task list as well as getting the icon (tick and cross, or 1 and 0)
 * which corresponds to the isDone field. Task class also
 * supports a setDone method which sets isDone field  to true and a setTaskList method which sets
 * taskList field to the main task list of the application.
 */
public abstract class Task {
    private String description;
    private boolean isDone;
    private TaskType type;

    public static TaskList taskList;

    /**
     * Initialises a Task that has a default isDone field of false.
     *
     * @param description Description of the task.
     * @param type Type of the task.
     */
    public Task(String description, TaskType type) {
        this.description = description;
        this.type = type;
        this.isDone = false;

        DukeAssertions.assertNotNull(description, type);

    }

    /**
     * Returns the type of Task
     *
     * @return TaskType
     */
    public TaskType getType() {
        assert type != null;
        return type;
    }

    /**
     * Returns a tick or a cross depending on the field isDone.
     *
     * @return Icon which shows a tick or a cross.
     */
    public String getStatusIcon() {
        return (isDone ? "\u2713" : "\u2718");
    }


    /**
     * Returns a 1 or 0 depending on the field isDone, used for Storage purposes.
     *
     * @return String number which is 1 or 0.
     */
    public String getStorageStatusIcon() {
        return (isDone ? "1" : "0");
    }

    /**
     * Returns the description of the task.
     *
     * @return String that represents the description of the task.
     */
    public String getDescription() {
        assert description != null;
        return description;
    }

    /**
     * Returns a string that includes the status icon and the description of the task.
     *
     * @return String with status icon and description of task.
     */
    public String toString() {
        return String.format("[%s] %s", getStatusIcon(), getDescription());
    }


    /**
     * Returns the type of Task
     *
     * @return Completion status of the task
     */
    public boolean getDoneStatus() {
        return isDone;
    }


    /**
     * Sets the boolean isDone to true and returns the  previous status of the task
     *
     * @return Previous boolean value of isDone.
     */
    public boolean setDone() {
        if (!isDone) {
            this.isDone = true;
            return false;
        } else {
            return true;
        }
    }

    /**
     * Initialises a reference to the task list of the application in the Task class
     */
    public static void setTaskList(TaskList taskList) {
        Task.taskList = taskList;
        assert taskList != null;
    }

    /**
     * Returns the position of the specified task in current list
     *
     * @return Position of the specified task in current list
     */
    public static int getTaskID(Task task) {
        assert taskList != null;
        return Task.taskList.getTaskID(task);
    }
}