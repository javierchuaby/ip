import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final Path dataDir = Paths.get("data");                 // ./data (relative, OS-independent)
    private final Path dataFile = dataDir.resolve("duke.txt");      // ./data/duke.txt

    public List<Task> load() {
        ensureDataDir();
        if (!Files.exists(dataFile)) {
            // First run: nothing to load — return empty list.
            return new ArrayList<>();
        }
        try {
            List<String> lines = Files.readAllLines(dataFile, StandardCharsets.UTF_8);
            List<Task> tasks = new ArrayList<>();
            for (String line : lines) {
                if (line == null || line.isBlank()) continue;
                Task t = parseLine(line);
                tasks.add(t);
            }
            return tasks;
        } catch (Exception ex) {
            // Stretch goal: file corrupted → back it up and start clean.
            backupCorruptFile(ex);
            return new ArrayList<>();
        }
    }

    public void save(List<Task> tasks) {
        ensureDataDir();
        // Write to a temp file and then move atomically to avoid partial writes.
        Path tmp = dataDir.resolve("duke.txt.tmp");
        try (BufferedWriter w = Files.newBufferedWriter(tmp, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (Task t : tasks) {
                w.write(encode(t));
                w.newLine();
            }
        } catch (IOException ioe) {
            // If save fails, we surface but do not crash the app.
            System.err.println("[WARN] Failed to save tasks: " + ioe.getMessage());
            return;
        }
        try {
            Files.move(tmp, dataFile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException ioe) {
            System.err.println("[WARN] Failed to finalise save: " + ioe.getMessage());
        }
    }

    private void ensureDataDir() {
        try {
            if (!Files.exists(dataDir)) {
                Files.createDirectories(dataDir);
            }
        } catch (IOException ioe) {
            throw new RuntimeException("Unable to create data directory: " + dataDir, ioe);
        }
    }

    // ----- Serialisation format -----
    // T | 1 | read book
    // D | 0 | return book | 2025-06-06
    // E | 0 | project meeting | 2025-08-06T14:00/2025-08-06T16:00
    // (Keep UI output unchanged; this is only the file format.)
    private String encode(Task t) {
        String done = t.isDone() ? "1" : "0";
        if (t instanceof Todo) {
            return String.join(" | ", "T", done, t.getDescription());
        } else if (t instanceof Deadline) {
            Deadline d = (Deadline) t;
            return String.join(" | ", "D", done, d.getDescription(), d.getBy()); // keep raw text you already accept
        } else if (t instanceof Event) {
            Event e = (Event) t;
            return String.join(" | ", "E", done, e.getDescription(), e.getFrom() + " | " + e.getTo()); // keep raw text you already accept
        } else {
            // Fallback to plain task
            return String.join(" | ", "T", done, t.getDescription());
        }
    }

    private Task parseLine(String line) {
        // Split exactly on " | " to tolerate spaces in description.
        String[] parts = line.split("\\s\\|\\s");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Malformed line: " + line);
        }
        String type = parts[0].trim();
        boolean done = "1".equals(parts[1].trim());
        String desc = parts[2];

        Task t;
        switch (type) {
            case "T":
                t = new Todo(desc);
                break;
            case "D":
                if (parts.length < 4) throw new IllegalArgumentException("Missing deadline date: " + line);
                // Use the same parser logic your app already uses (raw string preserved)
                t = new Deadline(desc, parts[3]);
                break;
            case "E":
                if (parts.length >= 5) {
                    t = new Event(desc, parts[3], parts[4]);
                } else if (parts.length == 4) {
                    String raw = parts[3];
                    int slash = raw.indexOf('/');
                    if (slash == -1) throw new IllegalArgumentException("Missing event end time: " + line);
                    String from = raw.substring(0, slash).trim();
                    String to = raw.substring(slash + 1).trim();
                    t = new Event(desc, from, to);
                } else {
                    throw new IllegalArgumentException("Missing event time: " + line);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown task type: " + type);
        }
        if (done) t.mark();
        return t;
    }

    private void backupCorruptFile(Exception ex) {
        try {
            String suffix = ".corrupt-" + System.currentTimeMillis();
            Path backup = dataDir.resolve("duke.txt" + suffix);
            Files.move(dataFile, backup, StandardCopyOption.REPLACE_EXISTING);
            System.err.println("[WARN] Data file appears corrupted: " + ex.getClass().getSimpleName()
                    + ". Backed up to " + backup);
        } catch (IOException ioe) {
            System.err.println("[WARN] Failed to back up corrupt file: " + ioe.getMessage());
        }
    }
}