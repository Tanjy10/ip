package tanjy.ui;

import java.util.ArrayList;
import java.util.Scanner;

import tanjy.task.Task;

/**
 * Handles all user interaction for the chatbot.
 */
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

    /**
     * Prints an error message to the user.
     *
     * @param error error String to be printed.
     */
    public void printError(String error) {
        System.out.print(BORDER
                + error + "\n"
                + BORDER);
    }

    /**
     * Prints a message when loading from file is successful.
     */
    public void printLoadSuccess() {
        System.out.print(BORDER
                + "Your saved list has been found and loaded! :D\n"
                + BORDER);
    }

    /**
     * Prints a message when creating a new file is successful.
     */
    public void printCreatedNewFile() {
        System.out.print(BORDER
                + "Looks like this is your first time. Let me create a list for you!\n"
                + BORDER);
    }

    /**
     * Prints a message when loading the file did not work.
     */
    public void printLoadFail() {
        System.out.print(BORDER
                + "Could not load file. :( \n"
                + BORDER);
    }

    /**
     * Prints a message when the "save" command has been run successfully.
     */
    public void printSaveSuccess() {
        System.out.print(BORDER
                + "Your list has been saved successfully!\n"
                + BORDER);
    }

    /**
     * Prints a message when the "save" command did not work.
     */
    public void printSaveFail() {
        System.out.print(BORDER
                + "Could not save file. :(\n"
                + BORDER);
    }

    /**
     * Prints the entire TaskList out, with each task having their task type,
     * their completion status, description, and by/from/to dates.
     * @param list the TaskList to parse through to print each task.
     */
    public void printList(ArrayList<Task> list) {
        System.out.print(BORDER
                + "Here are the task(s) in your list:\n");
        for (int i = 0; i < list.size(); i++) {
            System.out.print((i + 1) + ".");
            System.out.print(list.get(i).toString() + "\n");
        }
        System.out.print(BORDER);
    }

    /**
     * Prints a message when adding a task to the list is done successfully.
     * @param t the task to be added to the list.
     * @param total the list of tasks.
     */
    public void printAddSuccess(Task t, int total) {
        System.out.print(BORDER
                + "Got it. I've added this task:\n"
                + t.toString() + "\n"
                + "Now you have " + total + " task(s) in your list.\n"
                + BORDER);
    }

    /**
     * Prints a message when marking the specified task is done successfully.
     * @param t the task to be marked.
     */
    public void printMarkSuccess(Task t) {
        System.out.print(BORDER
                + "Nice! I've marked this task as done:\n"
                + t.toString() + "\n"
                + BORDER);
    }

    /**
     * Prints a message when unmarking the specified task is done successfully.
     * @param t the task to be unmarked.
     */
    public void printUnmarkSuccess(Task t) {
        System.out.print(BORDER
                + "OK, I've marked this task as not done yet:\n"
                + t.toString() + "\n"
                + BORDER);
    }

    /**
     * Prints a message when deleting the specified task from the list is done successfully.
     * @param t the task to be deleted.
     * @param total the list after deletion.
     */
    public void printDeleteSuccess(Task t, int total) {
        System.out.print(BORDER
                + "Got it. I've removed this task:\n"
                + t.toString() + "\n"
                + "Now you have " + total + " task(s) in your list.\n"
                + BORDER);
    }

    /**
     * Prints the list of matching tasks.
     *
     * @param matchingTasks Tasks that match the search keyword.
     */
    public void printMatchingTasks(ArrayList<Task> matchingTasks) {
        System.out.print(BORDER
                + "Here are the matching tasks in your list:\n");

        for (int i = 0; i < matchingTasks.size(); i++) {
            System.out.println((i + 1) + "." + matchingTasks.get(i));
        }

        System.out.print(BORDER);
    }

}
