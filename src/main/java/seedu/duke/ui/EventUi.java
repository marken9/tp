package seedu.duke.ui;

import seedu.duke.task.Event;

//@@author sushmiithaa
public class EventUi {

    public static void printEventAdded(Event event, String categoryName, int count) {
        GeneralUi.printDottedLine();
        System.out.println(" Got it. I've added this event to category: " + categoryName);
        System.out.println("   " + event);
        System.out.println(" Now you have " + count + " events in this category.");
        GeneralUi.printDottedLine();
    }

    public static void printRecurringEventAdded(Event event) {
        GeneralUi.printWithBorder("This recurring event has been added:", event.toStringRecurring());
    }

    public static void printRecurringEventDeleted(Event event) {
        GeneralUi.printWithBorder("This recurring event has been deleted:", event.toString());
    }

    public static void printNormalEventDeleted(Event event) {
        GeneralUi.printWithBorder("This event has been deleted:", event.toString());
    }
    public static void printRecurringEventDeletedGroup(Event event) {
        GeneralUi.printWithBorder("This recurring event has been deleted:", event.toStringRecurringList());
    }
}
