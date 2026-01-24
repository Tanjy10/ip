import java.util.ArrayList;
import java.util.Objects;
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

        // Print intro, start scanner to scan for inputs
        System.out.println(intro);
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
                        Todo todo = new Todo(remainder.trim(), -1);
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
                        Deadline deadline = new Deadline(details[0].trim(), -1, details[1].trim());
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
                        Event event = new Event(details[0].trim(), -1, timeRange[0].trim(), timeRange[1].trim());
                        System.out.print(border + "Got it. I've added this task:\n");
                        System.out.print(event.toString() + "\n");
                        list.add(event);
                        System.out.print("Now you have " + list.size() + " task(s) in your list\n" + border);
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
}
