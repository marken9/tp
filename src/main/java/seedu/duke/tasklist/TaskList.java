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

    public void deleteMarked() {
        tasks.removeIf(Task::getIsDone);
    }

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


    public boolean contains(String description) {
        for (T task : tasks) {
            if (task.getDescription().equalsIgnoreCase(description.trim())) {
                return true;
            }
        }
        return false;
    }
}

