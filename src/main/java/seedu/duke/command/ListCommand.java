package seedu.duke.command;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import seedu.duke.appcontainer.AppContainer;
import seedu.duke.exception.UniTaskerException;
import seedu.duke.task.Deadline;
import seedu.duke.task.Event;
import seedu.duke.ui.CategoryUi;
import seedu.duke.ui.ErrorUi;
import seedu.duke.ui.GeneralUi;
import seedu.duke.ui.LimitUi;
import seedu.duke.util.DateUtils;
import seedu.duke.exception.IllegalDateException;

public class ListCommand implements Command {
    public static final int LIST_MIN_LENGTH = 2;
    public static final int LIST_WITH_INDEX_LENGTH = 3;
    public static final int LIST_RANGE_MIN_LENGTH = 4;

    public static final int INDEX_OF_LIST_TYPE = 1;
    public static final int INDEX_OF_LIST_RANGE_TASKTYPE = 4;
    public static final int INDEX_OF_LIST_RANGE_STARTTIME = 2;
    public static final int INDEX_OF_LIST_RANGE_ENDTIME = 3;
    public static final int INDEX_OF_ADDITIONAL_INFO = 2;


    private final String[] sentence;

    public ListCommand(String[] sentence) {
        this.sentence = sentence;
    }

    @Override
    public void execute(AppContainer container) {
        if (sentence.length < LIST_MIN_LENGTH) {
            ErrorUi.printUnknownCommand("list", "category, todo, deadline, event, range, " +
                    "recurring, occurrence or limit");
            return;
        }

        String secondCommand = sentence[INDEX_OF_LIST_TYPE];
        switch (secondCommand) {
        //@@author marken9
        case "category":
            handleListCategory(container);
            break;
        case "todo":
            CategoryUi.printList(container.categories().getAllTodos());
            break;
        //@@author WenJunYu5984
        case "deadline":
            CategoryUi.printList(container.categories().getAllDeadlines());
            break;
        //@@author sushmiithaa
        case "event":
            boolean showAll = (sentence.length > LIST_MIN_LENGTH
                    && sentence[INDEX_OF_ADDITIONAL_INFO].equalsIgnoreCase("/all"));
            boolean showNormalEventsOnly = (sentence.length > LIST_MIN_LENGTH
                    && sentence[INDEX_OF_ADDITIONAL_INFO].equalsIgnoreCase("/normal"));
            GeneralUi.printWithBorder(null, container.categories().getAllEvents(showAll, showNormalEventsOnly));
            break;
        //@@author WenJunYu5984
        case "range":
            handleListRange(container);
            break;
        //@@author sushmiithaa
        case "recurring":
            GeneralUi.printWithBorder(null, container.categories().getAllRecurringEvents());
            break;
        case "occurrence":
            try {
                int catIdx = CommandSupport.getCategoryIndex(container, sentence);
                String currentView = container.categories().getCurrentView();
                if (!currentView.equals("EVENT")) {
                    throw new UniTaskerException("Please use: list event first before list occurrence");
                }
                int recurringUiIdx = Integer.parseInt(sentence[LIST_WITH_INDEX_LENGTH]);
                String allRecurringEventsWithinGroup = container.categories()
                        .getOccurrencesOfRecurringEvent(catIdx, recurringUiIdx);
                GeneralUi.printWithBorder(null, allRecurringEventsWithinGroup);
            } catch (UniTaskerException e) {
                ErrorUi.printError(e.getMessage());
            } catch (Exception e) {
                ErrorUi.printError("Please use: list event first then list occurrence [cat_idx] [event_idx]");
            }
            break;
        //@@author WenJunYu5984
        case "limit":
            LimitUi.printCurrentLimits(container.getStartYear(), container.getEndYear(), container.getDailyTaskLimit());
            break;
        //@@author
        default:
            ErrorUi.printUnknownCommand("list", "category, todo, deadline, event, " +
                    "range, recurring, occurrence or limit");
            break;
        }
    }

    //@@author marken9
    private void handleListCategory(AppContainer container) {
        int sentenceLength = sentence.length;
        switch (sentenceLength) {
        case LIST_MIN_LENGTH:
            CategoryUi.printList(container.categories().toString());
            break;
        case LIST_WITH_INDEX_LENGTH:
            try {
                int catIndex = CommandSupport.getCategoryIndex(container, sentence);
                CategoryUi.printList(container.categories().getCategory(catIndex).toString());
            } catch (Exception e) {
                ErrorUi.printCommandFailed("list category [index]", e.getMessage(), null);
            }
            break;
        default:
            ErrorUi.printMissingArgs("List command has too many arguments");
            break;
        }
    }

    //@@author WenJunYu5984
    private void handleListRange(AppContainer container) {
        try {
            if (sentence.length < LIST_RANGE_MIN_LENGTH) {
                ErrorUi.printRangeMissingDates();
                return;
            }

            // reject if user provided times alongside dates
            if (sentence[INDEX_OF_LIST_RANGE_ENDTIME].matches("\\d{4}") ||
                    (sentence.length > LIST_RANGE_MIN_LENGTH &&
                            sentence[INDEX_OF_LIST_RANGE_TASKTYPE].matches("\\d{4}"))) {
                ErrorUi.printUseDateOnly();
                return;
            }

            LocalDate start = DateUtils.parseLocalDateNoValidation(sentence[INDEX_OF_LIST_RANGE_STARTTIME]);
            LocalDate end = DateUtils.parseLocalDateNoValidation(sentence[INDEX_OF_LIST_RANGE_ENDTIME]);

            if (start.isAfter(end)) {
                throw new IllegalArgumentException("Start date must be earlier than End date");
            }

            if (sentence.length > LIST_RANGE_MIN_LENGTH) {
                String taskType = sentence[INDEX_OF_LIST_RANGE_TASKTYPE];
                if (taskType.matches("\\d{4}")) {
                    ErrorUi.printUseDateOnly();
                    return;
                }
                if (sentence[INDEX_OF_LIST_RANGE_TASKTYPE].equalsIgnoreCase("/deadline")) {
                    container.calendar().displaySpecificTypeInRange(start, end, Deadline.class);
                } else if (sentence[INDEX_OF_LIST_RANGE_TASKTYPE].equalsIgnoreCase("/event")) {
                    container.calendar().displaySpecificTypeInRange(start, end, Event.class);
                } else {
                    ErrorUi.printError("Unknown flag '" + taskType + "'. " +
                            "Use: list range dd-MM-yyyy dd-MM-yyyy [/deadline | /event]");
                }
            } else {
                container.calendar().displayRange(start, end);
            }

        } catch (DateTimeParseException e) {
            ErrorUi.printRangeDateFormatError();
        } catch (ArrayIndexOutOfBoundsException e) {
            ErrorUi.printRangeMissingDates();
        } catch (IllegalArgumentException e) {
            ErrorUi.printRangeStartAfterEnd();
        } catch (IllegalDateException e) {
            ErrorUi.printError(e.getMessage());
        }
    }
    //@@author
}
