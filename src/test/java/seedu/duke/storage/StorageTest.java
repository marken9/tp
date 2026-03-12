package seedu.duke.storage;

import org.junit.jupiter.api.Test;

import seedu.duke.tasklist.CategoryList;

import java.io.File;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StorageTest {

    @Test
    public void saveAndLoad_deadlines_success() throws Exception {
        Storage testStorage = new Storage("test_todos.txt", "test_deadlines.txt","test_events.txt");
        CategoryList listToSave = new CategoryList();
        listToSave.addCategory("TestCat");
        listToSave.addDeadline(0, "Test Deadline", LocalDateTime.of(2026, 4, 5, 19, 0));

        testStorage.save(listToSave);

        CategoryList loadedList = new CategoryList();
        testStorage.load(loadedList);

        assertEquals(1, loadedList.getAmount());
        assertEquals("TestCat", loadedList.getCategory(0).getName());
        assertEquals("Test Deadline", loadedList.getCategory(0).getDeadlineList().get(0).getDescription());

        new File("test_todos.txt").delete();
        new File("test_deadlines.txt").delete();
    }
}
