import java.util.ArrayList;
import java.util.Scanner;

public class Ui {
    private static final String BORDER = "____________________________________________________________\n";
    private static final String INTRO = BORDER
            + "Hello! I'm Tanjy.\n"
            + "What can I do for you?\n"
            + BORDER;
    private static final String OUTRO = BORDER
            + "Bye. Hope to see you again soon!\n"
            + BORDER;
    private final Scanner scanner;


    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void printIntro() {
        System.out.print(INTRO);
    }

    public void printOutro() {
        System.out.print(OUTRO);
    }

    public void close() {
        scanner.close();
    }

    public void printError(String error) {
        System.out.print(BORDER +
                error + "\n" +
                BORDER );
    }

    public void printLoadSuccess() {
        System.out.print(BORDER +
                "Your saved list has been found and loaded! :D\n"
                + BORDER);
    }

    public void printCreatedNewFile() {
        System.out.print(BORDER +
                "Looks like this is your first time. Let me create a list for you!\n"
                + BORDER);
    }

    public void printLoadFail() {
        System.out.print(BORDER +
                "Could not load file / file not found. :( \n"
                + BORDER);
    }

    public void printSaveSuccess() {
        System.out.print(BORDER +
                "Your list has been saved successfully!\n"
                + BORDER);
    }

    public void printSaveFail() {
        System.out.print(BORDER +
                "Could not save file / file not found. :(\n"
                + BORDER);
    }

    public void printList(ArrayList<Task> list) {
        System.out.print(BORDER +
                "Here are the task(s) in your list:\n");
        for (int i = 0; i < list.size(); i++) {
            System.out.print((i + 1) + ".");
            System.out.print(list.get(i).toString() + "\n");
        }
        System.out.print(BORDER);
    }

    public void printAddSuccess(Task t, int total) {
        System.out.print(BORDER +
                "Got it. I've added this task:\n"
                + t.toString() + "\n"
                + "Now you have " + total + " task(s) in your list.\n"
                + BORDER);
    }

    public void printMarkSuccess(Task t) {
        System.out.print(BORDER +
                "Nice! I've marked this task as done:\n"
                + t.toString() + "\n"
                + BORDER);
    }

    public void printUnmarkSuccess(Task t) {
        System.out.print(BORDER
                + "OK, I've marked this task as not done yet:\n"
                + t.toString() + "\n"
                + BORDER);
    }

    public void printDeleteSuccess(Task t, int total) {
        System.out.print(BORDER +
                "Got it. I've removed this task:\n"
                + t.toString() + "\n"
                + "Now you have " + total + " task(s) in your list.\n"
                + BORDER);
    }
}
