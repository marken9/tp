package seedu.duke.command;

import seedu.duke.appcontainer.AppContainer;
import seedu.duke.ui.GeneralUi;

import java.time.LocalDate;

public class ReminderCommand implements Command {

    public void execute(AppContainer container) {
        GeneralUi.printReminders(container.categories().findTasksForTheDay(LocalDate.now()));
    }
}
