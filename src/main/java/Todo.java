public class Todo extends Task{


    public Todo(String name, int status) {
        super(name, status);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
