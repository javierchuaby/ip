package duke.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;
import java.util.List;

public final class DateTimeUtil {
    private DateTimeUtil() {}

    private static final DateTimeFormatter STORAGE_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter STORAGE_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final DateTimeFormatter PRETTY_DATE = DateTimeFormatter.ofPattern("d MMM yyyy");
    private static final DateTimeFormatter PRETTY_DATETIME = DateTimeFormatter.ofPattern("d MMM yyyy, h:mm a").withLocale(Locale.ENGLISH);

    private static final List<DateTimeFormatter> DATE_PATTERNS = List.of(
            DateTimeFormatter.ISO_LOCAL_DATE,
            new DateTimeFormatterBuilder().parseCaseInsensitive()
                    .appendPattern("d/M/uuuu").toFormatter(Locale.ENGLISH),
            new DateTimeFormatterBuilder().parseCaseInsensitive()
                    .appendPattern("d-M-uuuu").toFormatter(Locale.ENGLISH),
            new DateTimeFormatterBuilder().parseCaseInsensitive()
                    .appendPattern("d.M.uuuu").toFormatter(Locale.ENGLISH),
            new DateTimeFormatterBuilder().parseCaseInsensitive()
                    .appendPattern("d MMM uuuu").toFormatter(Locale.ENGLISH),

            dayMonNoYearFormatter()
    );

    private static final List<DateTimeFormatter> DATETIME_PATTERNS = List.of(
            new DateTimeFormatterBuilder().parseCaseInsensitive()
                    .appendPattern("d/M/uuuu HHmm").toFormatter(Locale.ENGLISH),
            new DateTimeFormatterBuilder().parseCaseInsensitive()
                    .appendPattern("d-M-uuuu HHmm").toFormatter(Locale.ENGLISH),
            new DateTimeFormatterBuilder().parseCaseInsensitive()
                    .appendPattern("d.M.uuuu HHmm").toFormatter(Locale.ENGLISH),
            new DateTimeFormatterBuilder().parseCaseInsensitive()
                    .appendPattern("d MMM uuuu HHmm").toFormatter(Locale.ENGLISH),
            new DateTimeFormatterBuilder().parseCaseInsensitive()
                    .appendPattern("uuuu-MM-dd HHmm").toFormatter(Locale.ENGLISH),
            new DateTimeFormatterBuilder().parseCaseInsensitive()
                    .appendPattern("yyyy-MM-dd'T'HH:mm").toFormatter(Locale.ENGLISH)
    );

    private static DateTimeFormatter dayMonNoYearFormatter() {
        int currentYear = LocalDate.now().getYear();
        return new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("d MMM")
                .parseDefaulting(ChronoField.YEAR, currentYear)
                .toFormatter(Locale.ENGLISH);
    }

    public static final class ParseResult {
        public final LocalDateTime dt;
        public final boolean hasTime;
        private ParseResult(LocalDateTime dt, boolean hasTime) {
            this.dt = dt;
            this.hasTime = hasTime;
        }
    }

    public static ParseResult parseLenientResult(String raw) {
        String s = raw == null ? "" : raw.trim();
        if (s.isEmpty()) throw new IllegalArgumentException("Empty date/time");

        for (DateTimeFormatter f : DATETIME_PATTERNS) {
            try {
                LocalDateTime dt = LocalDateTime.parse(s, f);
                return new ParseResult(dt, true);
            } catch (DateTimeParseException ignored) {}
        }

        for (DateTimeFormatter f : DATE_PATTERNS) {
            try {
                LocalDate d = LocalDate.parse(s, f);
                if (!s.matches(".*\\d{4}.*")) {
                    d = d.withYear(LocalDate.now().getYear());
                }
                return new ParseResult(d.atStartOfDay(), false);
            } catch (DateTimeParseException ignored) {}
        }

        throw new IllegalArgumentException("Unrecognised date/time: \"" + raw + "\". " + examplesHelp());
    }

    public static String toPrettyString(LocalDateTime dt, boolean hasTime) {
        return hasTime ? dt.format(PRETTY_DATETIME) : dt.toLocalDate().format(PRETTY_DATE);
    }

    public static String toStorageString(LocalDateTime dt, boolean hasTime) {
        return hasTime ? dt.format(STORAGE_DATETIME) : dt.toLocalDate().format(STORAGE_DATE);
    }

    public static String examplesHelp() {
        return "Examples: 2/12/2025 1800, 12-3-2025 1800, 2019-10-15, 9 Aug 2025 1830, 9 Aug";
    }
}