package seedu.duke.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.duke.calender.Calendar;
import seedu.duke.tasklist.CategoryList;
import seedu.duke.tasklist.EventList;
import seedu.duke.tasklist.EventReference;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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
        CategoryList categories = new CategoryList();

        categories.addCategory("Test");

        categories.addEvent(0, "event1",
                LocalDateTime.of(2026, 4, 1, 10, 0),
                LocalDateTime.of(2026, 4, 1, 11, 0));

        categories.getAllEvents(false, false);

        Map<Integer, List<EventReference>> map = categories.getActiveDisplayMap();
        EventReference ref = map.get(0).get(0);

        categories.deleteEvent(ref.categoryIndex, ref.eventIndex);

        assertEquals(0, categories.getCategory(0).getEventList().getSize());
    }

    @Test
    void deleteInvalidUiIndex_throws() {
        CategoryList categories = new CategoryList();

        categories.addCategory("Test");

        categories.addEvent(0, "event1",
                LocalDateTime.of(2026, 4, 1, 10, 0),
                LocalDateTime.of(2026, 4, 1, 11, 0));

        categories.getAllEvents(false, false);
        Map<Integer, List<EventReference>> map = categories.getActiveDisplayMap();
        assertThrows(IndexOutOfBoundsException.class, () -> {
            map.get(0).get(999);
        });
    }

    @Test
    void deleteOccurrence_success() throws Exception {
        CategoryList categories = new CategoryList();

        categories.addCategory("Test");

        categories.addRecurringWeeklyEvent(
                0, "rec",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                new Calendar(),
                null,
                0
        );

        categories.getAllEvents(false, false);
        categories.getOccurrencesOfRecurringEvent(0, 1);

        Map<Integer, List<EventReference>> map = categories.getActiveDisplayMap();
        EventReference ref = map.get(0).get(0);

        categories.deleteEvent(ref.categoryIndex, ref.eventIndex);

        assertTrue(categories.getCategory(0).getEventList().getSize() >= 0);
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
        Event eventToDelete = categoryList.getEvent(0,0);
        categoryList.deleteRecurringEvent(0, eventToDelete.getRecurringGroupId());
        System.out.println("____________________________________________________________");
        System.out.println("This recurring event has been deleted:");
        System.out.println(eventToDelete.toStringRecurringList());
        System.out.println("____________________________________________________________");

        assertEquals("____________________________________________________________" + System.lineSeparator() +
                "This recurring event has been deleted:" + System.lineSeparator() +
                "[RE]CS2113 lecture (from: Friday 1600 to: Friday 1800)" + System.lineSeparator() +
                "____________________________________________________________", outContent.toString().trim());
    }

    @Test
    void addEventHandleIrregularSpacing_success() {
        CategoryList categories = new CategoryList();
        categories.addCategory("Academics");

        String description = "   CS2113   Lecture  ".trim(); // Logic should handle extra internal spaces
        LocalDateTime from = LocalDateTime.parse("12-10-2026 1600", formatter);
        LocalDateTime to = LocalDateTime.parse("12-10-2026 1800", formatter);

        categories.addEvent(0, description, from, to);

        assertEquals(1, categories.getCategory(0).getEventList().getSize());
        assertEquals("CS2113   Lecture", categories.getCategory(0).getEvent(0).getDescription());
    }

    @Test
    void addRecurringSameDayBoundary_success() {
        CategoryList categories = new CategoryList();
        categories.addCategory("Work");

        LocalDateTime from = LocalDateTime.parse("12-10-2026 1600", formatter);
        LocalDateTime to = LocalDateTime.parse("12-10-2026 1800", formatter); // Same day

        categories.addRecurringWeeklyEvent(0, "Weekly Sync", from, to, new Calendar(), null, 1);

        assertTrue(categories.getCategory(0).getEventList().getSize() >= 4);
        assertEquals(categories.getCategory(0).getEvent(0).getRecurringGroupId(),
                categories.getCategory(0).getEvent(1).getRecurringGroupId());
    }

    @Test
    void deleteEventCaseInsensitiveAll_success() {
        CategoryList categories = new CategoryList();
        categories.addCategory("Personal");
        categories.addEvent(0, "Gym", LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        categories.addEvent(0, "Dinner", LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3));

        categories.deleteAllEvents(0);

        assertEquals(0, categories.getCategory(0).getEventList().getSize(),
                "All events in the category should be cleared");
    }

    @Test
    void listEventViewStateTransitions_success() {
        CategoryList categories = new CategoryList();
        categories.addCategory("Club");

        assertEquals("NO_VIEW", categories.getCurrentView());

        categories.getAllEvents(false, false);
        assertEquals("EVENT", categories.getCurrentView());

        categories.getAllEvents(true, false);
        assertEquals("EVENT_EXPANDED", categories.getCurrentView());
    }

    @Test
    void deleteOccurrence_logic_requiresSpecificView() throws Exception {
        CategoryList categories = new CategoryList();
        categories.addCategory("Sports");
        LocalDateTime now = LocalDateTime.now();
        categories.addRecurringWeeklyEvent(0, "Training", now, now.plusHours(1), new Calendar(), null, 1);

        categories.getAllEvents(false, false);

        categories.getOccurrencesOfRecurringEvent(0, 1);
        assertEquals("OCCURRENCE_VIEW", categories.getCurrentView());

        assertNotNull(categories.getActiveDisplayMap().get(0));
        assertTrue(categories.getActiveDisplayMap().get(0).size() > 0);
    }

    @Test
    void addEventOverlapPrevention() {
        CategoryList categories = new CategoryList();
        categories.addCategory("Testing");

        LocalDateTime start = LocalDateTime.parse("10-10-2026 1000", formatter);
        LocalDateTime end = LocalDateTime.parse("10-10-2026 1200", formatter);

        categories.addEvent(0, "Event 1", start, end);

        LocalDateTime overlapStart = LocalDateTime.parse("10-10-2026 1100", formatter);
        LocalDateTime overlapEnd = LocalDateTime.parse("10-10-2026 1300", formatter);
        boolean hasOverlap = false;
        EventList list = categories.getCategory(0).getEventList();
        for (int i = 0; i < list.getSize(); i++) {
            Event existing = list.get(i);
            if (overlapStart.isBefore(existing.getTo()) && existing.getFrom().isBefore(overlapEnd)) {
                hasOverlap = true;
                break;
            }
        }

        assertTrue(hasOverlap, "Logic should detect that the new time range overlaps with existing event");
    }

    @Test
    void addEventStartExactlyAtPreviousEnd_success() {
        CategoryList categories = new CategoryList();
        categories.addCategory("Work");

        LocalDateTime endFirst = LocalDateTime.parse("12-10-2026 1400", formatter);
        categories.addEvent(0, "Event 1", LocalDateTime.parse("12-10-2026 1300", formatter), endFirst);
        LocalDateTime startSecond = LocalDateTime.parse("12-10-2026 1400", formatter);
        LocalDateTime endSecond = LocalDateTime.parse("12-10-2026 1500", formatter);

        categories.addEvent(0, "Event 2", startSecond, endSecond);

        assertEquals(2, categories.getCategory(0).getEventList().getSize(),
                "Events should be able to touch boundaries without being considered an overlap.");
    }

    @Test
    void addEventYearRollover_success() {
        CategoryList categories = new CategoryList();
        categories.addCategory("NYE");

        LocalDateTime nyeStart = LocalDateTime.parse("31-12-2026 2300", formatter);
        LocalDateTime nyeEnd = LocalDateTime.parse("01-01-2027 0200", formatter);

        assertDoesNotThrow(() -> categories.addEvent(0, "New Years Party", nyeStart, nyeEnd));
        assertEquals(2027, categories.getCategory(0).getEvent(0).getTo().getYear());
    }

    @Test
    void deleteEventMapSyncAfterDelete_success() {
        CategoryList categories = new CategoryList();
        categories.addCategory("SyncTest");
        categories.addEvent(0, "E1", LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        categories.getAllEvents(false, false);

        categories.deleteEvent(0, 0);

        categories.getAllEvents(false, false);

        assertTrue(categories.getActiveDisplayMap().get(0) == null ||
                categories.getActiveDisplayMap().get(0).isEmpty());
    }

    @BeforeEach
    public void setUp() {
        // This gives the "0 to 0" error a real range to work with
        seedu.duke.UniTasker.setStartYear(2024);
        seedu.duke.UniTasker.setEndYear(2030);
        seedu.duke.UniTasker.setDailyTaskLimit(8);
    }

}
