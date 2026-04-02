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
import seedu.duke.exception.OverlapEventException;

import seedu.duke.task.Deadline;
import seedu.duke.task.Event;

import seedu.duke.ui.CategoryUi;
import seedu.duke.ui.DeadlineUi;
import seedu.duke.ui.ErrorUi;
import seedu.duke.ui.EventUi;
import seedu.duke.ui.TaskUi;
import seedu.duke.util.DateUtils;
import seedu.duke.util.TaskValidator;

import static seedu.duke.tasklist.CategoryList.refreshCalendar;

public class AddCommand implements Command {
    public static final int ADD_MIN_LENGTH = 2;
    public static final int ADD_CATEGORY_MIN_LENGTH = 3;
    public static final int ADD_TODO_MIN_LENGTH = 4;
    public static final int ADD_EVENT_MIN_LENGTH = 9;
    public static final int ADD_RECURRING_EVENT_MIN_LENGTH = 5;
    public static final int MIN_LENGTH_OF_TOFROM_COMPONENTS = 2;

    public static final int INDEX_OF_ADD_TYPE = 1;
    public static final int INDEX_OF_DAY_EVENTS = 0;
    public static final int INDEX_OF_TIME_EVENTS = 1;
    public static final int INDEX_OF_CATEGORY_INFO = 2;
    public static final int INDEX_OF_TASK_INFO = 3;
    public static final int INDEX_OF_RECURRING_EVENT_INFO = 5;
    public static final int INDEX_OF_DEADLINE_EVENT_DESCRIPTION = 0;
    public static final int INDEX_OF_DEADLINE_EVENT_DATETIME = 1;
    public static final int INDEX_OF_WORD_WEEKLY = 3;
    public static final int INDEX_OF_WORD_EVENT = 4;

    private final String[] sentence;

    public AddCommand(String[] sentence) {
        this.sentence = sentence;
    }

