package seedu.duke.appcontainer;

import seedu.duke.calender.Calendar;
import seedu.duke.UniTasker;
import seedu.duke.command.CourseParser;
import seedu.duke.storage.Storage;
import seedu.duke.tasklist.CategoryList;

//@@author marken9
public record AppContainer(CategoryList categories, Calendar calendar, Storage storage, CourseParser courseParser) {

    //@@author WenJunYu5984
    public int getDailyTaskLimit() {
        return UniTasker.getDailyTaskLimit();
    }

    public void setDailyTaskLimit(int dailyTaskLimit) {
        UniTasker.setDailyTaskLimit(dailyTaskLimit);
    }

    public int getStartYear() {
        return UniTasker.getStartYear();
    }

    public int getEndYear() {
        return UniTasker.getEndYear();
    }

    public boolean setEndYear(int endYear) {
        return UniTasker.setEndYear(endYear);
    }
    //@@author
}
