package seedu.duke.command;

import seedu.duke.appcontainer.AppContainer;
import seedu.duke.exception.UniTaskerException;
import seedu.duke.task.Event;
import seedu.duke.tasklist.EventReference;

import seedu.duke.ui.ErrorUi;
import seedu.duke.ui.GeneralUi;
import seedu.duke.ui.TaskUi;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Set;
import java.util.LinkedHashSet;

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

        List<LocalDate> relevantDates = new ArrayList<>();
        try {
            String secondCommand = sentence[INDEX_OF_MARK_TYPE];
            switch (secondCommand) {
            case "todo":
                handleTodo(container);
                break;
            case "deadline":
                relevantDates = handleDeadline(container);
                break;
            case "event":
                relevantDates = handleEvent(container);
                break;
            case "occurrence":
                relevantDates = handleOccurrence(container);
                break;
            default:
                ErrorUi.printUnknownCommand("mark/unmark", "todo, deadline or event");
                break;
            }
            CommandSupport.saveData(container, relevantDates);
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
        ArrayList<String> validDuplicateIndexes = new ArrayList<>();
        int successCount = 0;

        for (int i = INDEX_OF_FIRST_TASK_TO_MARK; i < sentence.length; i++) {
            try {
                int taskIndex = Integer.parseInt(sentence[i]) - 1;

                if (checkMarkedTodo(container, categoryIndex, taskIndex, isMark)) {
                    if (isMark) {
                        container.categories().markTodo(categoryIndex, taskIndex);
                    } else {
                        container.categories().unmarkTodo(categoryIndex, taskIndex);
                    }
                    successCount++;
                } else {
                    validDuplicateIndexes.add(sentence[i]);
                }
            } catch (Exception e) {
                invalidIndexes.add(sentence[i]);
            }
        }

        TaskUi.printBatchResult("todo", successCount, invalidIndexes, validDuplicateIndexes, isMark);
    }

    private boolean checkMarkedTodo(AppContainer container, int categoryIndex, int todoIndex, boolean isMark) {
        return (container.categories().getCategory(categoryIndex).getTodo(todoIndex).getIsDone() != isMark);
    }

    //@@author WenJunYu5984
    private List<LocalDate> handleDeadline(AppContainer container) {
        int categoryIndex;
        try {
            categoryIndex = CommandSupport.getCategoryIndex(container, sentence);
        } catch (Exception e) {
            ErrorUi.printError(e.getMessage());
            return new ArrayList<>();
        }

        ArrayList<String> invalidIndexes = new ArrayList<>();
        ArrayList<String> validDuplicateIndexes = new ArrayList<>();
        int successCount = 0;
        Set<LocalDate> dates = new LinkedHashSet<>();

        for (int i = INDEX_OF_FIRST_TASK_TO_MARK; i < sentence.length; i++) {
            try {
                int taskIndex = Integer.parseInt(sentence[i]) - 1;
                if (checkMarkedDeadline(container, categoryIndex, taskIndex, isMark)) {
                    container.categories().setDeadlineStatus(categoryIndex, taskIndex, isMark);
                    dates.add(container.categories().getCategory(categoryIndex)
                            .getDeadline(taskIndex).getBy().toLocalDate());
                    successCount++;
                } else {
                    validDuplicateIndexes.add(sentence[i]);
                }
            } catch (Exception e) {
                invalidIndexes.add(sentence[i]);
            }
        }

        TaskUi.printBatchResult("deadline", successCount, invalidIndexes, validDuplicateIndexes, isMark);
        return new ArrayList<>(dates);
    }

    private boolean checkMarkedDeadline(AppContainer container, int categoryIndex, int taskIndex, boolean isMark) {
        return (container.categories().getCategory(categoryIndex).getDeadline(taskIndex).getIsDone() != isMark);
    }

    //@@author sushmiithaa
    private List<LocalDate> handleEvent(AppContainer container) {
        int categoryIndex;
        try {
            categoryIndex = CommandSupport.getCategoryIndex(container, sentence);
            String[] validViews = {"EVENT", "EVENT_EXPANDED", "NORMAL_EVENT_ONLY"};
            validateView(container, validViews, "To mark/unmark a specific event please " +
                    "use 'list event' or 'list event /all' first. " +
                    "To mark/unmark a occurrence use 'list occurrence' then 'mark/unmark occurrence'");
        } catch (Exception e) {
            ErrorUi.printError(e.getMessage());
            return new ArrayList<>();
        }

        ArrayList<String> invalidIndexes = new ArrayList<>();
        ArrayList<String> validDuplicateIndexes = new ArrayList<>();
        int successCount = 0;
        Set<LocalDate> dates = new LinkedHashSet<>();
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
                    if (checkMarkedEvent(container, ref, isMark)) {
                        successCount++;
                        setStatusAndPrintMessage(container, ref, event);
                        dates.add(event.getFrom().toLocalDate());
                    } else {
                        validDuplicateIndexes.add(sentence[i]);
                    }
                }
            } catch (Exception e) {
                invalidIndexes.add(sentence[i]);
            }
        }
        TaskUi.printBatchResult("event", successCount, invalidIndexes, validDuplicateIndexes, isMark);
        return new ArrayList<>(dates);
    }

    private boolean checkMarkedEvent(AppContainer container, EventReference ref, boolean isMark) {
        return (container.categories().getCategory(ref.categoryIndex).getEvent(ref.eventIndex).getIsDone() != isMark);
    }

    private List<LocalDate> handleOccurrence(AppContainer container) {
        int categoryIndex;
        try {
            categoryIndex = CommandSupport.getCategoryIndex(container, sentence);
            String[] validViews = {"OCCURRENCE_VIEW"};
            validateView(container, validViews, "To mark/unmark a specific event please " +
                    "use 'list event' or 'list event /all' first. " +
                    "To mark/unmark a occurrence use 'list occurrence' then 'mark/unmark occurrence'");
        } catch (Exception e) {
            ErrorUi.printError(e.getMessage());
            return new ArrayList<>();
        }
        ArrayList<String> invalidIndexes = new ArrayList<>();
        ArrayList<String> validDuplicateIndexes = new ArrayList<>();
        int successCount = 0;
        Set<LocalDate> dates = new LinkedHashSet<>();

        for (int i = INDEX_OF_FIRST_TASK_TO_MARK; i < sentence.length; i++) {
            try {
                int uiIndex = Integer.parseInt(sentence[i]) - 1;
                EventReference ref = getEventReference(container, categoryIndex, uiIndex);
                Event event = container.categories().getEvent(ref.categoryIndex, ref.eventIndex);
                if (checkMarkedEvent(container, ref, isMark)) {
                    setStatusAndPrintMessage(container, ref, event);
                    dates.add(event.getFrom().toLocalDate());
                    successCount++;
                } else {
                    validDuplicateIndexes.add(sentence[i]);
                }
            } catch (Exception e) {
                invalidIndexes.add(sentence[i]);
            }
        }

        TaskUi.printBatchResult("event", successCount, invalidIndexes, validDuplicateIndexes, isMark);
        return new ArrayList<>(dates);
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
