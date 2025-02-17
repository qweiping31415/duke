package duke.commands;

import java.io.IOException;

import duke.core.TaskList;
import duke.core.Ui;

import duke.errors.DukeAssertions;
import duke.tasks.ToDo;


/**
 * Represents a command which contains an execute method that adds a to-do task to the task list.
 * The AddToDoCommand object requires the parameters of the task that is to be
 * added to the list.
 */
public class AddToDoCommand extends Command{

    private String [] tokens;

    /**
     * Initialises the add command which contains the parameters of the task to be created
     *
     * @param tokens user input split by space, required for creating a to-do task
     */
    public AddToDoCommand(String [] tokens) {
        super(CommandType.COMMAND_ADD_TODO);
        this.tokens = tokens;

    }

    /**
     * Adds the to-do task to the task list and prints the result.
     *
     * @param taskList The main task list of the application.
     * @param ui The main user interface of the application.
     * @throws IOException Thrown when the new task cannot be added to the file.
     */
    @Override
    public String execute(TaskList taskList, Ui ui) throws IOException {
        ToDo task = ToDo.createToDo(tokens);
        DukeAssertions.assertNotNull(taskList,ui);
        taskList.addToList(task);
        return ui.printAddMessage(task, taskList);
    }
    
}
