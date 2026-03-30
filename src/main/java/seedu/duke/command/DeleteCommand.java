package seedu.duke.command;

import seedu.duke.appcontainer.AppContainer;
import seedu.duke.exception.UniTaskerException;
import seedu.duke.task.Event;
import seedu.duke.ui.CategoryUi;
import seedu.duke.ui.DeadlineUi;
import seedu.duke.ui.ErrorUi;
import seedu.duke.ui.EventUi;
import seedu.duke.ui.TaskUi;
import seedu.duke.ui.GeneralUi;

import seedu.duke.tasklist.EventReference;
import java.util.List;
import java.util.Map;

import static seedu.duke.tasklist.CategoryList.refreshCalendar;

public class DeleteCommand implements Command {
    public static final int DELETE_MIN_LENGTH = 2;

    public static final int INDEX_OF_DELETE_TYPE = 1;
    public static final int INDEX_OF_TASK_TO_DELETE = 3;
    public static final int INDEX_OF_CATEGORY_TO_DELETE = 2;


    private final String[] sentence;

    public DeleteCommand(String[] sentence) {
        this.sentence = sentence;
    }

    @Override
    public void execute(AppContainer container) {
        if (sentence.length < DELETE_MIN_LENGTH) {
            ErrorUi.printMissingArgs("Use: delete [type] [index]");
            return;
        }

        try {
            String secondCommand = sentence[INDEX_OF_DELETE_TYPE];
            int categoryIndex = -1;
            if (!secondCommand.equals("marked") && !secondCommand.equals("category")) {
                categoryIndex = CommandSupport.getCategoryIndex(container, sentence);
            }

            switch (secondCommand) {
            //@@author marken9
            case "marked":
                container.categories().deleteMarkedTasks();
                CategoryUi.printAllMarkedDeleted();
                break;
            case "category":
                int deleteIndex = Integer.parseInt(sentence[INDEX_OF_CATEGORY_TO_DELETE]) - 1;
                String catName = container.categories().getCategory(deleteIndex).getName();
                container.categories().deleteCategory(deleteIndex);
                CategoryUi.printCategoryDeleted(catName);
                break;
            case "todo":
                int todoIndex = Integer.parseInt(sentence[INDEX_OF_TASK_TO_DELETE]) - 1;
                String todoName = container.categories().getCategory(categoryIndex)
                        .getTodo(todoIndex).getDescription();
                container.categories().deleteTodo(categoryIndex, todoIndex);
                TaskUi.printTaskAction("Deleted", "todo", todoName);
                break;
            //@@author WenJunYu5984
            case "deadline":
                if (sentence[INDEX_OF_TASK_TO_DELETE].equalsIgnoreCase("all")) {
                    container.categories().deleteAllDeadlines(categoryIndex);
                    DeadlineUi.printItemDeleted("deadline", null, categoryIndex);
                } else {
                    int deadlineIndex = Integer.parseInt(sentence[INDEX_OF_TASK_TO_DELETE]) - 1;
                    container.categories().deleteDeadline(categoryIndex, deadlineIndex);
                    DeadlineUi.printItemDeleted("deadline", deadlineIndex, categoryIndex);
                }
                break;
            //@@author sushmiithaa
            case "event":
                if (sentence[INDEX_OF_TASK_TO_DELETE].equalsIgnoreCase("all")) {
                    container.categories().deleteAllEvents(categoryIndex);
                    DeadlineUi.printItemDeleted("event", null, categoryIndex);
                } else {
                    int uiIndex = Integer.parseInt(sentence[INDEX_OF_TASK_TO_DELETE]) - 1;
                    String currentView = container.categories().getCurrentView();
                    boolean incorrectView = !(currentView.equals("EVENT") || currentView.equals("EVENT_EXPANDED") ||
                            currentView.equals("NORMAL_EVENT_ONLY"));
                    if (incorrectView) {
                        throw new UniTaskerException("To delete a specific event please use " +
                                "'list event' or 'list event /all' first");
                    }
                    Map<Integer, List<EventReference>> map = container.categories().getActiveDisplayMap();
                    List<EventReference> categoryMap = map.get(categoryIndex);
                    EventReference ref = categoryMap.get(uiIndex);
                    Event eventToDelete = container.categories().getEvent(ref.categoryIndex, ref.eventIndex);
                    if (eventToDelete.getIsRecurring() &&
                            (!container.categories().getCurrentView().equals("EVENT_EXPANDED"))) {
                        GeneralUi.printBordered("This is a recurring group. To delete the specific occurrence, please" +
                                "use 'list event /all' or 'list occurrence " +
                                (categoryIndex + 1) + " " + (uiIndex + 1) + "' first");
                    } else {
                        container.categories().deleteEvent(ref.categoryIndex, ref.eventIndex);
                        EventUi.printNormalEventDeleted(eventToDelete);
                    }
                }
                break;
            case "recurring":
                String currentView = container.categories().getCurrentView();
                if (!(currentView.equals("RECURRING_OVERVIEW"))) {
                    throw new UniTaskerException("To delete a the recurring event group please " +
                            "use 'list recurring' first");
                }
                int uiIndex = Integer.parseInt(sentence[INDEX_OF_TASK_TO_DELETE]) - 1;
                Map<Integer, List<EventReference>> map = container.categories().getActiveDisplayMap();
                List<EventReference> categoryMap = map.get(categoryIndex);
                EventReference eventReference = categoryMap.get(uiIndex);
                Event event = container.categories().getEvent(eventReference.categoryIndex,
                        eventReference.eventIndex);
                container.categories().deleteRecurringEvent(categoryIndex, event.getRecurringGroupId());
                EventUi.printRecurringEventDeletedGroup(event);
                break;
            case "occurrence":
                int uiIdx = Integer.parseInt(sentence[INDEX_OF_TASK_TO_DELETE]) - 1;
                if (!container.categories().getCurrentView().equals("OCCURRENCE_VIEW")) {
                    GeneralUi.printBordered("Please run 'list occurrence' first to see individual dates.");
                    break;
                }
                Map<Integer, List<EventReference>> mapOccurrence = container.categories().getActiveDisplayMap();
                List<EventReference> categoryMapOccurrence = mapOccurrence.get(categoryIndex);
                EventReference target = categoryMapOccurrence.get(uiIdx);
                Event eventToDel = container.categories().getEvent(target.categoryIndex, target.eventIndex);
                container.categories().deleteEvent(target.categoryIndex, target.eventIndex);
                EventUi.printRecurringEventDeleted(eventToDel);
                break;
            //@@author
            default:
                ErrorUi.printUnknownCommand("delete",
                        "category/todo/deadline/event [index] or " +
                                "delete recurring [category index] [index number]");
                break;
            }

            CommandSupport.saveData(container);
            refreshCalendar(container.categories(), container.calendar());
        } catch (ArrayIndexOutOfBoundsException e) {
            ErrorUi.printMissingArgs("Example: delete todo 1 1");
        } catch (NumberFormatException e) {
            ErrorUi.printInvalidNumber();
        } catch (IndexOutOfBoundsException e) {
            ErrorUi.printIndexNotFound();
        } catch (UniTaskerException e) {
            ErrorUi.printError("Error occurred: ",e.getMessage());
        } catch (Exception e) {
            ErrorUi.printError("An unexpected error occurred", e.getMessage());
        }
    }
}
