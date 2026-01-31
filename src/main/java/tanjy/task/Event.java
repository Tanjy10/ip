package tanjy.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {

    protected LocalDateTime from;
    protected LocalDateTime to;

    private static final DateTimeFormatter dateOnly = DateTimeFormatter.ofPattern("MMM dd yyyy");
    private static final DateTimeFormatter dateAndTime = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");

    /**
     * Constructs an event task with the task description, task status, and time window to finish the task by.
     *
     * @param name The event task description.
     * @param status The event task status.
     * @param from The beginning date of the event task.
     * @param to The end date of the event task.
     */

    public Event(String name, int status, LocalDateTime from, LocalDateTime to) {
        super(name, status);
        this.from = from;
        this.to = to;
        setTaskType("E");
    }

    @Override
    public String getName() {
        return super.getName() + "|" + from.toString() + "|" + to.toString();
    }

    @Override
    public String toString() {
        String fromDate, toDate;
        if (from.getHour() == 0 && from.getMinute() == 0) {
            fromDate = from.format(dateOnly);
        } else {
            fromDate = from.format(dateAndTime);
        }

        if (to.getHour() == 0 && to.getMinute() == 0) {
            toDate = to.format(dateOnly);
        } else {
            toDate = to.format(dateAndTime);
        }
        return "[E]" + super.toString() + " (from: " + fromDate + " to: " + toDate + ")";
    }
}
