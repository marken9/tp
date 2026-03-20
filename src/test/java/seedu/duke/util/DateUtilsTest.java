package seedu.duke.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.exception.IllegalDateException;
import java.time.LocalDateTime;
import java.time.LocalDate;

public class DateUtilsTest {

    @Test
    void parseDateTime_validFullFormat_success() throws IllegalDateException {
        LocalDateTime result = DateUtils.parseDateTime("20-05-2026 1400");
        assertEquals(2026, result.getYear());
        assertEquals(14, result.getHour());
    }

    @Test
    void parseDateTime_validDateOnly_defaultsToEndOfDay() throws IllegalDateException {
        LocalDateTime result = DateUtils.parseDateTime("25-12-2026");
        assertEquals(23, result.getHour());
        assertEquals(59, result.getMinute());
    }

    @Test
    void parseDateTime_yearBefore2026_throwsException() {
        assertThrows(IllegalDateException.class, () -> {
            DateUtils.parseDateTime("31-12-2025 2359");
        });
    }

    @Test
    void parseLocalDate_validString_success() throws IllegalDateException {
        LocalDate result = DateUtils.parseLocalDate("01-01-2027");
        assertEquals(2027, result.getYear());
    }

    @Test
    void parseDateTime_valid2026Date_success() throws IllegalDateException {
        LocalDateTime date = DateUtils.parseDateTime("25-12-2026 1000");
        assertEquals(2026, date.getYear());
    }

    @BeforeEach
    public void setUp() {
        // This gives the "0 to 0" error a real range to work with
        seedu.duke.UniTasker.setStartYear(2024);
        seedu.duke.UniTasker.setEndYear(2030);
        seedu.duke.UniTasker.setDailyTaskLimit(8);
    }
}
