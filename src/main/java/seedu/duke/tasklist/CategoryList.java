package seedu.duke.tasklist;

import java.time.LocalDateTime;
import java.util.ArrayList;

import seedu.duke.calender.Calendar;
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

    public void markTodo(int categoryIndex, int todoIndex) {
        categories.get(categoryIndex).markTodo(todoIndex);
    }

    public void unmarkTodo(int categoryIndex, int todoIndex) {
        categories.get(categoryIndex).unmarkTodo(todoIndex);
    }

    public void reorderCategory(int categoryIndex1, int categoryIndex2) {
        Category category = categories.remove(categoryIndex1);
        categories.add(categoryIndex2, category);
    }

    public void reorderTodo(int categoryIndex, int todoIndex1, int todoIndex2) {
        categories.get(categoryIndex).reorderTodo(todoIndex1, todoIndex2);
    }

    public void setTodoPriority(int categoryIndex, int todoIndex, int priority) {
        categories.get(categoryIndex).setTodoPriority(todoIndex, priority);
    }

    public void addEvent(int categoryIndex, String description, LocalDateTime from, LocalDateTime to) {
        categories.get(categoryIndex).addEvent(new seedu.duke.task.Event(description,from,to));
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
            sb.append(cat.getEventList().toString());
        }
        return sb.toString();
    }

    public void addDeadline(int categoryIndex, String description, java.time.LocalDateTime by) {
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
            sb.append(cat.getName()).append(":").append(System.lineSeparator());
            sb.append(cat.getDeadlineList().toString());
        }
        return sb.toString();
    }

    public static void refreshCalendar(CategoryList categories, Calendar calendar) {
        calendar.clear();
        for (int i = 0; i < categories.getAmount(); i++) {
            Category cat = categories.getCategory(i);
            for (int j = 0; j < cat.getDeadlineList().getSize(); j++) {
                calendar.registerTask(cat.getDeadlineList().get(j));
            }
        }
    }


    public String toString() {
        String result = "";
        for (int i = 0; i < categories.size(); i += 1) {
            result += "[" + (i + 1) + "]" + categories.get(i).toString();
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
