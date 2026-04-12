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
    private boolean executedSuccessfully = false;
    private double previousScore = -1;
    private String scoreCourseCode;
    private String scoreAssessmentName;

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

            if (undoAction.equals("score")) {
                String[] scoreParts = undoArgument.split("/n|/s");
                if (scoreParts.length >= 3) {
                    scoreCourseCode = scoreParts[0].trim();
                    scoreAssessmentName = scoreParts[1].trim();
                    Course course = container.courseParser()
                            .getCourseManager()
                            .getCourseList()
                            .get(scoreCourseCode.toUpperCase());
                    if (course != null && course.getAssessment(scoreAssessmentName) != null) {
                        previousScore = course.getAssessment(scoreAssessmentName).getScoreObtained();
                    }
                }
            }

            String result = container.courseParser().parse(courseCommand);
            executedSuccessfully = true;
            LimitUi.printCourseResult(result);
        } catch (CourseException e) {
            ErrorUi.printError(e.getMessage());
        } catch (Exception e) {
            ErrorUi.printError("Invalid course command: " + e.getMessage());
        }

    }

    @Override
    public boolean isUndoable() {
        return executedSuccessfully && (
                "add".equals(undoAction) ||
                        "delete".equals(undoAction) ||
                        "add-assessment".equals(undoAction) ||
                        "score".equals(undoAction)
                );
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
                if (previousScore == -1) {
                    // was not graded before, reset to ungraded
                    Course course = container.courseParser()
                            .getCourseManager()
                            .getCourseList()
                            .get(scoreCourseCode.toUpperCase());
                    if (course != null && course.getAssessment(scoreAssessmentName) != null) {
                        course.getAssessment(scoreAssessmentName).resetScore();
                    }
                    LimitUi.printCourseResult("Undo: reverted score for " + scoreAssessmentName);
                } else {
                    container.courseParser().getCourseManager()
                            .recordScore(scoreCourseCode, scoreAssessmentName, previousScore);
                    LimitUi.printCourseResult("Undo: reverted score for " + scoreAssessmentName);
                }
                break;
            default:
                LimitUi.printCourseResult("This course action cannot be undone.");
            }
        } catch (Exception e) {
            ErrorUi.printError("Undo failed: " + e.getMessage());
        }
    }

}
