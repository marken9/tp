package seedu.duke;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.duke.calender.Calendar;
import seedu.duke.exception.DuplicateCategoryException;
import seedu.duke.exception.DuplicateTaskException;
import seedu.duke.exception.HighWorkloadException;
import seedu.duke.exception.IllegalDateException;
import seedu.duke.exception.UniTaskerException;
import seedu.duke.storage.Storage;
import seedu.duke.task.Deadline;
import seedu.duke.task.Event;
import seedu.duke.tasklist.CategoryList;

import static seedu.duke.tasklist.CategoryList.refreshCalendar;

import seedu.duke.coursestracker.CourseException;
import seedu.duke.coursestracker.CourseManager;
import seedu.duke.coursestracker.CourseParser;
import seedu.duke.ui.CategoryUi;
import seedu.duke.ui.DeadlineUi;
import seedu.duke.ui.ErrorUi;
import seedu.duke.ui.EventUi;
import seedu.duke.ui.GeneralUi;
import seedu.duke.ui.LimitUi;
import seedu.duke.ui.TaskUi;
import seedu.duke.util.DateUtils;
import seedu.duke.util.TaskValidator;


public class UniTasker {

    private static final Logger logger = Logger.getLogger(UniTasker.class.getName());

    private static CategoryList categories = new CategoryList();
    private static Calendar calendar = new Calendar();
    private static Storage storage = new Storage("todos.txt", "deadlines.txt", "events.txt");
    private static CourseManager courseManager;
    private static CourseParser courseParser;

    private static int dailyTaskLimit;
    private static int startYear;
    private static int endYear;

    private static final int DEFAULT_END_YEAR = 2030;
    private static final int DEFAULT_DAILY_TASK_LIMIT = 8;
    private static final DateTimeFormatter DATE_ONLY_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");


    public UniTasker() {
        try {
            storage.load(categories);
            refreshCalendar(categories, calendar);
            courseManager = new CourseManager("courses.txt");
            courseParser = new CourseParser(courseManager);
        } catch (Exception e) {
            ErrorUi.printError(e.getMessage());
        }
    }

    public static void handleMark(String[] sentence, boolean isMark) {
        if (sentence.length < 4) {
            ErrorUi.printUnknownCommand("mark/unmark", "todo, deadline or event");
            return;
        }
        try {
            String secondCommand = sentence[1];
            switch (secondCommand) {
            case "todo":
                try {
                    int categoryIndex = getCategoryIndex(sentence);
                    int taskIndex = Integer.parseInt(sentence[3]) - 1;
                    if (isMark) {
                        categories.markTodo(categoryIndex, taskIndex);
                    } else {
                        categories.unmarkTodo(categoryIndex, taskIndex);
                    }
                    TaskUi.printMarkTodoResult(isMark, null);
                } catch (Exception e) {
                    TaskUi.printMarkTodoResult(isMark, e.getMessage());
                }
                break;
            case "deadline":
                try {
                    int categoryIndex = getCategoryIndex(sentence);
                    int taskIndex = Integer.parseInt(sentence[3]) - 1;
                    categories.setDeadlineStatus(categoryIndex, taskIndex, isMark);
                    TaskUi.printStatusChanged(categories.getDeadline(categoryIndex, taskIndex), isMark);
                } catch (Exception e) {
                    ErrorUi.printError(e.getMessage());
                }
                break;
            case "event":
                try {
                    int categoryIndex = getCategoryIndex(sentence);
                    int taskIndex = Integer.parseInt(sentence[3]) - 1;
                    categories.setEventStatus(categoryIndex, taskIndex, isMark);
                    TaskUi.printStatusChanged(categories.getEvent(categoryIndex, taskIndex), isMark);
                } catch (Exception e) {
                    ErrorUi.printError(e.getMessage());
                }
                break;
            default:
                ErrorUi.printUnknownCommand("mark/unmark", "todo, deadline or event");
                break;
            }
            saveData();
        } catch (Exception e) {
            ErrorUi.printMarkTaskError();
        }
    }