    @Override
    public void execute(AppContainer container) {
        if (sentence.length < ADD_MIN_LENGTH) {
            ErrorUi.printUnknownCommand("add", "category, todo, deadline or event");
            return;
        }

        String secondCommand = sentence[INDEX_OF_ADD_TYPE];
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
            if (sentence.length < ADD_CATEGORY_MIN_LENGTH) {
                throw new UniTaskerException("Empty description.");
            }
            String[] nameArr = Arrays.copyOfRange(sentence, INDEX_OF_CATEGORY_INFO, sentence.length);
            String name = String.join(" ", nameArr).trim();

            TaskValidator.validateUniqueCategory(container.categories(), name);
            container.categories().addCategory(name);
            CategoryUi.printCategoryAdded(name);
        } catch (DuplicateCategoryException e) {
            ErrorUi.printCommandFailed("add category", e.getMessage(), null);
        } catch (Exception e) {
            ErrorUi.printCommandFailed("add category", e.getMessage(),
                    "add category [description]");
        }
    }
    //@@author marken9
    private void handleAddTodo(AppContainer container) {
        try {
            int todoCatIdx = CommandSupport.getCategoryIndex(container, sentence);

            if (sentence.length < ADD_TODO_MIN_LENGTH) {
                throw new UniTaskerException("Empty description.");
            }

            int priority = 0;
            int priorityFlagIndex = -1;

            // find optional /p
            for (int i = INDEX_OF_TASK_INFO; i < sentence.length; i++) {
                if (sentence[i].equals("/p")) {
                    priorityFlagIndex = i;
                    break;
                }
            }

            String description;

            if (priorityFlagIndex == -1) {
                // normal add todo
                String[] descriptionArr = Arrays.copyOfRange(sentence, INDEX_OF_TASK_INFO, sentence.length);
                description = String.join(" ", descriptionArr).trim();
            } else {
                // add todo with priority
                if (priorityFlagIndex == INDEX_OF_TASK_INFO) {
                    throw new UniTaskerException("Empty description.");
                }
                if (priorityFlagIndex == sentence.length - 1) {
                    throw new UniTaskerException("Missing priority after /p.");
                }

                String[] descriptionArr = Arrays.copyOfRange(sentence, INDEX_OF_TASK_INFO, priorityFlagIndex);
                description = String.join(" ", descriptionArr).trim();

                try {
                    priority = Integer.parseInt(sentence[priorityFlagIndex + 1]);
                } catch (NumberFormatException e) {
                    throw new UniTaskerException("Priority must be an integer.");
                }

                if (priority < 0 || priority > 5) {
                    throw new UniTaskerException("Priority must be between 0 and 5.");
                }

                if (priorityFlagIndex != sentence.length - 2) {
                    throw new UniTaskerException("Invalid format. Priority should be the last argument.");
                }
            }

            if (description.isEmpty()) {
                throw new UniTaskerException("Empty description.");
            }

            TaskValidator.validateUniqueTask(container.categories(), todoCatIdx, description);

            container.categories().addTodo(todoCatIdx, description);
            if (priorityFlagIndex != -1) {
                int newTodoIndex = container.categories()
                        .getCategory(todoCatIdx)
                        .getTodoList()
                        .getSize() - 1;
                container.categories().setTodoPriority(todoCatIdx, newTodoIndex, priority);
                TaskUi.printTaskAction("Added", "todo", description
                        + " with priority " + priority);
                return;
            }

            TaskUi.printTaskAction("Added", "todo", description);
        } catch (DuplicateTaskException e) {
            ErrorUi.printCommandFailed("add todo", e.getMessage(), null);
        } catch (Exception e) {
            ErrorUi.printCommandFailed("add todo", e.getMessage(),
                    "add todo [categoryIndex] [description] /p [priority]");
        }
    }

    //@@author WenJunYu5984
    private void handleAddDeadline(AppContainer container) {
        try {
            int deadlineCatIdx = CommandSupport.getCategoryIndex(container, sentence);
            String raw = String.join(" ", Arrays.copyOfRange(sentence, INDEX_OF_TASK_INFO, sentence.length));

            if (!raw.contains(" /by ")) {
                ErrorUi.printMissingByKeyword();
                return;
            }

            String[] parts = raw.split(" /by ");
            String description = parts[INDEX_OF_DEADLINE_EVENT_DESCRIPTION].trim();
            String dateString = parts[INDEX_OF_DEADLINE_EVENT_DATETIME].trim();

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
        } catch (IllegalDateException | DuplicateTaskException | DuplicateCategoryException | HighWorkloadException e) {
            ErrorUi.printError(e.getMessage());
        } catch (Exception e) {
            ErrorUi.printError("System Error", e.getMessage());
        }
    }

    //@@author sushmiithaa
    private void handleAddEvent(AppContainer container) {
        try {
            if (sentence.length < ADD_EVENT_MIN_LENGTH) {
                throw new UniTaskerException("Missing or invalid info. "
                        + "Expected format: add event <categoryIndex> <description> "
                        + "/from <start date> <start time> /to <end date> <end time>");
            }

            String raw = String.join(" ", Arrays.copyOfRange(sentence, INDEX_OF_TASK_INFO, sentence.length));


            if (raw.stripLeading().startsWith("/from")) {
                throw new UniTaskerException("Empty description! Include the event description");
            }

            if (!raw.matches("(?s).*\\S+.*\\s+/from\\s+\\S+.*\\s+/to\\s+\\S+.*")) {
                throw new UniTaskerException("Missing or invalid format. "
                        + "Expected: add event [index] [desc] /from dd-MM-yyyy HHmm /to dd-MM-yyyy HHmm");
            }

            String[] eventDetails = raw.split("\\s+/from\\s+");
            String[] eventTimeDetails = eventDetails[INDEX_OF_DEADLINE_EVENT_DATETIME].split("\\s+/to\\s+");

            LocalDateTime from = DateUtils.parseDateTime(eventTimeDetails[INDEX_OF_DAY_EVENTS]);
            LocalDateTime to = DateUtils.parseDateTime(eventTimeDetails[INDEX_OF_TIME_EVENTS]);

            int eventCategoryIndex = CommandSupport.getCategoryIndex(container, sentence);

            if (!from.isBefore(to)) {
                throw new UniTaskerException("Start date and time must be earlier than End date and time "
                        + "(e.g., add event 1 consultation /from 01-03-2026 1800 /to 07-03-2026 1900)");
            }
            String desc = eventDetails[INDEX_OF_DEADLINE_EVENT_DESCRIPTION];
            validateEvents(container, from, eventCategoryIndex, desc, to);
            container.categories().addEvent(eventCategoryIndex, desc, from, to);

            Event newEvent = container.categories().getCategory(eventCategoryIndex).getLatestEvent();
            if (newEvent != null) {
                container.calendar().registerTask(newEvent);
            }

            EventUi.printEventAdded(
                    container.categories().getLatestEvent(eventCategoryIndex),
                    container.categories().getCategory(eventCategoryIndex).getName(),
                    container.categories().getCategory(eventCategoryIndex).getEventList().getSize()
            );
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
            boolean isMissingInvalidInfo = sentence.length < ADD_RECURRING_EVENT_MIN_LENGTH
                    || !sentence[INDEX_OF_WORD_WEEKLY].equals("weekly")
                    || !sentence[INDEX_OF_WORD_EVENT].equals("event");
            if (isMissingInvalidInfo) {
                throw new UniTaskerException("Missing or invalid info. "
                        + "Expected format: add recurring <categoryIndex> weekly event <description> "
                        + "/from <day> <time> /to <day> <time>");
            }

            String raw = String.join(" ", Arrays.copyOfRange(sentence, INDEX_OF_RECURRING_EVENT_INFO, sentence.length));

            if (raw.stripLeading().startsWith("/from")) {
                throw new UniTaskerException("Empty description! Include the event description");
            }
            if (!raw.contains(" /from ")) {
                throw new UniTaskerException("Missing '/from'. "
                        + "Expected format: add recurring 1 weekly event CS2113 lecture "
                        + "/from Friday 1600 /to Friday 1800");
            }

            String[] eventDetails = raw.split(" /from ");
            if (!(eventDetails[INDEX_OF_DEADLINE_EVENT_DATETIME].contains(" /to "))) {
                throw new UniTaskerException("Missing '/to' or wrong format for '/to'. "
                        + "Expected format: add recurring 1 weekly event CS2113 lecture "
                        + "/from Friday 1600 /to Friday 1800");
            }
            String[] eventTimeDetails = eventDetails[INDEX_OF_DEADLINE_EVENT_DATETIME].split(" /to ");

            String[] fromComponents = getFromToComponents(eventTimeDetails,true);
            String fromDayOfWeek = fromComponents[INDEX_OF_DAY_EVENTS].trim();
            String fromTime = fromComponents[INDEX_OF_TIME_EVENTS].trim();

            String[] toComponents = getFromToComponents(eventTimeDetails,false);
            String toDayOfWeek = toComponents[INDEX_OF_DAY_EVENTS].trim();
            String toTime = toComponents[INDEX_OF_TIME_EVENTS].trim();

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

            LocalDateTime endDate = null;
            int months = 0;
            int indexOfLimitOption = sentence.length - 2;
            int indexOfMonthDateLimit = sentence.length - 1;
            if (sentence[indexOfLimitOption].equals("/month")) {
                try {
                    months = Integer.parseInt(sentence[indexOfMonthDateLimit]);
                    if (months <= 0) {
                        throw new UniTaskerException("Invalid number use a positive integer larger than 0");
                    }
                    endDate = from.plusMonths(months);
                } catch (NumberFormatException e) {
                    throw new UniTaskerException("Invalid month value");
                }
            } else if (sentence[indexOfLimitOption].equals("/date")) {
                try {
                    endDate = DateUtils.parse(sentence[indexOfMonthDateLimit], false);
                } catch (IllegalDateException e) {
                    throw new UniTaskerException("Date is invalid, "
                            + "follow format /date dd-MM-yyyy and keep date within limit");
                }
            } else {
                endDate = from.plusMonths(1);
            }
            if (endDate.getYear() > container.getEndYear()) {
                throw new UniTaskerException("End date exceeds the allowed year limit of "
                        + container.getEndYear());
            }
            String desc = eventDetails[INDEX_OF_DEADLINE_EVENT_DESCRIPTION];
            validateEvents(container, from, eventCategoryIndex, desc, to);
            container.categories().addRecurringWeeklyEvent(
                    eventCategoryIndex, desc, from, to,
                    container.calendar(), (months == 0 ? endDate : null), months);

            EventUi.printRecurringEventAdded(container.categories().getLatestEvent(eventCategoryIndex));
        } catch (IllegalDateException e) {
            ErrorUi.printError(e.getMessage());
        } catch (UniTaskerException e) {
            ErrorUi.printError(e.getMessage());
        } catch (HighWorkloadException | DuplicateTaskException e) {
            ErrorUi.printError(e.getMessage());
        } catch (Exception e) {
            ErrorUi.printAddRecurringEventFormatError();
        }
    }

    private static void validateEvents(AppContainer container, LocalDateTime from, int eventCategoryIndex,
        String desc, LocalDateTime to) throws OverlapEventException {
        TaskValidator.validateWorkload(
                container.categories(), from, container.getDailyTaskLimit());
        TaskValidator.validateUniqueTask(
                container.categories(), eventCategoryIndex, desc);
        TaskValidator.validateNoOverlap(
                container.categories().getCategory(eventCategoryIndex).getEventList(), from, to);
    }

    //@@author sushmiithaa
    private String[] getFromToComponents(String[] eventTimeDetails,boolean isFrom) throws UniTaskerException {
        String[] components = eventTimeDetails[(isFrom ? 0:1)].trim().split("\\s+");
        if (isFrom) {
            if (components.length != MIN_LENGTH_OF_TOFROM_COMPONENTS) {
                throw new UniTaskerException("Missing start day or time after '/from'. "
                        + "Expected: /from <day> <time> e.g. /from Friday 1600");
            }
        } else {
            if (components.length < MIN_LENGTH_OF_TOFROM_COMPONENTS) {
                throw new UniTaskerException("Missing end day or time after '/to'. "
                        + "Expected: /to <day> <time> e.g. /to Friday 1800");
            }
        }
        return components;
    }
}
