package duke.core;

import duke.commands.*;

import duke.errors.DukeAssertions;
import duke.errors.DukeException;
import duke.errors.DukeExceptionType;

import java.util.List;
import java.lang.StringBuilder;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Represents a class that takes in user inputs and translates them into different commands.
 */
public class Parser {

    private static String[] responses = new String[]{"/by","/at"};
    private static int[] startingIndex = new int[]{9,6};

    /**
     * Takes in user input and convert it into a command which performs a set of
     * instructions on the task list and ui.
     *
     * @param input String that contains user input
     * @return A command that execute a set of instructions.
     * @throws IllegalArgumentException Thrown when the length of the command is not sufficient
     * @throws DukeException Thrown when exceptions occur due to non-length checks
     */
    public static Command parseCommand(String input) throws DukeException, IllegalArgumentException {
        String[] tokens = input.split(" ");
        if (tokens.length == 0) {
            return new NullCommand();
        } else if (tokens[0].equals("bye")) {
            return new ExitCommand();
        } else if (tokens[0].equals("list")) {
            return new ListCommand();
        } else if (tokens[0].equals("help")) {
            return new HelpCommand();
        }
        checkValidLength(tokens);

        if (tokens[0].equals("done")) {
            return DoneCommand.createDoneIfValid(tokens);
        } else if (tokens[0].equals("delete")) {
            return DeleteCommand.createDeleteIfValid(tokens);
        } else if (tokens[0].equals("find")) {
            return FindCommand.createFindCommandIfValid(tokens);
        } else {
            return Parser.createAddCommandIfValid(tokens, input);
        }
    }

    /**
     * Throws an exception which tells
     * the user that the input command is not valid, by checking the length of command
     * the user that the input lacks information based on each case
     *
     * @param tokens User input split by space
     * @throws IllegalArgumentException Thrown when the length of the command is not sufficient
     */
    private static void checkValidLength(String[] tokens) throws IllegalArgumentException {
        DukeAssertions.assertArrayNotEmpty(tokens);
        List<String> group1 = List.of("todo", "deadline", "event");
        List<String> group2 = List.of("done", "delete");
        if (tokens.length == 1 && group1.contains(tokens[0])) {
            throw new IllegalArgumentException(String.format("OOPS!!! The description of a %s cannot be empty.",tokens[0]));
        } else if (tokens.length == 1 && group2.contains(tokens[0])) {
            throw new IllegalArgumentException(String.format("OOPS!!! %s command requires integer.",tokens[0]));
        } else if (tokens.length == 1 && tokens[0].equals("find")) {
            throw new IllegalArgumentException(String.format("OOPS!!! %s command requires keyword input.",tokens[0]));
        }
    }

    // helper method to check if user input can still be a valid to-do, deadline or event task
    private static Command createAddCommandIfValid(String[] tokens, String fullCommand) throws DukeException, IllegalArgumentException {
        DukeAssertions.assertArrayNotEmpty(tokens);

        List<String> validCommands = List.of("todo", "deadline", "event");

        if (!validCommands.contains(tokens[0])) {
            throw new DukeException("Command doesn't exist", DukeExceptionType.INVALID_COMMAND);
        }
        if (tokens[0].equals("todo")) {
            return new AddToDoCommand(tokens);
        } else if (tokens[0].equals("deadline")) {
            return createDateCommandIfValid(tokens,fullCommand,0);
        } else if (tokens[0].equals("event")) {
            return createDateCommandIfValid(tokens,fullCommand,1);
        } else {
            return new NullCommand();
        }
    }

    /**
     * Takes in a string and tries to parse input string as Date and Time in dd/MM/yyyy HHmm,
     * converting it into a more readable format
     * eg. dd/MM/yyyy HHmm(e.g. 11/12/1111 1111 -> 11th of DECEMBER 1111, 11:11am)
     *
     * @param dateTimeString String to be parsed, and converted, if possible
     * @return The formatted date and time, if it can be formatted
     * @throws DukeException Thrown when the input cannot be formatted
     */

