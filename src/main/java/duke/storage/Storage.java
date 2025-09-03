package duke.storage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import duke.task.Deadline;
import duke.task.Event;
import duke.task.Task;
import duke.task.Todo;


/**
 * Handles persistent storage of tasks to and from the file system.
 * Manages to encode tasks to text format and decoding them back to objects.
 * Provides error handling for corrupted files and atomic save operations.
 */
public class Storage {

    /**
     * The path to the data storage file
     */
    private final Path dataFile;

    /**
     * The directory containing the data file
     */
    private final Path dataDir;

    /**
     * Creates a Storage instance with the specified file path.
     * Automatically determines the data directory from the file path.
     *
     * @param filePath The path to the storage file
     */
    public Storage(String filePath) {
        this.dataFile = Paths.get(filePath);
        Path parent = dataFile.getParent();
        this.dataDir = (parent != null) ? parent : Paths.get(".");
    }

    /**
     * Creates a Storage instance with the default file path "data/duke.txt".
     */
    public Storage() {
        this("data/duke.txt");
    }

    /**
     * Loads tasks from the storage file.
     * Creates an empty list if the file doesn't exist.
     * Handles corrupted files by backing them up and returning empty list.
     *
     * @return List of loaded Task objects
     */
    public List<Task> load() {
        ensureDataDir();
        if (!Files.exists(dataFile)) {
            return new ArrayList<>();
        }

        try {
            List<String> lines = Files.readAllLines(dataFile, StandardCharsets.UTF_8);
            List<Task> tasks = new ArrayList<>();

            for (String line : lines) {
                if (line == null || line.isBlank()) {
                    continue;
                }
                Task t = parseLine(line);
                tasks.add(t);
            }

            return tasks;
        } catch (Exception ex) {
            backupCorruptFile(ex);
            return new ArrayList<>();
        }
    }

    /**
     * Saves the list of tasks to storage using atomic file operations.
     * Writes to a temporary file first, then moves it to the final location.
     *
     * @param tasks The list of Task objects to save
     */
    public void save(List<Task> tasks) {
        ensureDataDir();
        Path tmp = dataDir.resolve(dataFile.getFileName() + ".tmp");

        try (BufferedWriter w = Files.newBufferedWriter(tmp, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {

            for (Task t : tasks) {
                w.write(encode(t));
                w.newLine();
            }

        } catch (IOException ioe) {
            System.err.println("[WARN] Failed to save tasks: " + ioe.getMessage());
            return;
        }

        try {
            Files.move(tmp, dataFile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException ioe) {
            try {
                Files.move(tmp, dataFile, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ioe2) {
                System.err.println("[WARN] Failed to finalise save: " + ioe2.getMessage());
            }
        }
    }

    /**
     * Ensures the data directory exists, creating it if necessary.
     *
     * @throws RuntimeException if the directory cannot be created
     */
    private void ensureDataDir() {
        try {
            if (!Files.exists(dataDir)) {
                Files.createDirectories(dataDir);
            }
        } catch (IOException ioe) {
            throw new RuntimeException("Unable to create data directory: " + dataDir, ioe);
        }
    }

    /**
     * Encodes a Task object into a tab-separated string for file storage.
     * Format varies by task type (Todo, Deadline, Event).
     *
     * @param t The Task to encode
     * @return Tab-separated string representation of the task
     */
    private String encode(Task t) {
        String done = t.isDone() ? "1" : "0";

        if (t instanceof Todo) {
            return String.join("\t", "T", done, t.getDescription());
        } else if (t instanceof Deadline d) {
            return String.join("\t", "D", done, d.getDescription(), d.getBy());
        } else if (t instanceof Event e) {
            return String.join("\t", "E", done, e.getDescription(), e.getFrom(), e.getTo());
        } else {
            return String.join("\t", "T", done, t.getDescription());
        }
    }

    /**
     * Parses a line from the storage file into a Task object.
     * Handles different task types and their specific date/time formats.
     *
     * @param line The tab-separated string from the storage file
     * @return The corresponding Task object
     * @throws IllegalArgumentException if the line format is invalid
     */
    private Task parseLine(String line) {
        String[] parts = line.split("\t");
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
            if (parts.length < 4) {
                throw new IllegalArgumentException("Missing deadline date: " + line);
            }

            String byRaw = parts[3].trim();
            LocalDateTime by;
            boolean hasTime;

            if (byRaw.contains("T")) {
                by = LocalDateTime.parse(byRaw, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
                hasTime = true;
            } else {
                by = LocalDate.parse(byRaw, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
                hasTime = false;
            }

            t = new Deadline(desc, by, hasTime);
            break;

        case "E":
            if (parts.length < 5) {
                throw new IllegalArgumentException("Missing event dates: " + line);
            }

            String fromRaw = parts[3].trim();
            String toRaw = parts[4].trim();
            LocalDateTime from;
            LocalDateTime to;
            boolean fromHasTime;
            boolean toHasTime;

            if (fromRaw.contains("T")) {
                from = LocalDateTime.parse(fromRaw, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
                fromHasTime = true;
            } else {
                from = LocalDate.parse(fromRaw, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
                fromHasTime = false;
            }

            if (toRaw.contains("T")) {
                to = LocalDateTime.parse(toRaw, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
                toHasTime = true;
            } else {
                to = LocalDate.parse(toRaw, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
                toHasTime = false;
            }

            t = new Event(desc, from, fromHasTime, to, toHasTime);
            break;

        default:
            throw new IllegalArgumentException("Unknown duke.task type: " + type);
        }

        if (done) {
            t.mark();
        }

        return t;
    }

    /**
     * Backs up a corrupted data file by renaming it with a timestamp.
     * Prints warning messages to standard error.
     *
     * @param ex The exception that indicated file corruption
     */
    private void backupCorruptFile(Exception ex) {
        try {
            String suffix = ".corrupt-" + System.currentTimeMillis();
            Path backup = dataDir.resolve(dataFile.getFileName() + suffix);
            Files.move(dataFile, backup, StandardCopyOption.REPLACE_EXISTING);
            System.err.println("[WARN] Data file appears corrupted: " + ex.getClass().getSimpleName()
                    + ". Backed up to " + backup);
        } catch (IOException ioe) {
            System.err.println("[WARN] Failed to back up corrupt file: " + ioe.getMessage());
        }
    }
}
