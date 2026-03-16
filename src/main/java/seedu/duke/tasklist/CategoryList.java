package seedu.duke.tasklist;

import java.time.LocalDateTime;
import java.util.ArrayList;

import seedu.duke.calender.Calendar;
import seedu.duke.exception.UniTaskerException;
import seedu.duke.task.Todo;

public class CategoryList {
    private ArrayList<Category> categories;

    public CategoryList() {
        categories = new ArrayList<>();
    }

    public void addCategory(String name) {
        categories.add(new Category(name));
    }

    public int getAmount() {
        return categories.size();
    }

    public Category getCategory(int index) {
        return categories.get(index);
    }

    public void addTodo(int categoryIndex, String description) {
        categories.get(categoryIndex).addTodo(new Todo(description));
    }

    public String getAllTodos() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ALL TODOS ===").append(System.lineSeparator());
        for (Category cat : categories) {
            sb.append(cat.getName()).append(":").append(System.lineSeparator());
            sb.append(cat.getTodoList().toString());
        }
        return sb.toString();
    }

    public void deleteCategory(int index) {
        categories.remove(index);
    }

    public void deleteTodo(int categoryIndex, int todoIndex) {
        categories.get(categoryIndex).deleteTodo(todoIndex);
    }

    public void markTodo(int categoryIndex, int todoIndex) throws UniTaskerException {
        if (categoryIndex >= this.getAmount()) {
            throw new UniTaskerException("categoryIndex does not exist.");
        }
        if (todoIndex >= categories.get(categoryIndex).getTodoList().getSize()) {
            throw new UniTaskerException("todoIndex does not exist.");
        }
        categories.get(categoryIndex).markTodo(todoIndex);
    }

    public void unmarkTodo(int categoryIndex, int todoIndex) throws UniTaskerException {
        if (categoryIndex >= this.getAmount()) {
            throw new UniTaskerException("categoryIndex does not exist.");
        }
        if (todoIndex >= categories.get(categoryIndex).getTodoList().getSize()) {
            throw new UniTaskerException("todoIndex does not exist.");
        }
        categories.get(categoryIndex).unmarkTodo(todoIndex);
    }

    public void reorderCategory(int categoryIndex1, int categoryIndex2) throws UniTaskerException {
        if (categoryIndex1 >= this.getAmount()) {
            throw new UniTaskerException("First categoryIndex does not exist.");
        }
        if (categoryIndex2 >= this.getAmount()) {
            throw new UniTaskerException("Second categoryIndex does not exist.");
        }
        Category category = categories.remove(categoryIndex1);
        categories.add(categoryIndex2, category);
    }

    public void reorderTodo(int categoryIndex, int todoIndex1, int todoIndex2) throws UniTaskerException {
        if (categoryIndex > this.getAmount()) {
            throw new UniTaskerException("categoryIndex does not exist.");
        }
        categories.get(categoryIndex).reorderTodo(todoIndex1, todoIndex2);
    }

    public void setTodoPriority(int categoryIndex, int todoIndex, int priority) {
        categories.get(categoryIndex).setTodoPriority(todoIndex, priority);
    }

    public void addEvent(int categoryIndex, String description, LocalDateTime from, LocalDateTime to) {
        categories.get(categoryIndex).addEvent(new seedu.duke.task.Event(description, from, to));
    }

    public void deleteEvent(int categoryIndex, int eventIndex) {
        categories.get(categoryIndex).deleteEvent(eventIndex);
    }

    public void deleteAllEvents(int categoryIndex) {
        categories.get(categoryIndex).deleteAllEvents();
    }

    public void setEventStatus(int categoryIndex, int eventIndex, boolean isDone) {
        categories.get(categoryIndex).setEventStatus(eventIndex, isDone);
    }

    public String getAllEvents() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ALL EVENTS ===").append(System.lineSeparator());
        for (Category cat : categories) {
            sb.append(cat.getName()).append(":").append(System.lineSeparator());
            EventList eventList = cat.getEventList();
            eventList.sortByDate();
            sb.append(eventList.toString());
        }
        return sb.toString();
    }

    /**
     * Adds a deadline to a category and prints a success message.
     * The task is only added if the date parsing and year validation (2026+) passed.
     *
     * @param categoryIndex The index of the category to add to.
     * @param description   The task description.
     * @param by            The LocalDateTime of the deadline.
     */
    public void addDeadline(int categoryIndex, String description, LocalDateTime by) {
        categories.get(categoryIndex).addDeadline(new seedu.duke.task.Deadline(description, by));
    }

    public void deleteDeadline(int categoryIndex, int deadlineIndex) {
        categories.get(categoryIndex).deleteDeadline(deadlineIndex);
    }

    public void deleteAllDeadlines(int categoryIndex) {
        categories.get(categoryIndex).deleteAllDeadlines();
    }

    public void setDeadlineStatus(int categoryIndex, int deadlineIndex, boolean isDone) {
        categories.get(categoryIndex).setDeadlineStatus(deadlineIndex, isDone);
    }

    public void sortDeadlines(int categoryIndex) {
        categories.get(categoryIndex).sortDeadlines();
    }

    public String getAllDeadlines() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ALL DEADLINES ===").append(System.lineSeparator());
        for (Category cat : categories) {
            sb.append(cat.getName().trim()).append(":").append(System.lineSeparator());
            sb.append(cat.getDeadlineList().toString());
        }
        return sb.toString();
    }

    /**
     * Rebuilds the Calendar entries based on the current state of the CategoryList.
     * This ensures the Calendar view stays consistent with the task list.
     *
     * @param categories The source CategoryList.
     * @param calendar   The Calendar instance to be refreshed.
     */
    public static void refreshCalendar(CategoryList categories, Calendar calendar) {
        calendar.clear();
        for (int i = 0; i < categories.getAmount(); i++) {
            Category cat = categories.getCategory(i);
            for (int j = 0; j < cat.getDeadlineList().getSize(); j++) {
                calendar.registerTask(cat.getDeadlineList().get(j));
            }
            for (int w = 0; w < cat.getEventList().getSize(); w++) {
                calendar.registerTask(cat.getEventList().get(w));
            }
        }
    }


    public String toString() {
        String result = "";
        for (int i = 0; i < categories.size(); i += 1) {
            result += "[" + (i + 1) + "]" + categories.get(i).toString() + System.lineSeparator();
        }
        return result;
    }

    public String getEvent(int categoryIndex, int taskIndex) {
        return categories.get(categoryIndex).getEvent(taskIndex).toString();
    }

    public String getLatestEvent(int eventCategoryIndex) {
        return categories.get(eventCategoryIndex).getLatestEvent().toString();
    }
}
