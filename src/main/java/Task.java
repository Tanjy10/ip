public class Task {
    private int index;
    private String name;
    private int status;
    protected String description;

    public Task(int index, String name, int status) {
        this.index = index;
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        if (status == 1) {
            description = (index + 1) + "." + "[X] " + name + "\n";
        } else {
            description = (index + 1) + "." + "[ ] " + name + "\n";
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
