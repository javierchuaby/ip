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
import duke.task.TaskType;
import duke.task.Todo;

/**
 * Handles persistent storage of tasks to and from the file system. Uses TaskType enum for type
 * safety instead of magic strings. Manages encoding tasks to text format and decoding them back to
 * objects. Provides error handling for corrupted files and atomic save operations.
 */
public class Storage {
    // Constants for magic numbers
    private static final int MINIMUM_PARTS_COUNT = 3;
    private static final int DEADLINE_PARTS_COUNT = 4;
    private static final int EVENT_PARTS_COUNT = 5;
    private static final String DONE_FLAG = "1";
    private static final String NOT_DONE_FLAG = "0";

    // Date/time format constants
    private static final DateTimeFormatter STORAGE_DATETIME_FORMAT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final DateTimeFormatter STORAGE_DATE_FORMAT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String TIME_INDICATOR = "T";

    private final Path dataFile;
    private final Path dataDir;

    /**
     * Creates a Storage instance backed by the specified file path.
     * <p>
     * The parent directory is derived from the given path; if the path has no parent,
     * the current working directory (".") is used. The actual directory creation is
     * deferred and performed when needed by {@link #load()} or {@link #save(List)}.
     *
     * @param filePath the file path to persist tasks to (e.g., "data/duke.txt"); must be non-null and non-blank
     * @throws AssertionError if assertions are enabled and {@code filePath} is null or blank
     */
    public Storage(String filePath) {
        assert filePath != null && !filePath.trim().isEmpty() : "File path cannot be null or empty";

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
     * Loads tasks from the storage file. Creates an empty list if the file doesn't exist. Handles
     * corrupted files by backing them up and returning empty list.
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
     * Saves the list of tasks to storage using atomic file operations. Writes to a temporary file
     * first, then moves it to the final location.
     *
     * @param tasks The list of Task objects to save
     */
    public void save(List<Task> tasks) {
        assert tasks != null : "Task list cannot be null";

        ensureDataDir();

        assert Files.exists(dataDir) : "Data directory should exist after ensureDataDir()";

        Path tmp = dataDir.resolve(dataFile.getFileName() + ".tmp");
        try (BufferedWriter w =
                 Files.newBufferedWriter(
                     tmp,
                     StandardCharsets.UTF_8,
                     StandardOpenOption.CREATE,
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
            Files.move(
                tmp,
                dataFile,
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.ATOMIC_MOVE);
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
     * Encodes a Task object into a tab-separated string for file storage. Uses TaskType enum for
     * type safety instead of magic strings.
     *
     * @param t The Task to encode
     * @return Tab-separated string representation of the task
     */
    private String encode(Task t) {
        assert t != null : "Task cannot be null";

        String done = t.isDone() ? DONE_FLAG : NOT_DONE_FLAG;
        TaskType type = t.getTaskType();

        switch (type) {
        case TODO:
            return String.join("\t", type.getStorageCode(), done, t.getDescription());
        case DEADLINE:
            Deadline d = (Deadline) t;
            return String.join("\t", type.getStorageCode(), done, d.getDescription(), d.getBy());
        case EVENT:
            Event e = (Event) t;
            return String.join(
                "\t",
                type.getStorageCode(),
                done,
                e.getDescription(),
                e.getFrom(),
                e.getTo());
        default:
            throw new IllegalArgumentException();
        }
    }

    /**
     * Parses a line from the storage file into a Task object. Uses TaskType enum for type safety
     * instead of magic strings.
     *
     * @param line The tab-separated string from the storage file
     * @return The corresponding Task object
     * @throws IllegalArgumentException if the line format is invalid
     */
    private Task parseLine(String line) {
        assert line != null : "Line cannot be null";
        assert !line.trim().isEmpty() : "Line cannot be empty";

        String[] parts = line.split("\t");
        validateLineParts(parts, line);

        String typeCode = parts[0].trim();
        boolean done = isDoneFromString(parts[1].trim());
        String desc = parts[2];

        TaskType taskType = TaskType.fromStorageCode(typeCode);
        Task task = createTaskByType(taskType, desc, parts, line);

        if (done) {
            task.mark();
        }

        assert task != null : "Created task should not be null";
        return task;
    }

    /**
     * Validates that line parts meet minimum requirements.
     *
     * @param parts        The split line parts
     * @param originalLine The original line for error reporting
     * @throws IllegalArgumentException if validation fails
     */
    private void validateLineParts(String[] parts, String originalLine) {
        if (parts.length < MINIMUM_PARTS_COUNT) {
            throw new IllegalArgumentException("Malformed line: " + originalLine);
        }
    }

    /**
     * Converts done flag string to boolean.
     *
     * @param doneStr The done flag string
     * @return true if task is done, false otherwise
     */
    private boolean isDoneFromString(String doneStr) {
        assert DONE_FLAG.equals(doneStr) || NOT_DONE_FLAG.equals(doneStr)
            : "Done flag must be 0 or 1";
        return DONE_FLAG.equals(doneStr);
    }

    /**
     * Creates a task based on the TaskType enum.
     *
     * @param type         The TaskType enum value
     * @param desc         The task description
     * @param parts        The parsed line parts
     * @param originalLine The original line for error reporting
     * @return The created Task object
     */
    private Task createTaskByType(TaskType type, String desc, String[] parts, String originalLine) {
        switch (type) {
        case TODO:
            return new Todo(desc);
        case DEADLINE:
            return createDeadlineTask(desc, parts, originalLine);
        case EVENT:
            return createEventTask(desc, parts, originalLine);
        default:
            throw new IllegalArgumentException();
        }
    }

    /**
     * Creates a Deadline task from parsed parts.
     */
    private Task createDeadlineTask(String desc, String[] parts, String originalLine) {
        if (parts.length < DEADLINE_PARTS_COUNT) {
            throw new IllegalArgumentException("Missing deadline date: " + originalLine);
        }

        String byRaw = parts[3].trim();
        LocalDateTime by = parseStorageDateTime(byRaw);
        boolean hasTime = hasTimeComponent(byRaw);

        return new Deadline(desc, by, hasTime);
    }

    /**
     * Creates an Event task from parsed parts.
     */
    private Task createEventTask(String desc, String[] parts, String originalLine) {
        if (parts.length < EVENT_PARTS_COUNT) {
            throw new IllegalArgumentException("Missing event dates: " + originalLine);
        }

        String fromRaw = parts[3].trim();
        String toRaw = parts[4].trim();

        LocalDateTime from = parseStorageDateTime(fromRaw);
        LocalDateTime to = parseStorageDateTime(toRaw);
        boolean fromHasTime = hasTimeComponent(fromRaw);
        boolean toHasTime = hasTimeComponent(toRaw);

        return new Event(desc, from, fromHasTime, to, toHasTime);
    }

    /**
     * Parses a date/time string from storage format.
     *
     * @param dateTimeStr The date/time string
     * @return Parsed LocalDateTime
     */
    private LocalDateTime parseStorageDateTime(String dateTimeStr) {
        boolean hasTime = hasTimeComponent(dateTimeStr);
        if (hasTime) {
            return LocalDateTime.parse(dateTimeStr, STORAGE_DATETIME_FORMAT);
        } else {
            return LocalDate.parse(dateTimeStr, STORAGE_DATE_FORMAT).atStartOfDay();
        }
    }

    /**
     * Checks if a date/time string includes time component.
     *
     * @param dateTimeStr The date/time string
     * @return true if includes time, false otherwise
     */
    private boolean hasTimeComponent(String dateTimeStr) {
        return dateTimeStr.contains(TIME_INDICATOR);
    }

    /**
     * Backs up a corrupted data file by renaming it with a timestamp. Prints warning messages to
     * standard error.
     *
     * @param ex The exception that indicated file corruption
     */
    private void backupCorruptFile(Exception ex) {
        try {
            String suffix = ".corrupt-" + System.currentTimeMillis();
            Path backup = dataDir.resolve(dataFile.getFileName() + suffix);
            Files.move(dataFile, backup, StandardCopyOption.REPLACE_EXISTING);
            System.err.println(
                "[WARN] Data file appears corrupted: "
                    + ex.getClass().getSimpleName()
                    + ". Backed up to "
                    + backup);
        } catch (IOException ioe) {
            System.err.println("[WARN] Failed to back up corrupt file: " + ioe.getMessage());
        }
    }
}
