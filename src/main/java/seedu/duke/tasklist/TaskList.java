package seedu.duke.tasklist;

import java.util.ArrayList;

import seedu.duke.task.Task;

public abstract class TaskList<T extends Task> {
    protected ArrayList<T> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public void add(T task) {
        tasks.add(task);
    }

    public T get(int index) {
        return tasks.get(index);
    }

    public T delete(int index) {
        return tasks.remove(index);
    }

    public void mark(int index) {
        tasks.get(index).mark();
    }

    public void unmark(int index) {
        tasks.get(index).unmark();
    }

    public int getSize() {
        return tasks.size();
    }
}

