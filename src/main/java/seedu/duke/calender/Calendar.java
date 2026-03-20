package seedu.duke.calender;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;
import java.util.logging.Level;

import seedu.duke.task.Task;
import seedu.duke.task.Timed;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Manages a collection of tasks organized by their specific dates.
 * Uses a TreeMap to store tasks mapped to their respective dates, allowing for
 * efficient range-based queries and sorted displays.
 */

public class Calendar {

    private static final Logger logger = Logger.getLogger(Calendar.class.getName());

    private static final DateTimeFormatter UI_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Internal storage mapping a date to a list of tasks occurring on that day.
     */
    private TreeMap<LocalDate, List<Task>> schedule;

    public Calendar() {
        this.schedule = new TreeMap<>();
    }

    /**
     * Registers a task into the calendar if it contains date information.*
     *
     * @param task The task to be added to the schedule.
     *             Task must implement the {@link Timed} interface to be added to Calendar
     */
    public void registerTask(Task task) {
        assert task != null : "Cannot register a null task";

        if (task instanceof Timed) {
            LocalDateTime dateTime = ((Timed) task).getDate();
            if (dateTime == null) {
                logger.log(Level.WARNING, "Timed task missing date: " + task.getDescription());
                return;
            }
            LocalDate date = dateTime.toLocalDate();
            schedule.computeIfAbsent(date, k -> new ArrayList<>()).add(task);
            logger.log(Level.INFO, "Task registered on date: " + date);
        } else {
            logger.log(Level.WARNING, "Attempted to register non-Timed task to Calendar: "
                    + task.getClass().getSimpleName());
        }
    }

    public void clear() {
        schedule.clear();
        assert schedule.isEmpty() : "Schedule should be empty after clear()";
        logger.info("Calendar schedule cleared.");
    }

    public void displayRange(LocalDate start, LocalDate end) {
        var view = schedule.subMap(start, true, end, true);
        if (view.isEmpty()) {
            System.out.println("No tasks found in this range.");
            return;
        }

        for (var entry : view.entrySet()) {
            System.out.println("--- " + entry.getKey().format(UI_DATE_FORMATTER) + " ---");
            for (Task t : entry.getValue()) {
                System.out.println(t);
            }
        }
    }

    /**
     * Filters and displays tasks of a specific type within a date range.
     * Useful for showing only "Deadlines" or only "Events" in a given week.
     *
     * @param <T>      The specific subclass of {@link Task} to filter for.
     * @param start    The starting date of the range (inclusive).
     * @param end      The ending date of the range (inclusive).
     * @param taskType The class literal of the desired task type (e.g., Deadline.class).
     */
    public <T extends Task> void displaySpecificTypeInRange(LocalDate start, LocalDate end, Class<T> taskType) {
        var view = schedule.subMap(start, true, end, true);
        boolean foundAny = false;

        for (var entry : view.entrySet()) {
            StringBuilder dayOutput = new StringBuilder();
            boolean foundInDay = false;

            for (Task t : entry.getValue()) {
                if (taskType.isInstance(t)) {
                    if (!foundInDay) {
                        System.out.println("--- " + entry.getKey().format(UI_DATE_FORMATTER) + " ---");
                        foundInDay = true;
                    }
                    dayOutput.append(t).append("\n");
                    foundAny = true;
                }
            }
            System.out.print(dayOutput);
        }

        if (!foundAny) {
            System.out.println("No " + taskType.getSimpleName() + "s found in this range.");
        }
    }

    /**
     * Returns the number of tasks on a specific date.
     * NOTE: This method is primarily used for JUnit testing to verify Calendar state.
     */
    public int getTaskCountOnDate(LocalDate date) {
        if (!schedule.containsKey(date)) {
            return 0;
        }
        return schedule.get(date).size();
    }
}
