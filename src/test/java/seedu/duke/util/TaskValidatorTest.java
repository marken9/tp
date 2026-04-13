package seedu.duke.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.duke.exception.DuplicateCategoryException;
import seedu.duke.exception.DuplicateTaskException;
import seedu.duke.exception.HighWorkloadException;
import seedu.duke.exception.OverlapEventException;
import seedu.duke.tasklist.CategoryList;
import seedu.duke.tasklist.EventList;
import seedu.duke.task.Event;

import java.time.LocalDateTime;


public class TaskValidatorTest {

    @BeforeEach
    public void setUp() {
        seedu.duke.UniTasker.setStartYear(2024);
        seedu.duke.UniTasker.setEndYear(2030);
        seedu.duke.UniTasker.setDailyTaskLimit(8);
    }

    @Test
    void validateNoOverlap_emptyList_doesNotThrow() {
        EventList empty = new EventList();
        LocalDateTime start = LocalDateTime.of(2026, 5, 1, 9, 0);
        LocalDateTime end = LocalDateTime.of(2026, 5, 1, 10, 0);
        assertDoesNotThrow(() -> TaskValidator.validateNoOverlap(empty, start, end));
    }

    @Test
    void validateNoOverlap_nonOverlappingEvent_doesNotThrow() {
        EventList list = new EventList();
        LocalDateTime existFrom = LocalDateTime.of(2026, 5, 1, 14, 0);
        LocalDateTime existTo = LocalDateTime.of(2026, 5, 1, 15, 0);
        list.add(new Event("Existing", existFrom, existTo, false, -1));

        // New event ends before the existing one starts
        LocalDateTime newFrom = LocalDateTime.of(2026, 5, 1, 10, 0);
        LocalDateTime newTo = LocalDateTime.of(2026, 5, 1, 11, 0);
        assertDoesNotThrow(() -> TaskValidator.validateNoOverlap(list, newFrom, newTo));
    }

    @Test
    void validateNoOverlap_overlappingEvent_throwsException() {
        EventList list = new EventList();
        LocalDateTime existFrom = LocalDateTime.of(2026, 5, 1, 9, 0);
        LocalDateTime existTo = LocalDateTime.of(2026, 5, 1, 11, 0);
        list.add(new Event("Morning Meeting", existFrom, existTo, false, -1));

        // New event starts during the existing one
        LocalDateTime newFrom = LocalDateTime.of(2026, 5, 1, 10, 0);
        LocalDateTime newTo = LocalDateTime.of(2026, 5, 1, 12, 0);
        assertThrows(OverlapEventException.class, () ->
                TaskValidator.validateNoOverlap(list, newFrom, newTo));
    }

    @Test
    void validateNoOverlap_newEventContainsExisting_throwsException() {
        EventList list = new EventList();
        LocalDateTime existFrom = LocalDateTime.of(2026, 6, 1, 10, 0);
        LocalDateTime existTo = LocalDateTime.of(2026, 6, 1, 11, 0);
        list.add(new Event("Short Meeting", existFrom, existTo, false, -1));

        // New event completely wraps around the existing one
        LocalDateTime newFrom = LocalDateTime.of(2026, 6, 1, 9, 0);
        LocalDateTime newTo = LocalDateTime.of(2026, 6, 1, 12, 0);
        assertThrows(OverlapEventException.class, () ->
                TaskValidator.validateNoOverlap(list, newFrom, newTo));
    }

    @Test
    void validateNoOverlap_adjacentEvents_doesNotThrow() {
        // New event starts exactly when the existing one ends — no overlap
        EventList list = new EventList();
        LocalDateTime existFrom = LocalDateTime.of(2026, 6, 1, 8, 0);
        LocalDateTime existTo = LocalDateTime.of(2026, 6, 1, 9, 0);
        list.add(new Event("Early slot", existFrom, existTo, false, -1));

        LocalDateTime newFrom = LocalDateTime.of(2026, 6, 1, 9, 0);
        LocalDateTime newTo = LocalDateTime.of(2026, 6, 1, 10, 0);
        assertDoesNotThrow(() -> TaskValidator.validateNoOverlap(list, newFrom, newTo));
    }

    @Test
    void validateWorkload_underLimit_doesNotThrow() {
        CategoryList categories = new CategoryList();
        categories.addCategory("Work");
        // One deadline on target date — limit is 8
        categories.addDeadline(0, "Task A", LocalDateTime.of(2026, 7, 1, 10, 0));

        assertDoesNotThrow(() ->
                TaskValidator.validateWorkload(categories, LocalDateTime.of(2026, 7, 1, 11, 0), 8));
    }

    @Test
    void validateWorkload_atLimit_throwsException() {
        CategoryList categories = new CategoryList();
        categories.addCategory("Study");
        LocalDateTime date = LocalDateTime.of(2026, 8, 1, 10, 0);
        // Fill exactly up to the limit of 3
        for (int i = 0; i < 3; i++) {
            categories.addDeadline(0, "Task " + i, date);
        }

        assertThrows(HighWorkloadException.class, () ->
                TaskValidator.validateWorkload(categories, date, 3));
    }

