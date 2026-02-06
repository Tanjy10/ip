package tanjy;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;

import tanjy.exception.TanjyException;
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
    private static final String BORDER = "____________________________________________________________\n";
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
    public Tanjy(String filePath) {
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
        ui.printIntro();

        try {
            if (storage.doesFileExist()) {
                ui.printLoadSuccess();
                storage.loadSavedFile();
            } else {
                storage.createNewFile();
                ui.printCreatedNewFile();
            }
        } catch (IOException e) {
            ui.printLoadFail();
        }
        // Parse the lines in the file into actual tasks
        parser.stringListToTaskList(taskList.getTaskList(), storage.getSavedList());


        // Create while loop to continuously take inputs
        while (true) {
            String text = ui.readCommand();
            String[] parts = text.split("\\s+", 2);
            String command = parts[0].trim();
            String remainder = parts.length > 1 ? parts[1].trim() : "";

            try {
                switch (command) {
                // When command == bye, print outro and close scanner
                case "bye":
                    ui.printOutro();
                    ui.close();
                    return;

                // When command == list, print entire list
                case "list":
                    ui.printList(taskList.getTaskList());
                    break;

                // When command == mark, mark task as done
                case "mark":
                    if (remainder.isBlank()) {
                        throw new TanjyException("You did not indicate which task to mark!");
                    }
                    if (!parts[1].matches("\\d+")) {
                        throw new TanjyException("That's not a number :((");
                    }

                    int inputNumber = Integer.parseInt(parts[1]);

                    if (inputNumber <= 0 || inputNumber > taskList.size()) {
                        throw new TanjyException("Invalid index! Enter another number, or add a task!");
                    }
                    int index = inputNumber - 1;
                    taskList.markTask(index);
                    ui.printMarkSuccess(taskList.getTask(index));
                    break;

                // When command == unmark, unmark as done
                case "unmark":
                    if (remainder.isBlank()) {
                        throw new TanjyException("You did not indicate which task to unmark!");
                    }
                    if (!parts[1].matches("\\d+")) {
                        throw new TanjyException("That's not a number :((");
                    }

                    inputNumber = Integer.parseInt(parts[1]);

                    if (inputNumber <= 0 || inputNumber > taskList.size()) {
                        throw new TanjyException("Invalid index! Enter another number, or add a task!");
                    }
                    index = inputNumber - 1;
                    taskList.unmarkTask(index);
                    ui.printUnmarkSuccess(taskList.getTask(index));
                    break;

                // When command == to-do, it's a to-do task
                case "todo":
                    if (remainder.isBlank()) {
                        throw new TanjyException("Todo description cannot be empty.");
                    }
                    taskList.addTodo(remainder.trim());
                    ui.printAddSuccess(taskList.getMostRecentTask(), taskList.size());
                    break;

                // When command == deadline, it's a deadline task
                case "deadline":
                    if (remainder.isBlank()) {
                        throw new TanjyException("Deadline description cannot be empty.");
                    }
                    String[] details = remainder.split("/by", 2);
                    if (details.length == 1) {
                        throw new TanjyException("You need to set a deadline by adding '/by' after the task!");
                    }
                    LocalDateTime by = parser.parseDateTime(details[1].trim());
                    taskList.addDeadline(details[0].trim(), by);
                    ui.printAddSuccess(taskList.getMostRecentTask(), taskList.size());
                    break;

                // When command == Event, it's an event task
                case "event":
                    if (remainder.isBlank()) {
                        throw new TanjyException("Event description cannot be empty.");
                    }
                    details = remainder.split("/from", 2);
                    if (details.length == 1) {
                        throw new TanjyException("You need to set a start date by adding '/from' after the task!");
                    }
                    String[] timeRange = details[1].split("/to", 2);
                    if (timeRange.length == 1) {
                        throw new TanjyException("You need to set an end date by adding '/to' after '/from'!");
                    }
                    LocalDateTime from = parser.parseDateTime(timeRange[0].trim());
                    LocalDateTime to = parser.parseDateTime(timeRange[1].trim());
                    taskList.addEvent(details[0].trim(), from, to);
                    ui.printAddSuccess(taskList.getMostRecentTask(), taskList.size());
                    break;

                // When command == Delete, it's to delete a task in the list
                case "delete":
                    if (remainder.isBlank()) {
                        throw new TanjyException("You did not indicate which task to delete!");
                    }
                    if (!parts[1].matches("\\d+")) {
                        throw new TanjyException("That's not a number :((");
                    }
                    inputNumber = Integer.parseInt(parts[1]);
                    if (inputNumber <= 0 || inputNumber > taskList.size()) {
                        throw new TanjyException("Invalid index! Enter another number, or add a Task!");
                    }
                    index = inputNumber - 1;
                    ui.printDeleteSuccess(taskList.getTask(index), taskList.size() - 1);
                    taskList.delete(index);
                    break;

                case "save":
                    try {
                        storage.updateSavedList(taskList.getTaskList());
                        storage.saveFile();
                        ui.printSaveSuccess();
                    } catch (IOException e) {
                        ui.printSaveFail();
                    }
                    break;

                case "find":
                    if (remainder.isBlank()) {
                        throw new TanjyException("Give a keyword to search for!");
                    }
                    ArrayList<Task> matches = taskList.findTasks(remainder);
                    ui.printMatchingTasks(matches);
                    break;

                // When not a command, it is a task to be added
                default:
                    throw new TanjyException("Huh? No such command. Enter something else!");
                }
            } catch (TanjyException e) {
                ui.printError(e.getMessage());
            }
        }
    }

    private void initFromStorage() {
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
            welcomeMessage += ui.getLoadFailMessage();
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
            return BORDER + "Please type a command." + "\n" + BORDER;
        }

        String[] parts = text.split("\\s+", 2);
        String command = parts[0].trim();
        String remainder = parts.length > 1 ? parts[1].trim() : "";

        try {
            switch (command) {
            case "bye":
                isExit = true;
                return ui.getOutro();

            case "list":
                return ui.getListMessage(taskList.getTaskList());

            case "mark":
                return handleMark(remainder, true);

            case "unmark":
                return handleMark(remainder, false);

            case "todo":
                if (remainder.isBlank()) {
                    throw new TanjyException("Todo description cannot be empty.");
                }
                taskList.addTodo(remainder.trim());
                return ui.getAddSuccessMessage(taskList.getMostRecentTask(), taskList.size());

            case "deadline":
                if (remainder.isBlank()) {
                    throw new TanjyException("Deadline description cannot be empty.");
                }
                String[] details = remainder.split("/by", 2);
                if (details.length == 1) {
                    throw new TanjyException("You need to set a deadline by adding '/by' after the task!");
                }
                LocalDateTime by = parser.parseDateTime(details[1].trim());
                taskList.addDeadline(details[0].trim(), by);
                return ui.getAddSuccessMessage(taskList.getMostRecentTask(), taskList.size());

            case "event":
                if (remainder.isBlank()) {
                    throw new TanjyException("Event description cannot be empty.");
                }
                details = remainder.split("/from", 2);
                if (details.length == 1) {
                    throw new TanjyException("You need to set a start date by adding '/from' after the task!");
                }
                String[] timeRange = details[1].split("/to", 2);
                if (timeRange.length == 1) {
                    throw new TanjyException("You need to set an end date by adding '/to' after '/from'!");
                }
                LocalDateTime from = parser.parseDateTime(timeRange[0].trim());
                LocalDateTime to = parser.parseDateTime(timeRange[1].trim());
                taskList.addEvent(details[0].trim(), from, to);
                return ui.getAddSuccessMessage(taskList.getMostRecentTask(), taskList.size());

            case "delete":
                if (remainder.isBlank()) {
                    throw new TanjyException("You did not indicate which task to delete!");
                }
                if (!remainder.matches("\\d+")) {
                    throw new TanjyException("That's not a number :((");
                }
                int inputNumber = Integer.parseInt(remainder);
                if (inputNumber <= 0 || inputNumber > taskList.size()) {
                    throw new TanjyException("Invalid index! Enter another number, or add a Task!");
                }
                int index = inputNumber - 1;
                Task deleted = taskList.getTask(index);
                taskList.delete(index);
                return ui.getDeleteSuccessMessage(deleted, taskList.size());

            case "save":
                try {
                    storage.updateSavedList(taskList.getTaskList());
                    storage.saveFile();
                    return ui.getSaveSuccessMessage();
                } catch (IOException e) {
                    return ui.getSaveFailMessage();
                }

            case "find":
                if (remainder.isBlank()) {
                    throw new TanjyException("Give a keyword to search for!");
                }
                ArrayList<Task> matches = taskList.findTasks(remainder);
                return formatMatchingTasks(matches);

            default:
                throw new TanjyException("Huh? No such command. Enter something else!");
            }
        } catch (TanjyException e) {
            return BORDER + e.getMessage() + "\n" + BORDER;
        }
    }

    private String handleMark(String remainder, boolean isMark) throws TanjyException {
        if (remainder.isBlank()) {
            throw new TanjyException(isMark
                    ? "You did not indicate which task to mark!"
                    : "You did not indicate which task to unmark!");
        }
        if (!remainder.matches("\\d+")) {
            throw new TanjyException("That's not a number :((");
        }

        int inputNumber = Integer.parseInt(remainder);
        if (inputNumber <= 0 || inputNumber > taskList.size()) {
            throw new TanjyException("Invalid index! Enter another number, or add a task!");
        }

        int index = inputNumber - 1;
        if (isMark) {
            taskList.markTask(index);
            return ui.getMarkSuccessMessage(taskList.getTask(index));
        } else {
            taskList.unmarkTask(index);
            return ui.getUnmarkSuccessMessage(taskList.getTask(index));
        }
    }

    private String formatMatchingTasks(ArrayList<Task> matches) {
        StringBuilder sb = new StringBuilder();
        sb.append(BORDER).append("Here are the matching tasks in your list:\n");
        for (int i = 0; i < matches.size(); i++) {
            sb.append(i + 1).append(".").append(matches.get(i)).append("\n");
        }
        sb.append(BORDER);
        return sb.toString();
    }



    /**
     * Entry point of the chatbot.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        new Tanjy("data/tanjy.txt").run();
    }
}
