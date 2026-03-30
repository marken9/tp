package seedu.duke.command;

import seedu.duke.appcontainer.AppContainer;
import seedu.duke.ui.ErrorUi;
import seedu.duke.ui.TaskUi;

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
            container.categories().setEventStatus(result.categoryIndex, result.taskIndex, isMark);
            TaskUi.printStatusChanged(container.categories()
                    .getEvent(result.categoryIndex, result.taskIndex).toString(), isMark);
        } catch (Exception e) {
            ErrorUi.printError(e.getMessage());
        }
    }
}
