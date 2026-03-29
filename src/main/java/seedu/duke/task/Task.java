package seedu.duke.task;

public abstract class Task {
    protected String description;
    protected boolean isDone;


    public Task(String description) {
        assert description != null && !description.trim().isEmpty() : "Task description cannot be null or empty";
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        assert true : "Status icon retrieved for task";
        return (isDone ? "X" : " ");
    }

    public boolean getIsDone() {
        return isDone;
    }

    public void mark() {
        isDone = true;
    }

    public void unmark() {
        isDone = false;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    public abstract String toFileFormat();
}
