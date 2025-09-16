package duke.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        AssertionError ex = org.junit.jupiter.api.Assertions.assertThrows(
                AssertionError.class,
                () -> new Todo("")
        );
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
        assertTrue(result.matches(".*\\[\\s*\\].*"));
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
        assertEquals("  ", task.getStatusIcon());
    }

    @Test
    void getStatusIcon_doneTask_returnsX() {
        task.mark();
        assertEquals("X", task.getStatusIcon());
    }
}
