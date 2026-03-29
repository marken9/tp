package seedu.duke.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class Event extends Task implements Timed {
    private static final Logger logger = Logger.getLogger(Event.class.getName());

    private static final DateTimeFormatter STORAGE_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HHmm");

    protected LocalDateTime from;
    protected LocalDateTime to;
    protected boolean isRecurring;
    protected int recurringGroupId;

    public Event(String description, LocalDateTime from, LocalDateTime to, boolean isRecurring, int recurringGroupId) {
        super(description);
        assert from.isBefore(to) || from.isEqual(to) : "The start date time must be before the end date time";
        assert (recurringGroupId > 0 || recurringGroupId == -1) : "Recurring event must have a group ID > 0 " +
                "or if it is not recurring its group id must be -1";
        assert (from != null) : "Start date time cannot be null";
        assert (to != null) : "End date time cannot be null";

        this.from = from;
        this.to = to;
        this.isRecurring = isRecurring;
        this.recurringGroupId = recurringGroupId;
        logger.fine("Event created: " + description + " from " + from  +" to " + to
                + " recurring " + isRecurring + " recurringGroupId " + recurringGroupId);

    }

    public LocalDateTime getFrom(){
        return from;
    }

    public LocalDateTime getTo(){
        return to;
    }

    public int getRecurringGroupId(){
        return recurringGroupId;
    }

    public void setRecurringGroupId(int recurringGroupIndex){
        assert (recurringGroupIndex > 0): "Recurring event must have a group ID > 0";
        this.recurringGroupId = recurringGroupIndex;
    }

    public boolean getIsRecurring(){
        return isRecurring;
    }

    public String toString() {
        assert (from != null && to!=null): "There must be a start date time and end date time";
        assert (description != null && !description.isEmpty()): "There must be a description for the events";

        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HHmm");
        String fromFormatted = from.format(displayFormatter);
        String toFormatted = to.format(displayFormatter);
        return (isRecurring ? "[RE]" : "[E]") + super.toString()
                + " (from: " + fromFormatted + " to: " + toFormatted + ")";
    }

    public String toStringRecurring() {
        assert (from != null && to!=null): "There must be a start date time and end date time";
        assert (description != null && !description.isEmpty()): "There must be a description for the events";
        assert (isRecurring): "Event to be printed must be recurring";

        DateTimeFormatter displayFormatterTime = DateTimeFormatter.ofPattern("EEEE HHmm");
        String fromFormatted = from.format(displayFormatterTime);
        String toFormatted = to.format(displayFormatterTime);
        return "[RE]" + super.toString()
                + " (from: " + fromFormatted + " to: " + toFormatted + ")";
    }

    public String toStringRecurringList() {
        assert (from != null && to!=null): "There must be a start date time and end date time";
        assert (description != null && !description.isEmpty()): "There must be a description for the events";
        assert (isRecurring): "Event to be printed must be recurring";

        DateTimeFormatter displayFormatterTime = DateTimeFormatter.ofPattern("EEEE HHmm");
        String fromFormatted = from.format(displayFormatterTime);
        String toFormatted = to.format(displayFormatterTime);

        return "[RE]" + description
                + " (from: " + fromFormatted + " to: " + toFormatted + ")";
    }

    @Override
    public String toFileFormat() {
        assert (from != null && to!=null): "There must be a start date time and end date time";
        assert (description != null && !description.isEmpty()): "There must be a description for the events";

        String fromFormatted = from.format(STORAGE_FORMATTER);
        String toFormatted = to.format(STORAGE_FORMATTER);
        if (isRecurring) {
            return String.format("RE | %d | %s | %s | %s | %d", (isDone ? 1 : 0), description,
                    fromFormatted, toFormatted,recurringGroupId);
        }
        return String.format("E | %d | %s | %s | %s", (isDone ? 1 : 0), description,
                fromFormatted, toFormatted);
    }

    @Override
    public LocalDateTime getDate() {
        return from;
    }
}
