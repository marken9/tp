package seedu.duke.command;

import seedu.duke.appcontainer.AppContainer;
import seedu.duke.exception.UniTaskerException;
import seedu.duke.ui.ErrorUi;
import seedu.duke.ui.TaskUi;

public class PriorityCommand implements Command {
    private final String[] sentence;

    public PriorityCommand(String[] sentence) {
        this.sentence = sentence;
    }

    @Override
    public void execute(AppContainer container) {
        if (sentence.length <= 4) {
            ErrorUi.printMissingArgs("Insufficient arguments for priority command");
            return;
        }

        String secondCommand = sentence[1];
        switch (secondCommand) {
        case "todo":
            try {
                int categoryIndex = CommandSupport.getCategoryIndex(container, sentence);
                int todoIndex = Integer.parseInt(sentence[3]) - 1;
                int priority = Integer.parseInt(sentence[4]);
                if (priority < 0 || priority > 5) {
                    throw new UniTaskerException("Out of priority range allowed (0-5)");
                }
                container.getCategories().setTodoPriority(categoryIndex, todoIndex, priority);
                TaskUi.printPrioritySet(
                        container.getCategories().getCategory(categoryIndex).getTodo(todoIndex).getDescription(),
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
