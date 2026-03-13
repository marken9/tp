package seedu.duke.task;

import org.junit.jupiter.api.Test;
import seedu.duke.tasklist.CategoryList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventTest {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

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
}
