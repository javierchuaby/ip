package duke.task;

import duke.storage.Storage;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class TaskList {
    private final List<Task> tasks = new ArrayList<>();
    private final Storage storage;

    public TaskList(Storage storage, List<Task> initial) {
        this.storage = storage;
        if (initial != null) tasks.addAll(initial);
    }

    public int size() {
        return tasks.size();
    }

    public Task get(int idx) {
        return tasks.get(idx);
    }

    public List<Task> asUnmodifiable() {
        return List.copyOf(tasks);
    }

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

    public int indexOf(Task t) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i) == t) return i;
        }
        return -1;
    }

    public List<Task> tasksOn(LocalDate date) {
        List<Task> out = new ArrayList<>();
        for (Task t : this.tasks) {
            if (t instanceof Deadline d) {
                if (d.getByDateTime().toLocalDate().isEqual(date)) {
                    out.add(t);
                }
            } else if (t instanceof Event e) {
                LocalDate from = e.getFromDateTime().toLocalDate();
                LocalDate to   = e.getToDateTime().toLocalDate();
                if (!(date.isBefore(from) || date.isAfter(to))) {
                    out.add(t);
                }
            }
        }
        return out;
    }

    public void clear() {
        tasks.clear();
        storage.save(tasks);
    }
}