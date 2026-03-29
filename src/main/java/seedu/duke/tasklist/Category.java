package seedu.duke.tasklist;

import java.time.LocalDateTime;
import java.util.ArrayList;

import seedu.duke.calender.Calendar;
import seedu.duke.exception.UniTaskerException;
import seedu.duke.task.Deadline;
import seedu.duke.task.Event;
import seedu.duke.task.Todo;

public class Category {
    private String name;
    private TodoList todoList;
    private DeadlineList deadlineList;
    private EventList eventList;

    public Category(String name) {
        assert name != null && !name.trim().isEmpty() : "Category name cannot be null or empty";
        this.name = name;
        this.todoList = new TodoList();
        this.deadlineList = new DeadlineList();
        this.eventList = new EventList();
    }

    public void setName(String name) {
        assert name != null && !name.trim().isEmpty() : "Category name cannot be null or empty";
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public TodoList getTodoList() {
        return todoList;
    }

    public void addTodo(Todo todo) {
        todoList.add(todo);
    }

    public Todo getTodo(int index) {
        return todoList.get(index);
    }

    public void deleteTodo(int index) {
        todoList.delete(index);
    }

    public void markTodo(int index) {
        todoList.mark(index);
    }

    public void unmarkTodo(int index) {
        todoList.unmark(index);
    }

    public void setTodoPriority(int index, int priority) {
        todoList.setPriority(index, priority);
    }

    public void reorderTodo(int fromIndex, int toIndex) throws UniTaskerException {
        todoList.reorder(fromIndex, toIndex);
    }

    public void addEvent(Event event) {
        assert (event != null) : "Event must not be null";
        eventList.add(event);
    }

    public void addRecurringWeeklyEvent(Event event, Calendar calendar, LocalDateTime date, int months) {
        assert (event != null) : "Event must not be null";
        assert (calendar != null) : "Calendar must not be null";
        eventList.addRecurringWeeklyEvent(event, calendar,date,months);
    }

    public void deleteEvent(int index) {
        eventList.delete(index);
    }

    public void setEventStatus(int index, boolean isDone) {
        if (isDone) {
            eventList.mark(index);
        } else {
            eventList.unmark(index);
        }
    }

    public Event getEvent(int index) {
        return eventList.get(index);
    }

    public Event getLatestEvent() {
        return eventList.getLatest();
    }

    public EventList getEventList() {
        eventList.sortByDate();
        return eventList;
    }

    public void deleteAllEvents() {
        eventList.clearAll();
    }

    //@@author WenJunYu5984
    /**
     * Sorts all deadlines in this category by their due date in ascending order.
     */
    public void sortDeadlines() {
        deadlineList.sortByDate();
    }

    /**
     * Adds a deadline to this category and re-sorts the list by due date.
     *
     * @param deadline the {@link Deadline} to add; must not be {@code null}
     */
    public void addDeadline(Deadline deadline) {
        assert deadline != null : "Deadline cannot be null";
        deadlineList.add(deadline);
        sortDeadlines();
    }

    public void deleteDeadline(int index) {
        deadlineList.delete(index);
    }

    /**
     * Sets the completion status of the deadline at the given index.
     *
     * @param index  the 0-based index of the deadline to update
     * @param isDone {@code true} to mark the deadline as done,
     *               {@code false} to mark it as not done
     */
    public void setDeadlineStatus(int index, boolean isDone) {
        if (isDone) {
            deadlineList.mark(index);
        } else {
            deadlineList.unmark(index);
        }
    }


    public DeadlineList getDeadlineList() {
        return deadlineList;
    }

    public void deleteAllDeadlines() {
        deadlineList.clearAll();
    }

    public Deadline getDeadline(int index) {
        return deadlineList.get(index);
    }
    //@@author

    public void deleteMarkedTasks() {
        todoList.deleteMarked();
        deadlineList.deleteMarked();
        eventList.deleteMarked();
    }

    public boolean hasNoTasks() {
        return todoList.getSize() == 0
                && deadlineList.getSize() == 0
                && eventList.getSize() == 0;
    }

    public Category findMatches(String input) {
        Category foundCategory = new Category(getName());

        ArrayList<Todo> foundTodos = todoList.findMatchesList(input);
        ArrayList<Deadline> foundDeadlines = deadlineList.findMatchesList(input);
        ArrayList<Event> foundEvents = eventList.findMatchesList(input);

        for (int i = 0; i < foundTodos.size(); i += 1) {
            foundCategory.addTodo(foundTodos.get(i));
        }
        for (int i = 0; i < foundDeadlines.size(); i += 1) {
            foundCategory.addDeadline(foundDeadlines.get(i));
        }
        for (int i = 0; i < foundEvents.size(); i += 1) {
            foundCategory.addEvent(foundEvents.get(i));
        }

        return foundCategory;
    }

    //@@author marken9
    @Override
    public String toString() {
        String result = "";
        result += "---" + getName() + "---" + System.lineSeparator();
        result += "Todos:" + System.lineSeparator() + todoList.toString() + System.lineSeparator();
        result += "Deadlines:" + System.lineSeparator() + deadlineList.toString() + System.lineSeparator();
        result += "Events:" + System.lineSeparator() + eventList.toString() + System.lineSeparator();

        return result;
    }

}
