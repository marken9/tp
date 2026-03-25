package seedu.duke.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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
    void parseDateTime_nullInput_throwsException() {
        assertThrows(IllegalDateException.class, () ->
                DateUtils.parseDateTime(null));
    }

    @Test
    void parseDateTime_emptyString_throwsException() {
        assertThrows(IllegalDateException.class, () ->
                DateUtils.parseDateTime("   "));
    }

    @Test
    void parseDateTime_completelyGarbage_throwsException() {
        assertThrows(IllegalDateException.class, () ->
                DateUtils.parseDateTime("not-a-date-at-all"));
    }

    @Test
    void parseDateTime_wrongDateSeparator_throwsException() {
        // yyyy/MM/dd is not a supported format
        assertThrows(IllegalDateException.class, () ->
                DateUtils.parseDateTime("2026/05/20 1400"));
    }

    @Test
    void parseDateTime_dayAndMonthSwapped_throwsException() {
        // 20-13-2026 has month 13 which is invalid
        assertThrows(IllegalDateException.class, () ->
                DateUtils.parseDateTime("20-13-2026 1000"));
    }

    @Test
    void parseDateTime_validFullFormat_correctFields() throws IllegalDateException {
        LocalDateTime result = DateUtils.parseDateTime("15-06-2026 0900");
        assertEquals(15, result.getDayOfMonth());
        assertEquals(6,  result.getMonthValue());
        assertEquals(2026, result.getYear());
        assertEquals(9,  result.getHour());
        assertEquals(0,  result.getMinute());
    }

    @Test
    void parseDateTime_dateOnly_defaultsTo2359() throws IllegalDateException {
        LocalDateTime result = DateUtils.parseDateTime("10-10-2026");
        assertEquals(23, result.getHour());
        assertEquals(59, result.getMinute());
    }

    @Test
    void parseDateTime_endOfYearMidnight_parsedCorrectly() throws IllegalDateException {
        LocalDateTime result = DateUtils.parseDateTime("31-12-2026 0000");
        assertEquals(31, result.getDayOfMonth());
        assertEquals(12, result.getMonthValue());
        assertEquals(0,  result.getHour());
        assertEquals(0,  result.getMinute());
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
    void parseDateTime_valid2026Date_success() throws IllegalDateException {
        LocalDateTime date = DateUtils.parseDateTime("25-12-2026 1000");
        assertEquals(2026, date.getYear());
    }

    @Test
    void parseDateTime_yearBeyondEndYear_throwsException() {
        // endYear is set to 2030 in setUp
        assertThrows(IllegalDateException.class, () ->
                DateUtils.parseDateTime("01-01-2031 1000"));
    }

    @Test
    void parseDateTime_yearAtEndYear_success() throws IllegalDateException {
        LocalDateTime result = DateUtils.parseDateTime("01-01-2030 1000");
        assertEquals(2030, result.getYear());
    }

    @Test
    void parseLocalDate_validString_success() throws IllegalDateException {
        LocalDate result = DateUtils.parseLocalDate("01-01-2027");
        assertEquals(2027, result.getYear());
    }

    @Test
    void parseDateTimeFromFile_currentYearPastTime_doesNotThrow() throws IllegalDateException {
        LocalDateTime result = DateUtils.parseDateTimeFromFile("01-01-2026 1000");
        assertEquals(2026, result.getYear());
    }

    @Test
    void parseDateTimeFromFile_dateOnly_defaultsTo2359() throws IllegalDateException {
        LocalDateTime result = DateUtils.parseDateTimeFromFile("05-05-2026");
        assertEquals(23, result.getHour());
        assertEquals(59, result.getMinute());
    }

    @Test
    void parseDateTimeFromFile_yearOutOfRange_throwsException() {
        assertThrows(IllegalDateException.class, () ->
                DateUtils.parseDateTimeFromFile("01-01-2050 1000"));
    }

    @Test
    void parseLocalDate_validString_returnsCorrectDate() throws IllegalDateException {
        LocalDate result = DateUtils.parseLocalDate("25-12-2027");
        assertEquals(25, result.getDayOfMonth());
        assertEquals(12, result.getMonthValue());
        assertEquals(2027, result.getYear());
    }

    @Test
    void parseLocalDate_invalidString_throwsException() {
        assertThrows(IllegalDateException.class, () ->
                DateUtils.parseLocalDate("bad input"));
    }

    @Test
    void parseRecurringDayFrom_validDay_doesNotThrow() {
        assertDoesNotThrow(() -> DateUtils.parseRecurringDayFrom("Monday"));
    }

    @Test
    void parseRecurringDayFrom_caseInsensitive_doesNotThrow() {
        assertDoesNotThrow(() -> DateUtils.parseRecurringDayFrom("friday"));
    }

    @Test
    void parseRecurringDayFrom_invalidDay_throwsException() {
        assertThrows(IllegalDateException.class, () ->
                DateUtils.parseRecurringDayFrom("Someday"));
    }

    @Test
    void parseRecurringDayTo_validDay_doesNotThrow() {
        assertDoesNotThrow(() -> DateUtils.parseRecurringDayTo("WEDNESDAY"));
    }

    @Test
    void parseRecurringDayTo_invalidDay_throwsException() {
        assertThrows(IllegalDateException.class, () ->
                DateUtils.parseRecurringDayTo("Yesterday"));
    }

    @Test
    void parseRecurringTimeFrom_validDayAndTime_returnsDateTime() throws IllegalDateException {
        LocalDate today = LocalDate.of(2026, 3, 23); // Monday
        LocalDateTime result = DateUtils.parseRecurringTimeFrom(today, "Friday", "1600");
        assertEquals(16, result.getHour());
        assertEquals(0,  result.getMinute());
    }

    @Test
    void parseRecurringTimeFrom_invalidTimeFormat_throwsException() {
        LocalDate today = LocalDate.of(2026, 3, 23);
        assertThrows(IllegalDateException.class, () ->
                DateUtils.parseRecurringTimeFrom(today, "Monday", "16:00"));
    }

    @Test
    void parseRecurringTimeTo_nullTime_defaultsTo2359() throws IllegalDateException {
        LocalDate today = LocalDate.of(2026, 3, 23);
        LocalDateTime result = DateUtils.parseRecurringTimeTo(today, "Tuesday", null);
        assertEquals(23, result.getHour());
        assertEquals(59, result.getMinute());
    }

    @Test
    void parseRecurringTimeTo_blankTime_defaultsTo2359() throws IllegalDateException {
        LocalDate today = LocalDate.of(2026, 3, 23);
        LocalDateTime result = DateUtils.parseRecurringTimeTo(today, "Thursday", "   ");
        assertEquals(23, result.getHour());
        assertEquals(59, result.getMinute());
    }

    @BeforeEach
    public void setUp() {
        // This gives the "0 to 0" error a real range to work with
        seedu.duke.UniTasker.setStartYear(2024);
        seedu.duke.UniTasker.setEndYear(2030);
        seedu.duke.UniTasker.setDailyTaskLimit(8);
    }
}
