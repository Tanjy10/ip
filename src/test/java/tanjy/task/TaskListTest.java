package tanjy.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskListTest {

    @Test
    public void addTodo_increasesSize_andStoresTask() {
        TaskList list = new TaskList();

        list.addTodo("read book");

        assertEquals(1, list.size());
        assertTrue(list.getTask(0).toString().contains("read book"));
    }

    @Test
    public void markTask_marksCorrectTaskOnly() {
        TaskList list = new TaskList();
        list.addTodo("task one");
        list.addTodo("task two");

        list.markTask(0);

        assertTrue(list.getTask(0).toString().contains("[X]"));
        assertFalse(list.getTask(1).toString().contains("[X]"));
    }

    @Test
    public void delete_removesCorrectTask_andShiftsRemaining() {
        TaskList list = new TaskList();
        list.addTodo("A");
        list.addTodo("B");
        list.addTodo("C");

        list.delete(1);

        assertEquals(2, list.size());
        assertTrue(list.getTask(0).toString().contains("A"));
        assertTrue(list.getTask(1).toString().contains("C"));
    }
}
