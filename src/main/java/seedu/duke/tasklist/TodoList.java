package seedu.duke.tasklist;

import java.util.Comparator;

import seedu.duke.exception.UniTaskerException;
import seedu.duke.task.Todo;

public class TodoList extends TaskList<Todo> {
    public static final int INDEX_LOWER_LIMIT = 0;

    public void setPriority(int index, int priority) {
        tasks.get(index).setPriority(priority);
    }

    /**
     * Reorders a todo in the list by moving it from one index to another.
     *
     * <p>The todo at {@code fromIndex} is removed from its current position
     * and inserted at {@code toIndex}. Both indices must refer to valid
     * positions in the list.</p>
     *
     * @param fromIndex The current index of the todo to be moved.
     * @param toIndex The target index to move the todo to.
     * @throws UniTaskerException If either index is out of bounds.
     */
    public void reorder(int fromIndex, int toIndex) throws UniTaskerException {
        if (fromIndex >= this.getSize() || fromIndex < INDEX_LOWER_LIMIT) {
            throw new UniTaskerException("First todoIndex does not exist.");
        }
        if (toIndex >= this.getSize() || toIndex < INDEX_LOWER_LIMIT) {
            throw new UniTaskerException("Second todoIndex does not exist.");
        }
        assert (fromIndex >= 0 && fromIndex < tasks.size());
        assert (toIndex >= 0 && toIndex < tasks.size());
        Todo todo = tasks.remove(fromIndex);
        tasks.add(toIndex, todo);
    }

    /**
     * Sorts the todos in the list in descending order of priority.
     *
     * <p>Todos with higher priority values appear before those with lower
     * priority values. The sorting is performed in-place and modifies
     * the current task list.</p>
     */
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
