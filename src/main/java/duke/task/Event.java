package duke.task;

import duke.util.DateTimeUtil;
import java.time.LocalDateTime;

/**
 * Represents an event task with start and end date/time.
 * An event task has a description and occurs during a specific time period.
 * Extends the Task class with start and end date/time functionality.
 */
public class Event extends Task {

    /** The start date and time for this event */
    private final LocalDateTime from;

    /** The end date and time for this event */
    private final LocalDateTime to;

    /** Whether the start time includes a specific time component */
    private final boolean fromHasTime;

    /** Whether the end time includes a specific time component */
    private final boolean toHasTime;

    /**
     * Constructs an Event task with specified description and start/end times.
     *
     * @param description The event description
     * @param from The start date and time as LocalDateTime
     * @param fromHasTime true if start includes specific time, false for date only
     * @param to The end date and time as LocalDateTime
     * @param toHasTime true if end includes specific time, false for date only
     */
    public Event(
            String description,
            LocalDateTime from,
            boolean fromHasTime,
            LocalDateTime to,
            boolean toHasTime) {
        super(description);
        this.from = from;
        this.to = to;
        this.fromHasTime = fromHasTime;
        this.toHasTime = toHasTime;
    }

    /**
     * Constructs an Event task by parsing start and end date/time strings.
     * Uses DateTimeUtil to parse flexible date/time formats.
     *
     * @param description The event description
     * @param fromString The start date/time as a string to be parsed
     * @param toString The end date/time as a string to be parsed
     * @throws IllegalArgumentException if either date/time string cannot be parsed
     */
    public Event(String description, String fromString, String toString) {
        super(description);
        DateTimeUtil.ParseResult fromResult = DateTimeUtil.parseLenientResult(fromString);
        DateTimeUtil.ParseResult toResult = DateTimeUtil.parseLenientResult(toString);
        this.from = fromResult.dt;
        this.to = toResult.dt;
        this.fromHasTime = fromResult.hasTime;
        this.toHasTime = toResult.hasTime;
    }

    /**
     * Returns the start date/time as a LocalDateTime object.
     *
     * @return The start date and time
     */
    public LocalDateTime getFromDateTime() {
        return from;
    }

    /**
     * Returns the end date/time as a LocalDateTime object.
     *
     * @return The end date and time
     */
    public LocalDateTime getToDateTime() {
        return to;
    }

    /**
     * Returns the start date/time formatted for storage.
     *
     * @return The formatted start date/time string for file storage
     */
    public String getFrom() {
        return DateTimeUtil.toStorageString(from, fromHasTime);
    }

    /**
     * Returns the end date/time formatted for storage.
     *
     * @return The formatted end date/time string for file storage
     */
    public String getTo() {
        return DateTimeUtil.toStorageString(to, toHasTime);
    }

    /**
     * Returns the string representation of the event task.
     * Format: [E] [status] description (from: start_time, to: end_time)
     *
     * @return A formatted string describing the event task
     */
    @Override
    public String toString() {
        String fromStr = DateTimeUtil.toPrettyString(from, fromHasTime);
        String toStr = DateTimeUtil.toPrettyString(to, toHasTime);
        return "[E] ["
                + getStatusIcon()
                + "] "
                + description
                + " (from: "
                + fromStr
                + ", to: "
                + toStr
                + ")";
    }
}