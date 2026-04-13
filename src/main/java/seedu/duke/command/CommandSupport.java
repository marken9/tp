package seedu.duke.command;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDate;

import seedu.duke.util.TaskValidator;
import seedu.duke.ui.LimitUi;

import seedu.duke.appcontainer.AppContainer;
import seedu.duke.ui.ErrorUi;


/**
 * Provides shared utility methods used across command classes.
 *
 * <p>This class is a stateless utility class and cannot be instantiated.
 * It centralizes common operations such as persisting application data
 * and resolving validated category indices from user input.
 */

//@@author WenJunYu5984
public final class CommandSupport {
    public static final int INDEX_OF_CATEGORY = 2;
    public static final int CATEGORY_INDEX_LOWER_LIMIT = 0;

    private static final Logger logger = Logger.getLogger(CommandSupport.class.getName());

    private CommandSupport() {
    }


    public static void saveData(AppContainer container) {
        saveDataWithDates(container, new ArrayList<>());
    }

    public static void saveData(AppContainer container, LocalDate date) {
        if (date != null) {
            saveDataWithDates(container, List.of(date));
        } else {
            saveDataWithDates(container, new ArrayList<>());
        }
    }

    public static void saveData(AppContainer container, List<LocalDate> dates) {
        saveDataWithDates(container, dates != null ? dates : new ArrayList<>());
    }

    /**
     * Saves the current application state to persistent storage and prints a
     * daily task summary for each provided date.
     *
     * <p>If either the category list or the storage component is {@code null},
     * the save step is skipped silently. If {@code dates} is empty, no summary
     * is printed.
     *
     * @param container the {@link AppContainer} holding the category list
     *                  and the storage component to write to
     * @param dates     the list of dates for which to print a done/undone
     *                  task summary; must not be {@code null}
     */
    private static void saveDataWithDates(AppContainer container, List<LocalDate> dates) {
        try {
            if (container.categories() != null && container.storage() != null) {
                container.storage().save(container.categories());
            }
            for (LocalDate date : dates) {
                int[] summary = TaskValidator.countDoneUndoneOnDate(container.categories(), date);
                LimitUi.printDailyTaskSummary(date, summary[0], summary[1]);
            }
        } catch (java.io.IOException e) {
            ErrorUi.printFileSaveError();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Internal error during save", e);
        }
    }

    /**
     * Parses and validates a 1-based category index from the command tokens.
     *
     * <p>Expects the index to be located at {@code sentence[2]}.
     * The value is converted to a 0-based index and checked against the
     * number of categories currently registered in {@code container}.
     *
     * @param container the {@link AppContainer} whose category list is used
     *                  for bounds checking
     * @param sentence  the tokenised command input; {@code sentence[2]} must
     *                  contain a parseable integer representing a 1-based
     *                  category position
     * @return the validated 0-based category index
     * @throws NumberFormatException     if {@code sentence[2]} is not a valid integer
     * @throws IndexOutOfBoundsException if the resolved index falls outside the
     *                                   range of existing categories
     */
    public static int getCategoryIndex(AppContainer container, String[] sentence) {
        int categoryIndex = Integer.parseInt(sentence[INDEX_OF_CATEGORY]) - 1;
        if (categoryIndex < CATEGORY_INDEX_LOWER_LIMIT || categoryIndex >= container.categories().getAmount()) {
            throw new IndexOutOfBoundsException("Category " + sentence[INDEX_OF_CATEGORY] + " does not exist.");
        }
        return categoryIndex;
    }
}
