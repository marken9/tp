package seedu.duke.tasklist;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import seedu.duke.calender.Calendar;
import seedu.duke.task.Deadline;
import seedu.duke.task.Event;
import seedu.duke.exception.UniTaskerException;
import seedu.duke.task.Todo;

public class CategoryList {
    private static final Logger logger = Logger.getLogger(CategoryList.class.getName());

    private ArrayList<Category> categories;
    private int recurringGroupId = 0;
    private List<EventReference> activeDisplayMap = new ArrayList<>();
    private String currentView = "EVENT";

    public CategoryList() {
        categories = new ArrayList<>();
    }

    public List<EventReference> getActiveDisplayMap() {
        return activeDisplayMap;
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

    public String getCurrentView() {
        return currentView;
    }

    public void setCurrentView(String view) {
        this.currentView = view;
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
        if (categoryIndex >= this.getAmount() || categoryIndex < 0) {
            throw new UniTaskerException("categoryIndex does not exist.");
        }
        int validTodoIndex = categories.get(categoryIndex).getTodoList().getSize();
        if (todoIndex >= validTodoIndex || todoIndex < 0) {
            throw new UniTaskerException("todoIndex does not exist.");
        }
        categories.get(categoryIndex).markTodo(todoIndex);
    }

    public void unmarkTodo(int categoryIndex, int todoIndex) throws UniTaskerException {
        if (categoryIndex >= this.getAmount() || categoryIndex < 0) {
            throw new UniTaskerException("categoryIndex does not exist.");
        }
        if (todoIndex >= categories.get(categoryIndex).getTodoList().getSize() || todoIndex < 0) {
            throw new UniTaskerException("todoIndex does not exist.");
        }
        categories.get(categoryIndex).unmarkTodo(todoIndex);
    }

    public void reorderCategory(int fromCategoryIndex, int toCategoryIndex) throws UniTaskerException {
        if (fromCategoryIndex >= this.getAmount() || fromCategoryIndex < 0) {
            throw new UniTaskerException("First categoryIndex does not exist.");
        }
        if (toCategoryIndex >= this.getAmount() || toCategoryIndex < 0) {
            throw new UniTaskerException("Second categoryIndex does not exist.");
        }
        Category category = categories.remove(fromCategoryIndex);
        categories.add(toCategoryIndex, category);
    }

    public void reorderTodo(int categoryIndex, int fromTodoIndex, int toTodoIndex) throws UniTaskerException {
        if (categoryIndex >= this.getAmount() || categoryIndex < 0) {
            throw new UniTaskerException("categoryIndex does not exist.");
        }
        categories.get(categoryIndex).reorderTodo(fromTodoIndex, toTodoIndex);
    }

    public void setTodoPriority(int categoryIndex, int todoIndex, int priority) throws UniTaskerException {
        if (categoryIndex >= this.getAmount() || categoryIndex < 0) {
            throw new UniTaskerException("categoryIndex does not exist.");
        }
        if (todoIndex >= categories.get(categoryIndex).getTodoList().getSize() || todoIndex < 0) {
            throw new UniTaskerException("todoIndex does not exist.");
        }
        categories.get(categoryIndex).setTodoPriority(todoIndex, priority);
    }

    public void addEvent(int categoryIndex, String description, LocalDateTime from, LocalDateTime to) {
        assert (description != null && !description.isEmpty()) : "Event description should not be empty";
        assert (from != null && to != null) : "Start date and time and end date and time should not be null";
        assert from.isBefore(to) || from.isEqual(to) : "The start date time must be before the end date time";

        categories.get(categoryIndex).addEvent(new Event(description, from, to, false, -1));
        logger.info("Add event: " + description + " from " + from + " to " + to);

    }

    public void addRecurringWeeklyEventFile(int categoryIndex, String description,
            LocalDateTime from, LocalDateTime to, int recurringGroupIndex) {
        assert (recurringGroupIndex > 0) : "Recurring Group Id must be greater than 0";
        assert (description != null && !description.isEmpty()) : "Event description should not be empty";
        assert (from != null && to != null) : "Start date and time and end date and time should not be null";
        assert from.isBefore(to) || from.isEqual(to) : "The start date time must be before the end date time";


        categories.get(categoryIndex).addEvent(new Event(description,
                from, to, true, recurringGroupIndex));
        if (recurringGroupIndex > recurringGroupId) {
            recurringGroupId = recurringGroupIndex;
        }
        logger.info("Add recurring event from file : " + description + " from " + from + " to " + to +
                " recurringGroupId " + recurringGroupIndex);

    }

    public void addRecurringWeeklyEvent(int categoryIndex, String description, LocalDateTime from,
            LocalDateTime to, Calendar calendar, LocalDateTime date, int months) {
        assert (description != null && !description.isEmpty()) : "Event description should not be empty";
        assert (from != null && to != null) : "Start date and time and end date and time should not be null";
        assert from.isBefore(to) || from.isEqual(to) : "The start date time must be before the end date time";
        assert (calendar != null) : "Calendar should exist";

        recurringGroupId += 1;
        categories.get(categoryIndex).addRecurringWeeklyEvent(new Event(description,
                from, to, true, recurringGroupId), calendar, date, months);
        logger.info("Add recurring event : " + description + " from " + from + " to " + to +
                " recurringGroupId " + recurringGroupId);
    }

    public void deleteEvent(int categoryIndex, int eventIndex) {
        categories.get(categoryIndex).deleteEvent(eventIndex);
        logger.info("Delete event at : " + categoryIndex + " " + eventIndex);
    }

    public void deleteAllEvents(int categoryIndex) {
        categories.get(categoryIndex).deleteAllEvents();
        logger.info("Delete all event at : " + categoryIndex);

    }

    public void setEventStatus(int categoryIndex, int eventIndex, boolean isDone) {
        categories.get(categoryIndex).setEventStatus(eventIndex, isDone);
    }

    public String getAllEvents() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ALL EVENTS ===").append(System.lineSeparator());
        List<EventReference> newMap = new ArrayList<>();
        Set<Integer> printedGroups = new HashSet<>();
        for (int categoryIndex = 0; categoryIndex<categories.size();categoryIndex++){
            Category cat = categories.get(categoryIndex);
            sb.append(cat.getName()).append(":").append(System.lineSeparator());
            EventList eventList = cat.getEventList();
            eventList.sortByDate();

            for (int eventIndex = 0; eventIndex<eventList.getSize();eventIndex++){
                Event event = eventList.get(eventIndex);
                if (event.getIsRecurring()){
                    int groupId = event.getRecurringGroupId();
                    if (!printedGroups.contains(groupId)){
                        sb.append(newMap.size()+1).append(". ")
                                .append(event.toStringRecurring()).append(System.lineSeparator());
                        newMap.add(new EventReference(categoryIndex,eventIndex,event.getFrom()));
                        printedGroups.add(groupId);
                    }
                } else {
                    sb.append(newMap.size() + 1).append(". ")
                            .append(event.toString()).append(System.lineSeparator());
                    newMap.add(new EventReference(categoryIndex, eventIndex,event.getFrom()));
                }
            }
        }
        this.activeDisplayMap = newMap;
        this.currentView = "EVENT";
        return sb.toString();

    }

