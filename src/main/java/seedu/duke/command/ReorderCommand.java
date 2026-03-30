package seedu.duke.command;

import seedu.duke.appcontainer.AppContainer;
import seedu.duke.exception.UniTaskerException;
import seedu.duke.ui.ErrorUi;
import seedu.duke.ui.TaskUi;

//@@author marken9
public class ReorderCommand implements Command {
    public static final int REODER_MIN_LENGTH = 1;
    public static final int REODER_CATEGORY_MIN_LENGTH = 4;
    public static final int REODER_TODO_MIN_LENGTH = 5;

    public static final int INDEX_OF_TASKTYPE = 1;
    public static final int INDEX_OF_REORDER_CATEGORY_FROM = 2;
    public static final int INDEX_OF_REORDER_CATEGORY_TO = 3;
    public static final int INDEX_OF_REORDER_TODO1 = 3;
    public static final int INDEX_OF_REORDER_TODO2 = 4;

    private final String[] sentence;

    public ReorderCommand(String[] sentence) {
        this.sentence = sentence;
    }

    @Override
    public void execute(AppContainer container) {
        if (sentence.length <= REODER_MIN_LENGTH) {
            ErrorUi.printUnknownCommand("reorder", "category or todo");
            return;
        }

        String secondCommand = sentence[INDEX_OF_TASKTYPE];
        switch (secondCommand) {
        case "category":
            try {
                if (sentence.length < REODER_CATEGORY_MIN_LENGTH) {
                    throw new UniTaskerException("Insufficient arguments.");
                }
                int fromCategoryIndex = Integer.parseInt(sentence[INDEX_OF_REORDER_CATEGORY_FROM]) - 1;
                int toCategoryIndex = Integer.parseInt(sentence[INDEX_OF_REORDER_CATEGORY_TO]) - 1;
                container.categories().reorderCategory(fromCategoryIndex, toCategoryIndex);
                TaskUi.printReordered("Category",
                        container.categories().getCategory(toCategoryIndex).getName(), -1, toCategoryIndex + 1);
            } catch (UniTaskerException | NumberFormatException e) {
                ErrorUi.printCommandFailed("reorder category", e.getMessage(),
                        "reorder category [index1] [index2]");
            }
            break;
        case "todo":
            try {
                if (sentence.length < REODER_TODO_MIN_LENGTH) {
                    throw new UniTaskerException("Insufficient arguments.");
                }
                int categoryIndex = CommandSupport.getCategoryIndex(container, sentence);
                int todoIndex1 = Integer.parseInt(sentence[INDEX_OF_REORDER_TODO1]) - 1;
                int todoIndex2 = Integer.parseInt(sentence[INDEX_OF_REORDER_TODO2]) - 1;
                container.categories().reorderTodo(categoryIndex, todoIndex1, todoIndex2);
                TaskUi.printReordered("Todo",
                        container.categories().getCategory(categoryIndex).getTodo(todoIndex2).getDescription(),
                        categoryIndex + 1, todoIndex2 + 1);
            } catch (UniTaskerException | NumberFormatException e) {
                ErrorUi.printCommandFailed("reorder todo", e.getMessage(),
                        "reorder todo [catIndex] [todoIndex1] [todoIndex2]");
            }
            break;
        default:
            ErrorUi.printUnknownCommand("reorder", "category or todo");
            break;
        }

        CommandSupport.saveData(container);
    }
}