    //@@author qweiping31415-reused
    //Reused from https://github.com/briyanii/duke/blob/master/src/main/java/duke/command/Parser.java
    // with minor modifications
    public static String parseDateTime(String dateTimeString) throws DukeException {
        assert dateTimeString != null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm");
            LocalDateTime dateAndTime = LocalDateTime.parse(dateTimeString, formatter);

            int year = dateAndTime.getYear();
            String month = dateAndTime.getMonth().toString();
            int day = dateAndTime.getDayOfMonth();
            int hour = dateAndTime.getHour();
            int minute = dateAndTime.getMinute();

            StringBuilder dateTime = new StringBuilder();

            dateTime.append(getFormattedDay(day));
            dateTime.append(" of ");
            dateTime.append(month);
            dateTime.append(" ");
            dateTime.append(year);
            dateTime.append(", ");
            dateTime.append((hour > 12 ? hour - 12 : hour == 0 ? 12 : hour));
            if (minute != 0) {
                dateTime.append(":");
                dateTime.append(minute);
            }
            if (hour < 12) {
                dateTime.append("am");
            } else {
                dateTime.append("pm");
            }

            return dateTime.toString();
        } catch (DateTimeParseException exception) {
            throw new DukeException(dateTimeString + " is not in valid dd/MM/yyyy HHmm format.",
                    DukeExceptionType.INVALID_DATE_TIME_FORMAT);
        }
    }

    // helper method to attach a prefix to a day
    private static String getFormattedDay(int day) {
        assert day > 1;
        int remainderHundred = day % 100;
        if (remainderHundred > 9 && remainderHundred < 21) {
            return day + "th";
        } else {
            int remainderTen = day % 10;
            switch (remainderTen) {
                case 1:
                    return day + "st";
                case 2:
                    return day + "nd";
                case 3:
                    return day + "rd";
                default:
                    return day + "th";
            }
        }
    }
    //@@author



    // helper method to check if the given date and time of a deadline or event task
    // can be recognised as a DateTime format.
    private static Command createDateCommandIfValid(String[] tokens, String fullCommand, int mode)
            throws DukeException, IllegalArgumentException {
        DukeAssertions.assertArrayNotEmpty(tokens);
        assert fullCommand != null;

        List<String> lst = Arrays.asList(tokens);
        String key = responses[mode];
        if (!lst.contains(key)) {
            throw new IllegalArgumentException("Missing deadline keyword!!");
        }

        int index = lst.indexOf(key);
        checkTaskDescription(index);
        String[] datedTaskSplit = fullCommand.split(" " + key + " ");
        checkDeadline(datedTaskSplit);
        String description = datedTaskSplit[0].substring(startingIndex[mode]);
        String dateTime = datedTaskSplit[1];
        if (isDate(dateTime)) {
            String dateTimeString = tokens[index + 1] + " " + tokens[index + 2];
            dateTime = Parser.parseDateTime(dateTimeString);
        }

        return createDateCommand(mode, description, dateTime);
    }
    
    //helper method to create the correct kind of DateTime Command
    private static Command createDateCommand(int mode, String description, String correctDate) {
        if (mode==0) {
            return new AddDeadlineCommand(description,correctDate);
        } else {
            return new AddEventCommand(description,correctDate);
        }
    }

    //helper method to verify that the date is actually in dd/MM/yyyy HHmm format
    private static boolean isDate(String dateDescription){
        assert dateDescription != null;
        String[] dateSplit = dateDescription.split(" ");
        if (dateSplit.length != 2){
            return false;
        } else if (!dateSplit[0].contains("/") ||
                checkSlashCount(dateSplit[0])) {
            return false;
        }
        return true;
    }

    private static boolean checkSlashCount(String str) {
        return str.chars()
                .filter(ch -> ch == '/')
                .count() != 2;
    }

    //helper method to check if the datetime task can have a description
    private static void checkTaskDescription(int index) throws IllegalArgumentException {
        if (index -1 <=0) {
            throw new IllegalArgumentException("Please input task description for DateCommand");
        }

    }

    //helper method to check if the datetime task is given a deadline
    private static void checkDeadline(String [] datedTaskSplit) throws IllegalArgumentException {
        if (datedTaskSplit.length > 2) {
            throw new IllegalArgumentException("Multiple keyword detected!!");
        }
        if (datedTaskSplit.length < 2) {
            throw new IllegalArgumentException("Please insert a due date!!!");
        }
    }



}






