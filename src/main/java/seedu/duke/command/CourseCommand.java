package seedu.duke.command;

import seedu.duke.appcontainer.AppContainer;
import seedu.duke.course.Course;
import seedu.duke.exception.CourseException;
import seedu.duke.ui.ErrorUi;
import seedu.duke.ui.LimitUi;

/**
 * Represents a command that handles all course-related operations.
 * Supports adding, deleting, listing, viewing courses and assessments, recording scores for the assessments
 * and undoing the course actions
 */
public class CourseCommand implements Command {
    private static final double UNGRADED_SCORE = -1;
    private final String line;
    private String undoAction;
    private String undoArgument;
    private Course savedCourse;
    private boolean executedSuccessfully = false;
    private double previousScore = UNGRADED_SCORE;
    private String scoreCourseCode;
    private String scoreAssessmentName;

    /**
     * Constructs a CourseCommand with the full input line.
     *
     * @param line the full user input line starting with "course"
     */
    public CourseCommand(String line) {
        this.line = line;
    }

    /**
     * Executes the course command by parsing the sub-command and delegating
     * to CourseParser. Stores undo state before execution where necessary.
     * Only marks the command as successfully executed if no exception is thrown.
     *
     * @param container the AppContainer providing access to course data
     */
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

    /**
     * Returns true if this command supports undo.
     * Only successful add, delete, add-assessment and score commands are undoable.
     * Failed commands are never pushed to the undo stack.
     *
     * @return true if the command executed successfully and is undoable
     */
    @Override
    public boolean isUndoable() {
        return executedSuccessfully && (
                "add".equals(undoAction) ||
                        "delete".equals(undoAction) ||
                        "add-assessment".equals(undoAction) ||
                        "score".equals(undoAction)
                );
    }

    /**
     * Reverses the last undoable course command on the undo stack.
     * Restores the previous application state depending on the action taken.
     * Can be called multiple times to undo successive course commands.
     *
     * @param container the AppContainer providing access to course data
     */
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
                if (previousScore == UNGRADED_SCORE) {
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