    public static void handleDelete(String[] sentence) {
        if (sentence.length < 2) {
            ErrorUi.printMissingArgs("Use: delete [type] [index]");
            return;
        }


        try {
            String secondCommand = sentence[1];

            // Don't use getCategory if doing delete marked
            int categoryIndex = -1;
            if (!secondCommand.equals("marked") && !secondCommand.equals("category")) {
                categoryIndex = getCategoryIndex(sentence);
            }
            switch (secondCommand) {
            case "marked":
                categories.deleteMarkedTasks();
                CategoryUi.printAllMarkedDeleted();
                break;
            case "category":
                int deleteIndex = Integer.parseInt(sentence[2]) - 1;
                String catName = categories.getCategory(deleteIndex).getName();
                categories.deleteCategory(deleteIndex);
                CategoryUi.printCategoryDeleted(catName);
                break;
            case "todo":
                int todoIndex = Integer.parseInt(sentence[3]) - 1;
                String todoName = categories.getCategory(categoryIndex).
                        getTodo(todoIndex).getDescription();
                categories.deleteTodo(categoryIndex, todoIndex);
                TaskUi.printTaskAction("Deleted", "todo", todoName);
                break;
            case "deadline":
                if (sentence[3].equalsIgnoreCase("all")) {
                    categories.deleteAllDeadlines(categoryIndex);
                    DeadlineUi.printItemDeleted("deadline", null, categoryIndex);
                } else {
                    int deadlineIndex = Integer.parseInt(sentence[3]) - 1;
                    categories.deleteDeadline(categoryIndex, deadlineIndex);
                    DeadlineUi.printItemDeleted("deadline", deadlineIndex, categoryIndex);
                }
                break;
            case "event":
                if (sentence[3].equalsIgnoreCase("all")) {
                    categories.deleteAllEvents(categoryIndex);
                    DeadlineUi.printItemDeleted("event", null, categoryIndex);
                } else {
                    int eventIndex = Integer.parseInt(sentence[3]) - 1;
                    categories.deleteEvent(categoryIndex, eventIndex);
                    DeadlineUi.printItemDeleted("event", eventIndex, categoryIndex);
                }
                break;
            case "recurring":
                int groupIndex = Integer.parseInt(sentence[3]);
                Event eventToDelete = categories.findRecurringEventToDelete(categoryIndex, groupIndex);
                if (eventToDelete == null) {
                    throw new UniTaskerException("Choose a positive integer " +
                            "that represents the group number that belongs to the category");
                }
                categories.deleteRecurringEvent(categoryIndex, groupIndex);
                EventUi.printRecurringEventDeleted(eventToDelete);

                break;
            default:
                ErrorUi.printUnknownCommand("delete",
                        "category/todo/deadline/event [index] or " +
                                "delete recurring [category index] [group number]");
                break;
            }
            saveData();
            refreshCalendar(categories, calendar);
        } catch (ArrayIndexOutOfBoundsException e) {
            ErrorUi.printMissingArgs("Example: delete todo 1 1");
        } catch (NumberFormatException e) {
            ErrorUi.printInvalidNumber();
        } catch (IndexOutOfBoundsException e) {
            ErrorUi.printIndexNotFound();
        } catch (Exception e) {
            ErrorUi.printError("An unexpected error occurred", e.getMessage());
        }
    }

