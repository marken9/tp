package seedu.duke.task;

public class Todo extends Task {
    protected int priority;

    public Todo(String description) {
        super(description);
        this.priority = 0;
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
    }

    private String drawPriority(int priority) {
        String result = "";
        for (int i = 0; i < priority; i += 1) {
            result += "*";
        }
        return result;
    }

    public String toString() {
        if (priority > 0) {
            return "[" + getStatusIcon() + "]" + "[" + drawPriority(getPriority()) + "] " + getDescription();
        }
        return "[" + getStatusIcon() + "] " + getDescription();
    }

    public String toFileFormat() {
        int statusAsNumber = 0;
        if (isDone) {
            statusAsNumber = 1;
        }
        return "T | " + statusAsNumber + " | " + String.valueOf(getPriority()) + " | " + description;
    }

}
