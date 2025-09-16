package duke.task;

import java.time.LocalDateTime;

import duke.util.DateTimeUtil;

/**
 * Represents a deadline task with a due date and time. A deadline task has a description and a
 * specific due date/time. Extends the Task class with date/time functionality.
 */
public class Deadline extends Task {

    /**
     * The due date and time for this deadline
     */
    private final LocalDateTime by;

    /**
     * Whether the due time includes a specific time component
     */
    private final boolean hasTime;

    /**
     * Constructs a Deadline task with specified description and due date/time.
     *
     * @param description The task description
     * @param by          The due date and time as LocalDateTime
     * @param hasTime     true if the due date includes a specific time, false for date only
     */
    public Deadline(String description, LocalDateTime by, boolean hasTime) {
        super(description);
        this.by = by;
        this.hasTime = hasTime;
    }

    /**
     * Constructs a Deadline task by parsing a date/time string. Uses DateTimeUtil to parse flexible
     * date/time formats.
     *
     * @param description The task description
     * @param byString    The due date/time as a string to be parsed
     * @throws IllegalArgumentException if the date/time string cannot be parsed
     */
    public Deadline(String description, String byString) {
        super(description);
        DateTimeUtil.ParseResult result = DateTimeUtil.parseLenientResult(byString);
        this.by = result.dt;
        this.hasTime = result.hasTime;
    }

    /**
     * Returns the due date/time as a LocalDateTime object.
     *
     * @return The due date and time
     */
    public LocalDateTime getByDateTime() {
        return by;
    }

    /**
     * Returns the due date/time formatted for storage.
     *
     * @return The formatted due date/time string for file storage
     */
    public String getBy() {
        return DateTimeUtil.toStorageString(by, hasTime);
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.DEADLINE;
    }

    /**
     * Returns the string representation of the deadline task. Format: [D] [status] description (by:
     * formatted_date_time)
     *
     * @return A formatted string describing the deadline task
     */
    @Override
    public String toString() {
        return "[D] ["
            + getStatusIcon()
            + "] "
            + description
            + " (by: "
            + DateTimeUtil.toPrettyString(by, hasTime)
            + ")";
    }
}