    public static void handleAdd(String[] sentence) {
        if (sentence.length <= 1) {
            ErrorUi.printUnknownCommand("add", "category, todo, deadline or event");
            return;
        }
        String secondCommand = sentence[1];
        switch (secondCommand) {
        case "category":
            try {
                if (sentence.length <= 2) {
                    throw new UniTaskerException("Empty description.");
                }
                String[] nameArr = Arrays.copyOfRange(sentence, 2, sentence.length);
                String name = String.join(" ", nameArr).trim();
                TaskValidator.validateUniqueCategory(categories, name);
                categories.addCategory(name);
                CategoryUi.printCategoryAdded(name);
            } catch (Exception e) {
                ErrorUi.printCommandFailed("add category", e.getMessage(),
                        "add category [description]");
            }
            break;
        case "todo":
            try {
                int todoCatIdx = getCategoryIndex(sentence);
                if (sentence.length <= 3) {
                    throw new UniTaskerException("Empty description.");
                }
                String[] descriptionArr = Arrays.copyOfRange(sentence, 3, sentence.length);
                String description = String.join(" ", descriptionArr).trim();
                TaskValidator.validateUniqueTask(categories, todoCatIdx, description);
                categories.addTodo(todoCatIdx, description);
                TaskUi.printTaskAction("Added", "todo", description);
            } catch (Exception e) {
                ErrorUi.printCommandFailed("add todo", e.getMessage(),
                        "add todo [categoryIndex] [description]");
            }
            break;
        case "deadline":
            try {
                int deadlineCatIdx = getCategoryIndex(sentence);
                String raw = String.join(" ", Arrays.copyOfRange(sentence, 3, sentence.length));
                if (!raw.contains(" /by ")) {
                    ErrorUi.printMissingByKeyword();
                    break;
                }
                String[] parts = raw.split(" /by ");
                String description = parts[0].trim();
                String dateString = parts[1].trim();

                // Parse and validate (Handles 2026 limit and date-only fallback)
                LocalDateTime by = Deadline.parseDateTime(dateString);
                TaskValidator.validateWorkload(categories, by, dailyTaskLimit);
                TaskValidator.validateUniqueTask(categories, deadlineCatIdx, description);
                Deadline newDeadline = categories.addDeadline(deadlineCatIdx, description, by);
                refreshCalendar(categories, calendar);
                if (newDeadline != null) {
                    DeadlineUi.printDeadlineAdded(categories.getCategory(deadlineCatIdx).getName(), newDeadline,
                            categories.getCategory(deadlineCatIdx).getDeadlineList().getSize());
                }
            } catch (IllegalDateException e) {
                ErrorUi.printError(e.getMessage());
            } catch (DuplicateTaskException | DuplicateCategoryException | HighWorkloadException e) {
                GeneralUi.printWarning(e.getMessage());
            } catch (Exception e) {
                ErrorUi.printError("System Error", e.getMessage());
            }
            break;
        case "event":
            try {
                if (sentence.length < 9) {
                    throw new UniTaskerException("Missing or invalid info. " +
                            "Expected format: add event <categoryIndex> <description> " +
                            "/from <start date> <start time> /to <end date> <end time>");
                }

                String raw = String.join(" ", Arrays.copyOfRange(sentence, 3, sentence.length));
                if (raw.stripLeading().startsWith("/from")){
                    throw new UniTaskerException("Empty description! Include the event description");
                }

                if (!raw.contains(" /from ")) {
                    throw new UniTaskerException("Missing '/from' keyword! Use: /from dd-MM-yyyy HHmm");
                }
                if (!raw.contains(" /to ")) {
                    // This prevents the ArrayIndexOutOfBoundsException and gives a helpful message
                    throw new UniTaskerException("Missing '/to' keyword! " +
                            "Format: add event [index] [desc] /from [start] /to [end]");
                }

                String[] eventDetails = raw.split(" /from ");
                String[] eventTimeDetails = eventDetails[1].split(" /to ");

                LocalDateTime from = DateUtils.parseDateTime(eventTimeDetails[0]);
                LocalDateTime to = DateUtils.parseDateTime(eventTimeDetails[1]);

                int eventCategoryIndex = getCategoryIndex(sentence);

                if (!from.isBefore(to)) {
                    throw new UniTaskerException("Start date and time must be earlier than End date and time " +
                            "(e.g., add event 1 consultation /from 01-03-2026 1800 07-03-2026 1900)");
                }

                TaskValidator.validateWorkload(categories, from, dailyTaskLimit);
                TaskValidator.validateUniqueTask(categories, eventCategoryIndex, eventDetails[0]);
                TaskValidator.validateNoOverlap(categories.getCategory(eventCategoryIndex).getEventList(), from, to);
                categories.addEvent(eventCategoryIndex, eventDetails[0], from, to);

                Event newEvent = categories.getCategory(eventCategoryIndex).getLatestEvent();
                if (newEvent != null) {
                    calendar.registerTask(newEvent);
                }
                EventUi.printEventAdded(
                        categories.getLatestEvent(eventCategoryIndex),
                        categories.getCategory(eventCategoryIndex).getName(),
                        categories.getCategory(eventCategoryIndex).getEventList().getSize()
                );
            } catch (HighWorkloadException | DuplicateTaskException e) {
                GeneralUi.printWarning(e.getMessage());
            } catch (DateTimeParseException | ArrayIndexOutOfBoundsException e) {
                ErrorUi.printAddEventFormatError();
            } catch (Exception e) {
                ErrorUi.printError(e.getMessage());
            }
            break;
        case "recurring":
            try {
                int eventCategoryIndex = getCategoryIndex(sentence);
                if (sentence.length < 5 || !sentence[3].equals("weekly") || !sentence[4].equals("event")) {
                    throw new UniTaskerException("Missing or invalid info. " +
                            "Expected format: add recurring <categoryIndex> weekly event <description> " +
                            "/from <day> <time> /to <day> <time>");
                }
                String raw = String.join(" ", Arrays.copyOfRange(sentence, 5, sentence.length));

                if (raw.stripLeading().startsWith("/from")){
                    throw new UniTaskerException("Empty description! Include the event description");
                }
                if (!raw.contains(" /from ")) {
                    throw new UniTaskerException("Missing '/from'. " +
                            "Expected format: add recurring 1 weekly event CS2113 lecture " +
                            "/from Friday 1600 /to Friday 1800");
                }

                String[] eventDetails = raw.split(" /from ");
                if (!eventDetails[1].contains(" /to ")) {
                    throw new UniTaskerException("Missing '/to' or wrong format for '/to'." +
                            "Expected format: add recurring 1 weekly event CS2113 lecture " +
                            "/from Friday 1600 /to Friday 1800");
                }
                String[] eventTimeDetails = eventDetails[1].split(" /to ");

                String[] fromComponents = eventTimeDetails[0].split(" ");
                if (fromComponents.length != 2) {
                    throw new UniTaskerException("Missing start day or time after '/from'." +
                            " Expected: /from <day> <time> e.g. /from Friday 1600\n" +
                            "Ensure that the date format is EEEE HHmm" +
                            " where EEEE is 'Monday','Tuesday', 'Wednesday', 'Thursday','Friday','Saturday','Sunday'");
                }
                String fromDayOfWeek = fromComponents[0];
                String fromTime = fromComponents[1];

                String[] toComponents = getToComponents(eventTimeDetails);
                String toDayOfWeek = toComponents[0];
                String toTime = null;
                if (toComponents.length >= 2) {
                    toTime = toComponents[1];
                }

                DateUtils.parseRecurringDayFrom(fromDayOfWeek);
                DateUtils.parseRecurringDayTo(toDayOfWeek);

                if (!fromDayOfWeek.equals(toDayOfWeek)){
                    throw new UniTaskerException("Recurring events must start and end on the same day " +
                            "(e.g., add recurring 1 weekly event CS2113 lecture /from Friday 1600 /to Friday 1800)");
                }

                LocalDate today = LocalDate.now();
                LocalDateTime from = DateUtils.parseRecurringTimeFrom(today, fromDayOfWeek, fromTime);
                LocalDateTime to = DateUtils.parseRecurringTimeTo(today, toDayOfWeek, toTime);

                if (!from.isBefore(to)) {
                    throw new UniTaskerException("Start date and time must be earlier than End date and time " +
                            "(e.g., add recurring 1 weekly event CS2113 lecture /from Friday 1600 /to Friday 1800)");
                }

                if (sentence[sentence.length-2].equals("/month")){
                    try {
                        int month = Integer.parseInt(sentence[sentence.length-1]);
                        if (month <= 0) {
                            throw new UniTaskerException("Invalid number use a positive integer larger than 0");
                        }
                        LocalDateTime endDate = from.plusMonths(month);
                        if (endDate.getYear() > UniTasker.getEndYear()) {
                            throw new UniTaskerException("End date exceeds the allowed year limit of "
                                    + UniTasker.getEndYear());
                        }
                        categories.addRecurringWeeklyEvent(eventCategoryIndex, eventDetails[0],
                                from, to, calendar, null, month);
                    } catch (NumberFormatException e) {
                        throw new UniTaskerException("Invalid number use a positive integer larger than 0");
                    }

                } else if (sentence[sentence.length-2].equals("/date")) {
                    try {
                        LocalDateTime date = DateUtils.parse(sentence[sentence.length-1],false);
                        categories.addRecurringWeeklyEvent(eventCategoryIndex, eventDetails[0],
                                from, to, calendar,date,0);
                    } catch (IllegalDateException e) {
                        throw new UniTaskerException("Date is invalid, " +
                                "follow format /date dd-MM-yyyy and keep date within limit");
                    }
                } else {
                    LocalDateTime defaultEnd = from.plusMonths(1);
                    if (defaultEnd.getYear() > UniTasker.getEndYear()) {
                        throw new UniTaskerException("End date exceeds the allowed year limit of "
                                + UniTasker.getEndYear());
                    }
                    categories.addRecurringWeeklyEvent(eventCategoryIndex, eventDetails[0],
                            from, to, calendar,null,0);
                }
                EventUi.printRecurringEventAdded(categories.getLatestEvent(eventCategoryIndex));

            } catch (IllegalDateException e) {
                ErrorUi.printError(e.getMessage());
            } catch (UniTaskerException e) {
                ErrorUi.printError(e.getMessage());
            } catch (Exception e) {
                ErrorUi.printAddRecurringEventFormatError();
            }
            break;
        default:
            ErrorUi.printUnknownCommand("add", "category, todo, deadline or event");
            break;
        }

        saveData();
    }

