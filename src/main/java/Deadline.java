import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {

    protected LocalDateTime by;

    private static final DateTimeFormatter dateOnly = DateTimeFormatter.ofPattern("MMM dd yyyy");
    private static final DateTimeFormatter dateAndTime = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");

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
