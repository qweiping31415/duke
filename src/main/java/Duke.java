import duke.commands.Command;
import duke.commands.CommandType;

import duke.core.Storage;
import duke.core.Ui;
import duke.core.TaskList;
import duke.core.Parser;

import duke.errors.DukeException;
import duke.tasks.Task;

import java.io.IOException;

/**
 * The driver class to run user interface of Duke. Duke provides commands to add different tasks,
 * list out tasks, marking tasks as done, deleting tasks and storing the tasks into a file for
 * retrieval after reboot
 */
public class Duke {

    private Ui ui;
    private TaskList taskList;


    /**
     * Initialises a new Duke application.
     * @param filePath String of the directory the file is in.
     * @throws IOException Thrown when writing to file fails.
     * @throws DukeException Thrown when parts of the command cannot be executed.
     */
    private Duke(String filePath) throws DukeException, IOException {
        this.ui = new Ui();
        Storage storage = new Storage(filePath);
        this.taskList = new TaskList(storage.load(), storage);
        Task.setTaskList(taskList);
    }

    /**
     * run method which contains the logic of the application. It takes in user input
     * and parses the command. The command is executed accordingly
     * based on their type.
     */
    private void run() throws IOException {
        ui.printWelcomeMessage();
        while (ui.hasInputs()) {
            try {
                String input = ui.readCommand();
                Command command = Parser.parseCommand(input);
                command.execute(taskList, this.ui);
                if (command.getCommandType().equals(CommandType.EXIT)) {
                    break;
                }
            } catch (IllegalArgumentException | DukeException error2 ) {
                ui.printErrorMessage(error2);
            }
        }
    }

    /**
     * Main method to drive the application
     *
     * @param args Placeholder
     * @throws IOException Thrown when writing to file fails.
     * @throws DukeException Thrown when exceptions that arise when running the application
     */
    public static void main(String[] args) throws DukeException, IOException {
        new Duke("/Users/isaac/Desktop/CS2103T+CS2101/CS2103T/duke/data/duke.txt")
        .run();
    }
   
}
