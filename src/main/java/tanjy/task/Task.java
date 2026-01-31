package tanjy.task;

public class Task {
    private String taskType;
    private String name;
    private int status;
    protected String description;

    public Task(String name, int status) {
        this.name = name;
        this.status = status;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String s) {
        taskType = s;
    }

    public int getStatus() {
        return this.status;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        if (status == 1) {
            description = "[X] " + this.name;
        } else {
            description = "[ ] " + this.name;
        }
        return description;
    }

    public void mark() {
        this.status = 1;
    }

    public void unmark() {
        this.status = 0;
    }
}
