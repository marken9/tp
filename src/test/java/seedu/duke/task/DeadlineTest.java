package seedu.duke.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.duke.exception.IllegalDateException;
import seedu.duke.tasklist.CategoryList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    public void toString_notDone_containsUncheckedMark() {
        Deadline d = new Deadline("Write report", LocalDateTime.of(2026, 5, 5, 12, 0));
        String result = d.toString();
        assertTrue(result.startsWith("[D]"), "Should start with [D] type marker");
        assertTrue(result.contains("[ ]"), "Undone task should show empty checkbox");
    }

    @Test
    public void toString_done_containsCheckedMark() {
        Deadline d = new Deadline("Write report", LocalDateTime.of(2026, 5, 5, 12, 0));
        d.mark();
        assertTrue(d.toString().contains("[X]"), "Done task should show X in checkbox");
    }

    @Test
    public void toString_containsFormattedDate() {
        LocalDateTime time = LocalDateTime.of(2026, 12, 31, 23, 59);
        Deadline d = new Deadline("Year-end task", time);
        assertTrue(d.toString().contains("31-12-2026 2359"));
    }

    @Test
    public void parseDateTime_invalidYear_throwsIllegalDateException() {
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
        assertThrows(IllegalDateException.class, () -> {
            Deadline.parseDateTime("not a date");
        });
    }

    @Test
    public void getDate_returnsSameAsGetBy() {
        LocalDateTime time = LocalDateTime.of(2026, 9, 9, 9, 0);
        Deadline d = new Deadline("Same date", time);
        assertEquals(d.getBy(), d.getDate(),
                "getDate() must return the same value as getBy()");
    }

    @Test
    public void parseDateTime_midnightTime_parsedCorrectly() throws IllegalDateException {
        LocalDateTime result = Deadline.parseDateTime("05-05-2026 0000");
        assertEquals(0,    result.getHour());
        assertEquals(0,    result.getMinute());
        assertEquals(2026, result.getYear());
    }

    @Test
    public void parseDateTime_endOfYearDate_parsedCorrectly() throws IllegalDateException {
        LocalDateTime result = Deadline.parseDateTime("31-12-2026 2359");
        assertEquals(31,   result.getDayOfMonth());
        assertEquals(12,   result.getMonthValue());
        assertEquals(2026, result.getYear());
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

    @Test
    public void toFileFormat_doneTask_showsOneInStatus() {
        LocalDateTime time = LocalDateTime.of(2026, 6, 1, 9, 0);
        Deadline d = new Deadline("Submit form", time);
        d.mark();
        assertTrue(d.toFileFormat().contains("D | 1"),
                "Done deadline must serialise status as 1");
    }

    @Test
    public void toFileFormat_notDoneTask_showsZeroInStatus() {
        LocalDateTime time = LocalDateTime.of(2026, 6, 1, 9, 0);
        Deadline d = new Deadline("Submit form", time);
        assertTrue(d.toFileFormat().contains("D | 0"),
                "Undone deadline must serialise status as 0");
    }

    @Test
    public void deleteDeadline_removesCorrectEntry() {
        CategoryList list = new CategoryList();
        list.addCategory("Test");
        LocalDateTime t = LocalDateTime.of(2026, 3, 1, 10, 0);

        list.addDeadline(0, "First",  t);
        list.addDeadline(0, "Second", t);
        list.deleteDeadline(0, 0); // remove "First"

        assertEquals(1, list.getCategory(0).getDeadlineList().getSize());
        assertEquals("Second", list.getCategory(0).getDeadlineList().get(0).getDescription());
    }

    @Test
    public void setDeadlineStatus_toTrue_marksAsDone() {
        CategoryList list = new CategoryList();
        list.addCategory("Check");
        list.addDeadline(0, "Mark me", LocalDateTime.of(2026, 4, 4, 8, 0));

        list.setDeadlineStatus(0, 0, true);

        assertTrue(list.getCategory(0).getDeadlineList().get(0).getIsDone());
    }

    @Test
    public void setDeadlineStatus_toFalse_unmarksTask() {
        CategoryList list = new CategoryList();
        list.addCategory("Uncheck");
        list.addDeadline(0, "Unmark me", LocalDateTime.of(2026, 4, 4, 8, 0));
        list.setDeadlineStatus(0, 0, true);
        list.setDeadlineStatus(0, 0, false);

        assertFalse(list.getCategory(0).getDeadlineList().get(0).getIsDone());
    }

    @BeforeEach
    public void setUp() {
        // This gives the "0 to 0" error a real range to work with
        seedu.duke.UniTasker.setStartYear(2024);
        seedu.duke.UniTasker.setEndYear(2030);
        seedu.duke.UniTasker.setDailyTaskLimit(8);
    }

}
