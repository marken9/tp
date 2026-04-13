package seedu.duke.tasklist;

import seedu.duke.calender.Calendar;
import seedu.duke.task.Event;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Manages a list of {@link Event} tasks.
 * Provides specialised functionality for sorting events by their start date and time or day depending on the list type,
 * adding recurring weekly events based on the boundary provided and getting the last added event
 */
public class EventList extends TaskList<Event> {

    public EventList() {
        super();
    }

    public String toString(boolean viewType) {
        String result = "";
        Set<Integer> printedGroups = new HashSet<>();
        int uiIndex = 0;
        for (int i = 0; i < tasks.size(); i++) {
            Event event = tasks.get(i);
            assert event != null : "Event must exist";
            if (event.getIsRecurring() && !printedGroups.contains(event.getRecurringGroupId())) {
                result = result + (viewType? uiIndex+1: i+1) + ". " + (event.toStringRecurring()) + System.lineSeparator();
                printedGroups.add(event.getRecurringGroupId());
                uiIndex++;
            } else if (!event.getIsRecurring()) {
                result = result + (viewType? uiIndex+1 : i+1) + ". " + (event.toString()) + System.lineSeparator();
                uiIndex++;
            }
        }
        return result;
    }

    /**
     * Sorts events based on type and start time. If it is recurring it sorts by the day and then the start time.
     * If it is non-recurring it sorts by start time.
     */
    public void sortByDate() {
        List<Event> normal = tasks.stream()
                .filter(e -> !e.getIsRecurring())
                .sorted(Comparator.comparing(Event::getFrom).thenComparing(Event::getTo))
                .toList();

        List<Event> recurring = tasks.stream()
                .filter(Event::getIsRecurring)
                .sorted(Comparator
                        .comparingInt((Event e) -> e.getFrom().getDayOfWeek().getValue())
                        .thenComparing(e -> e.getFrom().toLocalTime()))
                .toList();

        tasks.clear();
        tasks.addAll(normal);
        tasks.addAll(recurring);
    }

    public Event getLatest() {
        if (tasks.isEmpty()) {
            return null;
        }
        return tasks.get(tasks.size() - 1);
    }

    /**
     * Adds recurring weekly events (batching adding of events) based on user specified end duration.
     *
     * @param event    The Event object.
     * @param calendar The Calendar object to save the event.
     * @param date     The end date to stop adding recurring events within that group.
     * @param months   The number of months to add weekly recurring events
     */
    public void addRecurringWeeklyEvent(Event event, Calendar calendar, LocalDateTime date, int months) {
        assert (calendar != null) : "There must be an instance of calendar";
        assert (event != null) : "Event must exist";
        LocalDateTime boundaryDateTime = null;
        if (date == null && months == 0) {
            boundaryDateTime = event.getFrom().plusMonths(1);
        } else if (months == 0) {
            boundaryDateTime = date;
        } else {
            boundaryDateTime = event.getFrom().plusMonths(months);
        }
        LocalDateTime currentFromDateTime = event.getFrom();
        LocalDateTime currentToDateTime = event.getTo();
        int recurringGroupId = event.getRecurringGroupId();
        String eventDescription = event.getDescription();

        while (currentFromDateTime.isBefore(boundaryDateTime)) {
            if (!(currentFromDateTime.isBefore(LocalDateTime.now()))) {
                Event newEvent = new Event(eventDescription, currentFromDateTime,
                        currentToDateTime, true, recurringGroupId);
                tasks.add(newEvent);
                calendar.registerTask(newEvent);
            }
                currentFromDateTime = currentFromDateTime.plusDays(7);
                currentToDateTime = currentToDateTime.plusDays(7);

        }
    }

    /**
     * Sorts recurring events based on the day of the week and then its start time.
     */
    public void sortByDay() {
        Comparator<Event> dayOfWeek = Comparator
                .comparingInt((Event e) -> e.getFrom().getDayOfWeek().getValue())
                .thenComparing(e -> e.getFrom().toLocalTime());
        tasks.sort(dayOfWeek);

    }
}
