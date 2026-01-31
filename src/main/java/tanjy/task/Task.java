package tanjy.task;

public class Task {
    private String taskType;
    private String name;
    private int status;
    protected String description;

    /**
     * Constructs a task with the task description and task status.
     *
     * @param name The task description.
     * @param status The task status.
     */
    public Task(String name, int status) {
        this.name = name;
        this.status = status;
    }

    public String getTaskType(){
        return taskType;
    }

    public void setTaskType(String s){
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

    /**
     *  Changes the task's status to done.
     */
    public void mark() {
        this.status = 1;
    }

    /**
     * Changes the task's status to not done.
     */
    public void unmark() {
        this.status = 0;
    }
}
