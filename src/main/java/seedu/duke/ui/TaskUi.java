package seedu.duke.ui;

import seedu.duke.tasklist.CategoryList;

public class TaskUi {
    public static final int INDEX_LOWER_LIMIT = 0;

    public static void printTaskAction(String action, String type, String description) {
        GeneralUi.printBordered(action + " " + type + ": " + description);
    }

    public static void printMarkTodoResult(boolean isMark, String errorMessage) {
        String verb = isMark ? "mark" : "unmark";
        if (errorMessage == null) {
            GeneralUi.printBordered(verb + " todo successful");
        } else {
            GeneralUi.printBordered(verb + " todo failed: " + errorMessage);
        }
    }

    public static void printStatusChanged(String taskString, boolean isMark) {
        GeneralUi.printWithBorder(
                "This task is marked as " + (isMark ? "done:" : "not done:"),
                taskString);
    }

    public static void printFindResults(CategoryList foundTasks) {
        GeneralUi.printDottedLine();
        System.out.println("Matching tasks found: " + System.lineSeparator());
        System.out.print(foundTasks.toString());
        GeneralUi.printDottedLine();
    }

    public static void printPrioritySet(String description, int priority) {
        GeneralUi.printBordered("Priority of [" + description + "] set to " + priority);
    }

    public static void printSortedByPriority(int categoryIndex) {
        GeneralUi.printBordered("Todos in category " + (categoryIndex + 1) + " have been sorted by priority.");
    }

    public static void printReordered(String type, String name, int categoryIndex, int newIndex) {
        if (categoryIndex < INDEX_LOWER_LIMIT) {
            GeneralUi.printBordered(type + ": " + name + " moved to index " + newIndex);
        } else {
            GeneralUi.printBordered(type + ": " + name + " inside category " + categoryIndex
                    + " moved to index " + newIndex);
        }
    }
}
