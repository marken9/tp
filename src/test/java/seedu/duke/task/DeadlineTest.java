package seedu.duke.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.duke.exception.IllegalDateException;
import seedu.duke.tasklist.CategoryList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeadlineTest {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HHmm");

    @Test
    public void addDeadline_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");

        LocalDateTime time = LocalDateTime.parse("12-03-2026 1830", formatter);
        categoryList.addDeadline(0, "submit project", time);

        assertEquals(1, categoryList.getCategory(0).getDeadlineList().getSize());
        assertEquals("submit project", categoryList.getCategory(0).getDeadlineList().get(0).getDescription());
    }

    @Test
    public void sortDeadlines_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");

        LocalDateTime early = LocalDateTime.parse("10-03-2026 1000", formatter);
        LocalDateTime late = LocalDateTime.parse("20-03-2026 1000", formatter);

        categoryList.addDeadline(0, "Late Task", late);
        categoryList.addDeadline(0, "Early Task", early);

        categoryList.sortDeadlines(0);

        assertEquals("Early Task", categoryList.getCategory(0).getDeadlineList().get(0).getDescription());
    }

    @Test
    public void deleteAllDeadlines_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("Personal");
        LocalDateTime time = LocalDateTime.parse("12-03-2026 1200", formatter);

        categoryList.addDeadline(0, "task 1", time);
        categoryList.addDeadline(0, "task 2", time);

        categoryList.deleteAllDeadlines(0);

        assertEquals(0, categoryList.getCategory(0).getDeadlineList().getSize());
    }

    @Test
    public void parseDateTime_invalidYear_throwsIllegalDateException() {
        // Verifies that years before 2026 throw the correct exception
        assertThrows(IllegalDateException.class, () -> {
            Deadline.parseDateTime("2025-12-31 2359");
        });
    }

    @Test
    public void parseDateTime_dateOnly_defaultsToLastMinute() throws IllegalDateException {
        // Verifies your logic of defaulting date-only input to 23:59
        LocalDateTime result = Deadline.parseDateTime("05-05-2026");
        assertEquals(23, result.getHour());
        assertEquals(59, result.getMinute());
        assertEquals(2026, result.getYear());
    }

    @Test
    public void parseDateTime_completelyInvalidString_exceptionThrown() {
        // Tests garbage input like "tomorrow" or "2026/13/40"
        assertThrows(IllegalDateException.class, () -> {
            Deadline.parseDateTime("not a date");
        });
    }

    @Test
    public void toFileFormat_correctOutput_success() {
        // Ensures the storage string matches your Storage.java requirements exactly
        LocalDateTime time = LocalDateTime.of(2026, 12, 1, 18, 0);
        Deadline deadline = new Deadline("Read book", time);
        // Assuming your format is "D | 0 | Read book | 2026-12-01 1800"
        String expected = "D | 0 | Read book | 01-12-2026 1800";
        assertEquals(expected, deadline.toFileFormat());
    }

    @BeforeEach
    public void setUp() {
        // This gives the "0 to 0" error a real range to work with
        seedu.duke.UniTasker.setStartYear(2024);
        seedu.duke.UniTasker.setEndYear(2030);
        seedu.duke.UniTasker.setDailyTaskLimit(8);
    }

}