    public String getAllRecurringEvents() {
        ArrayList<Integer> existingGroups = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int uiIndex = 1;
        List<EventReference> newMap = new ArrayList<>();
        Set<Integer> printedGroups = new HashSet<>();
        sb.append("=== ALL RECURRING EVENTS ===").append(System.lineSeparator());

        for (int categoryIndex = 0; categoryIndex < categories.size(); categoryIndex++) {
            Category cat = categories.get(categoryIndex);
            sb.append(cat.getName()).append(":").append(System.lineSeparator());
            EventList eventList = cat.getEventList();
            eventList.sortByDay();
            for (int eventIndex = 0; eventIndex < eventList.getSize(); eventIndex++) {
                Event event = eventList.get(eventIndex);
                if (event.getIsRecurring()) {
                    int groupId = event.getRecurringGroupId();
                    if (!printedGroups.contains(groupId)) {
                        sb.append(newMap.size() + 1).append(". ")
                                .append(event.toStringRecurring()).append(System.lineSeparator());
                        newMap.add(new EventReference(categoryIndex, eventIndex,event.getFrom()));
                        printedGroups.add(groupId);
                    }
                }
            }
        }
        this.activeDisplayMap = newMap;
        this.currentView = "RECURRING_OVERVIEW";
        return sb.toString();
    }

