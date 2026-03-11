package seedu.duke.tasklist;

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
}
