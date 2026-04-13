package seedu.duke.ui;

public class ErrorUi {

    public static void printError(String message) {
        GeneralUi.printBordered("Error: " + message);
    }

    public static void printError(String prefix, String message) {
        GeneralUi.printBordered(prefix + ": " + message);
    }

    public static void printUnknownCommand(String command, String options) {
        GeneralUi.printBordered("Unknown " + command + " command: try " + options);
    }

    public static void printCommandFailed(String action, String message, String format) {
        GeneralUi.printDottedLine();
        System.out.println(action + " failed: " + message);
        if (format != null) {
            System.out.println("Correct format: " + format);
        }
        GeneralUi.printDottedLine();
    }

    public static void printMissingArgs(String hint) {
        GeneralUi.printBordered("Error: Missing arguments. " + hint);
    }

    public static void printInvalidNumber() {
        GeneralUi.printBordered("Error: Please provide a valid number.");
    }

    public static void printIndexNotFound() {
        GeneralUi.printBordered("Error: That index does not exist in the list.");
    }

    public static void printMarkTaskError() {
        printError("Could not mark task. Format: mark deadline [cat] [index]");
    }


    public static void printMissingByKeyword() {
        printError("Missing '/by' keyword. Example: add deadline 1 Homework /by 01-01-2026");
    }

    public static void printMissingDescription() {
        printError("Missing task description.");
    }

    public static void printAddEventFormatError() {
        printError("Use format dd-MM-yyyy HHmm (e.g., 11-12-2026 1830) " +
                "and follow this format: add event <categoryIndex> <description> " +
                "/from <startDateTime> /to <endDateTime>");
    }

    public static void printAddRecurringEventFormatError() {
        printError("Could not add event. Check your input format. " +
                "(e.g., add recurring 1 weekly event CS2113 lecture /from Friday 1600 /to Friday 1800)");
    }

    public static void printRangeOutOfBounds(int startYear, int endYear) {
        printError("Range search must be within " + startYear + "-" + endYear);
    }

    public static void printRangeDateFormatError() {
        printError("Use date format dd-mm-yyyy (e.g., list range 01-05-2026 07-05-2026)");
    }

    public static void printRangeMissingDates() {
        printError("Include start date and end date using the date format dd-mm-yyyy " +
                "(e.g., list range 01-03-2026 07-03-2026)");
    }

    public static void printMissingDates() {
        printError("Missing date. Include date in command.");
    }

    public static void printRangeStartAfterEnd() {
        printError("Start date must be earlier than End date " +
                "(e.g., list range 01-11-2026 07-11-2026)");
    }

    public static void printLimitFormatError() {
        printError("Use format 'limit task [number]' or 'limit year [number]' to change the daily limit");
    }

    public static void printLimitMinError() {
        printError("Limit must be at least 1 task per day.");
    }

    public static void printLimitYearBeforeStart(int startYear) {
        printError("End year cannot be before the start year (" + startYear + ").");
    }

    public static void printUseDateOnly(){
        printError("list range only accepts dates, not times. " +
                "Use: list range dd-MM-yyyy dd-MM-yyyy");
    }


    public static void printUnknownLimitType() {
        printError("Unknown limit type. Use 'task' or 'year'.");
    }

    public static void printFileSaveError() {
        printError("File write failed.");
    }

    /**
     * Print a helpful message when user enters an unknown command.
     */
    public static void printUnknownCommandHint(String invalidCommand) {
        GeneralUi.printDottedLine();
        System.out.println("Unknown command: '" + invalidCommand + "'");
        System.out.println("\nAvailable commands:");
        System.out.println("  add, delete, list, mark, unmark, reorder, sort,");
        System.out.println("  priority, find, course, limit, help, exit");
        System.out.println("\nTry 'help' to see detailed command information.");
        String tip = "Try 'help <topic>' for help on a specific mode";
        String example = " (e.g., 'help task', 'help deadline')";
        System.out.println(tip + example);
        GeneralUi.printDottedLine();
    }
}