    private static String[] getToComponents(String[] eventTimeDetails) throws UniTaskerException {
        String[] toComponents = eventTimeDetails[1].trim().split("\\s+", 3);
        if (toComponents.length < 2) {
            throw new UniTaskerException("Missing start day or time after '/to'." +
                    " Expected: /to <day> <time> e.g. /to Friday 1800\n" +
                    "Ensure that the date format is EEEE HHmm" +
                    " where EEEE is 'Monday','Tuesday', 'Wednesday', 'Thursday','Friday','Saturday','Sunday'");
        }
        return toComponents;
    }

    public static void handleReorder(String[] sentence) {
        if (sentence.length <= 1) {
            ErrorUi.printUnknownCommand("reorder", "category or todo");
            return;
        }
        String secondCommand = sentence[1];
        switch (secondCommand) {
        case "category":
            try {
                if (sentence.length <= 3) {
                    throw new UniTaskerException("Insufficient arguments.");
                }
                int fromCategoryIndex = Integer.parseInt(sentence[2]) - 1;
                int toCategoryIndex = Integer.parseInt(sentence[3]) - 1;
                categories.reorderCategory(fromCategoryIndex, toCategoryIndex);
                TaskUi.printReordered("Category",
                        categories.getCategory(toCategoryIndex).getName(), -1, toCategoryIndex + 1);
            } catch (UniTaskerException | NumberFormatException e) {
                ErrorUi.printCommandFailed("reorder category", e.getMessage(),
                        "reorder category [index1] [index2]");
            }
            break;
        case "todo":
            try {
                if (sentence.length <= 4) {
                    throw new UniTaskerException("Insufficient arguments.");
                }
                int categoryIndex = getCategoryIndex(sentence);
                int todoIndex1 = Integer.parseInt(sentence[3]) - 1;
                int todoIndex2 = Integer.parseInt(sentence[4]) - 1;
                categories.reorderTodo(categoryIndex, todoIndex1, todoIndex2);
                TaskUi.printReordered("Todo",
                        categories.getCategory(categoryIndex).getTodo(todoIndex2).getDescription(),
                        categoryIndex + 1, todoIndex2 + 1);
            } catch (UniTaskerException | NumberFormatException e) {
                ErrorUi.printCommandFailed("reorder todo", e.getMessage(),
                        "reorder todo [catIndex] [todoIndex1] [todoIndex2]");
            }
            break;
        default:
            break;
        }
        saveData();
    }

