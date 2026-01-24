public class Event extends Task{

    protected String by;

    public Event(String name, int status, String by) {
        super(name, status);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
