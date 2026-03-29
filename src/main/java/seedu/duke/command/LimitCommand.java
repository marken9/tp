package seedu.duke.command;

import seedu.duke.appcontainer.AppContainer;
import seedu.duke.ui.ErrorUi;
import seedu.duke.ui.LimitUi;

/**
 * Command that updates a configurable application limit.
 *
 * <p>Handles two limit types:
 * task — sets the maximum number of daily tasks (minimum 1)</li>
 * year — sets the end year (must not precede the configured start year)</li>
 *
 * <p>Expected token layout: {@code limit <type> <value>}
 */

//@@author WenJunYu5984
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
