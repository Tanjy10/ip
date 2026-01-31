package tanjy;

import tanjy.exception.TanjyException;
import tanjy.parser.Parser;
import tanjy.storage.Storage;
import tanjy.task.TaskList;
import tanjy.ui.Ui;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.io.IOException;

public class Tanjy {
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
    }

    /**
     * Runs the chatbot.
     */
    public void run() {
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
                    if (remainder.isBlank()) throw new TanjyException("You did not indicate which task to mark!");
                    if (!parts[1].matches("\\d+")) throw new TanjyException("That's not a number :((");

                    int inputNumber = Integer.parseInt(parts[1]);

                    if (inputNumber <= 0 || inputNumber > taskList.size())
                        throw new TanjyException("Invalid index! Enter another number, or add a task!");

                    int index = inputNumber - 1;
                    taskList.markTask(index);
                    ui.printMarkSuccess(taskList.getTask(index));
                    break;

                // When command == unmark, unmark as done
                case "unmark":
                    if (remainder.isBlank()) throw new TanjyException("You did not indicate which task to unmark!");
                    if (!parts[1].matches("\\d+")) throw new TanjyException("That's not a number :((");

                    inputNumber = Integer.parseInt(parts[1]);

                    if (inputNumber <= 0 || inputNumber > taskList.size())
                        throw new TanjyException("Invalid index! Enter another number, or add a task!");

                    index = inputNumber - 1;
                    taskList.unmarkTask(index);
                    ui.printUnmarkSuccess(taskList.getTask(index));
                    break;

                // When command == to-do, it's a to-do task
                case "todo":
                    if (remainder.isBlank()) throw new TanjyException("Todo description cannot be empty.");
                    taskList.addTodo(remainder.trim());
                    ui.printAddSuccess(taskList.getMostRecentTask(), taskList.size());
                    break;

                // When command == deadline, it's a deadline task
                case "deadline":
                    if (remainder.isBlank()) throw new TanjyException("Deadline description cannot be empty.");
                    String[] details = remainder.split("/by", 2);
                    if (details.length == 1)
                        throw new TanjyException("You need to set a deadline by adding '/by' after the task!");
                    LocalDateTime by = parser.parseDateTime(details[1].trim());
                    taskList.addDeadline(details[0].trim(), by);
                    ui.printAddSuccess(taskList.getMostRecentTask(), taskList.size());
                    break;

                // When command == Event, it's an event task
                case "event":
                    if (remainder.isBlank()) throw new TanjyException("Event description cannot be empty.");
                    details = remainder.split("/from", 2);
                    if (details.length == 1)
                        throw new TanjyException("You need to set a start date by adding '/from' after the task!");
                    String[] timeRange = details[1].split("/to", 2);
                    if (timeRange.length == 1)
                        throw new TanjyException("You need to set an end date by adding '/to' after '/from'!");
                    LocalDateTime from = parser.parseDateTime(timeRange[0].trim());
                    LocalDateTime to = parser.parseDateTime(timeRange[1].trim());
                    taskList.addEvent(details[0].trim(), from, to);
                    ui.printAddSuccess(taskList.getMostRecentTask(), taskList.size());
                    break;

                // When command == Delete, it's to delete a task in the list
                case "delete":
                    if (remainder.isBlank()) throw new TanjyException("You did not indicate which task to delete!");
                    if (!parts[1].matches("\\d+")) throw new TanjyException("That's not a number :((");
                    inputNumber = Integer.parseInt(parts[1]);
                    if (inputNumber <= 0 || inputNumber > taskList.size())
                        throw new TanjyException("Invalid index! Enter another number, or add a Task!");
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

                // When not a command, it is a task to be added
                default:
                    throw new TanjyException("Huh? No such command. Enter something else!");
                }
            } catch (TanjyException e) {
                ui.printError(e.getMessage());
            }
        }
    }

    /**
     * Entry point of the chatbot.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        new Tanjy("data/Tanjy.txt").run();
    }
}
