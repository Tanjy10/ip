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
            String[] parts = text.trim().split("\\s+");
            String command = parts[0];

            switch (command) {
                // When command == bye, print outro and close scanner
                case "bye":
                    System.out.print(outro);
                    scanner.close();
                    return;

                // When command == list, print entire list
                case "list":
                    System.out.print(border +
                            "Here are the tasks in your list:\n");
                    for (int i = 0; i < list.size(); i++) {
                        System.out.print( (i + 1) + ".");
                        System.out.print(list.get(i).toString());
                    }
                    System.out.print(border);
                    break;

                // When command == mark, mark task as done
                case "mark":
                    int index = Integer.parseInt(parts[1]) - 1;
                    System.out.print(border + "Nice! I've marked this task as done:\n"
                            + "[X] " + list.get(index).getName() + "\n" + border);
                    Task curr = list.get(index);
                    curr.mark();
                    break;

                // When command == unmark, unmark as done
                case "unmark":
                    index = Integer.parseInt(parts[1]) - 1;
                    System.out.print(border + "OK, I've marked this task as not done yet:\n"
                            + "[ ] " + list.get(index).getName() + "\n" + border);
                    curr = list.get(index);
                    curr.unmark();
                    break;

                // When not a command, it is a task to be added
                default:
                    System.out.print(border + "added: " + text + "\n" + border);
                    curr = new Task(text, -1);
                    list.add(curr);
                    break;
            }
        }
    }
}
