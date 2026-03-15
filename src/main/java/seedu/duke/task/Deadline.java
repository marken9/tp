package seedu.duke.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

import seedu.duke.exception.IllegalDateException;
import seedu.duke.util.DateUtils;

/**
 * Represents a deadline task.
 * Deadline task includes a description and a date and/or time by which
 * the task must be completed. It implements the {@link Timed} interface to
 * allow for chronological sorting and calendar integration.
 */
public class Deadline extends Task implements Timed {
    private static final Logger logger = Logger.getLogger(Deadline.class.getName());
    protected LocalDateTime by;

    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
        logger.fine("Deadline created: " + description + " by " + by);
    }

    public static LocalDateTime parseDateTime(String input) throws IllegalDateException {
        return DateUtils.parseDateTime(input);
    }

    /**
     * Gets the deadline date and time.
     */
    public LocalDateTime getBy() {
        return by;
    }

    @Override
    public String toFileFormat() {
        DateTimeFormatter storageFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
        return String.format("D | %d | %s | %s", (isDone ? 1 : 0), description, by.format(storageFormatter));
    }

    @Override
    public String toString() {
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
        return "[D]" + super.toString() + " (by: " + by.format(displayFormatter) + ")";
    }

    /**
     * Retrieves the date/time associated with this task for calendar synchronization.
     * This uses the {@link Timed} interface.
     *
     * @return The deadline (by) date and time.
     */
    @Override
    public LocalDateTime getDate() {
        return by; // This satisfies the Timed interface
    }
}
