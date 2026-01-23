public class Task {
    private int index;
    private String name;
    private int status;
    private String message;

    public Task(int index, String name, int status) {
        this.index = index;
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return this.name;
    }

    public String getMessage() {
        if (status == 1) {
            message = (index + 1) + "." + "[X] " + name + "\n";
        } else {
            message = (index + 1) + "." + "[ ] " + name + "\n";
        }
        return message;
    }

    public void changeStatus() {
        this.status = status * -1;
    }
}
