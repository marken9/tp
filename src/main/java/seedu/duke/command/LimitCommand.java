package seedu.duke.command;

import seedu.duke.appcontainer.AppContainer;
import seedu.duke.ui.ErrorUi;
import seedu.duke.ui.LimitUi;

public class LimitCommand implements Command {
    private final String[] sentence;

    public LimitCommand(String[] sentence) {
        this.sentence = sentence;
    }

    @Override
    public void execute(AppContainer container) {
        try {
            if (sentence.length < 3) {
                ErrorUi.printLimitFormatError();
                return;
            }

            String type = sentence[1].toLowerCase();
            int newValue = Integer.parseInt(sentence[2]);

            switch (type) {
            case "task":
                if (newValue < 1) {
                    ErrorUi.printLimitMinError();
                    return;
                }
                container.setDailyTaskLimit(newValue);
                LimitUi.printDailyTaskLimitUpdated(newValue);
                break;
            case "year":
                if (newValue < container.getStartYear()) {
                    ErrorUi.printLimitYearBeforeStart(container.getStartYear());
                    return;
                }
                container.setEndYear(newValue);
                LimitUi.printEndYearUpdated(newValue);
                break;
            default:
                ErrorUi.printUnknownLimitType();
                break;
            }
        } catch (NumberFormatException e) {
            ErrorUi.printInvalidNumber();
        }
    }
}