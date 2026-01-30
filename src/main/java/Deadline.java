public class Deadline extends Task {

    protected String by;

    public Deadline(String name, int status, String by) {
        super(name, status);
        this.by = by;
        setTaskType("D");
    }

    @Override
    public String getName() {
        return super.getName() + "|" + this.by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
