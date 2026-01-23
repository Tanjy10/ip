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

        //Introduce commandList to check for commands
        ArrayList<String> commandList = new ArrayList<>();
        commandList.add("list");
        commandList.add("mark");
        commandList.add("unmark");

        // Print intro, start scanner to scan for inputs
        System.out.println(intro);
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();
        String[] parts = text.trim().split("\\s+");

        // Create while loop to continuously take inputs
        while (!Objects.equals(text, "bye")) {
            // Conditional to print entire list
            if (Objects.equals(text, "list")) {
                System.out.print(border +
                        "Here are the tasks in your list:\n");
                for (int i = 0; i < list.size(); i++) {
                    System.out.print(list.get(i).getMessage());
                }
                System.out.print(border);
                text = scanner.nextLine();
                parts = text.trim().split("\\s+");
            } else

            // Conditional to mark Task as done
            if (Objects.equals(parts[0], "mark")) {
                int index = Integer.parseInt(parts[1]) - 1;
                // Conditional to check that number given is allowed:
                if (index >= list.size() || index < 0) {
                    System.out.print(border +
                            "Index is out of bounds! Enter a valid number. \n"
                    + border);
                } else {
                    // Mark task as done
                    System.out.print(border + "Nice! I've marked this task as done:\n"
                            + "[X] " + list.get(index).getName() + "\n" + border);
                    Task curr = list.get(index);
                    curr.changeStatus();
                }
                text = scanner.nextLine();
                parts = text.trim().split("\\s+");
            } else

            // Conditional to mark Task as undone
            if (Objects.equals(parts[0], "unmark")) {
                int index = Integer.parseInt(parts[1]) - 1;
                // Conditional to check that number given is allowed:
                if (index >= list.size() || index < 0) {
                    System.out.print(border +
                            "Index is out of bounds! Enter a valid number. \n"
                            + border);
                } else {
                    // Mark task as done
                    System.out.print(border + "OK, I've marked this task as not done yet:\n"
                            + "[ ]" + list.get(index).getName() + "\n" + border);
                    Task curr = list.get(index);
                    curr.changeStatus();
                }
                text = scanner.nextLine();
                parts = text.trim().split("\\s+");
            } else

            // If inputs are not as above, it is a Task that should be added to the list
                {
                System.out.print(border + "added: " + text + "\n" + border);
                Task curr = new Task(list.size(), text, -1);
                list.add(curr);
                text = scanner.nextLine();
                parts = text.trim().split("\\s+");
            }
        }

        System.out.print(outro);
    }
}
