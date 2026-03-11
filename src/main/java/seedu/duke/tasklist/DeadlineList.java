package seedu.duke.tasklist;

import java.util.Comparator;

import seedu.duke.task.Deadline;

public class DeadlineList extends TaskList<Deadline> {
    //@Override
    public DeadlineList() {
        super();
    }

    public String toString() {
        String result = "";
        for (int i = 0; i < tasks.size(); i++) {
            result = result + (i + 1) + ". " + (tasks.get(i).toString()) + System.lineSeparator();
        }
        return result;
    }

    public void sortByDate() {
        tasks.sort(Comparator.comparing(Deadline::getBy));
    }

    public void clearAll() {
        tasks.clear();
    }

    public Deadline getLatest() {
        if (tasks.isEmpty()) {
            return null;
        }
        return tasks.get(tasks.size() - 1);
    }
}
