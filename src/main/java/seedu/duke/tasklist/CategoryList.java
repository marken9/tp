package seedu.duke.tasklist;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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

    public void reorderCategory(int categoryIndex1, int categoryIndex2) throws UniTaskerException {
        if (categoryIndex1 >= this.getAmount() || categoryIndex1 < 0) {
            throw new UniTaskerException("First categoryIndex does not exist.");
        }
        if (categoryIndex2 >= this.getAmount() || categoryIndex2 < 0) {
            throw new UniTaskerException("Second categoryIndex does not exist.");
        }
        Category category = categories.remove(categoryIndex1);
        categories.add(categoryIndex2, category);
    }

    public void reorderTodo(int categoryIndex, int todoIndex1, int todoIndex2) throws UniTaskerException {
        if (categoryIndex >= this.getAmount() || categoryIndex < 0) {
            throw new UniTaskerException("categoryIndex does not exist.");
        }
        categories.get(categoryIndex).reorderTodo(todoIndex1, todoIndex2);
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
        assert (categoryIndex >= 0 && categoryIndex < categories.size()) : "Category index out of bounds";
        assert (description != null && !description.isEmpty()): "Event description should not be empty";
        assert (from != null && to != null): "Start date and time and end date and time should not be null";
        assert from.isBefore(to) || from.isEqual(to) : "The start date time must be before the end date time";


        categories.get(categoryIndex).addEvent(new Event(description, from, to,false,-1));
        logger.info("Add event: " + description + " from " + from  +" to " + to);

    }

    public void addRecurringWeeklyEventFile(int categoryIndex, String description,
                                            LocalDateTime from, LocalDateTime to,int recurringGroupIndex) {
        assert (recurringGroupIndex > 0): "Recurring Group Id must be greater than 0";
        assert (categoryIndex >= 0 && categoryIndex < categories.size()) : "Category index out of bounds";
        assert (description != null && !description.isEmpty()): "Event description should not be empty";
        assert (from != null && to != null): "Start date and time and end date and time should not be null";
        assert from.isBefore(to) || from.isEqual(to) : "The start date time must be before the end date time";


        categories.get(categoryIndex).addEvent(new Event(description,
                from, to,true,recurringGroupIndex));
        if (recurringGroupIndex > recurringGroupId) {
            recurringGroupId = recurringGroupIndex;
        }
        logger.info("Add recurring event from file : " + description + " from " + from  + " to " + to +
                " recurringGroupId " + recurringGroupIndex);

    }

    public void addRecurringWeeklyEvent(int categoryIndex, String description,
                                        LocalDateTime from, LocalDateTime to, Calendar calendar){
        assert (categoryIndex >= 0 && categoryIndex < categories.size()) : "Category index out of bounds";
        assert (description != null && !description.isEmpty()): "Event description should not be empty";
        assert (from != null && to != null): "Start date and time and end date and time should not be null";
        assert from.isBefore(to) || from.isEqual(to) : "The start date time must be before the end date time";
        assert (calendar != null): "Calendar should exist";

        recurringGroupId +=1;
        categories.get(categoryIndex).addRecurringWeeklyEvent(new Event(description,
                from, to,true,recurringGroupId),calendar);
        logger.info("Add recurring event : " + description + " from " + from  + " to " + to +
                " recurringGroupId " + recurringGroupId);
    }

    public void deleteEvent(int categoryIndex, int eventIndex) {
        assert (categoryIndex >= 0 && categoryIndex < categories.size()) : "Category index out of bounds";
        assert (eventIndex >= 0 && eventIndex < categories.get(categoryIndex).getEventList().getSize())
                : "Event index out of bounds";

        categories.get(categoryIndex).deleteEvent(eventIndex);
        logger.info("Delete event at : " + categoryIndex + " " + eventIndex);
    }

    public void deleteAllEvents(int categoryIndex) {
        assert (categoryIndex >= 0 && categoryIndex < categories.size()) : "Category index out of bounds";

        categories.get(categoryIndex).deleteAllEvents();
        logger.info("Delete all event at : " + categoryIndex);

    }

    public void setEventStatus(int categoryIndex, int eventIndex, boolean isDone) {
        assert (categoryIndex >= 0 && categoryIndex < categories.size()) : "Category index out of bounds";
        assert (eventIndex >= 0 && eventIndex < categories.get(categoryIndex).getEventList().getSize())
                : "Event index out of bounds";

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

    public String getEvent(int categoryIndex, int taskIndex) {
        assert (categoryIndex >= 0 && categoryIndex < categories.size()) : "Category index out of bounds";
        assert (taskIndex >= 0 && taskIndex < categories.get(categoryIndex).getEventList().getSize())
                : "Event index out of bounds";

        return categories.get(categoryIndex).getEvent(taskIndex).toString();
    }

    public Event getLatestEvent(int eventCategoryIndex) {
        assert (eventCategoryIndex >= 0 && eventCategoryIndex < categories.size()) : "Category index out of bounds";

        return categories.get(eventCategoryIndex).getLatestEvent();
    }

    public String getAllRecurringEvents() {
        ArrayList<Integer> existingGroups = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("=== ALL RECURRING EVENTS ===").append(System.lineSeparator());
        for (Category cat : categories) {
            sb.append(cat.getName()).append(":").append(System.lineSeparator());
            EventList eventList = cat.getEventList();
            eventList.sortByDay();
            for (int i=0;i<eventList.getSize();i++){
                if(eventList.get(i).getIsRecurring()){
                    if (! existingGroups.contains(eventList.get(i).getRecurringGroupId())) {
                        sb.append(eventList.get(i).toStringRecurringList() + "\n");
                        existingGroups.add(eventList.get(i).getRecurringGroupId());
                    }
                }
            }
        }
        return sb.toString();
    }
    
    public Event findRecurringEventToDelete(int categoryIndex, int groupIndex){
        assert (categoryIndex >= 0 && categoryIndex < categories.size()) : "Category index out of bounds";

        EventList eventList = categories.get(categoryIndex).getEventList();
        Event event = null;
        for (int i = eventList.getSize() - 1; i >= 0; i--) {
            if (eventList.get(i).getRecurringGroupId() == groupIndex) {
                event = categories.get(categoryIndex).getEvent(i);
                break;
            }
        }
        return event;
    }

    public void deleteRecurringEvent(int categoryIndex, int groupIndex) {
        assert (categoryIndex >= 0 && categoryIndex < categories.size()) : "Category index out of bounds";
        assert (groupIndex > 0) : "Group Id must be greater than 0";

        EventList eventList = categories.get(categoryIndex).getEventList();
        for (int i = eventList.getSize() - 1; i >= 0; i--) {
            if (eventList.get(i).getRecurringGroupId() == groupIndex) {
                int eventIndex = i;
                categories.get(categoryIndex).deleteEvent(eventIndex);
            }
        }
        // Find existing group numbers
        ArrayList<Integer> currentGroupNumbers = getCurrentGroupNumbers();
        Collections.sort(currentGroupNumbers);

        // Add to a hashmap to reassign the number accordingly
        Map<Integer, Integer> mapping = new HashMap<>();
        for (int k = 0; k < currentGroupNumbers.size(); k++) {
            mapping.put(currentGroupNumbers.get(k), k + 1);
        }

        for (Category cat : categories) {
            for (int w = 0; w < cat.getEventList().getSize(); w++) {
                Event event = cat.getEventList().get(w);
                if (event.getIsRecurring()) {
                    event.setRecurringGroupId(mapping.get(event.getRecurringGroupId()));
                }
            }
        }
        recurringGroupId = currentGroupNumbers.size();
        logger.info("Delete recurring event group at : " + categoryIndex + " " + groupIndex);


    }

    private ArrayList<Integer> getCurrentGroupNumbers() {
        ArrayList<Integer> currentGroupNumbers = new ArrayList<>();
        for (Category cat : categories) {
            EventList newEventList = cat.getEventList();
            for (int j = 0; j < newEventList.getSize(); j++) {
                if (newEventList.get(j).getIsRecurring() &&
                        !currentGroupNumbers.contains(newEventList.get(j).getRecurringGroupId())) {
                    currentGroupNumbers.add(newEventList.get(j).getRecurringGroupId());
                }
            }
        }
        return currentGroupNumbers;
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

}