    public static void handlePriority(String[] sentence) {
        if (sentence.length <= 4) {
            ErrorUi.printMissingArgs("Insufficient arguments for priority command");
            return;
        }
        String secondCommand = sentence[1];
        switch (secondCommand) {
        case "todo":
            try {
                int categoryIndex = getCategoryIndex(sentence);
                int todoIndex = Integer.parseInt(sentence[3]) - 1;
                int priority = Integer.parseInt(sentence[4]);
                if (priority < 0 || priority > 5) {
                    throw new UniTaskerException("Out of priority range allowed (0-5)");
                }
                categories.setTodoPriority(categoryIndex, todoIndex, priority);
                TaskUi.printPrioritySet(
                        categories.getCategory(categoryIndex).getTodo(todoIndex).getDescription(), priority);
            } catch (Exception e) {
                ErrorUi.printCommandFailed("priority todo", e.getMessage(), null);
            }
            break;
        default:
            ErrorUi.printUnknownCommand("priority", "todo");
            break;
        }
        saveData();
    }

    public static void handleListCategory(String[] sentence) {
        int sentenceLength = sentence.length;
        switch (sentenceLength) {
        case 2:
            CategoryUi.printList(categories.toString());
            break;
        case 3:
            try {
                int catIndex = getCategoryIndex(sentence);
                CategoryUi.printList(categories.getCategory(catIndex).toString());
                break;
            } catch (Exception e) {
                ErrorUi.printCommandFailed("list category [index]", e.getMessage(), null);
            }
            break;
        default:
            ErrorUi.printMissingArgs("List command has too many arguments");
            break;
        }
    }

