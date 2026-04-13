package seedu.duke.calender;

import org.junit.jupiter.api.Test;

import seedu.duke.task.Deadline;
import seedu.duke.task.Event;
import seedu.duke.task.Todo;
import seedu.duke.tasklist.CategoryList;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalendarTest {

    @Test
    public void registerTask_deadlineAdded_success() {
        Calendar calendar = new Calendar();
        LocalDate targetDate = LocalDate.of(2026, 5, 15);
        LocalDateTime time = LocalDateTime.of(2026, 5, 15, 18, 30);
        Deadline deadline = new Deadline("Finish JUnit tests", time);

        calendar.registerTask(deadline);

        assertEquals(1, calendar.getTaskCountOnDate(targetDate));
    }

    @Test
    public void registerTask_nonTimedTask_notAddedToCalendar() {
        Calendar calendar = new Calendar();
        LocalDate today = LocalDate.now();
        Todo todo = new Todo("Buy milk");

        calendar.registerTask(todo);

        assertEquals(0, calendar.getTaskCountOnDate(today));
    }

    @Test
    public void registerTask_eventAdded_success() {
        Calendar calendar = new Calendar();
        LocalDate targetDate = LocalDate.of(2026, 7, 4);
        LocalDateTime from = targetDate.atTime(9, 0);
        LocalDateTime to = targetDate.atTime(10, 0);

        calendar.registerTask(new Event("Sprint review", from, to, false, -1));

        assertEquals(1, calendar.getTaskCountOnDate(targetDate));
    }

    @Test
    public void registerTask_taskOnBoundaryDates_countedCorrectly() {
        // Ensures tasks added exactly on the start and end of a range are both visible.
        Calendar calendar = new Calendar();
        LocalDate start = LocalDate.of(2026, 5, 1);
        LocalDate end = LocalDate.of(2026, 5, 31);

        calendar.registerTask(new Deadline("Start boundary", start.atTime(0, 0)));
        calendar.registerTask(new Deadline("End boundary", end.atTime(23, 59)));

        assertEquals(1, calendar.getTaskCountOnDate(start));
        assertEquals(1, calendar.getTaskCountOnDate(end));
    }

    @Test
    public void registerTask_multipleTypeSameDate_allCounted() {
        Calendar calendar = new Calendar();
        LocalDate date = LocalDate.of(2026, 5, 10);
        LocalDateTime dt = date.atTime(12, 0);

        calendar.registerTask(new Deadline("Deadline here", dt));
        calendar.registerTask(new Event("Event here", dt, dt.plusHours(1), false,
                -1));

        assertEquals(2, calendar.getTaskCountOnDate(date));
    }

    @Test
    public void clear_emptyCalendar_doesNotThrow() {
        Calendar calendar = new Calendar();
        calendar.clear();
        assertEquals(0, calendar.getTaskCountOnDate(LocalDate.now()));
    }

    @Test
    public void clear_calendarWiped_success() {
        Calendar calendar = new Calendar();
        LocalDate targetDate = LocalDate.now();
        calendar.registerTask(new Deadline("Task", LocalDateTime.now()));

        assertEquals(1, calendar.getTaskCountOnDate(targetDate));

        calendar.clear();

        assertEquals(0, calendar.getTaskCountOnDate(targetDate));
    }

    @Test
    public void refreshCalendar_includesEvents_success() {
        Calendar calendar = new Calendar();
        CategoryList categories = new CategoryList();
        categories.addCategory("Work");

        LocalDateTime from = LocalDateTime.of(2026, 6, 1, 9, 0);
        LocalDateTime to = LocalDateTime.of(2026, 6, 1, 10, 0);
        categories.addEvent(0, "Meeting", from, to);

        CategoryList.refreshCalendar(categories, calendar);
        assertEquals(1, calendar.getTaskCountOnDate(LocalDate.of(2026, 6, 1)));
    }

    @Test
    public void refreshCalendar_afterAddingDeadlines_reflectsNewState() {
        Calendar calendar = new Calendar();
        CategoryList categories = new CategoryList();
        categories.addCategory("Study");

        categories.addDeadline(0, "Essay", LocalDateTime.of(2026, 11, 1, 18, 0));
        CategoryList.refreshCalendar(categories, calendar);

        assertEquals(1, calendar.getTaskCountOnDate(LocalDate.of(2026, 11, 1)));
    }

    @Test
    public void refreshCalendar_calledTwice_doesNotDuplicate() {
        Calendar calendar = new Calendar();
        CategoryList categories = new CategoryList();
        categories.addCategory("Work");
        categories.addDeadline(0, "Report", LocalDateTime.of(2026, 12, 5, 9, 0));

        // Calling refresh twice must NOT double the count.
        CategoryList.refreshCalendar(categories, calendar);
        CategoryList.refreshCalendar(categories, calendar);

        assertEquals(1, calendar.getTaskCountOnDate(LocalDate.of(2026, 12, 5)));
    }

    @Test
    public void clear_multipleEntriesThenClear_allGone() {
        Calendar calendar = new Calendar();
        calendar.registerTask(new Deadline("T1",
                LocalDate.of(2026, 5, 1).atTime(9, 0)));
        calendar.registerTask(new Deadline("T2",
                LocalDate.of(2026, 11, 6).atTime(9, 0)));

        calendar.clear();

        assertEquals(0, calendar.getTaskCountOnDate(LocalDate.of(2026, 5, 1)));
        assertEquals(0, calendar.getTaskCountOnDate(LocalDate.of(2026, 11, 6)));
    }

    @Test
    public void getTaskCountOnDate_dateWithNoTasks_returnsZero() {
        Calendar calendar = new Calendar();
        LocalDate emptyDate = LocalDate.of(2026, 12, 25);
        assertEquals(0, calendar.getTaskCountOnDate(emptyDate));
    }

    @Test
    public void registerTask_multipleTasksSameDate_incrementsCount() {
        Calendar calendar = new Calendar();
        LocalDate targetDate = LocalDate.of(2026, 5, 20);
        calendar.registerTask(new Deadline("Task 1", targetDate.atTime(10, 0)));
        calendar.registerTask(new Deadline("Task 2", targetDate.atTime(14, 0)));

        assertEquals(2, calendar.getTaskCountOnDate(targetDate));
    }

    @Test
    public void displayRange_tasksOutsideRange_notDisplayed() {
        Calendar calendar = new Calendar();
        LocalDate start = LocalDate.of(2026, 6, 1);
        LocalDate end = LocalDate.of(2027, 1, 10);

        // Task exactly on the boundary (Should display)
        calendar.registerTask(new Deadline("Inside", LocalDateTime.of(2027, 1, 5,
                12, 0)));
        // Task before range (Should NOT display)
        calendar.registerTask(new Deadline("Before", LocalDateTime.of(2026, 5, 31,
                23, 59)));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        calendar.displayRange(start, end);

        String output = outContent.toString();
        assert (output.contains("Inside"));
        assert (!output.contains("Before"));

        System.setOut(System.out); // Reset console output
    }

    @Test
    public void displayRange_emptyCalendar_printsNoTasksMessage() {
        Calendar calendar = new Calendar();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        calendar.displayRange(LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 12, 31));

        System.setOut(System.out);
        assert out.toString().contains("No tasks found");
    }

    @Test
    public void displayRange_startEqualsEnd_showsTaskOnThatDay() {
        Calendar calendar = new Calendar();
        LocalDate single = LocalDate.of(2026, 8, 8);
        calendar.registerTask(new Deadline("Solo task", single.atTime(10, 0)));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        calendar.displayRange(single, single);

        System.setOut(System.out);
        assert out.toString().contains("Solo task");
    }

    @Test
    public void displayRange_taskAfterEnd_notDisplayed() {
        Calendar calendar = new Calendar();
        LocalDate end = LocalDate.of(2026, 5, 31);
        LocalDate late = LocalDate.of(2026, 6, 1); // one day past the end

        calendar.registerTask(new Deadline("Outside", late.atTime(8, 0)));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        calendar.displayRange(LocalDate.of(2026, 5, 1), end);

        System.setOut(System.out);
        assert !out.toString().contains("Outside");
    }

    @Test
    public void displaySpecificTypeInRange_filtersCorrectly() {
        Calendar calendar = new Calendar();
        LocalDate date = LocalDate.of(2026, 5, 2);
        LocalDateTime dateTime = date.atTime(12, 0);

        calendar.registerTask(new Deadline("I am a Deadline", dateTime));
        calendar.registerTask(new Event("I am an Event", dateTime, dateTime.plusHours(1), false,
                -1));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Ask specifically for Deadlines
        calendar.displaySpecificTypeInRange(date, date, Deadline.class);

        String output = outContent.toString();
        assert (output.contains("I am a Deadline"));
        assert (!output.contains("I am an Event"));

        System.setOut(System.out);
    }

    @Test
    public void displaySpecificTypeInRange_noMatchingType_printsNotFound() {
        Calendar calendar = new Calendar();
        LocalDate date = LocalDate.of(2026, 9, 9);
        calendar.registerTask(new Event("Only event", date.atTime(9, 0), date.atTime(10, 0), false, -1));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        calendar.displaySpecificTypeInRange(date, date, Deadline.class);

        System.setOut(System.out);
        assert out.toString().contains("No Deadline");
    }

    @Test
    public void displaySpecificTypeInRange_onlyEventsRequested_deadlinesHidden() {
        Calendar calendar = new Calendar();
        LocalDate date = LocalDate.of(2026, 10, 1);
        LocalDateTime dt = date.atTime(11, 0);

        calendar.registerTask(new Deadline("Hidden deadline", dt));
        calendar.registerTask(new Event("Visible event", dt, dt.plusHours(1), false, -1));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        calendar.displaySpecificTypeInRange(date, date, Event.class);

        System.setOut(System.out);
        String output = out.toString();
        assert output.contains("Visible event");
        assert !output.contains("Hidden deadline");
    }
}
