package tanjy.task;

import java.time.LocalDateTime;
import java.util.ArrayList;

import tanjy.exception.TanjyException;

/**
 * Represents the list of tasks.
 */
public class TaskList {
    private static final int STATUS_NOT_DONE = 0;
    private static final int STATUS_DONE = 1;
    private final ArrayList<Task> list = new ArrayList<>();

    public ArrayList<Task> getTaskList() {
        return list;
    }

    /**
     * @return Size of the list.
     */
    public int size() {
        return list.size();
    }

    public Task getMostRecentTask() {
        return list.get(list.size() - 1);
    }

    public Task getTask(int index) {
        return list.get(index);
    }

    /**
     * Marks the task based on the given index.
     *
     * @param index The index of the task in the task list.
     */
    public void markTask(int index) {
        Task task = list.get(index);
        task.mark();
    }

    /**
     * Unmarks the task based on the given index.
     *
     * @param index The index of the task in the task list.
     */
    public void unmarkTask(int index) {
        Task task = list.get(index);
        task.unmark();
    }

    /**
     * Adds the todo task to the task list.
     *
     * @param desc The description of the todo task.
     */
    public void addTodo(String desc) {
        Todo todo = new Todo(desc, STATUS_NOT_DONE);
        list.add(todo);
    }

    /**
     * Adds the deadline task to the task list.
     *
     * @param desc The description of the deadline task.
     * @param by The date to finish the task by.
     */
    public void addDeadline(String desc, LocalDateTime by) {
        Deadline deadline = new Deadline(desc, STATUS_NOT_DONE, by);
        list.add(deadline);
    }

    /**
     * Adds the event task to the task list.
     *
     * @param desc The description of the event task.
     * @param from The beginning date of the event task.
     * @param to The end date of the event task.
     */
    public void addEvent(String desc, LocalDateTime from, LocalDateTime to) {
        Event event = new Event(desc, STATUS_NOT_DONE, from, to);
        list.add(event);
    }

    /**
     * Deletes the task from the task list.
     *
     * @param index The index of the task in the task list.
     */
    public void delete(int index) {
        list.remove(index);
    }

    /**
     * Returns a list of tasks whose descriptions contain the given keyword.
     *
     * @param s Keyword to search for.
     * @return List of matching tasks.
     */
    public ArrayList<Task> findTasks(String s) {
        ArrayList<Task> matches = new ArrayList<>();
        String lowerKeyword = s.toLowerCase();

        for (Task task : list) {
            if (task.getName().toLowerCase().contains(lowerKeyword)) {
                matches.add(task);
            }
        }
        return matches;
    }

    public void snoozeDeadline(int index, LocalDateTime newBy) throws TanjyException {
        Task task = list.get(index);
        if (!(task instanceof Deadline)) {
            throw new TanjyException("You can only snooze a Deadline with /by.");
        }

        Deadline d = (Deadline) task;
        LocalDateTime oldBy = d.getBy();

        if (!newBy.isAfter(oldBy)) {
            throw new TanjyException("Snooze time must be later than the current deadline time.");
        }

        d.setBy(newBy);
    }


    public void snoozeEvent(int index, LocalDateTime newFrom, LocalDateTime newTo) throws TanjyException {
        Task task = list.get(index);
        if (!(task instanceof Event)) {
            throw new TanjyException("You can only snooze an Event with /from ... /to ...");
        }

        if (!newFrom.isBefore(newTo)) {
            throw new TanjyException("Event start time must be before end time.");
        }

        Event e = (Event) task;
        LocalDateTime oldFrom = e.getFrom();
        LocalDateTime oldTo = e.getTo();

        if (!newFrom.isAfter(oldFrom) || !newTo.isAfter(oldTo)) {
            throw new TanjyException("Snooze time must be later than the current event time.");
        }

        e.setFrom(newFrom);
        e.setTo(newTo);
    }



}
