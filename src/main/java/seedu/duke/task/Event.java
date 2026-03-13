package seedu.duke.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {

    protected LocalDateTime from;
    protected LocalDateTime to;

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getFrom(){
        return from;
    }

    public LocalDateTime getTo(){
        return to;
    }

    public String toString() {
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String fromFormatted = from.format(displayFormatter);
        String toFormatted = to.format(displayFormatter);
        return super.toString() + " (from: " + fromFormatted + " to: " + toFormatted + ")";
    }

    @Override
    public String toFileFormat() {
        DateTimeFormatter storageFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
        String fromFormatted = from.format(storageFormatter);
        String toFormatted = to.format(storageFormatter);
        return String.format("E | %d | %s | %s | %s", (isDone ? 1 : 0), description,
                fromFormatted, toFormatted);
    }
}
