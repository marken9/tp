package seedu.duke.tasklist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.duke.task.Deadline;

import java.time.LocalDateTime;

public class DeadlineListTest {

    @Test
    public void addAndGetLatest_success() {
        DeadlineList list = new DeadlineList();
        LocalDateTime time = LocalDateTime.of(2026, 3, 15, 10, 0);

        list.add(new Deadline("Task 1", time));
        assertEquals("Task 1", list.getLatest().getDescription());

        list.add(new Deadline("Task 2", time));
        assertEquals("Task 2", list.getLatest().getDescription());
    }

    @Test
    public void delete_updatesSize_success() {
        DeadlineList list = new DeadlineList();
        list.add(new Deadline("Task", LocalDateTime.now()));
        assertEquals(1, list.getSize());

        list.delete(0);
        assertEquals(0, list.getSize());
    }

    @Test
    public void delete_shiftsRemainingTasks_success() {
        DeadlineList list = new DeadlineList();
        list.add(new Deadline("First", LocalDateTime.of(2026, 1, 1, 10, 0)));
        list.add(new Deadline("Second", LocalDateTime.of(2026, 1, 1, 12, 0)));

        list.delete(0);

        assertEquals(1, list.getSize());
        assertEquals("Second", list.get(0).getDescription());
    }

    @Test
    public void getLatest_afterSort_returnsChronologicallyLatest() {
        DeadlineList list = new DeadlineList();
        LocalDateTime early = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime late = LocalDateTime.of(2026, 12, 31, 23, 59);

        list.add(new Deadline("Late Task", late));
        list.add(new Deadline("Early Task", early));

        list.sortByDate();

        // After sorting, the late task should be at the end
        assertEquals("Late Task", list.getLatest().getDescription());
    }

    @Test
    public void getLatest_emptyList_returnsNull() {
        DeadlineList list = new DeadlineList();
        assertNull(list.getLatest(), "getLatest on empty list must return null");
    }

    @Test
    public void getLatest_singleItem_returnsThatItem() {
        DeadlineList list = new DeadlineList();
        Deadline d = new Deadline("Only task", LocalDateTime.of(2026, 6, 6, 6, 0));
        list.add(d);
        assertEquals("Only task", list.getLatest().getDescription());
    }

    @Test
    public void clearAll_nonEmptyList_becomesEmpty() {
        DeadlineList list = new DeadlineList();
        list.add(new Deadline("A", LocalDateTime.of(2026, 1, 1, 10, 0)));
        list.add(new Deadline("B", LocalDateTime.of(2026, 2, 2, 10, 0)));

        list.clearAll();

        assertEquals(0, list.getSize());
    }

    @Test
    public void clearAll_emptyList_remainsEmpty() {
        DeadlineList list = new DeadlineList();
        list.clearAll();
        assertEquals(0, list.getSize());
    }

    @Test
    public void sortByDate_alreadySorted_orderUnchanged() {
        DeadlineList list = new DeadlineList();
        list.add(new Deadline("Jan", LocalDateTime.of(2026, 1, 1, 0, 0)));
        list.add(new Deadline("Feb", LocalDateTime.of(2026, 2, 1, 0, 0)));
        list.add(new Deadline("Mar", LocalDateTime.of(2026, 3, 1, 0, 0)));

        list.sortByDate();

        assertEquals("Jan", list.get(0).getDescription());
        assertEquals("Feb", list.get(1).getDescription());
        assertEquals("Mar", list.get(2).getDescription());
    }

    @Test
    public void sortByDate_reverseOrder_correctlySorted() {
        DeadlineList list = new DeadlineList();
        list.add(new Deadline("Dec", LocalDateTime.of(2026, 12, 1, 0, 0)));
        list.add(new Deadline("Jun", LocalDateTime.of(2026, 6, 1, 0, 0)));
        list.add(new Deadline("Jan", LocalDateTime.of(2026, 1, 1, 0, 0)));

        list.sortByDate();

        assertEquals("Jan", list.get(0).getDescription());
        assertEquals("Jun", list.get(1).getDescription());
        assertEquals("Dec", list.get(2).getDescription());
    }

    @Test
    public void sortByDate_sameDate_orderStable() {
        DeadlineList list = new DeadlineList();
        LocalDateTime same = LocalDateTime.of(2026, 5, 5, 12, 0);
        list.add(new Deadline("First", same));
        list.add(new Deadline("Second", same));
        list.add(new Deadline("Third", same));

        list.sortByDate();

        assertEquals(3, list.getSize());
    }

    @Test
    public void toString_listsAllItems_success() {
        DeadlineList list = new DeadlineList();
        list.add(new Deadline("Alpha", LocalDateTime.of(2026, 3, 1, 9, 0)));
        list.add(new Deadline("Beta", LocalDateTime.of(2026, 4, 1, 9, 0)));

        String result = list.toString();
        assertTrue(result.contains("Alpha"));
        assertTrue(result.contains("Beta"));
    }

    @Test
    public void toString_emptyList_returnsEmptyString() {
        DeadlineList list = new DeadlineList();
        assertEquals("", list.toString());
    }
}
