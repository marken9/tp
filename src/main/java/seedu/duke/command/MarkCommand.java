package seedu.duke.command;

import seedu.duke.appcontainer.AppContainer;
import seedu.duke.ui.ErrorUi;
import seedu.duke.ui.TaskUi;

public class MarkCommand implements Command {
    private final String[] sentence;
    private final boolean isMark;

    public MarkCommand(String[] sentence, boolean isMark) {
        this.sentence = sentence;
        this.isMark = isMark;
    }

    @Override
    public void execute(AppContainer container) {
        if (sentence.length < 4) {
            ErrorUi.printUnknownCommand("mark/unmark", "todo, deadline or event");
            return;
        }

        try {
            String secondCommand = sentence[1];
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

    private void handleTodo(AppContainer container) {
        try {
            int categoryIndex = CommandSupport.getCategoryIndex(container, sentence);
            int taskIndex = Integer.parseInt(sentence[3]) - 1;
            if (isMark) {
                container.getCategories().markTodo(categoryIndex, taskIndex);
            } else {
                container.getCategories().unmarkTodo(categoryIndex, taskIndex);
            }
            TaskUi.printMarkTodoResult(isMark, null);
        } catch (Exception e) {
            TaskUi.printMarkTodoResult(isMark, e.getMessage());
        }
    }

    private void handleDeadline(AppContainer container) {
        try {
            int categoryIndex = CommandSupport.getCategoryIndex(container, sentence);
            int taskIndex = Integer.parseInt(sentence[3]) - 1;
            container.getCategories().setDeadlineStatus(categoryIndex, taskIndex, isMark);
            TaskUi.printStatusChanged(container.getCategories().getDeadline(categoryIndex, taskIndex), isMark);
        } catch (Exception e) {
            ErrorUi.printError(e.getMessage());
        }
    }

    private void handleEvent(AppContainer container) {
        try {
            int categoryIndex = CommandSupport.getCategoryIndex(container, sentence);
            int taskIndex = Integer.parseInt(sentence[3]) - 1;
            container.getCategories().setEventStatus(categoryIndex, taskIndex, isMark);
            TaskUi.printStatusChanged(container.getCategories().getEvent(categoryIndex, taskIndex), isMark);
        } catch (Exception e) {
            ErrorUi.printError(e.getMessage());
        }
    }
}