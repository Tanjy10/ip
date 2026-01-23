import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Tanjy {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<String>();
        String border = "____________________________________________________________\n";

        String intro = border
                + "Hello! I'm Tanjy.\n"
                + "What can I do for you?\n"
                + border;

        String outro = border
                + "Bye. Hope to see you again soon!\n"
                + border;

        System.out.println(intro);
        Scanner scanner = new Scanner(System.in);
        String echo = scanner.next();

        while (!Objects.equals(echo, "bye")) {
            if (!Objects.equals(echo, "list")) {
                System.out.print(border + "added: " + echo + "\n" + border);
                list.add(echo);
                echo = scanner.next();
            }

            if (Objects.equals(echo, "list")){
                for (int i = 0; i < list.size(); i++){
                    System.out.print((i + 1) + ". " + list.get(i) + "\n");
                }
                echo = scanner.next();
            }
        }

        System.out.print(outro);
    }
}
