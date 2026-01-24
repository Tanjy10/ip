public class Task {
    private String name;
    private int status;
    protected String description;

    public Task(String name, int status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        if (status == 1) {
            description = "[X] " + name;
        } else {
            description = "[ ] " + name;
        }
        return description;
    }

    public void mark() {
        this.status = 1;
    }

    public void unmark() {
        this.status = -1;
    }
}
