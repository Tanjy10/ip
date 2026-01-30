public class Event extends Task{

    protected String from;
    protected String to;

    public Event(String name, int status, String from, String to) {
        super(name, status);
        this.from = from;
        this.to = to;
        setTaskType("E");
    }

    @Override
    public String getName() {
        return super.getName() + "|" + this.from + "|" + this.to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
