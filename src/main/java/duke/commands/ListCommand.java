package duke.commands;


import duke.core.TaskList;
import duke.core.Ui;
import duke.errors.DukeAssertions;


/**
 * Represents a command which contains an execute method that lists the tasks in the task list.
 */
public class ListCommand extends Command {

    /**
     * Initialises the list command
     */
    public ListCommand(){
        super(CommandType.COMMAND_SHOW_LIST);
    }

    /**
     * Lists all the tasks in the task list and prints them out.
     *
     * @param taskList The main task list of the application.
     * @param ui The main user interface of the application.
     */
    @Override
    public String execute(TaskList taskList, Ui ui) {
        DukeAssertions.assertNotNull(taskList, ui);
        return ui.printNumberList(taskList);
    }
}
