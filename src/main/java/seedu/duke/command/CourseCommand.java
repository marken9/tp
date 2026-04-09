package seedu.duke.command;

import seedu.duke.appcontainer.AppContainer;
import seedu.duke.course.Course;
import seedu.duke.exception.CourseException;
import seedu.duke.ui.ErrorUi;
import seedu.duke.ui.LimitUi;

public class CourseCommand implements Command {
    private final String line;
    private String undoAction;
    private String undoArgument;
    private Course savedCourse;

    public CourseCommand(String line) {
        this.line = line;
    }

    @Override
    public void execute(AppContainer container) {
        try {
            String courseCommand = line.substring("course".length()).trim();

            String[] parts = courseCommand.trim().split("\\s", 2);
            //remembers what action was taken so can be reversed later
            undoAction = parts[0];
            undoArgument = parts.length > 1 ? parts[1] : "";

            // store full course before delete
            if (undoAction.equals("delete")) {
                savedCourse = container.courseParser()
                        .getCourseManager()
                        .getCourseList()
                        .get(undoArgument.trim().toUpperCase());
            }

            String result = container.courseParser().parse(courseCommand);
            LimitUi.printCourseResult(result);
        } catch (CourseException e) {
            ErrorUi.printError(e.getMessage());
        } catch (Exception e) {
            ErrorUi.printError("Could not process course command.");
        }
    }

    @Override
    public boolean isUndoable() {
        if (undoAction == null) {
            return false;
        }
        switch (undoAction) {
        case "add":
        case "delete":
        case "add-assessment":
            return true;
        default:
            return false;
        }
    }

    @Override
    public void undo(AppContainer container) {
        if (undoAction == null) {
            LimitUi.printCourseResult("Nothing to undo.");
            return;
        }
        try {
            switch (undoAction) {
            case "add":
                container.courseParser().parse("delete " + undoArgument);
                LimitUi.printCourseResult("Undo: removed course " + undoArgument);
                break;
            case "delete":
                container.courseParser().getCourseManager().getCourseList().add(savedCourse);
                LimitUi.printCourseResult("Undo: restored course " + undoArgument);
                break;
            case "add-assessment":
                String[] assessParts = undoArgument.split("/n");
                String courseCode = assessParts[0].trim();
                String assessName = assessParts[1].trim().split("/")[0].trim();
                container.courseParser().parse("delete-assessment " + courseCode + " /n " + assessName);
                LimitUi.printCourseResult("Undo: removed assessment " + assessName);
                break;
            case "delete-assessment":
                LimitUi.printCourseResult("Cannot undo delete-assessment: assessment data not stored.");
                break;
            case "score":
                LimitUi.printCourseResult("Cannot undo score: original score not stored.");
                break;
            default:
                LimitUi.printCourseResult("This course action cannot be undone.");
            }
        } catch (Exception e) {
            ErrorUi.printError("Undo failed: " + e.getMessage());
        }
    }

}
