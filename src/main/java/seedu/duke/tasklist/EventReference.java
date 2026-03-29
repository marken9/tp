package seedu.duke.tasklist;

import java.time.LocalDateTime;

public class EventReference {
    public final int categoryIndex;
    public final int eventIndex;
    public final LocalDateTime startTime;

    public EventReference(int categoryIndex, int eventIndex, LocalDateTime startTime) {
        this.categoryIndex = categoryIndex;
        this.eventIndex = eventIndex;
        this.startTime = startTime;
    }
}
