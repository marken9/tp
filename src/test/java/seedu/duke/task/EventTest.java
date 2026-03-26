package seedu.duke.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


import seedu.duke.calender.Calendar;
import seedu.duke.tasklist.CategoryList;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class EventTest {

    private static final String SEP = "____________________________________________________________";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HHmm");
    private final DateTimeFormatter formatList = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Test
    public void addEvent_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");

        LocalDateTime from = LocalDateTime.parse("01-01-2026 1830", formatter);
        LocalDateTime to = LocalDateTime.parse("02-02-2026 1830", formatter);
        categoryList.addEvent(0, "consultation", from, to);

        assertEquals(1, categoryList.getCategory(0).getEventList().getSize());
        assertEquals("consultation", categoryList.getCategory(0).getEventList().get(0).getDescription());
    }


    @Test
    public void deleteEvent_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");

        LocalDateTime from = LocalDateTime.parse("01-01-2026 1830", formatter);
        LocalDateTime to = LocalDateTime.parse("02-02-2026 1830", formatter);
        categoryList.addEvent(0, "consultation", from, to);
        categoryList.deleteEvent(0, 0);

        assertEquals(0, categoryList.getCategory(0).getEventList().getSize());
    }


    @Test
    public void listEventRange_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        LocalDateTime from1 = LocalDateTime.parse("01-11-2026 1830", formatter);
        LocalDateTime to1 = LocalDateTime.parse("02-12-2026 1830", formatter);

        LocalDateTime from2 = LocalDateTime.parse("03-12-2026 1830", formatter);
        LocalDateTime to2 = LocalDateTime.parse("03-12-2026 1930", formatter);

        LocalDateTime from3 = LocalDateTime.parse("26-12-2026 1830", formatter);
        LocalDateTime to3 = LocalDateTime.parse("26-12-2026 1930", formatter);

        Calendar calendar = new Calendar();
        categoryList.addEvent(0, "consultation", from1, to1);
        calendar.registerTask(categoryList.getCategory(0).getLatestEvent());

        categoryList.addEvent(0, "meeting", from2, to2);
        calendar.registerTask(categoryList.getCategory(0).getLatestEvent());

        categoryList.addEvent(0, "interview", from3, to3);
        calendar.registerTask(categoryList.getCategory(0).getLatestEvent());

        calendar.displaySpecificTypeInRange(LocalDate.parse("01-11-2026", formatList),
                LocalDate.parse("25-12-2026", formatList), Event.class);

        String expected = "--- 01-11-2026 ---\n" +
                "[E][ ] consultation (from: 01-11-2026 1830 to: 02-12-2026 1830)\n" +
                "--- 03-12-2026 ---\n" +
                "[E][ ] meeting (from: 03-12-2026 1830 to: 03-12-2026 1930)\n";

        String actual = outContent.toString().replace("\r\n", "\n").replace("\r", "\n");

        assertEquals(expected, actual);

    }

    @Test
    public void addRecurringEvent_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");

        String fromDayOfWeek = "Friday 1600".split(" ")[0];
        String fromTime = "Friday 1600".split(" ")[1];

        String toDayOfWeek = "Friday 1800".split(" ")[0];
        String toTime = "Friday 1800".split(" ")[1];
        LocalDate today = LocalDate.now();

        LocalDate dateFrom = today.with(TemporalAdjusters.nextOrSame(
                DayOfWeek.valueOf(fromDayOfWeek.toUpperCase())));
        LocalDateTime from = LocalDateTime.of(dateFrom,
                LocalTime.parse(fromTime, DateTimeFormatter.ofPattern("HHmm")));

        LocalDate dateTo = today.with(TemporalAdjusters.nextOrSame(
                DayOfWeek.valueOf(toDayOfWeek.toUpperCase())));
        LocalDateTime to = LocalDateTime.of(dateTo, LocalTime.parse(
                toTime, DateTimeFormatter.ofPattern("HHmm")));

        categoryList.addRecurringWeeklyEvent(0, "CS2113 lecture", from, to,
                new Calendar(), null, 0);
        assertEquals(true, categoryList.getLatestEvent(0).getIsRecurring());
    }

    @Test
    public void deleteRecurringEvent_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");

        String fromDayOfWeek = "Friday 1600".split(" ")[0];
        String fromTime = "Friday 1600".split(" ")[1];

        String toDayOfWeek = "Friday 1800".split(" ")[0];
        String toTime = "Friday 1800".split(" ")[1];
        LocalDate today = LocalDate.now();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        LocalDate dateFrom = today.with(TemporalAdjusters.nextOrSame(
                DayOfWeek.valueOf(fromDayOfWeek.toUpperCase())));
        LocalDateTime from = LocalDateTime.of(dateFrom,
                LocalTime.parse(fromTime, DateTimeFormatter.ofPattern("HHmm")));

        LocalDate dateTo = today.with(TemporalAdjusters.nextOrSame(
                DayOfWeek.valueOf(toDayOfWeek.toUpperCase())));
        LocalDateTime to = LocalDateTime.of(dateTo, LocalTime.parse(
                toTime, DateTimeFormatter.ofPattern("HHmm")));

        categoryList.addRecurringWeeklyEvent(0, "CS2113 lecture", from, to,
                new Calendar(), null, 0);
        int groupIndex = 1;
        Event eventToDelete = categoryList.findRecurringEventToDelete(0, groupIndex);
        categoryList.deleteRecurringEvent(0, groupIndex);
        System.out.println("____________________________________________________________");
        System.out.println("This recurring event has been deleted:");
        System.out.println(eventToDelete.toStringRecurringList());
        System.out.println("____________________________________________________________");

        assertEquals("____________________________________________________________" + System.lineSeparator() +
                "This recurring event has been deleted:" + System.lineSeparator() +
                "[RE][Group 1]CS2113 lecture (from: Friday 1600 to: Friday 1800)" + System.lineSeparator() +
                "____________________________________________________________", outContent.toString().trim());
    }

    @BeforeEach
    public void setUp() {
        // This gives the "0 to 0" error a real range to work with
        seedu.duke.UniTasker.setStartYear(2024);
        seedu.duke.UniTasker.setEndYear(2030);
        seedu.duke.UniTasker.setDailyTaskLimit(8);
    }

}
