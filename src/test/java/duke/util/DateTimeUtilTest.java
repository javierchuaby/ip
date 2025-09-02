package duke.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class DateTimeUtilTest {

    @Test
    void parseLenientResult_validDateTime_returnsParseResult() {
        String validDateTime = "2025-12-25 1800";

        assertDoesNotThrow(() -> {
            DateTimeUtil.ParseResult result = DateTimeUtil.parseLenientResult(validDateTime);
            assertNotNull(result.dt);
            assertTrue(result.hasTime);
        });
    }

    @Test
    void parseLenientResult_validDate_returnsParseResult() {
        String validDate = "2025-12-25";

        assertDoesNotThrow(() -> {
            DateTimeUtil.ParseResult result = DateTimeUtil.parseLenientResult(validDate);
            assertNotNull(result.dt);
            assertFalse(result.hasTime);
        });
    }

    @Test
    void parseLenientResult_invalidFormat_throwsException() {
        String invalidDateTime = "invalid-date";

        assertThrows(IllegalArgumentException.class, () -> DateTimeUtil.parseLenientResult(invalidDateTime));
    }

    @Test
    void parseLenientResult_emptyString_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> DateTimeUtil.parseLenientResult(""));
    }

    @Test
    void toPrettyString_withTime_returnsFormattedString() {
        LocalDateTime dt = LocalDateTime.of(2025, 12, 25, 18, 0);
        String result = DateTimeUtil.toPrettyString(dt, true);

        assertNotNull(result);
        assertTrue(result.contains("25 Dec 2025"));
        assertTrue(result.contains("6:00 PM"));
    }

    @Test
    void toPrettyString_withoutTime_returnsFormattedDate() {
        LocalDateTime dt = LocalDateTime.of(2025, 12, 25, 18, 0);
        String result = DateTimeUtil.toPrettyString(dt, false);

        assertNotNull(result);
        assertTrue(result.contains("25 Dec 2025"));
        assertFalse(result.contains("6:00 PM"));
    }
}
