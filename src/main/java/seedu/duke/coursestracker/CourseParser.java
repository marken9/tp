package seedu.duke.coursestracker;

public class CourseParser {

    private final CourseManager courseManager;

    public CourseParser(CourseManager courseManager) {
        this.courseManager = courseManager;
    }


    //Parses the user input after the keyword "course".
    public String parse(String input) throws CourseException {

        if (input == null || input.trim().isEmpty()) {
            throw new CourseException("Course command cannot be empty.");
        }

        String[] parts = input.trim().split("\\s+", 2);
        String command = parts[0];

        String arguments = "";
        if (parts.length > 1) {
            arguments = parts[1];
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

        if (parts.length < 4) {
            throw new CourseException(
                    "Usage: course add-assessment COURSE /n NAME /w WEIGHT /ms MAX_SCORE");
        }

        String courseCode = parts[0].trim();
        String name = parts[1].trim();
        double weight = Double.parseDouble(parts[2].trim());
        double maxScore = Double.parseDouble(parts[3].trim());

        return courseManager.addAssessment(courseCode, name, weight, maxScore);
    }

    private String handleRecordScore(String args) throws CourseException {

        String[] parts = args.split("/n|/s");

        if (parts.length < 3) {
            throw new CourseException(
                    "Usage: course score COURSE /n NAME /s SCORE");
        }

        String courseCode = parts[0].trim();
        String name = parts[1].trim();
        double score = Double.parseDouble(parts[2].trim());

        return courseManager.recordScore(courseCode, name, score);
    }

    private String handleDeleteAssessment(String args) throws CourseException {

        String[] parts = args.split("/n");

        if (parts.length < 2) {
            throw new CourseException(
                    "Usage: course delete-assessment COURSE /n NAME");
        }

        String courseCode = parts[0].trim();
        String name = parts[1].trim();

        return courseManager.deleteAssessment(courseCode, name);
    }
}
