package tanjy.task;

public class Todo extends Task {


    public Todo(String name, int status) {
        super(name, status);
        setTaskType("T");
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
