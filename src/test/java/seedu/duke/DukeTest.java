package seedu.duke;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.duke.appcontainer.AppContainer;
import seedu.duke.calender.Calendar;
import seedu.duke.command.Command;
import seedu.duke.command.CommandParser;
import seedu.duke.exception.UniTaskerException;
import seedu.duke.storage.Storage;
import seedu.duke.tasklist.CategoryList;

class DukeTest {
    @Test
    public void sampleTest() {
        assertTrue(true);
    }

    @Test
    public void addCategory_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");

        assertEquals(1, categoryList.getAmount());
        assertEquals("School", categoryList.getCategory(0).getName());
    }

    @Test
    public void deleteCategory_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");
        categoryList.addCategory("Work");

        categoryList.deleteCategory(0);

        assertEquals(1, categoryList.getAmount());
        assertEquals("Work", categoryList.getCategory(0).getName());
    }

    @Test
    public void addTodo_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");

        categoryList.addTodo(0, "finish tutorial");

        assertEquals(1, categoryList.getCategory(0).getTodoList().getSize());
        assertEquals("finish tutorial", categoryList.getCategory(0).getTodo(0).getDescription());
    }

    @Test
    public void deleteTodo_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");
        categoryList.addTodo(0, "task 1");
        categoryList.addTodo(0, "task 2");

        categoryList.deleteTodo(0, 0);

        assertEquals(1, categoryList.getCategory(0).getTodoList().getSize());
        assertEquals("task 2", categoryList.getCategory(0).getTodo(0).getDescription());
    }

    @Test
    public void markTodo_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");
        categoryList.addTodo(0, "task 1");

        try {
            categoryList.markTodo(0, 0);
        } catch (UniTaskerException e) {
            //ignore
        }

        assertEquals(true, categoryList.getCategory(0).getTodo(0).getIsDone());
    }

    @Test
    public void unmarkTodo_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");
        categoryList.addTodo(0, "task 1");
        try {
            categoryList.markTodo(0, 0);
            categoryList.unmarkTodo(0, 0);
        } catch (UniTaskerException e) {
            //ignore
        }

        assertEquals(false, categoryList.getCategory(0).getTodo(0).getIsDone());
    }

    @Test
    public void reorderCategory_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");
        categoryList.addCategory("Work");

        try {
            categoryList.reorderCategory(0, 1);
        } catch (UniTaskerException e) {
            //ignore
        }

        assertEquals("Work", categoryList.getCategory(0).getName());
        assertEquals("School", categoryList.getCategory(1).getName());
    }

    @Test
    public void reorderTodo_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");
        categoryList.addTodo(0, "task 1");
        categoryList.addTodo(0, "task 2");

        try {
            categoryList.reorderTodo(0, 0, 1);
        } catch (UniTaskerException e) {
            //ignore
        }

        assertEquals("task 2", categoryList.getCategory(0).getTodo(0).getDescription());
        assertEquals("task 1", categoryList.getCategory(0).getTodo(1).getDescription());
    }

    @Test
    public void setTodoPriority_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");
        categoryList.addTodo(0, "finish tutorial");

        try {
            categoryList.setTodoPriority(0, 0, 3);
        } catch (UniTaskerException e) {
            // ignore
        }

        assertEquals(3, categoryList.getCategory(0).getTodo(0).getPriority());
    }

    @Test
    public void sortTodoByPriority_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");
        categoryList.addTodo(0, "low priority task");
        categoryList.addTodo(0, "high priority task");
        categoryList.addTodo(0, "medium priority task");

        try {
            categoryList.setTodoPriority(0, 0, 1); // low
            categoryList.setTodoPriority(0, 1, 5); // high
            categoryList.setTodoPriority(0, 2, 3); // medium
        } catch (UniTaskerException e) {
            // ignore
        }

        categoryList.getCategory(0).getTodoList().sortByPriority();

        assertEquals("high priority task", categoryList.getCategory(0).getTodo(0).getDescription());
        assertEquals("medium priority task", categoryList.getCategory(0).getTodo(1).getDescription());
        assertEquals("low priority task", categoryList.getCategory(0).getTodo(2).getDescription());
    }

    @Test
    public void listCategory_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");
        categoryList.addCategory("Work");

        String output = categoryList.toString();

        assertTrue(output.contains("School"));
        assertTrue(output.contains("Work"));
    }

    @Test
    public void listSingleCategory_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");
        categoryList.addTodo(0, "finish tutorial");

        String output = categoryList.getCategory(0).toString();

        assertTrue(output.contains("School"));
        assertTrue(output.contains("finish tutorial"));
    }

    @Test
    public void parseAndExecute_commandTodos_success() {
        CategoryList categories = new CategoryList();

        AppContainer container = new AppContainer(
                categories,
                new Calendar(),
                new Storage("testTodo.txt", "testDeadline.txt", "testEvent.txt"),
                null
        );

        CommandParser parser = new CommandParser();
        Command command;

        // add category [name]
        command = parser.parse("add category School");
        command.execute(container);
        assertEquals(1, categories.getAmount());
        assertEquals("School", categories.getCategory(0).getName());

        command = parser.parse("add category Work");
        command.execute(container);
        assertEquals(2, categories.getAmount());
        assertEquals("Work", categories.getCategory(1).getName());

        // add todo [categoryindex] [desc]
        command = parser.parse("add todo 1 finish tutorial");
        command.execute(container);
        command = parser.parse("add todo 1 submit lab");
        command.execute(container);
        command = parser.parse("add todo 2 prepare slides");
        command.execute(container);

        assertEquals(2, categories.getCategory(0).getTodoList().getSize());
        assertEquals("finish tutorial", categories.getCategory(0).getTodo(0).getDescription());
        assertEquals("submit lab", categories.getCategory(0).getTodo(1).getDescription());
        assertEquals(1, categories.getCategory(1).getTodoList().getSize());
        assertEquals("prepare slides", categories.getCategory(1).getTodo(0).getDescription());

        // mark todo [categoryindex] [todoindex]
        command = parser.parse("mark todo 1 1");
        command.execute(container);
        assertTrue(categories.getCategory(0).getTodo(0).getIsDone());

        // unmark todo [categoryindex] [todoindex]
        command = parser.parse("unmark todo 1 1");
        command.execute(container);
        assertFalse(categories.getCategory(0).getTodo(0).getIsDone());

        // priority todo [categoryindex] [todoindex] [priority level]
        command = parser.parse("priority todo 1 1 2");
        command.execute(container);
        command = parser.parse("priority todo 1 2 5");
        command.execute(container);

        assertEquals(2, categories.getCategory(0).getTodo(0).getPriority());
        assertEquals(5, categories.getCategory(0).getTodo(1).getPriority());

        // sort todo [categoryindex]
        command = parser.parse("sort todo 1");
        command.execute(container);

        assertEquals("submit lab", categories.getCategory(0).getTodo(0).getDescription());
        assertEquals(5, categories.getCategory(0).getTodo(0).getPriority());
        assertEquals("finish tutorial", categories.getCategory(0).getTodo(1).getDescription());
        assertEquals(2, categories.getCategory(0).getTodo(1).getPriority());

        // reorder todo [categoryindex] [todoindex1] [todoindex2]
        command = parser.parse("reorder todo 1 1 2");
        command.execute(container);

        assertEquals("finish tutorial", categories.getCategory(0).getTodo(0).getDescription());
        assertEquals("submit lab", categories.getCategory(0).getTodo(1).getDescription());

        // reorder category [categoryindex1] [categoryindex2]
        command = parser.parse("reorder category 1 2");
        command.execute(container);

        assertEquals("Work", categories.getCategory(0).getName());
        assertEquals("School", categories.getCategory(1).getName());

        // delete todo [categoryindex] [todoindex]
        // after reorder category, School is now category 2
        command = parser.parse("delete todo 2 1");
        command.execute(container);

        assertEquals(1, categories.getCategory(1).getTodoList().getSize());
        assertEquals("submit lab", categories.getCategory(1).getTodo(0).getDescription());

        // delete category [index]
        command = parser.parse("delete category 1");
        command.execute(container);

        assertEquals(1, categories.getAmount());
        assertEquals("School", categories.getCategory(0).getName());
    }

    @Test
    public void parseAndExecute_batchMark_deleteMarked_deleteTodoAll_success() {
        CategoryList categories = new CategoryList();

        AppContainer container = new AppContainer(
                categories,
                new Calendar(),
                new Storage("testTodo.txt", "testDeadline.txt", "testEvent.txt"),
                null
        );

        CommandParser parser = new CommandParser();
        Command command;

        command = parser.parse("add category School");
        command.execute(container);
        command = parser.parse("add todo 1 CS2113 tp");
        command.execute(container);
        command = parser.parse("add todo 1 CS2113 demo video");
        command.execute(container);
        command = parser.parse("add todo 1 CS2113 quiz");
        command.execute(container);
        command = parser.parse("add todo 1 CS2113 finals cheatsheet");
        command.execute(container);

        assertEquals(4, container.categories().getCategory(0).getTodoList().getSize());

        command = parser.parse("mark todo 1 2 4 6 2 4");
        command.execute(container);

        assertTrue(container.categories().getCategory(0).getTodo(1).getIsDone());
        assertTrue(container.categories().getCategory(0).getTodo(3).getIsDone());

        assertFalse(container.categories().getCategory(0).getTodo(0).getIsDone());
        assertFalse(container.categories().getCategory(0).getTodo(2).getIsDone());

        command = parser.parse("delete marked");
        command.execute(container);

        assertEquals(2, container.categories().getCategory(0).getTodoList().getSize());

        command = parser.parse("delete todo 1 all");
        command.execute(container);

        assertEquals(0, container.categories().getCategory(0).getTodoList().getSize());

    }
}


