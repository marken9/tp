package seedu.duke.command;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import seedu.duke.appcontainer.AppContainer;
import seedu.duke.task.Deadline;
import seedu.duke.task.Event;
import seedu.duke.ui.CategoryUi;
import seedu.duke.ui.ErrorUi;
import seedu.duke.ui.GeneralUi;
import seedu.duke.ui.LimitUi;
import seedu.duke.util.DateUtils;
import seedu.duke.exception.IllegalDateException;

public class ListCommand implements Command {
    private final String[] sentence;

    public ListCommand(String[] sentence) {
        this.sentence = sentence;
    }

    @Override
    public void execute(AppContainer container) {
        if (sentence.length < 2) {
            ErrorUi.printUnknownCommand("list", "category, todo, deadline, event, range, recurring or limit");
            return;
        }

        String secondCommand = sentence[1];
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
            GeneralUi.printWithBorder(null, container.categories().getAllEvents());
            break;
        //@@author WenJunYu5984
        case "range":
            handleListRange(container);
            break;
        //@@author sushmiithaa
        case "recurring":
            GeneralUi.printWithBorder(null, container.categories().getAllRecurringEvents());
            break;
        //@@author WenJunYu5984
        case "limit":
            LimitUi.printCurrentLimits(container.getStartYear(), container.getEndYear(), container.getDailyTaskLimit());
            break;
        //@@author
        default:
            ErrorUi.printUnknownCommand("list", "category, todo, deadline, event, range, recurring or limit");
            break;
        }
    }

    //@@author marken9
    private void handleListCategory(AppContainer container) {
        int sentenceLength = sentence.length;
        switch (sentenceLength) {
        case 2:
            CategoryUi.printList(container.categories().toString());
            break;
        case 3:
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
            LocalDate start = DateUtils.parseLocalDate(sentence[2]);
            LocalDate end = DateUtils.parseLocalDate(sentence[3]);

            if (start.getYear() < container.getStartYear() || end.getYear() > container.getEndYear()) {
                ErrorUi.printRangeOutOfBounds(container.getStartYear(), container.getEndYear());
                return;
            }
            if (start.isAfter(end)) {
                throw new IllegalArgumentException("Start date must be earlier than End date");
            }

            if (sentence.length > 4 && sentence[4].equalsIgnoreCase("/deadline")) {
                container.calendar().displaySpecificTypeInRange(start, end, Deadline.class);
            } else if (sentence.length > 4 && sentence[4].equalsIgnoreCase("/event")) {
                container.calendar().displaySpecificTypeInRange(start, end, Event.class);
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
