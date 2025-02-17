package duke.commands;

import java.io.IOException;

import duke.errors.DukeAssertions;
import duke.errors.DukeException;

import duke.core.TaskList;
import duke.core.Ui;

/**
 * Base class for the other commands. Specifies the abstract method that is required to be implemented
 * by the children commands.
 */
public abstract class Command {
    private CommandType commandType;

    /**
     * Initialises the command of a specific command type
     *
     * @param commandType The type of command to be created.
     */
    public Command(CommandType commandType) {
        this.commandType = commandType;

        DukeAssertions.assertNotNull(commandType);
    }


    /**
     * getter for type of command
     *
     * @return type of command
     */
    public CommandType getCommandType() {
        return commandType;
    }

    /**
     * Executes what the command is suppose to do.
     *
     * @param taskList The main task list of the application.
     * @param ui The main user interface of the application.
     * @throws DukeException Occurs when parts of the command cannot be executed.
     * @throws IOException Thrown when the file update fails.
     */
    public abstract String execute(TaskList taskList, Ui ui) throws DukeException, IOException;

}



