package seedu.duke;

import static seedu.duke.tasklist.CategoryList.refreshCalendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

import seedu.duke.calender.Calendar;
import seedu.duke.storage.Storage;
import seedu.duke.task.Deadline;
import seedu.duke.tasklist.CategoryList;

import seedu.duke.coursestracker.CourseException;
import seedu.duke.coursestracker.CourseManager;
import seedu.duke.coursestracker.CourseParser;

public class UniTasker {
    private static CategoryList categories = new CategoryList();
    private static Calendar calendar = new Calendar();
    private static Storage storage = new Storage("todos.txt", "deadlines.txt","events.txt");
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
        String secondCommand = sentence[1];
        int categoryIndex = getCategoryIndex(sentence);
        int taskIndex = Integer.parseInt(sentence[3]) - 1;
        switch (secondCommand) {
        case "todo":
            if (isMark) {
                categories.markTodo(categoryIndex, taskIndex);
            } else {
                categories.unmarkTodo(categoryIndex, taskIndex);
            }
            break;
        case "deadline":
            categories.setDeadlineStatus(categoryIndex, taskIndex, isMark);
            break;
        case "event":
            categories.setEventStatus(categoryIndex, taskIndex, isMark);
            if (isMark) {
                System.out.println("This task is marked as done:");
            } else {
                System.out.println("This task is marked as not done:");
            }
            System.out.println(categories.getEvent(categoryIndex,taskIndex));
            break;
        default:
            break;
        }
        saveData();
    }

    public static void handleDelete(String[] sentence) {
        try {
            String secondCommand = sentence[1];
            int categoryIndex = getCategoryIndex(sentence);
            switch (secondCommand) {
            case "category":
                int deleteIndex = Integer.parseInt(sentence[2]);
                categories.deleteCategory(deleteIndex - 1);
                break;
            case "todo":
                int todoIndex = Integer.parseInt(sentence[3]) - 1;
                categories.deleteTodo(categoryIndex, todoIndex);
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
            default:
                System.out.println("Unknown delete command. Use: delete category/todo/deadline [index]");
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
        String secondCommand = sentence[1];
        switch (secondCommand) {
        case "category":
            String[] nameArr = Arrays.copyOfRange(sentence, 2, sentence.length);
            String name = String.join(" ", nameArr);
            categories.addCategory(name);
            System.out.println("Added category: " + name);
            break;
        case "todo":
            int todoCatIdx = getCategoryIndex(sentence);
            String[] descriptionArr = Arrays.copyOfRange(sentence, 3, sentence.length);
            String description = String.join(" ", descriptionArr);
            categories.addTodo(todoCatIdx, description);
            break;
        case "deadline":
            try {
                int deadlineCatIdx = getCategoryIndex(sentence);
                String raw = String.join(" ", Arrays.copyOfRange(sentence, 3, sentence.length));
                String[] parts = raw.split(" /by ");

                // Parse and validate (Handles 2026 limit and date-only fallback)
                LocalDateTime by = Deadline.parseDateTime(parts[1]);
                categories.addDeadline(deadlineCatIdx, parts[0], by);
                Deadline newDeadline = categories.getCategory(deadlineCatIdx).getDeadlineList().getLatest();
                if (newDeadline != null) {
                    calendar.registerTask(newDeadline);
                }

                System.out.println("____________________________________________________________");
                System.out.println(" Got it. I've added this deadline to category: "
                        + categories.getCategory(deadlineCatIdx).getName());
                System.out.println("   " + newDeadline);
                int count = categories.getCategory(deadlineCatIdx).getDeadlineList().getSize();
                System.out.println(" Now you have " + count + " deadlines in this category.");
                System.out.println("____________________________________________________________");
                
            } catch (java.time.format.DateTimeParseException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error: Could not add deadline. Check your input format.");
            }
            break;
        case "event":
            try {
                int eventCategoryIndex = getCategoryIndex(sentence);
                String raw = String.join(" ", Arrays.copyOfRange(sentence, 3, sentence.length));
                String[] eventDetails = raw.split(" /from ");
                String[] eventTimeDetails = eventDetails[1].split(" /to ");

                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
                java.time.LocalDateTime from = java.time.LocalDateTime.parse(eventTimeDetails[0], inputFormatter);
                java.time.LocalDateTime to = java.time.LocalDateTime.parse(eventTimeDetails[1], inputFormatter);

                categories.addEvent(eventCategoryIndex, eventDetails[0], from,to);
                System.out.println("This event has been added:");
                System.out.println(categories.getLatestEvent(eventCategoryIndex));
            } catch (java.time.format.DateTimeParseException e) {
                System.out.println("Error: Use format yyyy-MM-dd HHmm (e.g., 2026-03-11 1830)");
            } catch (Exception e) {
                System.out.println("Error: Could not add event. Check your input format.");
            }
            break;
        default:
            break;
        }

        saveData();
    }

    public static void handleReorder(String[] sentence) {
        String secondCommand = sentence[1];
        switch (secondCommand) {
        case "category":
            int categoryIndex1 = getCategoryIndex(sentence);
            int categoryIndex2 = Integer.parseInt(sentence[3]) - 1;
            categories.reorderCategory(categoryIndex1, categoryIndex2);
            break;
        case "todo":
            int categoryIndex = getCategoryIndex(sentence);
            int todoIndex1 = Integer.parseInt(sentence[3]) - 1;
            int todoIndex2 = Integer.parseInt(sentence[4]) - 1;
            categories.reorderTodo(categoryIndex, todoIndex1, todoIndex2);
            break;
        default:
            break;
        }
        saveData();
    }

    public static void handlePriority(String[] sentence) {
        String secondCommand = sentence[1];
        switch (secondCommand) {
        case "todo":
            int categoryIndex = getCategoryIndex(sentence);
            int todoIndex = Integer.parseInt(sentence[3]) - 1;
            int priority = Integer.parseInt(sentence[4]);
            categories.setTodoPriority(categoryIndex, todoIndex, priority);
            break;
        default:
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
            System.out.println(categories.getAllEvents());
            break;
        case "range":
            try {
                LocalDate start = LocalDate.parse(sentence[2]);
                LocalDate end = LocalDate.parse(sentence[3]);

                if (sentence.length > 4 && sentence[4].equalsIgnoreCase("/deadline")) {
                    calendar.displaySpecificTypeInRange(start, end, Deadline.class);
                } else {
                    calendar.displayRange(start, end);
                }
            } catch (Exception e) {
                System.out.println("Error: Use date format yyyy-mm-dd (e.g., list range 2026-03-01 2026-03-07)");
            }
            break;
        default:
            System.out.println("Unknown list command.");
            break;
        }

    }

    public static void handleSort(String[] sentence) {
        String secondCommand = sentence[1];
        int categoryIndex = getCategoryIndex(sentence);

        switch (secondCommand) {
        case "deadline":
            categories.sortDeadlines(categoryIndex);
            System.out.println("Deadlines in category " + (categoryIndex + 1) + " have been sorted.");
            break;
        case "todo":
            categories.getCategory(categoryIndex).getTodoList().sortByPriority();
            System.out.println("Todos in category " + (categoryIndex + 1) + " have been sorted by priority.");
            break;
        default:
            break;
        }
        saveData();
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
            storage.save(categories);
        } catch (java.io.IOException e) {
            System.out.println("Error: Could not save data to files.");
        }
    }

    private static int getCategoryIndex(String[] sentence) {
        int categoryIndex = Integer.parseInt(sentence[2]) - 1;
        return categoryIndex;
    }

    public static void main(String[] args) {
        new UniTasker().run();
    }
}
