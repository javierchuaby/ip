package duke.task;

import duke.storage.Storage;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the collection of tasks in the Duke application.
 * Manages a list of tasks and handles persistence through Storage.
 * Provides methods for adding, removing, marking, and querying tasks.
 */
public class TaskList {

    /** The list of tasks managed by this TaskList */
    private final List<Task> tasks = new ArrayList<>();

    /** The storage system for persisting tasks */
    private final Storage storage;

    /**
     * Constructs a TaskList with the specified storage and optional initial tasks.
     *
     * @param storage The Storage object used for saving and loading tasks
     * @param initial An optional list of initial tasks, can be null
     */
    public TaskList(Storage storage, List<Task> initial) {
        this.storage = storage;
        if (initial != null) tasks.addAll(initial);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return The total count of tasks
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the task at the specified index.
     *
     * @param idx The index of the task to retrieve (0-based)
     * @return The Task object at the given index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public Task get(int idx) {
        return tasks.get(idx);
    }

    /**
     * Returns an unmodifiable view of the task list.
     * Changes to the returned list will not affect the original task list.
     *
     * @return An unmodifiable copy of the task list
     */
    public List<Task> asUnmodifiable() {
        return List.copyOf(tasks);
    }

    /**
     * Adds a new task to the list and saves the updated list to storage.
     *
     * @param t The task to add to the list
     */
    public void add(Task t) {
        tasks.add(t);
        storage.save(tasks);
    }

    /**
     * Removes the task at the specified index and saves the updated list.
     *
     * @param idx The index of the task to remove (0-based)
     * @return The removed Task object
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public Task remove(int idx) {
        Task removed = tasks.remove(idx);
        storage.save(tasks);
        return removed;
    }

    /**
     * Marks the task at the specified index as completed and saves changes.
     *
     * @param idx The index of the task to mark (0-based)
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public void mark(int idx) {
        tasks.get(idx).mark();
        storage.save(tasks);
    }

    /**
     * Marks the task at the specified index as not completed and saves changes.
     *
     * @param idx The index of the task to unmark (0-based)
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public void unmark(int idx) {
        tasks.get(idx).unmark();
        storage.save(tasks);
    }

    /**
     * Finds the index of the specified task in the list.
     * Uses object identity comparison to find the task.
     *
     * @param t The task to find
     * @return The index of the task, or -1 if not found
     */
    public int indexOf(Task t) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i) == t) return i;
        }
        return -1;
    }

    /**
     * Returns a list of tasks that occur on the specified date.
     * For deadlines, matches tasks due on the date.
     * For events, matches tasks that span or include the date.
     *
     * @param date The date to search for tasks
     * @return A list of tasks occurring on the given date
     */
    public List<Task> tasksOn(LocalDate date) {
        List<Task> out = new ArrayList<>();
        for (Task t : this.tasks) {
            if (t instanceof Deadline d) {
                if (d.getByDateTime().toLocalDate().isEqual(date)) {
                    out.add(t);
                }
            } else if (t instanceof Event e) {
                LocalDate from = e.getFromDateTime().toLocalDate();
                LocalDate to = e.getToDateTime().toLocalDate();
                if (!(date.isBefore(from) || date.isAfter(to))) {
                    out.add(t);
                }
            }
        }
        return out;
    }

    /**
     * Removes all tasks from the list and saves the empty list to storage.
     */
    public void clear() {
        tasks.clear();
        storage.save(tasks);
    }

    /**
     * Finds all tasks whose descriptions contain the specified keyword (case-insensitive).
     *
     * @param keyword The keyword to search for in task descriptions
     * @return A list of tasks that contain the keyword in their description
     */
    public List<Task> findByKeyword(String keyword) {
        List<Task> matches = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();

        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(lowerKeyword)) {
                matches.add(task);
            }
        }

        return matches;
    }
}
