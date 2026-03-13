package seedu.duke;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

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

        categoryList.markTodo(0, 0);

        assertEquals(true, categoryList.getCategory(0).getTodo(0).getIsDone());
    }

    @Test
    public void unmarkTodo_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");
        categoryList.addTodo(0, "task 1");
        categoryList.markTodo(0, 0);

        categoryList.unmarkTodo(0, 0);

        assertEquals(false, categoryList.getCategory(0).getTodo(0).getIsDone());
    }

    @Test
    public void reorderCategory_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");
        categoryList.addCategory("Work");

        categoryList.reorderCategory(0, 1);

        assertEquals("Work", categoryList.getCategory(0).getName());
        assertEquals("School", categoryList.getCategory(1).getName());
    }

    @Test
    public void reorderTodo_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");
        categoryList.addTodo(0, "task 1");
        categoryList.addTodo(0, "task 2");

        categoryList.reorderTodo(0, 0, 1);

        assertEquals("task 2", categoryList.getCategory(0).getTodo(0).getDescription());
        assertEquals("task 1", categoryList.getCategory(0).getTodo(1).getDescription());
    }

}

