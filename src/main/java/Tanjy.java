import java.time.LocalDateTime;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Tanjy {
    private static Ui ui;
    private static Storage storage;
    private static Parser parser;


    public static void main(String[] args) {
        ui = new Ui();
        storage = new Storage();
        parser = new Parser();
        ArrayList<Task> list = new ArrayList<>();
        Path filePath = Paths.get("data", "Tanjy.txt");
        List<String> savedList = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);

        // Print intro
        ui.printIntro();

        // if file exits, load it. Else, create a new file.
        try {
            if (storage.doesFileExist(filePath)) {
                ui.printLoadSuccess();
                savedList = storage.loadSavedFile(filePath);
            } else {
                storage.createNewFile(filePath);
                ui.printCreatedNewFile();
            }
        } catch (IOException e) {
            ui.printLoadFail();
        }

        // Parse the lines in the file into actual tasks
        for (String line : savedList) {
            Task t = parser.lineToTaskParser(line);
            list.add(t);
        }

        // Create while loop to continuously take inputs
        while (true) {
            String text = scanner.nextLine();
            String[] parts = text.split("\\s+", 2);
            String command = parts[0].trim();
            String remainder = parts.length > 1 ? parts[1].trim() : "";

            try {
                switch (command) {
                    // When command == bye, print outro and close scanner
                    case "bye":
                        ui.printOutro();
                        scanner.close();
                        return;

                    // When command == list, print entire list
                    case "list":
                        ui.printList(list);
                        break;

                    // When command == mark, mark task as done
                    case "mark":
                        if (remainder.isBlank())
                            throw new TanjyException("You did not indicate which task to mark!");
                        if (!parts[1].matches("\\d+"))
                            throw new TanjyException("That's not a number :((");
                        int inputNumber = Integer.parseInt(parts[1]);
                        if (inputNumber <= 0 || inputNumber > list.size())
                            throw new TanjyException("Invalid index! Enter another number, or add a task!");
                        int index = inputNumber - 1;
                        ui.printMarkSuccess(list.get(index));
                        Task task = list.get(index);
                        task.mark();
                        break;

                    // When command == unmark, unmark as done
                    case "unmark":
                        if (remainder.isBlank())
                            throw new TanjyException("You did not indicate which task to unmark!");
                        if (!parts[1].matches("\\d+"))
                            throw new TanjyException("That's not a number :((");
                        inputNumber = Integer.parseInt(parts[1]);
                        if (inputNumber <= 0 || inputNumber > list.size())
                            throw new TanjyException("Invalid index! Enter another number, or add a task!");
                        index = inputNumber - 1;
                        ui.printUnmarkSuccess(list.get(index));
                        task = list.get(index);
                        task.unmark();
                        break;

                    // When command == to-do, it's a to-do task
                    case "todo":
                        if (remainder.isBlank())
                            throw new TanjyException("Todo description cannot be empty.");
                        Todo todo = new Todo(remainder.trim(), 0);
                        list.add(todo);
                        ui.printAddSuccess(todo, list.size() + 1);
                        break;

                    // When command == deadline, it's a deadline task
                    case "deadline":
                        if (remainder.isBlank())
                            throw new TanjyException("Deadline description cannot be empty.");
                        String[] details = remainder.split("/by", 2);
                        if (details.length == 1)
                            throw new TanjyException("You need to set a deadline by adding '/by' after the task!");
                        LocalDateTime by = parser.parseDateTime(details[1].trim());
                        Deadline deadline = new Deadline(details[0].trim(), 0, by);
                        list.add(deadline);
                        ui.printAddSuccess(deadline, list.size() + 1);
                        break;

                    // When command == Event, it's an event task
                    case "event":
                        if (remainder.isBlank())
                            throw new TanjyException("Event description cannot be empty.");
                        details = remainder.split("/from", 2);
                        if (details.length == 1)
                            throw new TanjyException("You need to set a start date by adding '/from' after the task!");
                        String[] timeRange = details[1].split("/to", 2);
                        if (timeRange.length == 1)
                            throw new TanjyException("You need to set an end date by adding '/to' after '/from'!");
                        LocalDateTime from = parser.parseDateTime(timeRange[0].trim());
                        LocalDateTime to = parser.parseDateTime(timeRange[1].trim());
                        Event event = new Event(details[0].trim(), 0, from, to);
                        list.add(event);
                        ui.printAddSuccess(event, list.size() + 1);
                        break;

                    // When command == Delete, it's to delete a task in the list
                    case "delete":
                        if (remainder.isBlank())
                            throw new TanjyException("You did not indicate which task to delete!");
                        if (!parts[1].matches("\\d+"))
                            throw new TanjyException("That's not a number :((");
                        inputNumber = Integer.parseInt(parts[1]);
                        if (inputNumber <= 0 || inputNumber > list.size())
                            throw new TanjyException("Invalid index! Enter another number, or add a Task!");
                        index = inputNumber - 1;
                        Task deleted = list.get(index);
                        list.remove(deleted);
                        ui.printDeleteSuccess(deleted, list.size() - 1);
                        break;

                    case "save":
                        try {
                            Paths.get("data", "tanjy.txt");
                            Files.createDirectories(filePath.getParent());

                            List<String> linesToSave = new ArrayList<>();
                            for (Task t : list) {
                                String line = t.getTaskType() + "|" + t.getStatus() + "|" + t.getName();
                                linesToSave.add(line);
                            }
                            storage.saveFile(filePath, linesToSave);
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
                throw new RuntimeException();
            }
        }
    }
}
