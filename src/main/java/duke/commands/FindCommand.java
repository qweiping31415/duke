package duke.commands;

import java.util.List;

import duke.core.TaskList;
import duke.core.Ui;
import duke.errors.DukeAssertions;
import duke.errors.DukeException;
import duke.errors.DukeExceptionType;
import duke.tasks.Task;



/**
 * Represents a command which contains an execute method that finds tasks with the matching keyword.
 */
public class FindCommand extends Command {

    private String keyword;




    /**
     * Initialises the find command which contains the keyword
     * where the tasks will be searched against
     *
     * @param keyword Keyword to be searched against
     */
    private FindCommand(String keyword) {
        super(CommandType.COMMAND_FIND_TASK);
        this.keyword = keyword;
        assert keyword != null;
    }

    /**
     * Service for creating a find command that checks for multiple keywords
     * @param tokens User input split by space, required for creating a find command
     * @throws DukeException Thrown when the parameters contains multiple keywords
     */
    public static FindCommand createFindCommandIfValid(String[] tokens) throws DukeException{
        if (tokens.length > 2) {
            throw new DukeException("Must be a single keyword", DukeExceptionType.NOT_SINGLE_WORD);
        }
        return new FindCommand(tokens[1]);
    }

    /**
     * Executes by storing all tasks with descriptions containing the keyword
     * and prints to the user
     * @param taskList The main task list of the application.
     * @param ui The main user interface of the application.
     */
    public String execute(TaskList taskList, Ui ui) {
        DukeAssertions.assertNotNull(taskList, ui);
        List<Task> resultList = taskList.findTasks(this.keyword);
        return ui.printFindResults(resultList);
    }


}

