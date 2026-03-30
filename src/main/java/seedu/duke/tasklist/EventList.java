package seedu.duke.tasklist;

import seedu.duke.calender.Calendar;
import seedu.duke.task.Event;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class EventList extends TaskList<Event> {

    public EventList() {
        super();
    }

    public String toString() {
        String result = "";
        Set<Integer> printedGroups = new HashSet<>();
        for (int i = 0; i < tasks.size(); i++) {
            Event event = tasks.get(i);
            assert event != null : "Event must exist";
            if (event.getIsRecurring() && !printedGroups.contains(event.getRecurringGroupId())) {
                result = result + (i+1) + ". " + (event.toStringRecurring()) + System.lineSeparator();
                printedGroups.add(event.getRecurringGroupId());
            } else if (!event.getIsRecurring()) {
                result = result + (i+1) + ". " + (event.toString()) + System.lineSeparator();

            }
        }
        return result;
    }

    public void clearAll() {
        tasks.clear();
    }

    public void sortByDate() {
        tasks.sort(Comparator
                .comparing(Event::getFrom)
                .thenComparing(Event::getTo));
    }

    public Event getLatest() {
        if (tasks.isEmpty()) {
            return null;
        }
        return tasks.get(tasks.size() - 1);
    }

    public void addRecurringWeeklyEvent(Event event, Calendar calendar, LocalDateTime date,int months) {
        assert (calendar != null): "There must be an instance of calendar";
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

        while (currentFromDateTime.isBefore(boundaryDateTime)){
            Event newEvent = new Event(eventDescription,currentFromDateTime,
                    currentToDateTime,true,recurringGroupId);
            tasks.add(newEvent);
            calendar.registerTask(newEvent);
            currentFromDateTime = currentFromDateTime.plusDays(7);
            currentToDateTime = currentToDateTime.plusDays(7);

        }
    }

    public void sortByDay() {
        Comparator<Event> dayOfWeek = Comparator.comparingInt(d -> d.getFrom().getDayOfWeek().getValue());
        tasks.sort(dayOfWeek);
    }
}
