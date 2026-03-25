package seedu.duke.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.duke.tasklist.CategoryList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    public void saveAndLoad_todos_success() throws Exception {
        Storage storage = new Storage("st_todos.txt", "st_deadlines.txt", "st_events.txt");
        CategoryList list = new CategoryList();
        list.addCategory("Personal");
        list.addTodo(0, "Buy groceries");

        storage.save(list);

        CategoryList loaded = new CategoryList();
        storage.load(loaded);

        assertEquals(1, loaded.getAmount());
        assertEquals("Buy groceries", loaded.getCategory(0).getTodoList().get(0).getDescription());
    }

    @Test
    public void saveAndLoad_events_success() throws Exception {
        Storage storage = new Storage("st_todos.txt", "st_deadlines.txt", "st_events.txt");
        CategoryList list = new CategoryList();
        list.addCategory("Work");
        LocalDateTime from = LocalDateTime.of(2026, 6, 15, 9, 0);
        LocalDateTime to   = LocalDateTime.of(2026, 6, 15, 10, 0);
        list.addEvent(0, "Standup", from, to);

        storage.save(list);

        CategoryList loaded = new CategoryList();
        storage.load(loaded);

        assertEquals("Standup", loaded.getCategory(0).getEventList().get(0).getDescription());
    }

    @Test
    public void saveAndLoad_markedDeadline_preservesDoneStatus() throws Exception {
        Storage storage = new Storage("st_todos.txt", "st_deadlines.txt", "st_events.txt");
        CategoryList list = new CategoryList();
        list.addCategory("Fitness");
        list.addDeadline(0, "Morning run", LocalDateTime.of(2026, 5, 1, 7, 0));
        list.setDeadlineStatus(0, 0, true);

        storage.save(list);

        CategoryList loaded = new CategoryList();
        storage.load(loaded);

        assertTrue(loaded.getCategory(0).getDeadlineList().get(0).getIsDone(),
                "Marked deadline should still be done after reload");
    }

    @Test
    public void saveAndLoad_multipleCategories_allRestored() throws Exception {
        Storage storage = new Storage("st_todos.txt", "st_deadlines.txt", "st_events.txt");
        CategoryList list = new CategoryList();
        list.addCategory("Alpha");
        list.addCategory("Beta");
        list.addDeadline(0, "Alpha task", LocalDateTime.of(2026, 3, 1, 10, 0));
        list.addDeadline(1, "Beta task",  LocalDateTime.of(2026, 4, 1, 10, 0));

        storage.save(list);

        CategoryList loaded = new CategoryList();
        storage.load(loaded);

        assertEquals(2, loaded.getAmount());
        assertEquals("Alpha task", loaded.getCategory(0).getDeadlineList().get(0).getDescription());
        assertEquals("Beta task",  loaded.getCategory(1).getDeadlineList().get(0).getDescription());
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
            writer.write("School | D | 0 | Homework | 10-10-2026" + System.lineSeparator());
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
            writer.write("Work | D | 0 | Valid Task | 12-12-2026 1200" + System.lineSeparator());
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
            writer.write("Old | D | 0 | Old Task | 31-12-2025 2359" + System.lineSeparator());
            // Line 2: Year 2026 (Should be loaded)
            writer.write("New | D | 0 | New Task | 01-12-2026 1000" + System.lineSeparator());
        }

        Storage storage = new Storage("empty.txt", dlPath, "empty.txt");
        CategoryList list = new CategoryList();
        storage.load(list);

        // List should only contain the 2026 task
        assertEquals(1, list.getAmount(), "Should only have 1 category loaded");
        assertEquals("New Task", list.getCategory(0).getDeadlineList().get(0).getDescription());

        file.delete();
    }

    @Test
    public void load_deadlineFileWithOnlyCorruptedLines_emptyListResult() throws IOException {
        String dlPath = "only_corrupt.txt";
        try (FileWriter w = new FileWriter(dlPath)) {
            w.write("garbage" + System.lineSeparator());
            w.write("also|bad" + System.lineSeparator());
        }

        Storage storage = new Storage("empty.txt", dlPath, "empty.txt");
        CategoryList list = new CategoryList();
        storage.load(list);

        assertEquals(0, list.getAmount(), "No valid categories should be created from corrupt lines");
        new File(dlPath).delete();
    }

    @Test
    public void load_eventFileMissingToDate_skipsLine() throws IOException {
        String evPath = "bad_event.txt";
        try (FileWriter w = new FileWriter(evPath)) {
            // Only 5 parts instead of the required 6
            w.write("Work | E | 0 | Missing end date | 01-06-2026 0900" + System.lineSeparator());
            // Valid line follows
            w.write("Work | E | 0 | Full event | 01-06-2026 0900 | 01-06-2026 1000" + System.lineSeparator());
        }

        Storage storage = new Storage("empty.txt", "empty.txt", evPath);
        CategoryList list = new CategoryList();
        storage.load(list);

        // Only the valid event line should be loaded
        assertEquals(1, list.getCategory(0).getEventList().getSize());
        new File(evPath).delete();
    }

    @Test
    public void load_emptyFiles_leaveListUnchanged() throws IOException {
        new File("empty.txt").createNewFile(); // ensure truly empty files exist

        Storage storage = new Storage("empty.txt", "empty.txt", "empty.txt");
        CategoryList list = new CategoryList();
        storage.load(list);

        assertEquals(0, list.getAmount());
    }

    @Test
    public void load_todoFileWithMarkedTask_preservesDoneStatus() throws IOException {
        String todoPath = "marked_todo.txt";
        try (FileWriter w = new FileWriter(todoPath)) {
            w.write("Home | C" + System.lineSeparator());
            w.write("Home | T | 1 | 2 | Water plants" + System.lineSeparator());
        }

        Storage storage = new Storage(todoPath, "empty.txt", "empty.txt");
        CategoryList list = new CategoryList();
        storage.load(list);

        assertTrue(list.getCategory(0).getTodoList().get(0).getIsDone(),
                "Todo marked as done in file should be loaded as done");
        new File(todoPath).delete();
    }

    @AfterEach
    void tearDown() {
        new File("test_todos.txt").delete();
        new File("test_deadlines.txt").delete();
        new File("test_events.txt").delete();
    }

    @BeforeEach
    public void setUp() {
        // This gives the "0 to 0" error a real range to work with
        seedu.duke.UniTasker.setStartYear(2024);
        seedu.duke.UniTasker.setEndYear(2030);
        seedu.duke.UniTasker.setDailyTaskLimit(8);
    }
}