    public boolean deleteEventByStartTime(int categoryIndex, LocalDateTime startTime) {
        EventList eventList = categories.get(categoryIndex).getEventList();
        for (int i = 0; i < eventList.getSize(); i++) {
            if (eventList.get(i).getFrom().equals(startTime)) {
                categories.get(categoryIndex).deleteEvent(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a deadline to a category and prints a success message.
     * The task is only added if the date parsing and year validation (2026+) passed.
     *
     * @param categoryIndex The index of the category to add to.
     * @param description   The task description.
     * @param by            The LocalDateTime of the deadline.
     */
    public Deadline addDeadline(int categoryIndex, String description, LocalDateTime by) {
        Deadline newDeadline = new Deadline(description, by);
        categories.get(categoryIndex).addDeadline(newDeadline);
        return newDeadline;
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
            DeadlineList deadlineList = cat.getDeadlineList();
            deadlineList.sortByDate();
            sb.append(deadlineList);
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

    public Event getEvent(int categoryIndex, int taskIndex) {
        return categories.get(categoryIndex).getEvent(taskIndex);
    }

    public Event getLatestEvent(int eventCategoryIndex) {
        return categories.get(eventCategoryIndex).getLatestEvent();
    }

    public void deleteRecurringEvent(int categoryIndex, int groupIndex) {
        EventList eventList = categories.get(categoryIndex).getEventList();
        for (int i = eventList.getSize() - 1; i >= 0; i--) {
            if (eventList.get(i).getRecurringGroupId() == groupIndex) {
                int eventIndex = i;
                categories.get(categoryIndex).deleteEvent(eventIndex);
            }
        }
        logger.info("Delete recurring event group at : " + categoryIndex + " " + groupIndex);
    }

    public void deleteMarkedTasks() {
        for (int i = 0; i < categories.size(); i += 1) {
            categories.get(i).deleteMarkedTasks();
        }
    }

    public CategoryList returnFoundTasks(String input) {
        CategoryList foundTasks = new CategoryList();
        for (int i = 0; i < this.getAmount(); i += 1) {
            Category foundCategory = this.getCategory(i).findMatches(input);
            if (!foundCategory.hasNoTasks()) {
                foundTasks.categories.add(foundCategory);
            }
        }
        return foundTasks;
    }

    public String getDeadline(int categoryIndex, int taskIndex) {
        assert (categoryIndex >= 0 && categoryIndex < categories.size()) : "Category index out of bounds";
        assert (taskIndex >= 0 && taskIndex < categories.get(categoryIndex).getDeadlineList().getSize())
                : "Event index out of bounds";

        return categories.get(categoryIndex).getDeadline(taskIndex).toString();
    }

    public Event getEventByStartTime(int categoryIndex, LocalDateTime startTime) {
        EventList eventList = categories.get(categoryIndex).getEventList();
        for (int i = 0; i < eventList.getSize(); i++) {
            if (eventList.get(i).getFrom().equals(startTime)) {
                return eventList.get(i);
            }
        }
        return null;
    }
}
