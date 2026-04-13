package seedu.duke;

import java.util.Stack;
import java.time.LocalDate;

import java.util.Scanner;
import java.util.logging.Logger;

import seedu.duke.calender.Calendar;
import seedu.duke.storage.Storage;
import seedu.duke.tasklist.CategoryList;

import static seedu.duke.storage.Storage.loadSettings;
import static seedu.duke.storage.Storage.saveSettings;
import static seedu.duke.tasklist.CategoryList.refreshCalendar;

import seedu.duke.course.CourseManager;
import seedu.duke.command.CourseParser;

import seedu.duke.ui.ErrorUi;
import seedu.duke.ui.GeneralUi;
import seedu.duke.ui.LimitUi;

import seedu.duke.command.Command;
import seedu.duke.command.CommandParser;
import seedu.duke.command.ExitCommand;

import seedu.duke.appcontainer.AppContainer;
import seedu.duke.util.DateUtils;

public class UniTasker {

    private static final Logger logger = Logger.getLogger(UniTasker.class.getName());

    private static CategoryList categories = new CategoryList();
    private static Calendar calendar = new Calendar();
    private static Storage storage = new Storage("todos.txt", "deadlines.txt", "events.txt");
    private static CourseParser courseParser;

    private static int dailyTaskLimit;
    private static int startYear;
    private static int endYear;

    private static final int DEFAULT_END_YEAR = 2030;
    private static final int DEFAULT_DAILY_TASK_LIMIT = 8;

    //keeps track of commands that can be undone
    private static final Stack<Command> commandHistory = new Stack<>();

    public UniTasker() {
        try {
            storage.load(categories);
            refreshCalendar(categories, calendar);
            CourseManager courseManager = new CourseManager("courses.txt");
            courseParser = new CourseParser(courseManager);
        } catch (Exception e) {
            ErrorUi.printError(e.getMessage());
        }
    }


    public void run(boolean isTestMode) {
        logger.info("UniTasker session started.");
        GeneralUi.printWelcome(startYear, endYear, dailyTaskLimit, isTestMode);

        AppContainer container = new AppContainer(
                categories, calendar, storage, courseParser
        );

        GeneralUi.printReminders(container.categories().findTasksForTheDay(LocalDate.now()));
        CommandParser parser = new CommandParser();
        Scanner in = new Scanner(System.in);

        while (true) {
            if (!in.hasNextLine()) {
                break;
            }

            String line = in.nextLine();
            Command command = parser.parse(line);

            if (command instanceof ExitCommand) {
                GeneralUi.printMessage("Exiting UniTasker.");
                return;
            }

            command.execute(container);
            //only push if command supports undo
            if (command.isUndoable()) {
                commandHistory.push(command);
            }
        }
        in.close();
    }

    public static void main(String[] args) {
        seedu.duke.logging.LogConfig.setup();
        logger.info("UniTasker is launching...");

        startYear = LocalDate.now().getYear();
        endYear = DEFAULT_END_YEAR;
        dailyTaskLimit = DEFAULT_DAILY_TASK_LIMIT;

        loadSettings();

        boolean isTestMode = args.length > 0 && args[0].equalsIgnoreCase("-test");
        new UniTasker().run(isTestMode);
    }

    //@@author WenJunYu5984
    public static int getEndYear() {
        return endYear;
    }

    public static void setStartYear(int year) {
        startYear = year;
    }

    public static boolean setEndYear(int year) {
        try {
            DateUtils.validateEndYearReduction(categories, year);
        } catch (IllegalArgumentException e) {
            ErrorUi.printError(e.getMessage());
            return false;
        }
        endYear = year;
        saveSettings();
        return true;
    }

    public static int getStartYear() {
        return startYear;
    }

    public static void setDailyTaskLimit(int newLimit) {
        dailyTaskLimit = newLimit;
        LimitUi.printDailyTaskLimitUpdated(dailyTaskLimit);
        saveSettings();
    }

    public static Stack<Command> getCommandHistory() {
        return commandHistory;
    }

    public static int getDailyTaskLimit() {
        return dailyTaskLimit;
    }
    //@@author

}
