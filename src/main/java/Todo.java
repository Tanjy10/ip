public class Todo extends Task{

    protected String by;

    public Todo(int index, String name, int status, String by) {
        super(index, name, status);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
