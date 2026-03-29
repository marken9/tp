package seedu.duke.command;

import seedu.duke.appcontainer.AppContainer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

import seedu.duke.exception.DuplicateCategoryException;
import seedu.duke.exception.DuplicateTaskException;
import seedu.duke.exception.HighWorkloadException;
import seedu.duke.exception.IllegalDateException;
import seedu.duke.exception.UniTaskerException;
import seedu.duke.task.Deadline;
import seedu.duke.task.Event;
import seedu.duke.ui.CategoryUi;
import seedu.duke.ui.DeadlineUi;
import seedu.duke.ui.ErrorUi;
import seedu.duke.ui.EventUi;
import seedu.duke.ui.GeneralUi;
import seedu.duke.ui.TaskUi;
import seedu.duke.util.DateUtils;
import seedu.duke.util.TaskValidator;

import static seedu.duke.tasklist.CategoryList.refreshCalendar;

public class AddCommand implements Command {
    private final String[] sentence;

    public AddCommand(String[] sentence) {
        this.sentence = sentence;
    }

    @Override
    public void execute(AppContainer container) {
        if (sentence.length <= 1) {
            ErrorUi.printUnknownCommand("add", "category, todo, deadline or event");
            return;
        }

        String secondCommand = sentence[1];
        switch (secondCommand) {
        case "category":
            handleAddCategory(container);
            break;
        case "todo":
            handleAddTodo(container);
            break;
        case "deadline":
            handleAddDeadline(container);
            break;
        case "event":
            handleAddEvent(container);
            break;
        case "recurring":
            handleAddRecurring(container);
            break;
        default:
            ErrorUi.printUnknownCommand("add", "category, todo, deadline or event");
            break;
        }

        CommandSupport.saveData(container);
    }

    //@@author marken9
    private void handleAddCategory(AppContainer container) {
        try {
            if (sentence.length <= 2) {
                throw new UniTaskerException("Empty description.");
            }
            String[] nameArr = Arrays.copyOfRange(sentence, 2, sentence.length);
            String name = String.join(" ", nameArr).trim();

            TaskValidator.validateUniqueCategory(container.categories(), name);
            container.categories().addCategory(name);
            CategoryUi.printCategoryAdded(name);
        } catch (Exception e) {
            ErrorUi.printCommandFailed("add category", e.getMessage(),
                    "add category [description]");
        }
    }
    //@@author marken9
    private void handleAddTodo(AppContainer container) {
        try {
            int todoCatIdx = CommandSupport.getCategoryIndex(container, sentence);
            if (sentence.length <= 3) {
                throw new UniTaskerException("Empty description.");
            }
            String[] descriptionArr = Arrays.copyOfRange(sentence, 3, sentence.length);
            String description = String.join(" ", descriptionArr).trim();

            TaskValidator.validateUniqueTask(container.categories(), todoCatIdx, description);
            container.categories().addTodo(todoCatIdx, description);
            TaskUi.printTaskAction("Added", "todo", description);
        } catch (Exception e) {
            ErrorUi.printCommandFailed("add todo", e.getMessage(),
                    "add todo [categoryIndex] [description]");
        }
    }

    //@@author WenJunYu5984
    private void handleAddDeadline(AppContainer container) {
        try {
            int deadlineCatIdx = CommandSupport.getCategoryIndex(container, sentence);
            String raw = String.join(" ", Arrays.copyOfRange(sentence, 3, sentence.length));

            if (!raw.contains(" /by ")) {
                ErrorUi.printMissingByKeyword();
                return;
            }

            String[] parts = raw.split(" /by ");
            String description = parts[0].trim();
            String dateString = parts[1].trim();

            LocalDateTime by = Deadline.parseDateTime(dateString);
            TaskValidator.validateWorkload(
                    container.categories(), by, container.getDailyTaskLimit());
            TaskValidator.validateUniqueTask(
                    container.categories(), deadlineCatIdx, description);

            Deadline newDeadline = container.categories().addDeadline(deadlineCatIdx, description, by);
            refreshCalendar(container.categories(), container.calendar());

            if (newDeadline != null) {
                DeadlineUi.printDeadlineAdded(
                        container.categories().getCategory(deadlineCatIdx).getName(),
                        newDeadline,
                        container.categories().getCategory(deadlineCatIdx).getDeadlineList().getSize()
                );
            }
        } catch (IllegalDateException e) {
            ErrorUi.printError(e.getMessage());
        } catch (DuplicateTaskException | DuplicateCategoryException | HighWorkloadException e) {
            GeneralUi.printWarning(e.getMessage());
        } catch (Exception e) {
            ErrorUi.printError("System Error", e.getMessage());
        }
    }

