package seedu.duke.command;

import seedu.duke.course.CourseManager;
import seedu.duke.exception.CourseException;

/**
 * Parses course-related sub-commands entered by the user and delegates
 * the corresponding operations to CourseManager.
 * Handles commands: add, delete, list, view, add-assessment, score, delete-assessment.
 */
public class CourseParser {
    private static final int ADD_ASSESSMENT_MIN_PARTS = 4;
    private static final int RECORD_SCORE_MIN_PARTS = 3;
    private static final int DELETE_ASSESSMENT_MIN_PARTS = 2;
    private static final int INDEX_COURSE_CODE = 0;
    private static final int INDEX_NAME = 1;
    private static final int INDEX_WEIGHT = 2;
    private static final int INDEX_MAX_SCORE = 3;
    private static final int INDEX_SCORE = 2;

    private final CourseManager courseManager;

    /**
     * Constructs a CourseParser with the given CourseManager.
     *
     * @param courseManager the CourseManager to delegate course operations to
     */
    public CourseParser(CourseManager courseManager) {
        this.courseManager = courseManager;
    }

    /**
     * Returns the CourseManager used by this parser.
     *
     * @return the CourseManager instance
     */
    public CourseManager getCourseManager() {
        return courseManager;
    }

    /**
     * Parses the user input after the "course" keyword and executes
     * the corresponding course operation.
     *
     * @param input the sub-command string after "course"
     * @return result message to display to the user
     * @throws CourseException if the sub-command is unknown, empty, or the operation fails
     */
    public String parse(String input) throws CourseException {

        if (input == null || input.trim().isEmpty()) {
            throw new CourseException("Course command cannot be empty.");
        }

        String[] parts = input.trim().split("\\s+", 2);
        String command = parts[0];

        String arguments = "";
        if (parts.length > INDEX_NAME) {
            arguments = parts[INDEX_NAME];
        }

        switch (command) {
        case "add":
            return handleAddCourse(arguments);
        case "list":
            return courseManager.listCourses();
        case "delete":
            return handleDeleteCourse(arguments);
        case "view":
            return handleViewCourse(arguments);
        case "add-assessment":
            return handleAddAssessment(arguments);
        case "score":
            return handleRecordScore(arguments);
        case "delete-assessment":
            return handleDeleteAssessment(arguments);
        default:
            throw new CourseException("Unknown course command: " + command);
        }
    }

    private String handleAddCourse(String args) throws CourseException {
        if (args.trim().isEmpty()) {
            throw new CourseException("Usage: course add COURSE_CODE");
        }
        return courseManager.addCourse(args.trim());
    }

    private String handleDeleteCourse(String args) throws CourseException {
        if (args.trim().isEmpty()) {
            throw new CourseException("Usage: course delete COURSE_CODE");
        }
        return courseManager.deleteCourse(args.trim());
    }

    private String handleViewCourse(String args) throws CourseException {
        if (args.trim().isEmpty()) {
            throw new CourseException("Usage: course view COURSE_CODE");
        }
        return courseManager.viewCourse(args.trim());
    }

    private String handleAddAssessment(String args) throws CourseException {

        String[] parts = args.split("/n|/w|/ms");

        if (parts.length < ADD_ASSESSMENT_MIN_PARTS) {
            throw new CourseException(
                    "Usage: course add-assessment COURSE /n NAME /w WEIGHT /ms MAX_SCORE");
        }

        String courseCode = parts[INDEX_COURSE_CODE].trim();
        String name = parts[INDEX_NAME].trim();

        double weight = parseDouble(parts[INDEX_WEIGHT], "Weightage");
        double maxScore = parseDouble(parts[INDEX_MAX_SCORE], "Max Score");

        return courseManager.addAssessment(courseCode, name, weight, maxScore);
    }

    private String handleRecordScore(String args) throws CourseException {

        String[] parts = args.split("/n|/s");

        if (parts.length < RECORD_SCORE_MIN_PARTS) {
            throw new CourseException(
                    "Usage: course score COURSE /n NAME /s SCORE");
        }

        String courseCode = parts[INDEX_COURSE_CODE].trim();
        String name = parts[INDEX_NAME].trim();
        double score = parseDouble(parts[INDEX_SCORE], "Score");

        return courseManager.recordScore(courseCode, name, score);
    }

    private String handleDeleteAssessment(String args) throws CourseException {

        String[] parts = args.split("/n");

        if (parts.length < DELETE_ASSESSMENT_MIN_PARTS) {
            throw new CourseException(
                    "Usage: course delete-assessment COURSE /n NAME");
        }

        String courseCode = parts[INDEX_COURSE_CODE].trim();
        String name = parts[INDEX_NAME].trim();

        return courseManager.deleteAssessment(courseCode, name);
    }

    /**
     * Parses a string value as a double for numeric course fields.
     * Validates that the result is a finite number to prevent NaN or Infinity inputs.
     *
     * @param value     the string to parse
     * @param fieldName the name of the field, used in error messages
     * @return the parsed double value
     * @throws CourseException if the value is not a valid finite number
     */
    private double parseDouble(String value, String fieldName) throws CourseException {
        try {
            double result = Double.parseDouble(value.trim());
            if (!Double.isFinite(result)) {
                throw new CourseException(fieldName + " must be a valid finite number.");
            }
            return result;
        } catch (NumberFormatException e) {
            throw new CourseException(fieldName + " must be a valid number. Please enter a numeric value (eg. 40)");
        }
    }
}
