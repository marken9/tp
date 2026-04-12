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

    public void clearAll() {
        tasks.clear();
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

    /**
     * Removes all tasks that are marked as done from the task list.
     *
     * <p>This method iterates through the task list and deletes any task
     * whose {@code isDone} status is {@code true}.</p>
     */
    public void deleteMarked() {
        tasks.removeIf(Task::getIsDone);
    }

    /**
     * Returns a list of tasks whose descriptions contain the given input substring.
     * The matching is case-insensitive.
     *
     * @param input The substring to search for within task descriptions.
     * @return An ArrayList containing all tasks that match the given input.
     */
    public ArrayList<T> findMatchesList(String input) {
        ArrayList<T> findMatchList = new ArrayList<T>();
        String lowerInput = input.toLowerCase();

        for (int i = 0; i < this.getSize(); i += 1) {
            T task = tasks.get(i);
            if (task.getDescription().toLowerCase().contains(lowerInput)) {
                findMatchList.add(task);
            }
        }
        return findMatchList;
    }


    /**
     * Returns {@code true} if any task in this list has a description
     * that matches {@code description}, ignoring case and leading/trailing whitespace.
     *
     * @param description the description to search for
     * @return {@code true} if a match is found, {@code false} otherwise
     */
    public boolean contains(String description) {
        for (T task : tasks) {
            if (task.getDescription().equalsIgnoreCase(description.trim())) {
                return true;
            }
        }
        return false;
    }
}