    //@@author sushmiithaa
    private void handleAddEvent(AppContainer container) {
        try {
            if (sentence.length < 9) {
                throw new UniTaskerException("Missing or invalid info. "
                        + "Expected format: add event <categoryIndex> <description> "
                        + "/from <start date> <start time> /to <end date> <end time>");
            }

            String raw = String.join(" ", Arrays.copyOfRange(sentence, 3, sentence.length));

            if (raw.stripLeading().startsWith("/from")) {
                throw new UniTaskerException("Empty description! Include the event description");
            }

            if (!raw.contains(" /from ")) {
                throw new UniTaskerException("Missing '/from' keyword! Use: /from dd-MM-yyyy HHmm");
            }
            if (!raw.contains(" /to ")) {
                throw new UniTaskerException("Missing '/to' keyword! "
                        + "Format: add event [index] [desc] /from [start] /to [end]");
            }

            String[] eventDetails = raw.split(" /from ");
            String[] eventTimeDetails = eventDetails[1].split(" /to ");

            LocalDateTime from = DateUtils.parseDateTime(eventTimeDetails[0]);
            LocalDateTime to = DateUtils.parseDateTime(eventTimeDetails[1]);

            int eventCategoryIndex = CommandSupport.getCategoryIndex(container, sentence);

            if (!from.isBefore(to)) {
                throw new UniTaskerException("Start date and time must be earlier than End date and time "
                        + "(e.g., add event 1 consultation /from 01-03-2026 1800 /to 07-03-2026 1900)");
            }

            TaskValidator.validateWorkload(
                    container.categories(), from, container.getDailyTaskLimit());
            TaskValidator.validateUniqueTask(
                    container.categories(), eventCategoryIndex, eventDetails[0]);
            TaskValidator.validateNoOverlap(
                    container.categories().getCategory(eventCategoryIndex).getEventList(), from, to);

            container.categories().addEvent(eventCategoryIndex, eventDetails[0], from, to);

            Event newEvent = container.categories().getCategory(eventCategoryIndex).getLatestEvent();
            if (newEvent != null) {
                container.calendar().registerTask(newEvent);
            }

            EventUi.printEventAdded(
                    container.categories().getLatestEvent(eventCategoryIndex),
                    container.categories().getCategory(eventCategoryIndex).getName(),
                    container.categories().getCategory(eventCategoryIndex).getEventList().getSize()
            );
        } catch (HighWorkloadException | DuplicateTaskException e) {
            GeneralUi.printWarning(e.getMessage());
        } catch (DateTimeParseException | ArrayIndexOutOfBoundsException e) {
            ErrorUi.printAddEventFormatError();
        } catch (Exception e) {
            ErrorUi.printError(e.getMessage());
        }
    }
    //@@author sushmiithaa
    private void handleAddRecurring(AppContainer container) {
        try {
            int eventCategoryIndex = CommandSupport.getCategoryIndex(container, sentence);

            if (sentence.length < 5 || !sentence[3].equals("weekly") || !sentence[4].equals("event")) {
                throw new UniTaskerException("Missing or invalid info. "
                        + "Expected format: add recurring <categoryIndex> weekly event <description> "
                        + "/from <day> <time> /to <day> <time>");
            }

            String raw = String.join(" ", Arrays.copyOfRange(sentence, 5, sentence.length));

            if (raw.stripLeading().startsWith("/from")) {
                throw new UniTaskerException("Empty description! Include the event description");
            }
            if (!raw.contains(" /from ")) {
                throw new UniTaskerException("Missing '/from'. "
                        + "Expected format: add recurring 1 weekly event CS2113 lecture "
                        + "/from Friday 1600 /to Friday 1800");
            }

            String[] eventDetails = raw.split(" /from ");
            if (!(eventDetails[1].contains(" /to "))) {
                throw new UniTaskerException("Missing '/to' or wrong format for '/to'. "
                        + "Expected format: add recurring 1 weekly event CS2113 lecture "
                        + "/from Friday 1600 /to Friday 1800");
            }
            String[] eventTimeDetails = eventDetails[1].split(" /to ");

            String[] fromComponents = eventTimeDetails[0].split(" ");
            if (fromComponents.length != 2) {
                throw new UniTaskerException("Missing start day or time after '/from'. "
                        + "Expected: /from <day> <time> e.g. /from Friday 1600");
            }
            String fromDayOfWeek = fromComponents[0];
            String fromTime = fromComponents[1];

            String[] toComponents = getToComponents(eventTimeDetails);
            String toDayOfWeek = toComponents[0];
            String toTime = toComponents[1];

            DateUtils.parseRecurringDayFrom(fromDayOfWeek);
            DateUtils.parseRecurringDayTo(toDayOfWeek);

            if (!fromDayOfWeek.equals(toDayOfWeek)) {
                throw new UniTaskerException("Recurring events must start and end on the same day "
                        + "(e.g., add recurring 1 weekly event CS2113 lecture /from Friday 1600 /to Friday 1800)");
            }

            LocalDate today = LocalDate.now();
            LocalDateTime from = DateUtils.parseRecurringTimeFrom(today, fromDayOfWeek, fromTime);
            LocalDateTime to = DateUtils.parseRecurringTimeTo(today, toDayOfWeek, toTime);

            if (!from.isBefore(to)) {
                throw new UniTaskerException("Start date and time must be earlier than End date and time "
                        + "(e.g., add recurring 1 weekly event CS2113 lecture /from Friday 1600 /to Friday 1800)");
            }

            if (sentence[sentence.length - 2].equals("/month")) {
                try {
                    int month = Integer.parseInt(sentence[sentence.length - 1]);
                    if (month <= 0) {
                        throw new UniTaskerException("Invalid number use a positive integer larger than 0");
                    }
                    LocalDateTime endDate = from.plusMonths(month);
                    if (endDate.getYear() > container.getEndYear()) {
                        throw new UniTaskerException("End date exceeds the allowed year limit of "
                                + container.getEndYear());
                    }
                    container.categories().addRecurringWeeklyEvent(
                            eventCategoryIndex, eventDetails[0], from, to,
                            container.calendar(), null, month);
                } catch (NumberFormatException e) {
                    throw new UniTaskerException("Invalid number use a positive integer larger than 0");
                }
            } else if (sentence[sentence.length - 2].equals("/date")) {
                try {
                    LocalDateTime date = DateUtils.parse(sentence[sentence.length - 1], false);
                    container.categories().addRecurringWeeklyEvent(
                            eventCategoryIndex, eventDetails[0], from, to,
                            container.calendar(), date, 0);
                } catch (IllegalDateException e) {
                    throw new UniTaskerException("Date is invalid, "
                            + "follow format /date dd-MM-yyyy and keep date within limit");
                }
            } else {
                LocalDateTime defaultEnd = from.plusMonths(1);
                if (defaultEnd.getYear() > container.getEndYear()) {
                    throw new UniTaskerException("End date exceeds the allowed year limit of "
                            + container.getEndYear());
                }
                container.categories().addRecurringWeeklyEvent(
                        eventCategoryIndex, eventDetails[0], from, to,
                        container.calendar(), null, 0);
            }

            EventUi.printRecurringEventAdded(container.categories().getLatestEvent(eventCategoryIndex));
        } catch (IllegalDateException e) {
            ErrorUi.printError(e.getMessage());
        } catch (UniTaskerException e) {
            ErrorUi.printError(e.getMessage());
        } catch (Exception e) {
            ErrorUi.printAddRecurringEventFormatError();
        }
    }
    //@@author sushmiithaa
    private String[] getToComponents(String[] eventTimeDetails) throws UniTaskerException {
        String[] toComponents = eventTimeDetails[1].split(" ");
        if (toComponents.length < 2) {
            throw new UniTaskerException("Missing end day or time after '/to'. "
                    + "Expected: /to <day> <time> e.g. /to Friday 1800");
        }
        return toComponents;
    }
}
