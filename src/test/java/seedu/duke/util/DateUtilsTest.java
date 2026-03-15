package seedu.duke.util;

import org.junit.jupiter.api.Test;
import seedu.duke.exception.IllegalDateException;
import java.time.LocalDateTime;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class DateUtilsTest {

    @Test
    void parseDateTime_validFullFormat_success() throws IllegalDateException {
        LocalDateTime result = DateUtils.parseDateTime("2026-05-20 1400");
        assertEquals(2026, result.getYear());
        assertEquals(14, result.getHour());
    }

    @Test
    void parseDateTime_validDateOnly_defaultsToEndOfDay() throws IllegalDateException {
        LocalDateTime result = DateUtils.parseDateTime("2026-12-25");
        assertEquals(23, result.getHour());
        assertEquals(59, result.getMinute());
    }

    @Test
    void parseDateTime_yearBefore2026_throwsException() {
        assertThrows(IllegalDateException.class, () -> {
            DateUtils.parseDateTime("2025-12-31 2359");
        });
    }

    @Test
    void parseLocalDate_validString_success() throws IllegalDateException {
        LocalDate result = DateUtils.parseLocalDate("2027-01-01");
        assertEquals(2027, result.getYear());
    }

    @Test
    void parseDateTime_valid2026Date_success() throws IllegalDateException {
        LocalDateTime date = DateUtils.parseDateTime("2026-03-15 1000");
        assertEquals(2026, date.getYear());
    }
}