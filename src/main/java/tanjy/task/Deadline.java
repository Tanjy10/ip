package tanjy.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a deadline task with a due date.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter dateOnly = DateTimeFormatter.ofPattern("MMM dd yyyy");
    private static final DateTimeFormatter dateAndTime = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");
    private LocalDateTime by;
    /**
     * Constructs a deadline task with the task description, task status and date to finish the task by.
     *
     * @param name The deadline task description.
     * @param status The deadline task status.
     * @param by The date to finish the deadline task by.
     */
    public Deadline(String name, int status, LocalDateTime by) {
        super(name, status);
        this.by = by;
        setTaskType("D");
    }

    @Override
    public String getName() {
        return super.getName() + "|" + by.toString();
    }

    @Override
    public String toString() {
        boolean hasTime = !(by.getHour() == 0 && by.getMinute() == 0);
        String date;
        if (!hasTime) {
            date = by.format(dateAndTime);
        } else {
            date = by.format(dateOnly);
        }
        return "[D]" + super.toString() + " (by: " + date + ")";
    }
}
