import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private final List<Task> tasks = new ArrayList<>();
    private final Storage storage;

    public TaskList(Storage storage, List<Task> initial) {
        this.storage = storage;
        if (initial != null) tasks.addAll(initial);
    }

    public int size() { return tasks.size(); }
    public Task get(int idx) { return tasks.get(idx); }
    public List<Task> asUnmodifiable() { return List.copyOf(tasks); }

    public void add(Task t) {
        tasks.add(t);
        storage.save(tasks);
    }

    public Task remove(int idx) {
        Task removed = tasks.remove(idx);
        storage.save(tasks);
        return removed;
    }

    public void mark(int idx) {
        tasks.get(idx).mark();
        storage.save(tasks);
    }

    public void unmark(int idx) {
        tasks.get(idx).unmark();
        storage.save(tasks);
    }
}