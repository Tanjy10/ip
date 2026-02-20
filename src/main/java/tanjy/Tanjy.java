package tanjy;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;

import tanjy.exception.TanjyException;
import tanjy.parser.CommandType;
import tanjy.parser.Parser;
import tanjy.storage.Storage;
import tanjy.task.Task;
import tanjy.task.TaskList;
import tanjy.ui.Ui;


/**
 * The main class of the Tanjy chatbot application.
 * Initialises program, coordinates user input, manages tasks,
 * storage, and output display.
 * Runs a command-processing loop until the user exits the program.
 */
public class Tanjy {
    private static final String TOKEN_BY = "/by";
    private static final String TOKEN_FROM = "/from";
    private static final String TOKEN_TO = "/to";

    private boolean isExit = false;
    private String welcomeMessage;

    private final Ui ui;
    private final Storage storage;
    private final Parser parser;
    private final TaskList taskList;
    /**
     * Constructs the chatbot with the specified filePath.
     *
     * @param filePath FilePath containing the list of tasks.
     */
    public Tanjy(String filePath) throws TanjyException {
        ui = new Ui();
        storage = new Storage(Paths.get(filePath));
        parser = new Parser();
        taskList = new TaskList();
        welcomeMessage = ui.getIntro();
        initFromStorage();
    }
    /**
     * Runs the chatbot.
     */
    public void run() {
        ui.printIntro();

        while (!isExit()) {
            String input = ui.readCommand();
            String response = getResponse(input);
            ui.print(response);
        }
    }

