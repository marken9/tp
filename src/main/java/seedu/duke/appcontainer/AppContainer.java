package seedu.duke.appcontainer;

import seedu.duke.calender.Calendar;
import seedu.duke.coursestracker.CourseParser;
import seedu.duke.storage.Storage;
import seedu.duke.tasklist.CategoryList;

public class AppContainer {
    private final CategoryList categories;
    private final Calendar calendar;
    private final Storage storage;
    private final CourseParser courseParser;

    private int dailyTaskLimit;
    private int startYear;
    private int endYear;

    public AppContainer(CategoryList categories, Calendar calendar, Storage storage,
                      CourseParser courseParser, int dailyTaskLimit, int startYear, int endYear) {
        this.categories = categories;
        this.calendar = calendar;
        this.storage = storage;
        this.courseParser = courseParser;
        this.dailyTaskLimit = dailyTaskLimit;
        this.startYear = startYear;
        this.endYear = endYear;
    }

    public CategoryList getCategories() {
        return categories;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public Storage getStorage() {
        return storage;
    }

    public CourseParser getCourseParser() {
        return courseParser;
    }

    public int getDailyTaskLimit() {
        return dailyTaskLimit;
    }

    public void setDailyTaskLimit(int dailyTaskLimit) {
        this.dailyTaskLimit = dailyTaskLimit;
    }

    public int getStartYear() {
        return startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }
}
