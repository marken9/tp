package seedu.duke.tasklist;

import java.util.Comparator;

import seedu.duke.exception.UniTaskerException;
import seedu.duke.task.Todo;

public class TodoList extends TaskList<Todo> {
    public static final int INDEX_LOWER_LIMIT = 0;

    public void setPriority(int index, int priority) {
        tasks.get(index).setPriority(priority);
    }

    public void reorder(int fromIndex, int toIndex) throws UniTaskerException {
        if (fromIndex >= this.getSize() || fromIndex < INDEX_LOWER_LIMIT) {
            throw new UniTaskerException("First todoIndex does not exist.");
        }
        if (toIndex >= this.getSize() || toIndex < INDEX_LOWER_LIMIT) {
            throw new UniTaskerException("Second todoIndex does not exist.");
        }
        assert(fromIndex >= 0 && fromIndex < tasks.size());
        assert(toIndex >= 0 && toIndex < tasks.size());
        Todo todo = tasks.remove(fromIndex);
        tasks.add(toIndex, todo);
    }

    public void sortByPriority() {
        tasks.sort(Comparator.comparingInt(Todo::getPriority).reversed());
    }

    public String toString() {
        String result = "";
        for (int i = 0; i < tasks.size(); i += 1) {
            result = result + (i + 1) + ". " + (tasks.get(i).toString()) + System.lineSeparator();
        }
        return result;
    }

}
