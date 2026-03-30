package seedu.duke.tasklist;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

import java.util.logging.Logger;

import seedu.duke.calender.Calendar;
import seedu.duke.task.Deadline;
import seedu.duke.task.Event;
import seedu.duke.task.Task;
import seedu.duke.task.Todo;

import seedu.duke.exception.UniTaskerException;

public class CategoryList {
    public static final String DOTTED_LINE = "-------------------------------------------------------------------";
    public static final String EQUALSIGN_LINE = "===============================================================" +
            "=======";
    public static final int INDEX_LOWER_LIMIT = 0;
    public static final int GROUP_ID_FOR_NON_RECURRING = -1;

    private static final Logger logger = Logger.getLogger(CategoryList.class.getName());

    private ArrayList<Category> categories;
    private int recurringGroupId = 0;
    private Map<Integer, List<EventReference>> activeDisplayMap;
    private String currentView = "NO_VIEW";

    public CategoryList() {
        categories = new ArrayList<>();
    }

    public Map<Integer, List<EventReference>> getActiveDisplayMap() {
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

    public void addTodo(int categoryIndex, String description) {
        categories.get(categoryIndex).addTodo(new Todo(description));
    }

    public String getAllTodos() {
        StringBuilder sb = new StringBuilder();
        sb.append("ALL TODOS").append(System.lineSeparator());
        sb.append(DOTTED_LINE).append(System.lineSeparator());
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

    /**
     * Marks a specific todo as completed within a given category.
     *
     * <p>This method validates the provided category and todo indices before
     * delegating the marking operation to the corresponding {@code Category}.</p>
     *
     * @param categoryIndex The index of the category containing the todo.
     * @param todoIndex The index of the todo to be marked.
     * @throws UniTaskerException If the categoryIndex or todoIndex is invalid.
     */
    public void markTodo(int categoryIndex, int todoIndex) throws UniTaskerException {
        if (categoryIndex >= this.getAmount() || categoryIndex < INDEX_LOWER_LIMIT) {
            throw new UniTaskerException("categoryIndex does not exist.");
        }
        int validTodoIndex = categories.get(categoryIndex).getTodoList().getSize();
        if (todoIndex >= validTodoIndex || todoIndex < INDEX_LOWER_LIMIT) {
            throw new UniTaskerException("todoIndex does not exist.");
        }
        categories.get(categoryIndex).markTodo(todoIndex);
    }
    /**
     * Marks a specific todo as not completed within a given category.
     *
     * <p>This method validates the provided indices before delegating
     * the unmark operation to the corresponding {@code Category}.</p>
     *
     * @param categoryIndex The index of the category containing the todo.
     * @param todoIndex The index of the todo to be unmarked.
     * @throws UniTaskerException If the categoryIndex or todoIndex is invalid.
     */
    public void unmarkTodo(int categoryIndex, int todoIndex) throws UniTaskerException {
        if (categoryIndex >= this.getAmount() || categoryIndex < INDEX_LOWER_LIMIT) {
            throw new UniTaskerException("categoryIndex does not exist.");
        }
        if (todoIndex >= categories.get(categoryIndex).getTodoList().getSize() || todoIndex < INDEX_LOWER_LIMIT) {
            throw new UniTaskerException("todoIndex does not exist.");
        }
        categories.get(categoryIndex).unmarkTodo(todoIndex);
    }
    /**
     * Reorders categories by moving a category from one index to another.
     *
     * <p>This method removes the category at {@code fromCategoryIndex}
     * and inserts it at {@code toCategoryIndex}, shifting other categories accordingly.</p>
     *
     * @param fromCategoryIndex The original index of the category.
     * @param toCategoryIndex The target index to move the category to.
     * @throws UniTaskerException If either index is invalid.
     */
    public void reorderCategory(int fromCategoryIndex, int toCategoryIndex) throws UniTaskerException {
        if (fromCategoryIndex >= this.getAmount() || fromCategoryIndex < INDEX_LOWER_LIMIT) {
            throw new UniTaskerException("First categoryIndex does not exist.");
        }
        if (toCategoryIndex >= this.getAmount() || toCategoryIndex < INDEX_LOWER_LIMIT) {
            throw new UniTaskerException("Second categoryIndex does not exist.");
        }
        Category category = categories.remove(fromCategoryIndex);
        categories.add(toCategoryIndex, category);
    }
    /**
     * Reorders todos within a specific category.
     *
     * <p>This method delegates the reordering logic to the corresponding {@code Category},
     * moving a todo from one index to another within the same category.</p>
     *
     * @param categoryIndex The index of the category containing the todos.
     * @param fromTodoIndex The original index of the todo.
     * @param toTodoIndex The target index to move the todo to.
     * @throws UniTaskerException If any of the indexes entered are invalid.
     */
    public void reorderTodo(int categoryIndex, int fromTodoIndex, int toTodoIndex) throws UniTaskerException {
        if (categoryIndex >= this.getAmount() || categoryIndex < INDEX_LOWER_LIMIT) {
            throw new UniTaskerException("categoryIndex does not exist.");
        }
        categories.get(categoryIndex).reorderTodo(fromTodoIndex, toTodoIndex);
    }

    /**
     * Sets the priority level of a specific todo within a category.
     *
     * <p>This method validates the provided indices before delegating
     * the priority update to the corresponding {@code Category}.</p>
     *
     * @param categoryIndex The index of the category containing the todo.
     * @param todoIndex The index of the todo whose priority is to be updated.
     * @param priority The priority level to assign to the todo.
     * @throws UniTaskerException If the categoryIndex or todoIndex is invalid.
     */
    public void setTodoPriority(int categoryIndex, int todoIndex, int priority) throws UniTaskerException {
        if (categoryIndex >= this.getAmount() || categoryIndex < INDEX_LOWER_LIMIT) {
            throw new UniTaskerException("categoryIndex does not exist.");
        }
        if (todoIndex >= categories.get(categoryIndex).getTodoList().getSize() || todoIndex < INDEX_LOWER_LIMIT) {
            throw new UniTaskerException("todoIndex does not exist.");
        }
        categories.get(categoryIndex).setTodoPriority(todoIndex, priority);
    }

    public void addEvent(int categoryIndex, String description, LocalDateTime from, LocalDateTime to) {
        categories.get(categoryIndex).addEvent(createEvent(description, from, to, false, GROUP_ID_FOR_NON_RECURRING));
        logger.info("Add event: " + description + " from " + from + " to " + to);

    }

    public void addRecurringWeeklyEventFile(int categoryIndex, String description,
            LocalDateTime from, LocalDateTime to, int recurringGroupIndex) {
        assert (recurringGroupIndex > 0) : "Recurring Group Id must be greater than 0";
        addRecurring(categoryIndex, description, from, to, recurringGroupIndex,
                null,null, 0, true);

    }

    public void addRecurringWeeklyEvent(int categoryIndex, String description, LocalDateTime from,
            LocalDateTime to, Calendar calendar, LocalDateTime date, int months) {
        assert (calendar != null) : "Calendar should exist";
        recurringGroupId += 1;
        addRecurring(categoryIndex, description, from, to, recurringGroupId,
                calendar,date, months, false);
    }

    private void addRecurring(int categoryIndex, String description, LocalDateTime from, LocalDateTime to,
            int groupId, Calendar calendar, LocalDateTime date, int months, boolean isFromFile) {

        Event event = createEvent(description, from, to, true, groupId);

        if (isFromFile) {
            categories.get(categoryIndex).addEvent(event);
            if (groupId > recurringGroupId) {
                recurringGroupId = groupId;
            }
        } else {
            categories.get(categoryIndex).addRecurringWeeklyEvent(
                    event, calendar, date, months
            );
        }
        logger.info((isFromFile ? "Add recurring event from file: " : "Add recurring event: ")
                + description + " from " + from + " to " + to
                + " recurringGroupId " + groupId);
    }

    private void eventAssertions(String description, LocalDateTime from, LocalDateTime to) {
        assert (description != null && !description.isEmpty()) : "Event description should not be empty";
        assert (from != null && to != null) : "Start date and time and end date and time should not be null";
        assert from.isBefore(to) || from.isEqual(to) : "The start date time must be before the end date time";
    }

    private Event createEvent(String description, LocalDateTime from,
                              LocalDateTime to, boolean isRecurring, int groupId) {
        eventAssertions(description, from, to);
        return new Event(description, from, to, isRecurring, groupId);
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

    public String getAllEvents(boolean isExpanded,boolean isNormalEventOnly) {
        StringBuilder sb = new StringBuilder();
        sb.append(isExpanded ? "ALL OCCURRENCES" : isNormalEventOnly ?
                "ALL NON-RECURRING EVENTS" : "ALL EVENTS").append(System.lineSeparator());
        sb.append(DOTTED_LINE).append(System.lineSeparator());
        Map<Integer, List<EventReference>> newMap = new HashMap<>();
        Set<Integer> printedGroups = new HashSet<>();

        for (int categoryIndex = 0; categoryIndex < categories.size(); categoryIndex++) {
            Category cat = categories.get(categoryIndex);
            sb.append("[" + (categoryIndex + 1) + "]").append(cat.getName()).append(":").append(System.lineSeparator());
            EventList eventList = cat.getEventList();
            eventList.sortByDate();
            List<EventReference> categoryMap = new ArrayList<>();
            int uiIndex = 0;
            for (int eventIndex = 0; eventIndex < eventList.getSize(); eventIndex++) {
                Event event = eventList.get(eventIndex);
                if (shouldDisplayEvent(event, isExpanded, isNormalEventOnly, printedGroups)) {
                    displayEvent(sb, event, categoryMap, categoryIndex, eventIndex, isExpanded,uiIndex);
                    updatePrintedGroups(event, printedGroups);
                    uiIndex++;
                }
            }
            newMap.put(categoryIndex, categoryMap);
        }
        this.activeDisplayMap = newMap;
        this.currentView = isExpanded ? "EVENT_EXPANDED" : isNormalEventOnly ? "NORMAL_EVENT_ONLY" : "EVENT";
        return sb.toString();
    }

    private void displayEvent(StringBuilder sb, Event event, List<EventReference> map,
                              int categoryIndex, int eventIndex, boolean isExpanded,int uiIndex) {

        sb.append(uiIndex+1).append(". ")
                .append(event.getIsRecurring() && !isExpanded ? event.toStringRecurring() : event.toString())
                .append(System.lineSeparator());

        map.add(new EventReference(categoryIndex, eventIndex));
    }

    private void updatePrintedGroups(Event event, Set<Integer> printedGroups) {
        if (event.getIsRecurring()) {
            printedGroups.add(event.getRecurringGroupId());
        }
    }

    public String getAllRecurringEvents() {
        StringBuilder sb = new StringBuilder();
        Map<Integer, List<EventReference>> newMap = new HashMap<>();
        Set<Integer> printedGroups = new HashSet<>();
        sb.append("ALL RECURRING EVENTS").append(System.lineSeparator());
        sb.append(DOTTED_LINE).append(System.lineSeparator());

        for (int categoryIndex = 0; categoryIndex < categories.size(); categoryIndex++) {
            Category cat = categories.get(categoryIndex);
            sb.append("[" + (categoryIndex + 1) + "]").append(cat.getName()).append(":").append(System.lineSeparator());
            EventList eventList = cat.getEventList();
            eventList.sortByDay();

            List<EventReference> categoryMap = new ArrayList<>();
            int uiIndex = 0;
            for (int eventIndex = 0; eventIndex < eventList.getSize(); eventIndex++) {
                Event event = eventList.get(eventIndex);
                if (event.getIsRecurring()) {
                    int groupId = event.getRecurringGroupId();
                    if (!printedGroups.contains(groupId)) {
                        sb.append(uiIndex + 1).append(". ")
                                .append(event.toStringRecurring()).append(System.lineSeparator());
                        categoryMap.add(new EventReference(categoryIndex, eventIndex));
                        printedGroups.add(groupId);
                        uiIndex++;
                    }
                }
            }
            newMap.put(categoryIndex, categoryMap);
        }
        this.activeDisplayMap = newMap;
        this.currentView = "RECURRING_OVERVIEW";
        return sb.toString();
    }

    private boolean shouldDisplayEvent(Event event, boolean isExpanded,
                                       boolean isNormalEventOnly, Set<Integer> printedGroups) {
        if (isNormalEventOnly) {
            return !event.getIsRecurring();
        }

        if (event.getIsRecurring()) {
            int groupId = event.getRecurringGroupId();
            return isExpanded || !printedGroups.contains(groupId);
        }
        return true;
    }



    public String getOccurrencesOfRecurringEvent(int categoryIndex, int groupUiIndex) throws UniTaskerException {

        List<EventReference> categoryMap = activeDisplayMap.get(categoryIndex);

        if (categoryMap == null || groupUiIndex - 1 >= categoryMap.size()) {
            throw new UniTaskerException("Invalid index");
        }

        EventReference ref = categoryMap.get(groupUiIndex - 1);
        Event template = categories.get(ref.categoryIndex).getEvent(ref.eventIndex);
        if (!template.getIsRecurring()){
            throw new UniTaskerException("This is not a recurring event, list occurrence or " +
                    "other occurrence operations are only for recurring event");
        }
        int targetGroupId = template.getRecurringGroupId();

        StringBuilder sb = new StringBuilder();
        sb.append("OCCURRENCES FOR: ").append(template.getDescription()).append("\n");
        sb.append(DOTTED_LINE).append(System.lineSeparator());

        List<EventReference> newCategoryMap = new ArrayList<>();
        EventList eventList = categories.get(categoryIndex).getEventList();

        int uiIndex = 0;
        for (int i = 0; i < eventList.getSize(); i++) {
            Event e = eventList.get(i);
            if (e.getIsRecurring() && e.getRecurringGroupId() == targetGroupId) {
                sb.append(uiIndex + 1).append(". ").append(e.toString()).append(System.lineSeparator());
                newCategoryMap.add(new EventReference(categoryIndex, i));
                uiIndex++;
            }
        }
        Map<Integer, List<EventReference>> newMap = new HashMap<>();
        newMap.put(categoryIndex, newCategoryMap);

        this.activeDisplayMap = newMap;
        this.currentView = "OCCURRENCE_VIEW";
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
        sb.append("ALL DEADLINES").append(System.lineSeparator());
        sb.append(DOTTED_LINE).append(System.lineSeparator());
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

    public String getDeadline(int categoryIndex, int taskIndex) {
        assert (categoryIndex >= 0 && categoryIndex < categories.size()) : "Category index out of bounds";
        assert (taskIndex >= 0 && taskIndex < categories.get(categoryIndex).getDeadlineList().getSize())
                : "Event index out of bounds";

        return categories.get(categoryIndex).getDeadline(taskIndex).toString();
    }

    public String toString() {
        String result = "";
        result += EQUALSIGN_LINE + System.lineSeparator();
        for (int i = 0; i < categories.size(); i += 1) {
            result += "[" + (i + 1) + "]" + categories.get(i).toString() + System.lineSeparator();
        }
        result += EQUALSIGN_LINE;
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
    /**
     * Deletes all tasks that have been marked as completed across all categories.
     *
     * <p>This method iterates through every category in the {@code CategoryList}
     * and removes all tasks (todos, deadlines, and events) that are marked as done.
     * It provides a convenient way for users to perform bulk deletion of completed tasks.</p>
     */
    public void deleteMarkedTasks() {
        for (int i = 0; i < categories.size(); i += 1) {
            categories.get(i).deleteMarkedTasks();
        }
    }
    /**
     * Returns a new {@code CategoryList} containing tasks that match the given input substring.
     *
     * <p>This method searches through all categories and retrieves tasks whose descriptions
     * contain the specified input substring. Matching tasks are grouped under their respective
     * categories. Categories with no matching tasks are excluded from the result.</p>
     *
     * @param input The substring to search for within task descriptions.
     * @return A {@code CategoryList} containing only categories with matching tasks.
     */
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

    public Map<String, List<Task>> findTasksForTheDay(LocalDate today) {
        Map<String, List<Task>> reminders = new HashMap<>();
        for (Category category : categories) {
            EventList eventList = category.getEventList();
            DeadlineList deadlineList = category.getDeadlineList();
            addMatchedTask(today, category, eventList, reminders);
            addMatchedTask(today, category, deadlineList, reminders);
        }
        return reminders;
    }

    private void addMatchedTask(LocalDate today, Category category, TaskList taskList,
            Map<String, List<Task>> reminders) {
        for (int i = 0; i < taskList.getSize(); i++) {
            Task task = taskList.get(i);
            if (!task.getIsDone() && isTaskOnDate(task, today)) {
                reminders.computeIfAbsent(category.getName(), k -> new ArrayList<>()).add(task);
            }
        }
    }

    private boolean isTaskOnDate(Task task, LocalDate today) {
        if (task instanceof Event) {
            return ((Event) task).getFrom().toLocalDate().equals(today);
        } else if (task instanceof Deadline) {
            return ((Deadline) task).getBy().toLocalDate().equals(today);
        }
        return false;
    }

}
