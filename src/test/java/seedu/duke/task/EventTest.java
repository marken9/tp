package seedu.duke.task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import seedu.duke.calender.Calendar;
import seedu.duke.tasklist.CategoryList;

import static seedu.duke.UniTasker.handleAdd;
import static seedu.duke.UniTasker.handleDelete;
import static seedu.duke.UniTasker.handleList;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class EventTest {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private final DateTimeFormatter formatList = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Test
    public void addEvent_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");

        LocalDateTime from = LocalDateTime.parse("2026-01-01 1830", formatter);
        LocalDateTime to = LocalDateTime.parse("2026-02-02 1830", formatter);
        categoryList.addEvent(0, "consultation", from,to);

        assertEquals(1, categoryList.getCategory(0).getEventList().getSize());
        assertEquals("consultation", categoryList.getCategory(0).getEventList().get(0).getDescription());
    }

    @Test
    public void addEvent_wrongDateTimeFormat_throwException(){
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        handleAdd("add event 1 interview /from 2026/01/02 1800 /to 2026/01/02 1900".split(" "));
        assertEquals(
                "Error: Use format yyyy-MM-dd HHmm (e.g., 2026-03-11 1830)",
                outContent.toString().trim()
        );
    }

    @Test
    public void deleteEvent_success(){
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");

        LocalDateTime from = LocalDateTime.parse("2026-01-01 1830", formatter);
        LocalDateTime to = LocalDateTime.parse("2026-02-02 1830", formatter);
        categoryList.addEvent(0, "consultation", from,to);
        categoryList.deleteEvent(0, 0);

        assertEquals(0, categoryList.getCategory(0).getEventList().getSize());
    }

    @Test
    public void deleteEvent_invalidIndex_throwException(){
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");

        LocalDateTime from = LocalDateTime.parse("2026-01-01 1830", formatter);
        LocalDateTime to = LocalDateTime.parse("2026-02-02 1830", formatter);
        categoryList.addEvent(0, "consultation", from,to);
        handleDelete("delete event 1 0".split(" "));
        assertEquals(
                "Error: That index does not exist in the list.",
                outContent.toString().trim()
        );
    }

    @Test
    public void listEventRange_success(){
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        LocalDateTime from1 = LocalDateTime.parse("2026-01-01 1830", formatter);
        LocalDateTime to1 = LocalDateTime.parse("2026-02-02 1830", formatter);

        LocalDateTime from2 = LocalDateTime.parse("2026-01-03 1830", formatter);
        LocalDateTime to2 = LocalDateTime.parse("2026-02-03 1930", formatter);

        LocalDateTime from3 = LocalDateTime.parse("2026-03-03 1830", formatter);
        LocalDateTime to3 = LocalDateTime.parse("2026-03-03 1930", formatter);

        Calendar calendar = new Calendar();
        categoryList.addEvent(0, "consultation", from1,to1);
        calendar.registerTask(categoryList.getCategory(0).getLatestEvent());

        categoryList.addEvent(0, "meeting", from2,to2);
        calendar.registerTask(categoryList.getCategory(0).getLatestEvent());

        categoryList.addEvent(0, "interview", from3,to3);
        calendar.registerTask(categoryList.getCategory(0).getLatestEvent());

        calendar.displaySpecificTypeInRange(LocalDate.parse("2026-01-01",formatList),
                LocalDate.parse("2026-02-25",formatList), Event.class);

        assertEquals(
                """
                        --- 2026-01-01 ---
                        [E][ ] consultation (from: 2026-01-01 1830 to: 2026-02-02 1830)
                        --- 2026-01-03 ---
                        [E][ ] meeting (from: 2026-01-03 1830 to: 2026-02-03 1930)
                        """,
                outContent.toString()
        );

    }

    @Test
    public void listEventRange_startDateLargerThanEndDate_throwException(){
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        LocalDateTime from1 = LocalDateTime.parse("2026-01-01 1830", formatter);
        LocalDateTime to1 = LocalDateTime.parse("2026-02-02 1830", formatter);

        LocalDateTime from2 = LocalDateTime.parse("2026-01-03 1830", formatter);
        LocalDateTime to2 = LocalDateTime.parse("2026-02-03 1930", formatter);

        Calendar calendar = new Calendar();
        categoryList.addEvent(0, "consultation", from1,to1);
        calendar.registerTask(categoryList.getCategory(0).getLatestEvent());

        categoryList.addEvent(0, "meeting", from2,to2);
        calendar.registerTask(categoryList.getCategory(0).getLatestEvent());

        handleList("list range 2026-02-25 2026-01-03 /event".split(" "));

        assertEquals("Error: Start date must be earlier than End date "
                                + "(e.g., list range 2026-03-01 2026-03-07)",
                outContent.toString().trim()
        );

    }
    @Test
    public void addRecurringEvent_success(){
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

        categoryList.addRecurringWeeklyEvent(0, "CS2113 lecture",from,to,new Calendar());
        assertEquals(true,categoryList.getLatestEvent(0).getIsRecurring());
    }

    @Test
    public void deleteRecurringEvent_success(){
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

        categoryList.addRecurringWeeklyEvent(0, "CS2113 lecture",from,to,new Calendar());
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

}
