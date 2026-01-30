import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Tanjy {
    public static void main(String[] args) {
        // Introducing Task list, intro and outro for bot
        ArrayList<Task> list = new ArrayList<>();
        String border = "____________________________________________________________\n";

        String intro = border
                + "Hello! I'm Tanjy.\n"
                + "What can I do for you?\n"
                + border;

        String outro = border
                + "Bye. Hope to see you again soon!\n"
                + border;

        // Print intro
        System.out.println(intro);

        // Try to search for file for saved list
        Path filePath = Paths.get("data", "Tanjy.txt");
        List<String> savedList= new ArrayList<>();
        try {
            Files.createDirectories(filePath.getParent());

            // If file exists, load it. Else, create a new file.
            if (Files.exists(filePath)) {
                savedList = Files.readAllLines(filePath);
                System.out.print(border +
                        "Your saved list has been found and loaded! :D\n"
                        + border);
            } else {
                System.out.print(border +
                        "Looks like this is your first time. Let me create a list for you!\n"
                        + border);
                Files.createFile(filePath);
            }
        } catch (IOException e) {
            System.out.print(border +
                    "Something went wrong... idk man :<\n"
                    + border);
        }

        // Parse the lines in the file into actual tasks
        for (String line : savedList) {
            Task t = lineToTaskParser(line);
            list.add(t);
        }

        Scanner scanner = new Scanner(System.in);

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
                        System.out.print(outro);
                        scanner.close();
                        return;

                    // When command == list, print entire list
                    case "list":
                        System.out.print(border +
                                "Here are the task(s) in your list:\n");
                        for (int i = 0; i < list.size(); i++) {
                            System.out.print((i + 1) + ".");
                            System.out.print(list.get(i).toString() + "\n");
                        }
                        System.out.print(border);
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
                        System.out.print(border + "Nice! I've marked this task as done:\n"
                                + "[X] " + list.get(index).getName() + "\n" + border);
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
                        System.out.print(border + "OK, I've marked this task as not done yet:\n"
                                + "[ ] " + list.get(index).getName() + "\n" + border);
                        task = list.get(index);
                        task.unmark();
                        break;

                    // When command == to-do, it's a to-do task
                    case "todo":
                        if (remainder.isBlank())
                            throw new TanjyException("Todo description cannot be empty.");
                        Todo todo = new Todo(remainder.trim(), 0);
                        System.out.print(border + "Got it. I've added this task:\n");
                        System.out.print(todo.toString() + "\n");
                        list.add(todo);
                        System.out.print("Now you have " + list.size() + " task(s) in your list\n" + border);
                        break;

                    // When command == deadline, it's a deadline task
                    case "deadline":
                        if (remainder.isBlank())
                            throw new TanjyException("Deadline description cannot be empty.");
                        String[] details = remainder.split("/by", 2);
                        if (details.length == 1)
                            throw new TanjyException("You need to set a deadline by adding '/by' after the task!");
                        Deadline deadline = new Deadline(details[0].trim(), 0, details[1].trim());
                        System.out.print(border + "Got it. I've added this task:\n");
                        System.out.print(deadline.toString() + "\n");
                        list.add(deadline);
                        System.out.print("Now you have " + list.size() + " task(s) in your list\n" + border);
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
                        Event event = new Event(details[0].trim(), 0, timeRange[0].trim(), timeRange[1].trim());
                        System.out.print(border + "Got it. I've added this task:\n");
                        System.out.print(event.toString() + "\n");
                        list.add(event);
                        System.out.print("Now you have " + list.size() + " task(s) in your list\n" + border);
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
                        System.out.print(border + "Noted. I've removed this Task:\n");
                        Task deleted = list.get(index);
                        System.out.print(deleted.toString() + "\n");
                        list.remove(deleted);
                        System.out.print("Now you have " + list.size() + " task(s) in your list\n" + border);
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
                            Files.write(
                                    filePath,
                                    linesToSave,
                                    StandardOpenOption.CREATE,
                                    StandardOpenOption.TRUNCATE_EXISTING
                            );
                            System.out.print(border +
                                    "Your list has been saved successfully!\n"
                                    + border);

                        } catch (IOException e) {
                            System.out.print("Something went wrong... Oops :<");
                        }
                        break;

                    // When not a command, it is a task to be added
                    default:
                        throw new TanjyException("Huh? No such command. Enter something else!");
                }
            } catch (TanjyException e) {
                System.out.print(border + e.getMessage() + "\n" + border);
            }
        }
    }

    public static Task lineToTaskParser(String s) {
        String[] lineParts = s.split("\\|", 2);
        String typeOfTask = lineParts[0].trim();
        String taskContents = lineParts.length > 1 ? lineParts[1].trim() : "";

        try {
            switch (typeOfTask) {
                case "T":
                    String[] todoParts = taskContents.split("\\|", 2);
                    int taskStatus = Integer.parseInt(todoParts[0].trim());
                    return new Todo(todoParts[1].trim(), taskStatus);
                case "D":
                    String[] deadlineParts = taskContents.split("\\|", 3);
                    taskStatus = Integer.parseInt(deadlineParts[0].trim());
                    return new Deadline(deadlineParts[1].trim(), taskStatus, deadlineParts[2].trim());
                case "E":
                    String[] eventParts = taskContents.split("\\|", 4);
                    taskStatus = Integer.parseInt(eventParts[0].trim());
                    return new Event(eventParts[1].trim(), taskStatus,
                            eventParts[2].trim(), eventParts[3].trim());
                default:
                    return null;
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
