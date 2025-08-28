package duke.task;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private Todo task;

    @BeforeEach
    void setUp() {
        task = new Todo("Test task");
    }

    @Test
    void constructor_validDescription_createsTask() {
        assertEquals("Test task", task.getDescription());
        assertFalse(task.isDone());
    }

    @Test
    void constructor_emptyDescription_createsTask() {
        Todo emptyTask = new Todo("");
        assertEquals("", emptyTask.getDescription());
        assertFalse(emptyTask.isDone());
    }

    @Test
    void mark_defaultTask_marksTaskAsDone() {
        task.mark();
        assertTrue(task.isDone());
    }

    @Test
    void unmark_doneTask_marksTaskAsNotDone() {
        task.mark();
        task.unmark();
        assertFalse(task.isDone());
    }

    @Test
    void toString_newTask_containsDescription() {
        String result = task.toString();
        assertTrue(result.contains("Test task"));
        assertTrue(result.contains("[T]"));
        assertTrue(result.contains("[ ]"));
    }

    @Test
    void toString_doneTask_containsXMarker() {
        task.mark();
        String result = task.toString();
        assertTrue(result.contains("Test task"));
        assertTrue(result.contains("[T]"));
        assertTrue(result.contains("[X]"));
    }

    @Test
    void getStatusIcon_newTask_returnsSpace() {
        assertEquals(" ", task.getStatusIcon());
    }

    @Test
    void getStatusIcon_doneTask_returnsX() {
        task.mark();
        assertEquals("X", task.getStatusIcon());
    }
}