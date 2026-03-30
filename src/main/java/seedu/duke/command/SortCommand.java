package seedu.duke.command;

import seedu.duke.appcontainer.AppContainer;
import seedu.duke.exception.UniTaskerException;
import seedu.duke.ui.ErrorUi;
import seedu.duke.ui.TaskUi;

//@@author marken9
public class SortCommand implements Command {
    public static final int SORT_MIN_LENGTH = 2;
    public static final int INDEX_OF_TASKTYPE = 1;
    public static final int SORT_TODO_MIN_LENGTH = 3;
    
    private final String[] sentence;

    public SortCommand(String[] sentence) {
        this.sentence = sentence;
    }

    @Override
    public void execute(AppContainer container) {
        if (sentence.length < SORT_MIN_LENGTH) {
            ErrorUi.printUnknownCommand("sort", "todo");
            return;
        }

        String secondCommand = sentence[INDEX_OF_TASKTYPE];
        switch (secondCommand) {
        case "todo":
            try {
                if (sentence.length < SORT_TODO_MIN_LENGTH) {
                    throw new UniTaskerException("Insufficient arguments");
                }
                int categoryIndex = CommandSupport.getCategoryIndex(container, sentence);
                container.categories().getCategory(categoryIndex).getTodoList().sortByPriority();
                TaskUi.printSortedByPriority(categoryIndex);
            } catch (Exception e) {
                ErrorUi.printCommandFailed("sort todo", e.getMessage(), "sort todo [catIndex]");
            }
            break;
        default:
            ErrorUi.printUnknownCommand("sort", "todo");
            break;
        }

        CommandSupport.saveData(container);
    }
}
