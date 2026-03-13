package seedu.duke.tasklist;

import org.junit.jupiter.api.Test;

import seedu.duke.task.Deadline;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
