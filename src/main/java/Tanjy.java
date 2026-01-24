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

        // Create while loop to continuously take inputs
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String text = scanner.nextLine();
            String[] parts = text.split("\\s+", 2);
            String command = parts[0];
            String remainder = parts.length > 1 ? parts[1] : "";


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
                        System.out.print( (i + 1) + ".");
                        System.out.print(list.get(i).toString() + "\n");
                    }
                    System.out.print(border);
                    break;

                // When command == mark, mark task as done
                case "mark":
                    int index = Integer.parseInt(parts[1]) - 1;
                    System.out.print(border + "Nice! I've marked this task as done:\n"
                            + "[X] " + list.get(index).getName() + "\n" + border);
                    Task task = list.get(index);
                    task.mark();
                    break;

                // When command == unmark, unmark as done
                case "unmark":
                    index = Integer.parseInt(parts[1]) - 1;
                    System.out.print(border + "OK, I've marked this task as not done yet:\n"
                            + "[ ] " + list.get(index).getName() + "\n" + border);
                    task = list.get(index);
                    task.unmark();
                    break;

                // When command == to-do, it's a to-do task
                case "todo":
                    Todo todo = new Todo(remainder, -1);
                    System.out.print(border + "Got it. I've added this task:\n");
                    System.out.print(todo.toString() + "\n");
                    list.add(todo);
                    System.out.print("Now you have " + list.size() + " task(s) in your list\n" + border);
                    break;

                // When command == deadline, it's a deadline task
                case "deadline":
                    String[] details = remainder.split("/by", 2);
                    Deadline deadline = new Deadline(details[0], -1, details[1]);
                    System.out.print(border + "Got it. I've added this task:\n");
                    System.out.print(deadline.toString() + "\n");
                    list.add(deadline);
                    System.out.print("Now you have " + list.size() + " task(s) in your list\n" + border);
                    break;

                // When command == Event, it's an event task
                case "event":
                    details = remainder.split("/from", 2);
                    String[] timeRange = details[1].split("/to", 2);
                    Event event = new Event(details[0], -1, timeRange[0], timeRange[1]);
                    System.out.print(border + "Got it. I've added this task:\n");
                    System.out.print(event.toString() + "\n");
                    list.add(event);
                    System.out.print("Now you have " + list.size() + " task(s) in your list\n" + border);
                    break;

                // When not a command, it is a task to be added
                default:
                    System.out.print(border + "Invalid input! Try again! :((\n" + border);
                    break;
            }
        }
    }
}
