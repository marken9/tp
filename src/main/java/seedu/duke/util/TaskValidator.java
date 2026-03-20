package seedu.duke.util;

import seedu.duke.exception.DuplicateCategoryException;
import seedu.duke.exception.DuplicateTaskException;
import seedu.duke.exception.HighWorkloadException;
import seedu.duke.exception.OverlapEventException;
import seedu.duke.task.Event;
import seedu.duke.tasklist.Category;
import seedu.duke.tasklist.CategoryList;
import seedu.duke.tasklist.EventList;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TaskValidator {

    /**
     * Checks if a proposed time range overlaps with any existing events in a list.
     */
    public static void validateNoOverlap(EventList existingEvents, LocalDateTime start, LocalDateTime end)
            throws OverlapEventException {

        for (int i = 0; i < existingEvents.getSize(); i++) {
            Event existing = existingEvents.get(i);

            boolean isOverlapping = start.isBefore(existing.getTo()) && end.isAfter(existing.getFrom());

            if (isOverlapping) {
                throw new OverlapEventException("Conflict: Overlaps with '" + existing.getDescription() +
                        "' [" + existing.getFrom().toLocalTime() + " - " + existing.getTo().toLocalTime() + "]");
            }
        }
    }

    public static void validateWorkload(CategoryList categories, LocalDateTime dateTime, int maxTasks)
            throws HighWorkloadException {

        int totalTimedTasks = 0;
        LocalDate targetDate = dateTime.toLocalDate();

        for (int i = 0; i < categories.getAmount(); i++) {
            Category cat = categories.getCategory(i);

            // Count Deadlines on this date
            for (int j = 0; j < cat.getDeadlineList().getSize(); j++) {
                if (cat.getDeadlineList().get(j).getBy().toLocalDate().equals(targetDate)) {
                    totalTimedTasks++;
                }
            }

            // Count Events on this date
            for (int j = 0; j < cat.getEventList().getSize(); j++) {
                if (cat.getEventList().get(j).getFrom().toLocalDate().equals(targetDate)) {
                    totalTimedTasks++;
                }
            }
        }
        if (totalTimedTasks >= maxTasks ) {
            throw new HighWorkloadException("High Workload: You already have " + totalTimedTasks +
                    " scheduled tasks on " + dateTime.toLocalDate() + ". Take a break!");
        }
    }

    public static void validateUniqueTask(CategoryList categories, int catIdx, String description)
            throws DuplicateTaskException {

        String cleanDesc = description.trim();
        Category targetCat = categories.getCategory(catIdx);

        boolean existsAsTodo = targetCat.getTodoList().contains(cleanDesc);
        boolean existsAsDeadline = targetCat.getDeadlineList().contains(cleanDesc);
        boolean existsAsEvent = targetCat.getEventList().contains(cleanDesc);

        if (existsAsTodo || existsAsDeadline || existsAsEvent) {
            throw new DuplicateTaskException("Duplicate Error: Task '" + cleanDesc
                    + "' already exists in this category.");
        }
    }

    public static void validateUniqueCategory(CategoryList categories, String name)
            throws DuplicateCategoryException {
        for (int i = 0; i < categories.getAmount(); i++) {
            if (categories.getCategory(i).getName().equalsIgnoreCase(name)) {
                throw new DuplicateCategoryException("Category '" + name + "' already exists!");
            }
        }
    }

}
