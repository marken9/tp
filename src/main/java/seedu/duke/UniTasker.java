package seedu.duke;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.duke.calender.Calendar;
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
import seedu.duke.util.DateUtils;


public class UniTasker {

    public static final String DOTTED_LINE = "____________________________________________________________";

    private static final Logger logger = Logger.getLogger(UniTasker.class.getName());

    private static CategoryList categories = new CategoryList();
    private static Calendar calendar = new Calendar();
    private static Storage storage = new Storage("todos.txt", "deadlines.txt", "events.txt");
    private static CourseManager courseManager;
    private static CourseParser courseParser;

    public UniTasker() {
        try {
            storage.load(categories);
            refreshCalendar(categories, calendar);
            courseManager = new CourseManager("courses.txt");
            courseParser = new CourseParser(courseManager);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void handleMark(String[] sentence, boolean isMark) {
        if (sentence.length < 4) {
            System.out.println("Unknown mark/unmark command: try todo, deadline or event");
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
                        System.out.println("mark todo successful");
                    } else {
                        categories.unmarkTodo(categoryIndex, taskIndex);
                        System.out.println("unmark todo successful");
                    }
                } catch (Exception e) {
                    if (isMark) {
                        System.out.println("mark todo failed: " + e.getMessage());
                    } else {
                        System.out.println("unmark todo failed: " + e.getMessage());
                    }
                }
                break;
            case "deadline":
                try {
                    int categoryIndex = getCategoryIndex(sentence);
                    int taskIndex = Integer.parseInt(sentence[3]) - 1;
                    categories.setDeadlineStatus(categoryIndex, taskIndex, isMark);
                } catch (Exception e) {
                    //temp
                }
                break;
            case "event":
                try {
                    int categoryIndex = getCategoryIndex(sentence);
                    int taskIndex = Integer.parseInt(sentence[3]) - 1;
                    categories.setEventStatus(categoryIndex, taskIndex, isMark);
                    System.out.println(DOTTED_LINE);
                    if (isMark) {
                        System.out.println("This task is marked as done:");
                    } else {
                        System.out.println("This task is marked as not done:");
                    }
                    System.out.println(categories.getEvent(categoryIndex, taskIndex));
                    System.out.println(DOTTED_LINE);
                } catch (Exception e) {
                    //temp
                }
                break;
            default:
                System.out.println("Unknown mark/unmark command: Try todo, deadline or event");
                break;
            }
            saveData();
        } catch (Exception e) {
            System.out.println("Error: Could not mark task. Format: mark deadline [cat] [index]");

        }
    }

    public static void handleDelete(String[] sentence) {
        if (sentence.length < 2) {
            System.out.println("Error: Missing arguments. Use: delete [type] [index]");
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
                System.out.println("All marked tasks deleted.");
                break;
            case "category":
                int deleteIndex = Integer.parseInt(sentence[2]) - 1;
                String catName = categories.getCategory(deleteIndex).getName();
                categories.deleteCategory(deleteIndex);
                System.out.println("Deleted category: " + catName);
                break;
            case "todo":
                int todoIndex = Integer.parseInt(sentence[3]) - 1;
                String todoName = categories.getCategory(categoryIndex).
                        getTodo(todoIndex).getDescription();
                categories.deleteTodo(categoryIndex, todoIndex);
                System.out.println("Deleted todo: " + todoName);
                break;
            case "deadline":
                if (sentence[3].equalsIgnoreCase("all")) {
                    categories.deleteAllDeadlines(categoryIndex);
                    System.out.println("All deadlines in this category have been deleted.");
                } else {
                    int deadlineIndex = Integer.parseInt(sentence[3]) - 1;
                    categories.deleteDeadline(categoryIndex, deadlineIndex);
                    System.out.println("Deleted deadline " + (deadlineIndex + 1)
                            + " from category " + (categoryIndex + 1));
                }
                break;
            case "event":
                if (sentence[3].equalsIgnoreCase("all")) {
                    categories.deleteAllEvents(categoryIndex);
                    System.out.println("All events in this category have been deleted.");
                } else {
                    int eventIndex = Integer.parseInt(sentence[3]) - 1;
                    categories.deleteEvent(categoryIndex, eventIndex);
                    System.out.println("Deleted event " + (eventIndex + 1)
                            + " from category " + (categoryIndex + 1));
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
                System.out.println(DOTTED_LINE);
                System.out.println("This recurring event has been deleted:");
                System.out.println(eventToDelete.toStringRecurringList());
                System.out.println(DOTTED_LINE);
                break;
            default:
                System.out.println("Unknown delete command. Use: delete category/todo/deadline/event [index] or " +
                        "delete recurring [category index] [group number]");
                break;
            }
            saveData();
            refreshCalendar(categories, calendar);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error: Missing arguments. Example: delete todo 1 1");
        } catch (NumberFormatException e) {
            System.out.println("Error: Please provide a valid index number.");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Error: That index does not exist in the list.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    public static void handleAdd(String[] sentence) {
        if (sentence.length <= 1) {
            System.out.println("Unknown add command: Try category, todo, deadline or event");
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
                String name = String.join(" ", nameArr);
                categories.addCategory(name);
                System.out.println("Added category: " + name);
            } catch (Exception e) {
                System.out.println("add category failed: " + e.getMessage());
                System.out.println("Correct format: add category [description]");

            }
            break;
        case "todo":
            try {
                int todoCatIdx = getCategoryIndex(sentence);
                if (sentence.length <= 3) {
                    throw new UniTaskerException("Empty description.");
                }
                String[] descriptionArr = Arrays.copyOfRange(sentence, 3, sentence.length);
                String description = String.join(" ", descriptionArr);
                categories.addTodo(todoCatIdx, description);
                System.out.println("Added todo: " + description);

            } catch (Exception e) {
                System.out.println("add todo failed: " + e.getMessage());
                System.out.println("Correct format: add todo [categoryIndex] [description]");
            }
            break;
        case "deadline":
            try {
                int deadlineCatIdx = getCategoryIndex(sentence);
                String raw = String.join(" ", Arrays.copyOfRange(sentence, 3, sentence.length));
                if (!raw.contains(" /by ")) {
                    System.out.println("Error: Missing '/by' keyword. Example: add deadline 1 Homework /by 2026-01-01");
                    break;
                }
                String[] parts = raw.split(" /by ");
                if (parts.length < 2 || parts[1].trim().isEmpty()) {
                    System.out.println("Error: Please provide a date after '/by'.");
                    break;
                }

                // Parse and validate (Handles 2026 limit and date-only fallback)
                LocalDateTime by = Deadline.parseDateTime(parts[1]);
                Deadline newDeadline = categories.addDeadline(deadlineCatIdx, parts[0], by);
                refreshCalendar(categories, calendar);
                if (newDeadline != null) {
                    System.out.println(DOTTED_LINE);
                    System.out.println(" Got it. I've added this deadline to category: "
                            + categories.getCategory(deadlineCatIdx).getName());
                    System.out.println("   " + newDeadline);
                    int count = categories.getCategory(deadlineCatIdx).getDeadlineList().getSize();
                    System.out.println(" Now you have " + count + " deadlines in this category.");
                    System.out.println(DOTTED_LINE);
                }
            } catch (IllegalDateException e) {
                System.out.println("[WARNING] " + e.getMessage());
            } catch (DateTimeParseException e) {
                System.out.println("Error: Invalid date format.");
            } catch (Exception e) {
                System.out.println("System Error: " + e.getMessage());
            }
            break;
        case "event":
            try {
                int eventCategoryIndex = getCategoryIndex(sentence);
                String raw = String.join(" ", Arrays.copyOfRange(sentence, 3, sentence.length));

                String[] eventDetails = raw.split(" /from ");
                String[] eventTimeDetails = eventDetails[1].split(" /to ");

                LocalDateTime from = DateUtils.parseDateTime(eventTimeDetails[0]);
                LocalDateTime to = DateUtils.parseDateTime(eventTimeDetails[1]);

                if (!from.isBefore(to)) {
                    throw new UniTaskerException("Error: Start date and time must be earlier than End date and time " +
                            "(e.g., add event 1 consultation /from 2026-03-01 1800 2026-03-07 1900)");
                }
                categories.addEvent(eventCategoryIndex, eventDetails[0], from, to);

                Event newEvent = categories.getCategory(eventCategoryIndex).getLatestEvent();
                if (newEvent != null) {
                    calendar.registerTask(newEvent);
                }

                System.out.println(DOTTED_LINE);
                System.out.println("This event has been added:");
                System.out.println(categories.getLatestEvent(eventCategoryIndex).toString());
                System.out.println(DOTTED_LINE);

            } catch (DateTimeParseException | IllegalDateException
                     | IndexOutOfBoundsException | NumberFormatException e) {
                System.out.println("Error: Use format yyyy-MM-dd HHmm (e.g., 2026-03-11 1830) " +
                        "and follow this format: add event <categoryIndex> <description> " +
                        "/from <startDateTime> /to <endDateTime>");
            } catch (UniTaskerException e) {
                System.out.println("Error: Could not add event. Check your input format.");
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Error occurred");
            }
            break;
        case "recurring":
            try {
                int eventCategoryIndex = getCategoryIndex(sentence);
                if (sentence.length < 5 || !sentence[3].equals("weekly") || !sentence[4].equals("event")) {
                    throw new UniTaskerException("Error: Missing or invalid info. " +
                            "Expected format: add recurring <categoryIndex> weekly event <description> " +
                            "/from <day> <time> /to <day> <time>");
                }
                String raw = String.join(" ", Arrays.copyOfRange(sentence, 5, sentence.length));
                if (!raw.contains(" /from ") && !raw.startsWith("/from ")) {
                    throw new UniTaskerException("Error: Missing '/from'. " +
                            "Expected format: add recurring 1 weekly event CS2113 lecture " +
                            "/from Friday 1600 /to Friday 1800");
                }

                String[] eventDetails = raw.split(" /from ");
                if (!eventDetails[1].contains(" /to ")) {
                    throw new UniTaskerException("Error: Missing '/to'. " +
                            "Expected format: add recurring 1 weekly event CS2113 lecture " +
                            "/from Friday 1600 /to Friday 1800");
                }
                String[] eventTimeDetails = eventDetails[1].split(" /to ");

                String[] fromComponents = eventTimeDetails[0].split(" ");
                if (fromComponents.length < 2) {
                    throw new UniTaskerException("Error: Missing start day or time after '/from'." +
                            " Expected: /from <day> <time> e.g. /from Friday 1600\n" +
                            "Ensure that the date format is EEEE HHmm"
                            + "where EEEE is 'Monday','Tuesday', 'Wednesday', 'Thursday','Friday','Saturday','Sunday'");
                }
                String fromDayOfWeek = fromComponents[0];
                String fromTime = fromComponents[1];

                String[] toComponents = eventTimeDetails[1].split(" ");
                if (toComponents.length < 2) {
                    throw new UniTaskerException("Error: Missing start day or time after '/from'." +
                            " Expected: /to <day> <time> e.g. /tp Friday 1800\n" +
                            "Ensure that the date format is EEEE HHmm"
                            + "where EEEE is 'Monday','Tuesday', 'Wednesday', 'Thursday','Friday','Saturday','Sunday'");
                }
                String toDayOfWeek = toComponents[0];
                String toTime = toComponents[1];

                try {
                    DayOfWeek.valueOf(fromDayOfWeek.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new UniTaskerException("Error: Invalid start day '" + fromDayOfWeek + "'. " +
                            "Ensure that the date format is EEEE HHmm"
                            + "where EEEE is 'Monday','Tuesday', 'Wednesday', 'Thursday','Friday','Saturday','Sunday'");
                }
                try {
                    DayOfWeek.valueOf(toDayOfWeek.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new UniTaskerException("Error: Invalid end day '" + toDayOfWeek + "'. " +
                            "Ensure that the date format is EEEE HHmm"
                            + "where EEEE is 'Monday','Tuesday', 'Wednesday', 'Thursday','Friday','Saturday','Sunday'");
                }

                LocalDate today = LocalDate.now();

                LocalDate dateFrom;
                LocalDate dateTo;
                LocalDateTime from;
                LocalDateTime to;

                try {
                    dateFrom = today.with(TemporalAdjusters.nextOrSame(
                            DayOfWeek.valueOf(fromDayOfWeek.toUpperCase())));
                    from = LocalDateTime.of(dateFrom,
                            LocalTime.parse(fromTime, DateTimeFormatter.ofPattern("HHmm")));
                } catch (DateTimeParseException e) {
                    throw new UniTaskerException("Error: Invalid start time '" + fromTime + "'. " +
                            "Use 4-digit format e.g. 1600");
                }
                try {
                    dateTo = today.with(TemporalAdjusters.nextOrSame(
                            DayOfWeek.valueOf(toDayOfWeek.toUpperCase())));
                    to = LocalDateTime.of(dateTo, LocalTime.parse(
                            toTime, DateTimeFormatter.ofPattern("HHmm")));
                } catch (DateTimeParseException e) {
                    throw new UniTaskerException("Error: Invalid end time '" + toTime + "'. " +
                            "Use 4-digit format e.g. 1800");
                }
                if (!from.isBefore(to)) {
                    throw new UniTaskerException("Error: Start date and time must be earlier than End date and time " +
                            "(e.g., add recurring 1 weekly event CS2113 lecture /from Friday 1600 /to Friday 1800)");
                }

                categories.addRecurringWeeklyEvent(eventCategoryIndex, eventDetails[0], from, to, calendar);
                System.out.println(DOTTED_LINE);
                System.out.println("This recurring event has been added:");
                System.out.println(categories.getLatestEvent(eventCategoryIndex).toStringRecurring());
                System.out.println(DOTTED_LINE);

            } catch (UniTaskerException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Error: Could not add event. Check your input format. " +
                        "(e.g., add recurring 1 weekly event CS2113 lecture /from Friday 1600 /to Friday 1800)");
            }
            break;
        default:
            System.out.println("Unknown add command: Try category, todo, deadline or event");
            break;
        }

        saveData();
    }

    public static void handleReorder(String[] sentence) {
        if (sentence.length <= 1) {
            System.out.println("Unknown reorder command: try category or todo");
        }
        String secondCommand = sentence[1];
        switch (secondCommand) {
        case "category":
            try {
                if (sentence.length <= 3) {
                    throw new UniTaskerException("Insufficient arguments.");
                }
                int categoryIndex1 = Integer.parseInt(sentence[2]) - 1;
                int categoryIndex2 = Integer.parseInt(sentence[3]) - 1;
                categories.reorderCategory(categoryIndex1, categoryIndex2);
                System.out.println("Category: " + categories.getCategory(categoryIndex2).getName() +
                        " moved to index " + (categoryIndex2 + 1));
            } catch (UniTaskerException | NumberFormatException e) {
                System.out.println("reorder category failed: " + e.getMessage());
                System.out.println("Correct format: reorder category [index1] [index2]");
            }
            break;
        case "todo":
            try {
                if (sentence.length <= 4) {
                    throw new UniTaskerException("Insufficient arguments.");
                }
                int categoryIndex = Integer.parseInt(sentence[2]) - 1;
                int todoIndex1 = Integer.parseInt(sentence[3]) - 1;
                int todoIndex2 = Integer.parseInt(sentence[4]) - 1;
                categories.reorderTodo(categoryIndex, todoIndex1, todoIndex2);
                System.out.println("Todo: " +
                        categories.getCategory(categoryIndex).getTodo(todoIndex2).getDescription() +
                        " inside category " + (categoryIndex + 1) + " moved to index " + (todoIndex2 + 1));
            } catch (UniTaskerException | NumberFormatException e) {
                System.out.println("reorder todo failed: " + e.getMessage());
                System.out.println("Correct format: reorder todo [catIndex] [todoIndex1] [todoIndex2]");
            }
            break;
        default:
            break;
        }
        saveData();
    }

    public static void handlePriority(String[] sentence) {
        if (sentence.length <= 4) {
            System.out.println("Insufficient arguments for priority command");
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
                System.out.println("Priority of [" +
                        categories.getCategory(categoryIndex).getTodo(todoIndex).getDescription() +
                        "] set to " + priority);
            } catch (Exception e) {
                System.out.println("priority todo failed: " + e.getMessage());
            }
            break;
        default:
            System.out.println("Unknown priority command: try todo");
            break;
        }
        saveData();
    }

    public static void handleListCategory(String[] sentence) {
        int sentenceLength = sentence.length;
        int catIndex;
        switch (sentenceLength) {
        case 2:
            System.out.println(categories);
            break;
        case 3:
            catIndex = Integer.parseInt(sentence[2]);
            System.out.println(categories.getCategory(catIndex - 1));
            break;
        default:
            System.out.println("List command too many arguments");
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
            System.out.println(categories.getAllTodos());
            break;
        case "deadline":
            System.out.println(categories.getAllDeadlines());
            break;
        case "event":
            System.out.println(DOTTED_LINE);
            System.out.println(categories.getAllEvents());
            System.out.println(DOTTED_LINE);
            break;
        case "range":
            try {
                LocalDate start = DateUtils.parseLocalDate(sentence[2]);
                LocalDate end = DateUtils.parseLocalDate(sentence[3]);

                if (start.getYear() < 2026 || end.getYear() < 2026) {
                    System.out.println("Error: Range search is only available for 2026 onwards.");
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
                System.out.println("Error: Use date format yyyy-mm-dd (e.g., list range 2026-03-01 2026-03-07)");
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Error: Include start date and end date using the date format yyyy-mm-dd " +
                        "(e.g., list range 2026-03-01 2026-03-07)");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: Start date must be earlier than End date " +
                        "(e.g., list range 2026-03-01 2026-03-07)");
            } catch (IllegalDateException e) {
                System.out.println("Error: " + e.getMessage());
            }
            break;
        case "recurring":
            System.out.println(DOTTED_LINE);
            System.out.println(categories.getAllRecurringEvents());
            System.out.println(DOTTED_LINE);

            break;
        default:
            System.out.println("Unknown list command.");
            break;
        }

    }

    public static void handleSort(String[] sentence) {
        if (sentence.length <= 1) {
            System.out.println("Unknown sort command: try todo");
            return;
        }
        String secondCommand = sentence[1];
        switch (secondCommand) {
        case "todo":
            try {
                if (sentence.length <= 2) {
                    throw new UniTaskerException("Insufficient arguments");
                }
                int categoryIndex1 = getCategoryIndex(sentence);
                categories.getCategory(categoryIndex1).getTodoList().sortByPriority();
                System.out.println("Todos in category " + (categoryIndex1 + 1) + " have been sorted by priority.");
            } catch (Exception e) {
                System.out.println("sort todo failed: " + e.getMessage());
                System.out.println("Correct format: sort todo [catIndex]");

            }
            break;
        default:
            System.out.println("Unknown sort command: try todo or deadline");
            break;
        }
        saveData();
    }

    public static void handleFind(String[] sentence) {
        if (sentence.length <= 1) {
            System.out.println("Find command failed: missing string input.");
            return;
        }
        String[] split = Arrays.copyOfRange(sentence, 1, sentence.length);
        String input = String.join(" ", split);
        System.out.println("Matching tasks found: " + System.lineSeparator());
        System.out.println(categories.returnFoundTasks(input));
    }

    public static void handleCourse(String line) {
        try {
            String courseCommand = line.substring("course".length()).trim();
            String result = courseParser.parse(courseCommand);
            System.out.println(result);
        } catch (CourseException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: Could not process course command.");
        }
    }

    public void run() {
        logger.info("UniTasker session started.");
        System.out.println("Welcome to UniTasker");
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
                System.out.println("Exiting UniTasker.");
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
            default:
                System.out.println("default echo: " + line);
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
            System.out.println("Error: File write failed.");
        } catch (Exception e) {
            // This catches the NullPointerExceptions that are currently killing your JAR
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

    public static void main(String[] args) {
        seedu.duke.logging.LogConfig.setup();
        logger.info("UniTasker is launching...");
        new UniTasker().run();
    }

}
