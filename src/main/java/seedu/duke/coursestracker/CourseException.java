package seedu.duke.coursestracker;

/**
 * Represents an exception specific to the Course Tracker feature.
 * Similar to DukeException/OrionException style custom exceptions.
 */
public class CourseException extends Exception {
    public CourseException(String message) {
        super(message);
    }
}

