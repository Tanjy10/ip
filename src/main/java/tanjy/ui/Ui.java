package tanjy.ui;

import java.util.ArrayList;
import java.util.Scanner;

import tanjy.task.Task;

/**
 * Handles all user interaction for the chatbot.
 */
public class Ui {
    private static final String INTRO = "Hello! I'm Tanjy.\n" + "What can I do for you?\n";
    private static final String OUTRO = "Bye. Hope to see you again soon!\n";
    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public String getIntro() {
        return INTRO;
    }

    public void printIntro() {
        System.out.print(INTRO);
    }

    public String getOutro() {
        return OUTRO;
    }

    public String getLoadSuccessMessage() {
        return "Your saved list has been found and loaded! :D\n";
    }

    public String getCreatedNewFileMessage() {
        return "Looks like this is your first time. Let me create a list for you!\n";
    }

    public String getLoadFailMessage() {
        return "Could not load file. :( \n";
    }

    public String getSaveSuccessMessage() {
        return "Your list has been saved successfully!\n";
    }

    public String getSaveFailMessage() {
        return "Could not save file. :(\n";
    }

    public String getListMessage(ArrayList<Task> list) {
        StringBuilder sb = new StringBuilder("Here are the task(s) in your list:\n");
        for (int i = 0; i < list.size(); i++) {
            sb.append(i + 1).append(".").append(list.get(i)).append("\n");
        }
        return sb.toString();
    }

    public String getAddSuccessMessage(Task t, int total) {
        return "Got it. I've added this task:\n"
                + t.toString() + "\n"
                + "Now you have " + total + " task(s) in your list.\n";
    }

    public String getMarkSuccessMessage(Task t) {
        return "Nice! I've marked this task as done:\n"
                + t.toString() + "\n";
    }


    public String getUnmarkSuccessMessage(Task t) {
        return "OK, I've marked this task as not done yet:\n"
                + t.toString() + "\n";
    }

    public String getDeleteSuccessMessage(Task t, int total) {
        return "Got it. I've removed this task:\n"
                + t.toString() + "\n"
                + "Now you have " + total + " task(s) in your list.\n";
    }

    /**
     * Prints a string given.
     *
     * @param string The string to be printed.
     */
    public void print(String string) {
        System.out.print(string);
    }
}
