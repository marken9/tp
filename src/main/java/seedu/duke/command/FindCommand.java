package seedu.duke.command;

import java.util.Arrays;

import seedu.duke.appcontainer.AppContainer;
import seedu.duke.ui.ErrorUi;
import seedu.duke.ui.GeneralUi;
import seedu.duke.ui.TaskUi;

public class FindCommand implements Command {
    public static final int FIND_MIN_LENGTH = 2;
    public static final int INDEX_OF_FIND_INFO = 1;

    private final String[] sentence;

    public FindCommand(String[] sentence) {
        this.sentence = sentence;
    }

    @Override
    public void execute(AppContainer container) {
        if (sentence.length < FIND_MIN_LENGTH) {
            ErrorUi.printError("Find command failed: missing string input.");
            return;
        }
        String[] split = Arrays.copyOfRange(sentence, INDEX_OF_FIND_INFO, sentence.length);
        String input = String.join(" ", split);
        String result = container.categories().returnFoundTasks(input);
        if (!result.isEmpty()) {
            TaskUi.printFindResults(result);
        } else {
            GeneralUi.printBordered("No matching tasks found.");
        }
    }
}
