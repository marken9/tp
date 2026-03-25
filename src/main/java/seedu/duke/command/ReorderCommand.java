package seedu.duke.command;

import seedu.duke.appcontainer.AppContainer;
import seedu.duke.exception.UniTaskerException;
import seedu.duke.ui.ErrorUi;
import seedu.duke.ui.TaskUi;

public class ReorderCommand implements Command {
    private final String[] sentence;

    public ReorderCommand(String[] sentence) {
        this.sentence = sentence;
    }

    @Override
    public void execute(AppContainer container) {
        if (sentence.length <= 1) {
            ErrorUi.printUnknownCommand("reorder", "category or todo");
            return;
        }

        String secondCommand = sentence[1];
        switch (secondCommand) {
        case "category":
            try {
                if (sentence.length <= 3) {
                    throw new UniTaskerException("Insufficient arguments.");
                }
                int fromCategoryIndex = Integer.parseInt(sentence[2]) - 1;
                int toCategoryIndex = Integer.parseInt(sentence[3]) - 1;
                container.getCategories().reorderCategory(fromCategoryIndex, toCategoryIndex);
                TaskUi.printReordered("Category",
                        container.getCategories().getCategory(toCategoryIndex).getName(), -1, toCategoryIndex + 1);
            } catch (UniTaskerException | NumberFormatException e) {
                ErrorUi.printCommandFailed("reorder category", e.getMessage(),
                        "reorder category [index1] [index2]");
            }
            break;
        case "todo":
            try {
                if (sentence.length <= 4) {
                    throw new UniTaskerException("Insufficient arguments.");
                }
                int categoryIndex = CommandSupport.getCategoryIndex(container, sentence);
                int todoIndex1 = Integer.parseInt(sentence[3]) - 1;
                int todoIndex2 = Integer.parseInt(sentence[4]) - 1;
                container.getCategories().reorderTodo(categoryIndex, todoIndex1, todoIndex2);
                TaskUi.printReordered("Todo",
                        container.getCategories().getCategory(categoryIndex).getTodo(todoIndex2).getDescription(),
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
