package seedu.duke.calender;

import org.junit.jupiter.api.Test;

import seedu.duke.task.Deadline;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalendarTest {

    @Test
    public void registerTask_deadlineAdded_success() {
        Calendar calendar = new Calendar();
        LocalDate targetDate = LocalDate.of(2026, 3, 15);
        LocalDateTime time = LocalDateTime.of(2026, 3, 15, 18, 30);
        Deadline deadline = new Deadline("Finish JUnit tests", time);

        calendar.registerTask(deadline);

        assertEquals(1, calendar.getTaskCountOnDate(targetDate));
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
}
