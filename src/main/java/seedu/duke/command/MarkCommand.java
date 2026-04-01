package seedu.duke.command;

import seedu.duke.appcontainer.AppContainer;
import seedu.duke.exception.UniTaskerException;
import seedu.duke.task.Event;
import seedu.duke.tasklist.EventReference;

import seedu.duke.ui.ErrorUi;
import seedu.duke.ui.GeneralUi;
import seedu.duke.ui.TaskUi;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MarkCommand implements Command {
    public static final int MARK_MIN_LENGTH = 4;

    public static final int INDEX_OF_MARK_TYPE = 1;
    public static final int INDEX_OF_TASK_TO_MARK = 3;

    private final String[] sentence;
    private final boolean isMark;

    public MarkCommand(String[] sentence, boolean isMark) {
        this.sentence = sentence;
        this.isMark = isMark;
    }

    @Override
    public void execute(AppContainer container) {
        if (sentence.length < MARK_MIN_LENGTH) {
            ErrorUi.printUnknownCommand("mark/unmark", "todo, deadline or event");
            return;
        }

        try {
            String secondCommand = sentence[INDEX_OF_MARK_TYPE];
            switch (secondCommand) {
            case "todo":
                handleTodo(container);
                break;
            case "deadline":
                handleDeadline(container);
                break;
            case "event":
                handleEvent(container);
                break;
            case "occurrence":
                handleOccurrence(container);
                break;
            default:
                ErrorUi.printUnknownCommand("mark/unmark", "todo, deadline or event");
                break;
            }
            CommandSupport.saveData(container);
        } catch (Exception e) {
            ErrorUi.printMarkTaskError();
        }
    }
    //@@author marken9
    private void handleTodo(AppContainer container) {
        try {
            Result result = getResult(container);
            if (isMark) {
                container.categories().markTodo(result.categoryIndex(), result.taskIndex());
            } else {
                container.categories().unmarkTodo(result.categoryIndex(), result.taskIndex());
            }
            TaskUi.printMarkTodoResult(isMark, null);
        } catch (Exception e) {
            TaskUi.printMarkTodoResult(isMark, e.getMessage());
        }
    }

    //@@author WenJunYu5984
    private Result getResult(AppContainer container) {
        int categoryIndex = CommandSupport.getCategoryIndex(container, sentence);
        int taskIndex = Integer.parseInt(sentence[INDEX_OF_TASK_TO_MARK]) - 1;
        return new Result(categoryIndex, taskIndex);
    }

    private record Result(int categoryIndex, int taskIndex) {
    }

    //@@author WenJunYu5984
    private void handleDeadline(AppContainer container) {
        try {
            Result result = getResult(container);
            container.categories().setDeadlineStatus(result.categoryIndex, result.taskIndex, isMark);
            TaskUi.printStatusChanged(container.categories()
                    .getDeadline(result.categoryIndex, result.taskIndex), isMark);
        } catch (Exception e) {
            ErrorUi.printError(e.getMessage());
        }
    }

    //@@author sushmiithaa
    private void handleEvent(AppContainer container) {
        try {
            Result result = getResult(container);
            String[] validViews = {"EVENT", "EVENT_EXPANDED", "NORMAL_EVENT_ONLY"};
            validateView(container,validViews,"To mark/unmark a specific event please " +
                    "use 'list event' or 'list event /all' first. " +
                    "To mark/unmark a occurrence use 'list occurrence' then 'mark/unmark occurrence'");
            EventReference ref = getEventReference(container, result);
            Event event = container.categories().getEvent(ref.categoryIndex, ref.eventIndex);
            if (event.getIsRecurring() &&
                    (!container.categories().getCurrentView().equals("EVENT_EXPANDED"))) {
                GeneralUi.printBordered("This is a recurring group. To mark/unmark the specific occurrence, please " +
                        "use 'list event /all' or 'list occurrence " +
                        (result.categoryIndex + 1) + " " + (result.taskIndex + 1) + "' first");
            } else {
                setStatusAndPrintMessage(container, ref, event);
            }
        } catch (Exception e) {
            ErrorUi.printError(e.getMessage());
        }
    }

    private void handleOccurrence(AppContainer container) {
        try {
            Result result = getResult(container);
            String[] validViews = {"OCCURRENCE_VIEW"};
            validateView(
                    container, validViews,
                    "To mark/unmark a occurrence please " +
                            "use 'list occurrence' first to see individual events of recurring group");
            EventReference ref = getEventReference(container, result);
            Event event = container.categories().getEvent(ref.categoryIndex, ref.eventIndex);
            setStatusAndPrintMessage(container, ref, event);
        } catch (Exception e) {
            ErrorUi.printError(e.getMessage());
        }
    }

    private void setStatusAndPrintMessage(AppContainer container, EventReference ref, Event event) {
        container.categories().setEventStatus(ref.categoryIndex, ref.eventIndex, isMark);
        TaskUi.printStatusChanged(event.toString(), isMark);
    }

    private static void validateView(AppContainer container, String[] views,
            String errorMessage) throws UniTaskerException {
        String currentView = container.categories().getCurrentView();
        boolean isInvalid = !(Arrays.asList(views).contains(currentView));
        if (isInvalid) {
            throw new UniTaskerException(errorMessage);
        }
    }

    private static EventReference getEventReference(AppContainer container, Result result) {
        Map<Integer, List<EventReference>> map = container.categories().getActiveDisplayMap();
        List<EventReference> categoryMap = map.get(result.categoryIndex);
        return categoryMap.get(result.taskIndex);
    }
}
