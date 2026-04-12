package seedu.duke.ui;

import java.util.ArrayList;

import seedu.duke.task.Todo;

public class TaskUi {
    public static final int INDEX_LOWER_LIMIT = 0;

    public static void printTodoAdded(String categoryName, Todo todo, int count) {
        GeneralUi.printDottedLine();
        System.out.println(" Got it. I've added this todo to category: " + categoryName);
        System.out.println("   " + todo);
        System.out.println(" Now you have " + count + " todos in this category.");
        GeneralUi.printDottedLine();
    }

    public static void printTodoDeleted(Todo todo) {
        GeneralUi.printWithBorder("This todo has been deleted:", todo.toString());
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

    public static void printFindResults(String foundTasks) {
        GeneralUi.printDottedLine();
        System.out.println("Matching tasks found: " + System.lineSeparator());
        System.out.print(foundTasks);
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

    //@@author marken9
    public static void printBatchResult(String taskType,
                                        int successCount,
                                        ArrayList<String> invalidIndexes,
                                        ArrayList<String> validDuplicateIndexes,
                                        boolean isMark) {
        GeneralUi.printDottedLine();
        String action = isMark ? "Marked" : "Unmarked";
        String actionLower = isMark ? "marked" : "unmarked";

        // Success message
        if (successCount > 0) {
            System.out.println(action + " " + successCount + " " + taskType + "(s) successfully.");
        }

        if (!invalidIndexes.isEmpty()) {
            System.out.println("Skipped invalid indexes: " + String.join(", ", invalidIndexes));
        }

        if (!validDuplicateIndexes.isEmpty()) {
            System.out.println("Skipped valid " + actionLower + " indexes: "
                    + String.join(", ", validDuplicateIndexes));
        }

        if (successCount == 0 && invalidIndexes.isEmpty() && validDuplicateIndexes.isEmpty()) {
            System.out.println("No " + taskType + " indexes were provided.");
        }

        if (successCount == 0 && (!invalidIndexes.isEmpty() || !validDuplicateIndexes.isEmpty())) {
            System.out.println("No " + taskType + "s were " + actionLower + ".");
        }
    }
}
