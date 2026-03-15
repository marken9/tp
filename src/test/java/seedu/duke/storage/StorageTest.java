package seedu.duke.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import seedu.duke.tasklist.CategoryList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StorageTest {

    @Test
    public void saveAndLoad_deadlines_success() throws Exception {
        Storage testStorage = new Storage("test_todos.txt", "test_deadlines.txt", "test_events.txt");
        CategoryList listToSave = new CategoryList();
        listToSave.addCategory("TestCat");
        listToSave.addDeadline(0, "Test Deadline", LocalDateTime.of(2026, 4, 5, 19, 0));

        testStorage.save(listToSave);

        CategoryList loadedList = new CategoryList();
        testStorage.load(loadedList);

        assertEquals(1, loadedList.getAmount());
        assertEquals("TestCat", loadedList.getCategory(0).getName());
        assertEquals("Test Deadline", loadedList.getCategory(0).getDeadlineList().get(0).getDescription());

        // Cleanup
        new File("test_todos.txt").delete();
        new File("test_deadlines.txt").delete();
        new File("test_events.txt").delete();
    }

    @Test
    public void load_noFilesPresent_returnsEmptyList() {
        // Ensure files don't exist
        new File("non_existent_todo.txt").delete();
        new File("non_existent_dl.txt").delete();
        new File("non_existent_ev.txt").delete();

        Storage storage = new Storage("non_existent_todo.txt", "non_existent_dl.txt", "non_existent_ev.txt");
        CategoryList list = new CategoryList();

        // This should not crash and should leave the list empty
        storage.load(list);
        assertEquals(0, list.getAmount(), "List should be empty when no files exist");
    }

    @Test
    public void load_deadlineMissingTime_defaultsToLastMinute() throws IOException {
        String dlPath = "test_date_only.txt";
        File file = new File(dlPath);

        try (FileWriter writer = new FileWriter(file)) {
            // Format: Category | Status | Description | Date (Missing time)
            writer.write("School | D | 0 | Homework | 2026-10-10" + System.lineSeparator());
        }

        Storage storage = new Storage("empty.txt", dlPath, "empty.txt");
        CategoryList list = new CategoryList();
        storage.load(list);

        LocalDateTime loadedDate = list.getCategory(0).getDeadlineList().get(0).getBy();
        assertEquals(23, loadedDate.getHour(), "Should default hour to 23");
        assertEquals(59, loadedDate.getMinute(), "Should default minute to 59");

        file.delete();
    }

    @Test
    public void load_corruptedLine_skipsBadLineAndContinues() throws IOException {
        String dlPath = "corrupted.txt";
        File file = new File(dlPath);

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("ThisIsCorruptedDataWithoutPipes" + System.lineSeparator());
            writer.write("Work | D | 0 | Valid Task | 2026-12-12 1200" + System.lineSeparator());
        }

        Storage storage = new Storage("empty.txt", dlPath, "empty.txt");
        CategoryList list = new CategoryList();
        storage.load(list);

        // The first line fails the parts.length < 5 check, second line succeeds
        assertEquals(1, list.getCategory(0).getDeadlineList().getSize());
        assertEquals("Valid Task", list.getCategory(0).getDeadlineList().get(0).getDescription());

        file.delete();
    }
    @Test
    public void load_invalidYear_skipsLine() throws IOException {
        String dlPath = "invalid_year.txt";
        File file = new File(dlPath);

        try (FileWriter writer = new FileWriter(file)) {
            // Line 1: Year 2025 (Should be skipped by DateUtils)
            writer.write("Old | D | 0 | Old Task | 2025-12-31 2359" + System.lineSeparator());
            // Line 2: Year 2026 (Should be loaded)
            writer.write("New | D | 0 | New Task | 2026-01-01 1000" + System.lineSeparator());
        }

        Storage storage = new Storage("empty.txt", dlPath, "empty.txt");
        CategoryList list = new CategoryList();
        storage.load(list);

        // List should only contain the 2026 task
        assertEquals(1, list.getAmount(), "Should only have 1 category loaded");
        assertEquals("New Task", list.getCategory(0).getDeadlineList().get(0).getDescription());

        file.delete();
    }
    @AfterEach
    void tearDown() {
        new File("test_todos.txt").delete();
        new File("test_deadlines.txt").delete();
        new File("test_events.txt").delete();
    }
}
