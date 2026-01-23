import java.util.Objects;
import java.util.Scanner;

public class Tanjy {
    public static void main(String[] args) {
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
            System.out.print(border + echo + "\n" + border);
            echo = scanner.next();
        }

        System.out.print(outro);
    }
}
