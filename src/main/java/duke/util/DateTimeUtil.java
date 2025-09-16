package duke.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Locale;

/**
 * Utility class for parsing and formatting date/time strings. Supports multiple input formats and
 * provides consistent output formatting. Handles both date-only and date-time parsing with flexible
 * format support.
 */
public final class DateTimeUtil {

    /**
     * Formatter for storing date-times in storage files
     */
    private static final DateTimeFormatter STORAGE_DATETIME =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    /**
     * Formatter for displaying dates in user-friendly format
     */
    private static final DateTimeFormatter PRETTY_DATE = DateTimeFormatter.ofPattern("d MMM yyyy");

    /**
     * Formatter for displaying date-times in user-friendly format
     */
    private static final DateTimeFormatter PRETTY_DATETIME =
        DateTimeFormatter.ofPattern("d MMM yyyy, h:mm a").withLocale(Locale.ENGLISH);

    /**
     * List of supported date input patterns
     */
    private static final List<DateTimeFormatter> DATE_PATTERNS =
        List.of(
            DateTimeFormatter.ISO_LOCAL_DATE,
            new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("d/M/uuuu")
                .toFormatter(Locale.ENGLISH),
            new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("d-M-uuuu")
                .toFormatter(Locale.ENGLISH),
            new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("d.M.uuuu")
                .toFormatter(Locale.ENGLISH),
            new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("d MMM uuuu")
                .toFormatter(Locale.ENGLISH),
            dayMonNoYearFormatter());

    /**
     * List of supported date-time input patterns
     */
    private static final List<DateTimeFormatter> DATETIME_PATTERNS =
        List.of(
            new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("d/M/uuuu HHmm")
                .toFormatter(Locale.ENGLISH),
            new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("d-M-uuuu HHmm")
                .toFormatter(Locale.ENGLISH),
            new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("d.M.uuuu HHmm")
                .toFormatter(Locale.ENGLISH),
            new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("d MMM uuuu HHmm")
                .toFormatter(Locale.ENGLISH),
            new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("uuuu-MM-dd HHmm")
                .toFormatter(Locale.ENGLISH),
            new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("yyyy-MM-dd'T'HH:mm")
                .toFormatter(Locale.ENGLISH)
        );

    /**
     * Formatter for storing dates in storage files
     */
    private static final DateTimeFormatter STORAGE_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Private constructor to prevent instantiation of utility class
     */
    private DateTimeUtil() {
    }

    /**
     * Creates a date formatter that assumes current year for day-month patterns.
     *
     * @return DateTimeFormatter that defaults to current year
     */
    private static DateTimeFormatter dayMonNoYearFormatter() {
        int currentYear = LocalDate.now().getYear();
        return new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("d MMM")
            .parseDefaulting(ChronoField.YEAR, currentYear)
            .toFormatter(Locale.ENGLISH);
    }

    /**
     * Parses a date/time string using multiple format patterns. Tries date-time patterns first,
     * then date-only patterns.
     *
     * @param raw The raw date/time string to parse
     * @return ParseResult containing the parsed date/time and time flag
     * @throws IllegalArgumentException if no pattern matches the input
     */
    public static ParseResult parseLenientResult(String raw) {
        String s = raw == null ? "" : raw.trim();
        if (s.isEmpty()) {
            throw new IllegalArgumentException("Empty date/time");
        }

        // Try datetime patterns first
        for (DateTimeFormatter f : DATETIME_PATTERNS) {
            try {
                LocalDateTime dt = LocalDateTime.parse(s, f);
                return new ParseResult(dt, true);
            } catch (DateTimeParseException ignored) {
                // Continue trying other patterns
            }
        }

        // Try date-only patterns
        for (DateTimeFormatter f : DATE_PATTERNS) {
            assert f != null : "Formatter should not be null";
            try {
                LocalDate d = LocalDate.parse(s, f);
                // If no year specified, default to current year
                if (!s.matches(".*\\d{4}.*")) {
                    d = d.withYear(LocalDate.now().getYear());
                }
                return new ParseResult(d.atStartOfDay(), false);
            } catch (DateTimeParseException ignored) {
                // Continue trying other patterns
            }
        }

        throw new IllegalArgumentException(
            "Unrecognised date/time: \"" + raw + "\". " + examplesHelp());
    }

    /**
     * Formats a LocalDateTime for user display.
     *
     * @param dt      The LocalDateTime to format
     * @param hasTime true to include time in output, false for date only
     * @return Formatted string for display to users
     */
    public static String toPrettyString(LocalDateTime dt, boolean hasTime) {
        return hasTime ? dt.format(PRETTY_DATETIME) : dt.toLocalDate().format(PRETTY_DATE);
    }

    /**
     * Formats a LocalDateTime for storage in files.
     *
     * @param dt      The LocalDateTime to format
     * @param hasTime true to include time in output, false for date only
     * @return Formatted string suitable for file storage
     */
    public static String toStorageString(LocalDateTime dt, boolean hasTime) {
        return hasTime ? dt.format(STORAGE_DATETIME) : dt.toLocalDate().format(STORAGE_DATE);
    }

    /**
     * Returns example date/time formats for user help messages.
     *
     * @return String containing example date/time formats
     */
    public static String examplesHelp() {
        return "Examples: 2/12/2025 1800, 12-3-2025 1800, 2019-10-15, 9 Aug 2025 1830, 9 Aug";
    }

    /**
     * Represents the result of parsing a date/time string. Contains the parsed LocalDateTime and
     * whether it included time information.
     */
    public static final class ParseResult {
        /**
         * The parsed date/time
         */
        public final LocalDateTime dt;

        /**
         * Whether the original string included time information
         */
        public final boolean hasTime;

        /**
         * Creates a ParseResult with the given date/time and time flag.
         *
         * @param dt      The parsed LocalDateTime
         * @param hasTime true if the original string included time
         */
        private ParseResult(LocalDateTime dt, boolean hasTime) {
            this.dt = dt;
            this.hasTime = hasTime;
        }
    }
}
