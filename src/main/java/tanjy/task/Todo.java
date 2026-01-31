package tanjy.task;

public class Todo extends Task {

    /**
     * Constructs a todo task with the task description and task status.
     *
     * @param name The todo task description.
     * @param status The todo task status.
     */
    public Todo(String name, int status) {
        super(name, status);
        setTaskType("T");
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
