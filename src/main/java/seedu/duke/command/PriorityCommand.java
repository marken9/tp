package seedu.duke.command;

import seedu.duke.appcontainer.AppContainer;
import seedu.duke.exception.UniTaskerException;
import seedu.duke.ui.ErrorUi;
import seedu.duke.ui.TaskUi;

//@@author marken9
public class PriorityCommand implements Command {
    public static final int PRIORITY_MIN_LENGTH = 5;
    public static final int INDEX_OF_TASKTYPE = 1;
    public static final int INDEX_OF_TODO = 3;
    public static final int INDEX_OF_PRIORITY = 4;
    public static final int PRIORITY_LEVEL_UPPER_LIMIT = 5;
    public static final int PRIORITY_LEVEL_LOWER_LIMIT = 0;

    private final String[] sentence;

    public PriorityCommand(String[] sentence) {
        this.sentence = sentence;
    }

    @Override
    public void execute(AppContainer container) {
        if (sentence.length < PRIORITY_MIN_LENGTH) {
            ErrorUi.printMissingArgs("Insufficient arguments for priority command");
            return;
        }

        String secondCommand = sentence[INDEX_OF_TASKTYPE];
        switch (secondCommand) {
        case "todo":
            try {
                int categoryIndex = CommandSupport.getCategoryIndex(container, sentence);
                int todoIndex = Integer.parseInt(sentence[INDEX_OF_TODO]) - 1;
                int priority = Integer.parseInt(sentence[INDEX_OF_PRIORITY]);
                if (priority < PRIORITY_LEVEL_LOWER_LIMIT || priority > PRIORITY_LEVEL_UPPER_LIMIT) {
                    throw new UniTaskerException("Out of priority range allowed (0-5)");
                }
                container.categories().setTodoPriority(categoryIndex, todoIndex, priority);
                TaskUi.printPrioritySet(
                        container.categories().getCategory(categoryIndex).getTodo(todoIndex).getDescription(),
                        priority);
            } catch (Exception e) {
                ErrorUi.printCommandFailed("priority todo", e.getMessage(), null);
            }
            break;
        default:
            ErrorUi.printUnknownCommand("priority", "todo");
            break;
        }

        CommandSupport.saveData(container);
    }
}
