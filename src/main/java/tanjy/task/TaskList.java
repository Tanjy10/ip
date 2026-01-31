package tanjy.task;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> list = new ArrayList<>();

    public ArrayList<Task> getTaskList() {
        return list;
    }

    public void addAll(ArrayList<Task> tasks) {
        this.list.addAll(tasks);
    }

    public int size() {
        return list.size();
    }

    public Task getMostRecentTask() {
        return list.get(list.size() - 1);
    }

    public Task getTask(int index){
        return list.get(index);
    }

    public void markTask(int index) {
        Task task = list.get(index);
        task.mark();
    }

    public void unmarkTask(int index) {
        Task task = list.get(index);
        task.unmark();
    }

    public void addTodo(String desc) {
        Todo todo = new Todo(desc, 0);
        list.add(todo);
    }

    public void addDeadline(String desc, LocalDateTime by) {
        Deadline deadline = new Deadline(desc, 0, by);
        list.add(deadline);
    }

    public void addEvent(String desc, LocalDateTime from, LocalDateTime to) {
        Event event = new Event(desc, 0, from, to);
        list.add(event);
    }

    public void delete(int index) {
        list.remove(index);
    }
}
