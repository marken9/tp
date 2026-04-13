package seedu.duke.command;

import seedu.duke.appcontainer.AppContainer;
import seedu.duke.ui.ErrorUi;
import seedu.duke.ui.GeneralUi;
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
    public static final int LIMIT_MIN_LENGTH = 3;
    public static final int INDEX_OF_TYPE = 1;
    public static final int INDEX_OF_NEWVALUE = 2;
    public static final int MAX_DAILY_TASK_LIMIT = 24;
    public static final int MAX_END_YEAR = 2100;

    private final String[] sentence;

    public LimitCommand(String[] sentence) {
        this.sentence = sentence;
    }

    @Override
    public void execute(AppContainer container) {
        try {
            if (sentence.length < LIMIT_MIN_LENGTH) {
                ErrorUi.printLimitFormatError();
                return;
            }

            String type = sentence[INDEX_OF_TYPE].toLowerCase();
            int newValue = Integer.parseInt(sentence[INDEX_OF_NEWVALUE]);

            switch (type) {
            case "task":
                if (newValue < 1) {
                    ErrorUi.printLimitMinError();
                    return;
                }
                if (newValue == container.getDailyTaskLimit()) {
                    GeneralUi.printMessage("Daily timed task limit is already set to " + newValue + ".");
                    return;
                }
                if (newValue > MAX_DAILY_TASK_LIMIT) {
                    ErrorUi.printError("Daily timed task limit cannot exceed " + MAX_DAILY_TASK_LIMIT + ".");
                    return;
                }
                container.setDailyTaskLimit(newValue);
                break;
            case "year":
                if (newValue < container.getStartYear()) {
                    ErrorUi.printLimitYearBeforeStart(container.getStartYear());
                    return;
                }
                if (newValue > MAX_END_YEAR) {
                    ErrorUi.printError("End year cannot exceed " + MAX_END_YEAR + ".");
                    return;
                }
                if (newValue == container.getEndYear()) {
                    GeneralUi.printMessage("End year is already set to " + newValue + ".");
                    return;
                }

                if (container.setEndYear(newValue)) {
                    LimitUi.printEndYearUpdated(newValue);
                }
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
