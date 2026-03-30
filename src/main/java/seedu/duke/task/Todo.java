package seedu.duke.task;

import java.util.logging.Logger;

public class Todo extends Task {
    public static final int MIN_PRIORITY = 0;
    
    private static final Logger logger = Logger.getLogger(Todo.class.getName());
    protected int priority;


    public Todo(String description) {
        super(description);
        this.priority = MIN_PRIORITY;
        logger.info("Created Todo: " + description);
    }

    public Todo(String description, int priority) {
        super(description);
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
        logger.info("Updated priority to " + priority + " for Todo: " + description);
    }

    private String drawPriority(int priority) {
        String result = "";
        for (int i = 0; i < priority; i += 1) {
            result += "*";
        }
        return result;
    }

    public String toString() {
        if (priority > MIN_PRIORITY) {
            return "[" + getStatusIcon() + "]" + "[" + drawPriority(getPriority()) + "] " + getDescription();
        }
        return "[" + getStatusIcon() + "] " + getDescription();
    }

    public String toFileFormat() {
        int statusAsNumber = 0;
        if (isDone) {
            statusAsNumber = 1;
        }
        return "T | " + statusAsNumber + " | " + getPriority() + " | " + description;
    }

}