    private void initFromStorage() throws TanjyException {
        try {
            if (storage.doesFileExist()) {
                storage.loadSavedFile();
                parser.stringListToTaskList(taskList.getTaskList(), storage.getSavedList());
                welcomeMessage += ui.getLoadSuccessMessage();
            } else {
                storage.createNewFile();
                welcomeMessage += ui.getCreatedNewFileMessage();
            }
        } catch (IOException e) {
            welcomeMessage += ui.getLoadFailMessage() + " (" + e.getMessage() + ")\n";
        }
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public boolean isExit() {
        return isExit;
    }


    /**
     * Generates a response for a single line of user input.
     *
     * @param input User command
     * @return Response to be displayed to the user
     */
    public String getResponse(String input) {
        String text = input.trim();
        if (text.isEmpty()) {
            return "Please type a command.\n";
        }

        String[] parts = text.split("\\s+", 2);
        CommandType command = CommandType.from(parts[0]);
        String remainder = parts.length > 1 ? parts[1].trim() : "";

        try {
            switch (command) {
            case BYE:
                isExit = true;
                return ui.getOutro();

            case LIST:
                return ui.getListMessage(taskList.getTaskList());

            case MARK:
                return handleMark(remainder, true);

            case UNMARK:
                return handleMark(remainder, false);

            case TODO:
                return handleTodo(remainder);

            case DEADLINE:
                return handleDeadline(remainder);

            case EVENT:
                return handleEvent(remainder);

            case DELETE:
                return handleDelete(remainder);

            case SAVE:
                return handleSave();

            case FIND:
                return handleFind(remainder);

            case SNOOZE:
                return handleSnooze(remainder);

            default:
                throw new TanjyException("Huh? No such command. Enter something else!");
            }
        } catch (TanjyException e) {
            return e.getMessage() + "\n";
        }
    }

    private String handleMark(String remainder, boolean isMark) throws TanjyException {
        int inputNumber = parseTaskIndex1Based(remainder);
        int index = inputNumber - 1;
        if (isMark) {
            taskList.markTask(index);
            return ui.getMarkSuccessMessage(taskList.getTask(index));
        } else {
            taskList.unmarkTask(index);
            return ui.getUnmarkSuccessMessage(taskList.getTask(index));
        }
    }

    private String handleTodo(String remainder) throws TanjyException {
        if (remainder.isBlank()) {
            throw new TanjyException("Todo description cannot be empty.");
        }
        taskList.addTodo(remainder.trim());
        return ui.getAddSuccessMessage(taskList.getMostRecentTask(), taskList.size());
    }

    private String handleDeadline(String remainder) throws TanjyException {
        if (remainder.isBlank()) {
            throw new TanjyException("Deadline description cannot be empty.");
        }
        String[] details = remainder.split(TOKEN_BY, 2);
        if (details.length == 1) {
            throw new TanjyException("You need to set a deadline by adding '/by' after the task!");
        }
        LocalDateTime by = parser.parseDateTime(details[1].trim());
        taskList.addDeadline(details[0].trim(), by);
        return ui.getAddSuccessMessage(taskList.getMostRecentTask(), taskList.size());
    }

    private String handleEvent(String remainder) throws TanjyException {
        if (remainder.isBlank()) {
            throw new TanjyException("Event description cannot be empty.");
        }
        String[] details = remainder.split(TOKEN_FROM, 2);
        if (details.length == 1) {
            throw new TanjyException("You need to set a start date by adding '/from' after the task!");
        }
        String[] timeRange = details[1].split(TOKEN_TO, 2);
        if (timeRange.length == 1) {
            throw new TanjyException("You need to set an end date by adding '/to' after '/from'!");
        }
        LocalDateTime from = parser.parseDateTime(timeRange[0].trim());
        LocalDateTime to = parser.parseDateTime(timeRange[1].trim());
        taskList.addEvent(details[0].trim(), from, to);
        return ui.getAddSuccessMessage(taskList.getMostRecentTask(), taskList.size());
    }

    private String handleDelete(String remainder) throws TanjyException {
        int inputNumber = parseTaskIndex1Based(remainder);
        int index = inputNumber - 1;
        Task deleted = taskList.getTask(index);
        taskList.delete(index);
        return ui.getDeleteSuccessMessage(deleted, taskList.size());
    }

    private String handleSave() {
        try {
            storage.updateSavedList(taskList.getTaskList());
            storage.saveFile();
            return ui.getSaveSuccessMessage();
        } catch (IOException e) {
            return ui.getSaveFailMessage();
        }
    }
    private String formatMatchingTasks(ArrayList<Task> matches) {
        StringBuilder sb = new StringBuilder();
        sb.append("Here are the matching tasks in your list:\n");
        for (int i = 0; i < matches.size(); i++) {
            sb.append(i + 1).append(".").append(matches.get(i)).append("\n");
        }
        return sb.toString();
    }

    private String handleFind(String remainder) throws TanjyException {
        if (remainder.isBlank()) {
            throw new TanjyException("Give a keyword to search for!");
        }
        ArrayList<Task> matches = taskList.findTasks(remainder);
        return formatMatchingTasks(matches);
    }

    private String handleSnooze(String remainder) throws TanjyException {
        if (remainder.isBlank()) {
            throw new TanjyException("Usage: snooze <index> /by <date> OR snooze <index> /from <start> /to <end>");
        }

        int inputNumber = parseTaskIndex1Based(remainder);
        int index = inputNumber - 1;
        String[] parts = remainder.split("\\s+", 2);
        String args = parts.length > 1 ? parts[1].trim() : "";
        if (args.isBlank()) {
            throw new TanjyException("You need to specify a new time using /by or /from ... /to ...");
        }
        if (args.contains(TOKEN_BY)) {
            String[] details = args.split(TOKEN_BY, 2);
            if (details.length == 1 || details[1].isBlank()) {
                throw new TanjyException("You need to provide a date after '/by'!");
            }
            LocalDateTime newBy = parser.parseDateTime(details[1].trim());
            taskList.snoozeDeadline(index, newBy);
            return "Ok! I've snoozed that deadline.\n" + taskList.getTask(index) + "\n";
        }

        if (args.contains(TOKEN_FROM)) {
            String[] details = args.split(TOKEN_FROM, 2);
            if (details.length == 1 || details[1].isBlank()) {
                throw new TanjyException("You need to set a start date by adding '/from'!");
            }
            String[] timeRange = details[1].split(TOKEN_TO, 2);
            if (timeRange.length == 1 || timeRange[1].isBlank()) {
                throw new TanjyException("You need to set an end date by adding '/to' after '/from'!");
            }

            LocalDateTime newFrom = parser.parseDateTime(timeRange[0].trim());
            LocalDateTime newTo = parser.parseDateTime(timeRange[1].trim());
            taskList.snoozeEvent(index, newFrom, newTo);

            return "Ok! I've snoozed that event.\n" + taskList.getTask(index) + "\n";
        }

        throw new TanjyException("Usage: snooze <index> /by <date> OR snooze <index> /from <start> /to <end>");
    }

    private int parseTaskIndex1Based(String s) throws TanjyException {
        if (s.isBlank()) {
            throw new TanjyException("You did not indicate which task!");
        }
        if (!s.matches("\\d+")) {
            throw new TanjyException("That's not a number :((");
        }
        int n = Integer.parseInt(s);
        if (n <= 0 || n > taskList.size()) {
            throw new TanjyException("Invalid index! Enter another number, or add a task!");
        }
        return n - 1;
    }



    /**
     * Entry point of the chatbot.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) throws TanjyException {
        new Tanjy("data/tanjy.txt").run();
    }
}
