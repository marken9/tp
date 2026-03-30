package seedu.duke.ui;

import seedu.duke.task.Task;

import java.util.List;
import java.util.Map;

public class GeneralUi {
    public static final String DOTTED_LINE = "______________________________________________________________________";
    public static final String EQUALSIGN_LINE = "============================================================";

    public static void printDottedLine() {
        System.out.println(DOTTED_LINE);
    }

    public static void printWithBorder(String label, String content) {
        printDottedLine();
        if (label != null) {
            System.out.println(label);
        }
        System.out.println(content);
        printDottedLine();
    }

    public static void printWelcome(int startYear, int endYear, int dailyLimit, boolean isTestMode) {
        System.out.println("Welcome to UniTasker - All-in-One Task & Course Management");
        System.out.println("\n4 FEATURE MODES:");
        System.out.println("  1. Task Management    - Manage todos with priority & organization");
        System.out.println("  2. Deadline Manager   - Track assignments with auto-sorted dates");
        System.out.println("  3. Event Manager      - Schedule events and recurring meetings");
        System.out.println("  4. Course Tracker     - Manage courses and assessment grades");
        System.out.println("\n" + DOTTED_LINE);
        if (!isTestMode) {
            System.out.println("Current Year Range: " + startYear + " to " + endYear);
            System.out.println("Current Daily Task Limit: " + dailyLimit);
            System.out.println("\nGET STARTED:");
            System.out.println("  Type 'help' to see all available commands");
            System.out.println("  Type 'help <mode>' for specific help (e.g., 'help task')");
            System.out.println("\nCHANGE SETTINGS:");
            System.out.println("  - Use 'limit task [number]' to update daily workload.");
            System.out.println("  - Use 'limit year [number]' to extend the calendar range.");
            System.out.println(DOTTED_LINE);
        }
    }

    public static void printBordered(String message) {
        printDottedLine();
        System.out.println(message);
        printDottedLine();
    }

    public static void printReminders(Map<String, List<Task>> reminders) {
        System.out.println("======================== REMINDERS =========================");
        if (reminders.isEmpty()) {
            System.out.println("No pending deadlines/events for today");
        }
        for (String cat : reminders.keySet()) {
            System.out.println("CATEGORY:" + cat);
            for (Task task : reminders.get(cat)) {
                System.out.println(task.toString());
            }
        }
        System.out.println(EQUALSIGN_LINE);
        printDottedLine();

    }

    public static void printWarning(String message) {
        System.out.println("[WARNING] " + message);
    }

    public static void printMessage(String message) {
        System.out.println(message);
    }
}
