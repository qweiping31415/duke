package duke.commands;

import java.io.IOException;

import duke.core.TaskList;
import duke.core.Ui;

import duke.errors.DukeException;
import duke.errors.DukeExceptionType;

import duke.tasks.Task;

/**
 * Represents a command which contains an execute method that deletes a task to the task list.
 * The DeleteCommand object requires the task number of the task in the list that is to be
 * deleted from the list.
 */

public class DeleteCommand extends Command {

    private int index;


    /**
     * Initialises the command which contains the index of the task to be deleted
     * @param index the index of the task to be deleted
     */
    private DeleteCommand(int index){
        this.commandType = CommandType.DELETE;
        this.index = index;
    }

    /**
     * Service for creating a delete command that checks for number formatting errors
     * @param tokens User input split by space, required for creating a delete command
     * @throws DukeException Thrown when the parameters does not specify the index of  the task
     */
    public static DeleteCommand createDeleteIfValid(String [] tokens) throws DukeException {
        try {
            int index = Integer.parseInt(tokens[1])-1;
            return new DeleteCommand(index);
        } catch (NumberFormatException error) {
            throw new DukeException("Must be integer", DukeExceptionType.NOTINTEGER);
        }
    }

    /**
     * Deletes the specified task from the task list and prints the result.
     *
     * @param taskList The main task list of the application.
     * @param ui The main user interface of the application.
     * @throws IOException Thrown when the task cannot be removed from the file.
     */
    @Override
    public void execute(TaskList taskList, Ui ui) throws IOException {
        try {
            Task task = taskList.getTaskAt(index+1);
            taskList.removeFromList(task);
            ui.printDeletion(task, taskList);
        } catch (IndexOutOfBoundsException error3) {
            ui.printOneLine(new DukeException("No such task", DukeExceptionType.MISSINGTASK).getMessage());
        }
    }

}
