package duke.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import duke.storage.Storage;

class TaskListTest {
    private TaskList taskList;
    private Todo task1;
    private Todo task2;

    @BeforeEach
    void setUp() {
        Storage mockStorage = Mockito.mock(Storage.class);
        taskList = new TaskList(mockStorage, new ArrayList<>());
        task1 = new Todo("Task 1");
        task2 = new Todo("Task 2");
    }

    @Test
    void constructor_emptyList_createsEmptyTaskList() {
        assertEquals(0, taskList.size());
    }

    @Test
    void add_singleTask_increasesSize() {
        taskList.add(task1);
        assertEquals(1, taskList.size());
    }

    @Test
    void add_multipleTasks_maintainsOrder() {
        taskList.add(task1);
        taskList.add(task2);

        assertEquals(2, taskList.size());
        assertEquals(task1, taskList.get(0));
        assertEquals(task2, taskList.get(1));
    }

    @Test
    void remove_validIndex_removesTask() {
        taskList.add(task1);
        taskList.add(task2);

        Task removedTask = taskList.remove(0);

        assertEquals(task1, removedTask);
        assertEquals(1, taskList.size());
        assertEquals(task2, taskList.get(0));
    }

    @Test
    void remove_invalidIndex_throwsException() {
        taskList.add(task1);

        assertThrows(IndexOutOfBoundsException.class, () -> taskList.remove(5));
    }

    @Test
    void get_validIndex_returnsCorrectTask() {
        taskList.add(task1);
        taskList.add(task2);

        assertEquals(task1, taskList.get(0));
        assertEquals(task2, taskList.get(1));
    }

    @Test
    void get_invalidIndex_throwsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> taskList.get(0));
    }

    @Test
    void mark_validIndex_marksTask() {
        taskList.add(task1);
        taskList.mark(0);

        assertTrue(taskList.get(0).isDone());
    }

    @Test
    void unmark_validIndex_unmarksTask() {
        taskList.add(task1);
        taskList.mark(0);
        taskList.unmark(0);

        assertFalse(taskList.get(0).isDone());
    }

    @Test
    void clear_removesAllTasks() {
        taskList.add(task1);
        taskList.add(task2);

        taskList.clear();

        assertEquals(0, taskList.size());
    }
}