    public static void handleList(String[] sentence) {
        String secondCommand = sentence[1];
        switch (secondCommand) {
        case "category":
            handleListCategory(sentence);
            break;
        case "todo":
            CategoryUi.printList(categories.getAllTodos());
            break;
        case "deadline":
            CategoryUi.printList(categories.getAllDeadlines());
            break;
        case "event":
            GeneralUi.printWithBorder(null, categories.getAllEvents());
            break;
        case "range":
            try {
                LocalDate start = DateUtils.parseLocalDate(sentence[2]);
                LocalDate end = DateUtils.parseLocalDate(sentence[3]);

                if (start.getYear() < startYear || end.getYear() > endYear) {
                    ErrorUi.printRangeOutOfBounds(startYear, endYear);
                    break;
                }
                if (start.isAfter(end)) {
                    throw new IllegalArgumentException("Start date must be earlier than End date");
                }
                if (sentence.length > 4 && sentence[4].equalsIgnoreCase("/deadline")) {
                    calendar.displaySpecificTypeInRange(start, end, Deadline.class);
                } else if (sentence.length > 4 && sentence[4].equalsIgnoreCase("/event")) {
                    calendar.displaySpecificTypeInRange(start, end, Event.class);
                } else {
                    calendar.displayRange(start, end);
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
            break;
        case "recurring":
            GeneralUi.printWithBorder(null, categories.getAllRecurringEvents());
            break;
        case "limit":
            LimitUi.printCurrentLimits(startYear, endYear, dailyTaskLimit);
            break;
        default:
            ErrorUi.printUnknownCommand("list", "category, todo, deadline, event, range, recurring or limit");
            break;
        }

    }

    public static void handleSort(String[] sentence) {
        if (sentence.length <= 1) {
            ErrorUi.printUnknownCommand("sort", "todo");
            return;
        }
        String secondCommand = sentence[1];
        switch (secondCommand) {
        case "todo":
            try {
                if (sentence.length <= 2) {
                    throw new UniTaskerException("Insufficient arguments");
                }
                int categoryIndex = getCategoryIndex(sentence);
                categories.getCategory(categoryIndex).getTodoList().sortByPriority();
                TaskUi.printSortedByPriority(categoryIndex);
            } catch (Exception e) {
                ErrorUi.printCommandFailed("sort todo", e.getMessage(), "sort todo [catIndex]");
            }
            break;
        default:
            ErrorUi.printUnknownCommand("sort", "todo");
            break;
        }
        saveData();
    }

    public static void handleFind(String[] sentence) {
        if (sentence.length <= 1) {
            ErrorUi.printError("Find command failed: missing string input.");
            return;
        }
        String[] split = Arrays.copyOfRange(sentence, 1, sentence.length);
        String input = String.join(" ", split);
        TaskUi.printFindResults(categories.returnFoundTasks(input));
    }

    public static void handleCourse(String line) {
        try {
            String courseCommand = line.substring("course".length()).trim();
            String result = courseParser.parse(courseCommand);
            LimitUi.printCourseResult(result);
        } catch (CourseException e) {
            ErrorUi.printError(e.getMessage());
        } catch (Exception e) {
            ErrorUi.printError("Could not process course command.");
        }
    }

    public static void handleLimit(String[] sentence) {
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
                setDailyTaskLimit(newValue);
                break;
            case "year":
                if (newValue < startYear) {
                    ErrorUi.printLimitYearBeforeStart(startYear);
                    return;
                }
                setEndYear(newValue);
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

    public void run(boolean isTestMode) {
        logger.info("UniTasker session started.");

        GeneralUi.printWelcome(startYear, endYear, dailyTaskLimit, isTestMode);

        Scanner in = new Scanner(System.in);
        while (true) {
            if (!in.hasNextLine()) {  // Check if input is available
                break;
            }
            String line;
            line = in.nextLine();
            String[] sentence = line.split(" ");
            String commandWord = sentence[0];
            switch (commandWord) {
            case "exit":
                GeneralUi.printMessage("Exiting UniTasker.");
                return;
            case "add":
                handleAdd(sentence);
                break;
            case "delete":
                handleDelete(sentence);
                break;
            case "list":
                handleList(sentence);
                break;
            case "mark":
                handleMark(sentence, true);
                break;
            case "unmark":
                handleMark(sentence, false);
                break;
            case "reorder":
                handleReorder(sentence);
                break;
            case "priority":
                handlePriority(sentence);
                break;
            case "sort":
                handleSort(sentence);
                break;
            case "find":
                handleFind(sentence);
                break;
            case "course":
                handleCourse(line);
                break;
            case "limit":
                handleLimit(sentence);
                break;
            default:
                GeneralUi.printMessage("default echo: " + line);
                break;
            }
        }
        in.close();
    }

    private static void saveData() {
        try {
            if (categories != null && storage != null) {
                storage.save(categories);
            }
        } catch (java.io.IOException e) {
            ErrorUi.printFileSaveError();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Internal error during save", e);
        }
    }

    private static int getCategoryIndex(String[] sentence) throws IndexOutOfBoundsException, NumberFormatException {
        int categoryIndex = Integer.parseInt(sentence[2]) - 1;
        if (categoryIndex < 0 || categoryIndex >= categories.getAmount()) {
            // Throw a custom exception or return a sentinel value like -1
            throw new IndexOutOfBoundsException("Category " + sentence[2] + " does not exist.");
        }
        return categoryIndex;
    }

    public static void setDailyTaskLimit(int newLimit) {
        dailyTaskLimit = newLimit;
        LimitUi.printDailyTaskLimitUpdated(dailyTaskLimit);
    }

    public static void main(String[] args) {
        seedu.duke.logging.LogConfig.setup();
        logger.info("UniTasker is launching...");

        startYear = LocalDate.now().getYear();
        endYear = DEFAULT_END_YEAR;
        dailyTaskLimit = DEFAULT_DAILY_TASK_LIMIT;

        boolean isTestMode = args.length > 0 && args[0].equalsIgnoreCase("-test");
        new UniTasker().run(isTestMode);
    }

    public static int getEndYear() {
        return endYear;
    }

    public static void setStartYear(int year) {
        startYear = year;
    }

    public static void setEndYear(int year) {
        endYear = year;
    }

}