    @Test
    void validateWorkload_emptyList_doesNotThrow() {
        CategoryList categories = new CategoryList();
        categories.addCategory("Empty");
        assertDoesNotThrow(() ->
                TaskValidator.validateWorkload(categories,
                        LocalDateTime.of(2026, 9, 9, 9, 0), 1));
    }

    @Test
    void validateWorkload_countsAcrossMultipleCategories() {
        CategoryList categories = new CategoryList();
        categories.addCategory("A");
        categories.addCategory("B");
        LocalDateTime date = LocalDateTime.of(2026, 10, 10, 10, 0);
        categories.addDeadline(0, "Cat A task", date);
        categories.addDeadline(1, "Cat B task", date);

        // Limit of 2 means 2 tasks IS the limit — should throw
        assertThrows(HighWorkloadException.class, () ->
                TaskValidator.validateWorkload(categories, date, 2));
    }

    @Test
    void validateWorkload_tasksOnDifferentDates_doesNotThrow() {
        CategoryList categories = new CategoryList();
        categories.addCategory("Multi");
        // 5 deadlines but each on a different day
        for (int i = 1; i <= 5; i++) {
            categories.addDeadline(0, "Task " + i, LocalDateTime.of(2026, 11, i, 10, 0));
        }
        // Target date has 0 tasks — limit is 1
        assertDoesNotThrow(() ->
                TaskValidator.validateWorkload(categories,
                        LocalDateTime.of(2026, 11, 20, 10, 0), 1));
    }

    @Test
    void validateUniqueTask_newDescription_doesNotThrow() {
        CategoryList categories = new CategoryList();
        categories.addCategory("Fresh");
        assertDoesNotThrow(() ->
                TaskValidator.validateUniqueTask(categories, 0, "Brand new task"));
    }

    @Test
    void validateUniqueTask_duplicateTodo_throwsException() {
        CategoryList categories = new CategoryList();
        categories.addCategory("Todos");
        categories.addTodo(0, "Buy milk");

        assertThrows(DuplicateTaskException.class, () ->
                TaskValidator.validateUniqueTask(categories, 0, "Buy milk"));
    }

    @Test
    void validateUniqueTask_duplicateDeadline_throwsException() {
        CategoryList categories = new CategoryList();
        categories.addCategory("School");
        categories.addDeadline(0, "Submit essay", LocalDateTime.of(2026, 5, 1, 12, 0));

        assertThrows(DuplicateTaskException.class, () ->
                TaskValidator.validateUniqueTask(categories, 0, "Submit essay"));
    }

    @Test
    void validateUniqueTask_duplicateEvent_throwsException() {
        CategoryList categories = new CategoryList();
        categories.addCategory("Work");
        LocalDateTime from = LocalDateTime.of(2026, 6, 1, 9, 0);
        LocalDateTime to = LocalDateTime.of(2026, 6, 1, 10, 0);
        categories.addEvent(0, "Sprint Review", from, to);

        assertThrows(DuplicateTaskException.class, () ->
                TaskValidator.validateUniqueTask(categories, 0, "Sprint Review"));
    }

    @Test
    void validateUniqueTask_leadingTrailingSpaces_treatedAsDuplicate() {
        CategoryList categories = new CategoryList();
        categories.addCategory("Trim");
        categories.addTodo(0, "Buy milk");

        // Description with surrounding spaces should still match after trim
        assertThrows(DuplicateTaskException.class, () ->
                TaskValidator.validateUniqueTask(categories, 0, "  Buy milk  "));
    }

    @Test
    void validateUniqueCategory_newName_doesNotThrow() {
        CategoryList categories = new CategoryList();
        assertDoesNotThrow(() ->
                TaskValidator.validateUniqueCategory(categories, "Hobbies"));
    }

    @Test
    void validateUniqueCategory_duplicateName_throwsException() {
        CategoryList categories = new CategoryList();
        categories.addCategory("Work");

        assertThrows(DuplicateCategoryException.class, () ->
                TaskValidator.validateUniqueCategory(categories, "Work"));
    }

    @Test
    void validateUniqueCategory_caseInsensitiveDuplicate_throwsException() {
        CategoryList categories = new CategoryList();
        categories.addCategory("Work");

        // "WORK" should still be seen as a duplicate regardless of casing
        assertThrows(DuplicateCategoryException.class, () ->
                TaskValidator.validateUniqueCategory(categories, "WORK"));
    }

    @Test
    void validateUniqueCategory_emptyList_doesNotThrow() {
        CategoryList categories = new CategoryList();
        assertDoesNotThrow(() ->
                TaskValidator.validateUniqueCategory(categories, "Anything"));
    }
}
