package seedu.duke.ui;

import java.time.LocalDate;
import java.time.format.ResolverStyle;

public class LimitUi {

    public static void printDailyTaskLimitUpdated(int limit) {
        GeneralUi.printBordered("Daily timed task limit updated to: " + limit);
    }

    public static void printEndYearUpdated(int year) {
        GeneralUi.printBordered("Calendar end year updated to: " + year);
    }

    public static void printCurrentLimits(int startYear, int endYear, int dailyLimit) {
        GeneralUi.printDottedLine();
        System.out.println("Current Year Range: " + startYear + " to " + endYear);
        System.out.println("Current daily timed task limit: " + dailyLimit);
        GeneralUi.printDottedLine();
    }

    public static void printDailyTaskSummary(LocalDate date, int done, int undone) {
        GeneralUi.printDottedLine();
        System.out.println("Tasks on " + date.format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-uuuu")
                .withResolverStyle(ResolverStyle.STRICT))
                + ": " + undone + " incomplete, " + done + " completed.");
        GeneralUi.printDottedLine();
    }

    public static void printCourseResult(String result) {
        GeneralUi.printWithBorder(null, result);
    }
}
