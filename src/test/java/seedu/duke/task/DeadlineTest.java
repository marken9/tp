package seedu.duke.task;

import org.junit.jupiter.api.Test;

import seedu.duke.tasklist.CategoryList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeadlineTest {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    @Test
    public void addDeadline_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");

        LocalDateTime time = LocalDateTime.parse("2026-03-12 1830", formatter);
        categoryList.addDeadline(0, "submit project", time);

        assertEquals(1, categoryList.getCategory(0).getDeadlineList().getSize());
        assertEquals("submit project", categoryList.getCategory(0).getDeadlineList().get(0).getDescription());
    }

    @Test
    public void sortDeadlines_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");

        LocalDateTime early = LocalDateTime.parse("2026-03-10 1000", formatter);
        LocalDateTime late = LocalDateTime.parse("2026-03-20 1000", formatter);

        categoryList.addDeadline(0, "Late Task", late);
        categoryList.addDeadline(0, "Early Task", early);

        categoryList.sortDeadlines(0);

        assertEquals("Early Task", categoryList.getCategory(0).getDeadlineList().get(0).getDescription());
    }

    @Test
    public void deleteAllDeadlines_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("Personal");
        LocalDateTime time = LocalDateTime.parse("2026-03-12 1200", formatter);

        categoryList.addDeadline(0, "task 1", time);
        categoryList.addDeadline(0, "task 2", time);

        categoryList.deleteAllDeadlines(0);

        assertEquals(0, categoryList.getCategory(0).getDeadlineList().getSize());
    }
}


