package seedu.duke.tasklist;

import seedu.duke.task.Deadline;
import seedu.duke.task.Todo;

public class Category {
    private String name;
    private TodoList todoList;
    private DeadlineList deadlineList;

    public Category(String name) {
        this.name = name;
        this.todoList = new TodoList();
        this.deadlineList = new DeadlineList();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public TodoList getTodoList() {
        return todoList;
    }

    public void addTodo(Todo todo) {
        todoList.add(todo);
    }

    public Todo getTodo(int index) {
        return todoList.get(index);
    }

    public void deleteTodo(int index) {
        todoList.delete(index);
    }

    public void markTodo(int index) {
        todoList.mark(index);
    }

    public void unmarkTodo(int index) {
        todoList.unmark(index);
    }

    public void setTodoPriority(int index, int priority) {
        todoList.setPriority(index, priority);
    }

    public void reorderTodo(int fromIndex, int toIndex) {
        todoList.reorder(fromIndex, toIndex);
    }

    public void addDeadline(Deadline deadline) {
        deadlineList.add(deadline);
    }

    public void deleteDeadline(int index) {
        deadlineList.delete(index);
    }

    public void setDeadlineStatus(int index, boolean isDone) {
        if (isDone) {
            deadlineList.mark(index);
        } else {
            deadlineList.unmark(index);
        }
    }

    public DeadlineList getDeadlineList() {
        return deadlineList;
    }

    @Override
    public String toString() {
        String result = "";
        result += "---" + getName() + "---" + System.lineSeparator();
        result += "Todos:" + System.lineSeparator() + todoList.toString();
        result += "Deadlines:" + System.lineSeparator() + deadlineList.toString();
        return result;
    }

}
