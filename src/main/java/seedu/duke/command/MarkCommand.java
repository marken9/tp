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
import java.util.ArrayList;

public class MarkCommand implements Command {
    public static final int MARK_MIN_LENGTH = 4;

    public static final int INDEX_OF_MARK_TYPE = 1;
    public static final int INDEX_OF_FIRST_TASK_TO_MARK = 3;

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
        int categoryIndex;
        try {
            categoryIndex = CommandSupport.getCategoryIndex(container, sentence);
        } catch (Exception e) {
            TaskUi.printMarkTodoResult(isMark, e.getMessage());
            return;
        }

        ArrayList<String> invalidIndexes = new ArrayList<>();
        int successCount = 0;

        for (int i = INDEX_OF_FIRST_TASK_TO_MARK; i < sentence.length; i++) {
            try {
                int taskIndex = Integer.parseInt(sentence[i]) - 1;

                if (isMark) {
                    container.categories().markTodo(categoryIndex, taskIndex);
                } else {
                    container.categories().unmarkTodo(categoryIndex, taskIndex);
                }
                successCount++;
            } catch (Exception e) {
                invalidIndexes.add(sentence[i]);
            }
        }

        TaskUi.printBatchResult("todo", successCount, invalidIndexes, isMark);
    }

    //@@author WenJunYu5984
    private void handleDeadline(AppContainer container) {
        int categoryIndex;
        try {
            categoryIndex = CommandSupport.getCategoryIndex(container, sentence);
        } catch (Exception e) {
            ErrorUi.printError(e.getMessage());
            return;
        }

        ArrayList<String> invalidIndexes = new ArrayList<>();
        int successCount = 0;

        for (int i = INDEX_OF_FIRST_TASK_TO_MARK; i < sentence.length; i++) {
            try {
                int taskIndex = Integer.parseInt(sentence[i]) - 1;
                container.categories().setDeadlineStatus(categoryIndex, taskIndex, isMark);
                successCount++;
            } catch (Exception e) {
                invalidIndexes.add(sentence[i]);
            }
        }

        TaskUi.printBatchResult("deadline", successCount, invalidIndexes, isMark);


    }

    //@@author sushmiithaa
    private void handleEvent(AppContainer container) {
        int categoryIndex;
        try {
            categoryIndex = CommandSupport.getCategoryIndex(container, sentence);
            String[] validViews = {"EVENT", "EVENT_EXPANDED", "NORMAL_EVENT_ONLY"};
            validateView(container,validViews,"To mark/unmark a specific event please " +
                    "use 'list event' or 'list event /all' first. " +
                    "To mark/unmark a occurrence use 'list occurrence' then 'mark/unmark occurrence'");
        } catch (Exception e) {
            ErrorUi.printError(e.getMessage());
            return;
        }

        ArrayList<String> invalidIndexes = new ArrayList<>();
        int successCount = 0;
        for (int i = INDEX_OF_FIRST_TASK_TO_MARK; i < sentence.length; i++) {
            try {
                int uiIndex = Integer.parseInt(sentence[i]) - 1;
                EventReference ref = getEventReference(container, categoryIndex, uiIndex);
                Event event = container.categories().getEvent(ref.categoryIndex, ref.eventIndex);
                if (event.getIsRecurring() &&
                    (!container.categories().getCurrentView().equals("EVENT_EXPANDED"))) {
                    GeneralUi.printMessage((uiIndex + 1) + " is a recurring group. " +
                            "To mark/unmark the specific occurrence, please " +
                        "use 'list event /all' or 'list occurrence " +
                        (categoryIndex + 1) + " " + (uiIndex + 1) + "' first");
                } else {
                    successCount++;
                    setStatusAndPrintMessage(container, ref, event);
                }
            } catch (Exception e) {
                invalidIndexes.add(sentence[i]);
            }
        }
        TaskUi.printBatchResult("event", successCount, invalidIndexes, isMark);

    }


    private void handleOccurrence(AppContainer container) {
        int categoryIndex;
        try {
            categoryIndex = CommandSupport.getCategoryIndex(container, sentence);
            String[] validViews = {"OCCURRENCE_VIEW"};
            validateView(container,validViews,"To mark/unmark a specific event please " +
                    "use 'list event' or 'list event /all' first. " +
                    "To mark/unmark a occurrence use 'list occurrence' then 'mark/unmark occurrence'");
        } catch (Exception e) {
            ErrorUi.printError(e.getMessage());
            return;
        }
        ArrayList<String> invalidIndexes = new ArrayList<>();
        int successCount = 0;

        for (int i = INDEX_OF_FIRST_TASK_TO_MARK; i < sentence.length; i++) {
            try {
                int uiIndex = Integer.parseInt(sentence[i]) - 1;
                EventReference ref = getEventReference(container, categoryIndex,uiIndex);
                Event event = container.categories().getEvent(ref.categoryIndex, ref.eventIndex);
                setStatusAndPrintMessage(container, ref, event);
                successCount++;
            } catch (Exception e) {
                invalidIndexes.add(sentence[i]);
            }
        }

        TaskUi.printBatchResult("event", successCount, invalidIndexes, isMark);
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

    private static EventReference getEventReference(AppContainer container, int categoryIndex, int uiIndex) {
        Map<Integer, List<EventReference>> map = container.categories().getActiveDisplayMap();
        List<EventReference> categoryMap = map.get(categoryIndex);
        return categoryMap.get(uiIndex);
    }
}
