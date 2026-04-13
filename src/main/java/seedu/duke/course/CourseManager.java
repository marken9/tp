package seedu.duke.course;

import seedu.duke.exception.CourseException;
import seedu.duke.storage.CourseStorage;
import seedu.duke.ui.CourseUi;

public class CourseManager {
    //stores all courses
    private final CourseList courseList;
    //handles saving and loading courses
    private final CourseStorage courseStorage;

    public CourseManager(String filePath) throws CourseException {
        this.courseList = new CourseList();
        this.courseStorage = new CourseStorage(filePath);
        this.courseList.setAll(courseStorage.load());
    }

    public CourseList getCourseList() {
        return courseList;
    }

    public String addCourse(String courseCode) throws CourseException {
        if (courseCode == null || courseCode.trim().isEmpty()) {
            throw new CourseException("Course code cannot be empty.");
        }

        String normalizedCode = courseCode.toUpperCase().trim();

        if (courseList.contains(normalizedCode)) {
            throw new CourseException("Course already exists: " + normalizedCode);
        }

        courseList.add(new Course(normalizedCode));
        save();
        return "Added course: " + normalizedCode;
    }

    public String listCourses() {
        return CourseUi.formatCourseList(courseList);
    }

    public String deleteCourse(String courseCode) throws CourseException {
        if (courseCode == null || courseCode.trim().isEmpty()) {
            throw new CourseException("Course code cannot be empty.");
        }

        String normalizedCode = courseCode.toUpperCase().trim();

        if (!courseList.remove(normalizedCode)) {
            throw new CourseException("Course not found: " + normalizedCode);
        }

        save();
        return "Deleted course: " + normalizedCode;
    }

    public String addAssessment(String courseCode, String assessmentName,
                                double weightage, double maxScore) throws CourseException {
        if (courseCode == null || courseCode.trim().isEmpty()) {
            throw new CourseException("Course code cannot be empty.");
        }
        if (assessmentName == null || assessmentName.trim().isEmpty()) {
            throw new CourseException("Assessment name cannot be empty.");
        }
        if (weightage <= 0) {
            throw new CourseException("Weightage must be greater than 0.");
        }
        if (maxScore <= 0) {
            throw new CourseException("Maximum score must be greater than 0.");
        }

        String normalizedCode = courseCode.toUpperCase().trim();
        String normalizedAssessmentName = assessmentName.trim();

        Course course = courseList.get(normalizedCode);
        if (course == null) {
            throw new CourseException("Course not found: " + normalizedCode);
        }

        if (course.hasAssessment(normalizedAssessmentName)) {
            throw new CourseException("Assessment already exists in " + normalizedCode + ": "
                    + normalizedAssessmentName);
        }

        if (course.getTotalWeightage() + weightage > 100) {
            throw new CourseException("Total assessment weightage cannot exceed 100%.");
        }

        if (weightage > 100) {
            throw new CourseException("Weightage must be between 0 and 100.");
        }

        if (maxScore > 10000) {
            throw new CourseException("Maximum score cannot exceed 10000.");
        }

        course.addAssessment(new Assessment(normalizedAssessmentName, weightage, maxScore));
        save();

        return "Added assessment " + normalizedAssessmentName + " to " + normalizedCode
                + " (weight: " + weightage + "%, max score: " + maxScore + ")";
    }

    public String recordScore(String courseCode, String assessmentName, double score) throws CourseException {
        if (courseCode == null || courseCode.trim().isEmpty()) {
            throw new CourseException("Course code cannot be empty.");
        }
        if (assessmentName == null || assessmentName.trim().isEmpty()) {
            throw new CourseException("Assessment name cannot be empty.");
        }
        if (score < 0) {
            throw new CourseException("Score cannot be negative.");
        }

        String normalizedCode = courseCode.toUpperCase().trim();
        String normalizedAssessmentName = assessmentName.trim();

        Course course = courseList.get(normalizedCode);
        if (course == null) {
            throw new CourseException("Course not found: " + normalizedCode);
        }

        Assessment assessment = course.getAssessment(normalizedAssessmentName);
        if (assessment == null) {
            throw new CourseException("Assessment not found in " + normalizedCode + ": "
                    + normalizedAssessmentName);
        }

        if (score > assessment.getMaxScore()) {
            throw new CourseException("Score cannot exceed maximum score of " + assessment.getMaxScore() + ".");
        }

        assessment.recordScore(score);
        save();

        return "Recorded score for " + normalizedAssessmentName + " in " + normalizedCode
                + ": " + score + "/" + assessment.getMaxScore();
    }

    public String deleteAssessment(String courseCode, String assessmentName) throws CourseException {
        if (courseCode == null || courseCode.trim().isEmpty()) {
            throw new CourseException("Course code cannot be empty.");
        }
        if (assessmentName == null || assessmentName.trim().isEmpty()) {
            throw new CourseException("Assessment name cannot be empty.");
        }

        String normalizedCode = courseCode.toUpperCase().trim();
        String normalizedAssessmentName = assessmentName.trim();

        Course course = courseList.get(normalizedCode);
        if (course == null) {
            throw new CourseException("Course not found: " + normalizedCode);
        }

        if (!course.removeAssessment(normalizedAssessmentName)) {
            throw new CourseException("Assessment not found in " + normalizedCode + ": "
                    + normalizedAssessmentName);
        }

        save();
        return "Deleted assessment " + normalizedAssessmentName + " from " + normalizedCode;
    }

    public String viewCourse(String courseCode) throws CourseException {
        if (courseCode == null || courseCode.trim().isEmpty()) {
            throw new CourseException("Course code cannot be empty.");
        }

        String normalizedCode = courseCode.toUpperCase().trim();
        Course course = courseList.get(normalizedCode);

        if (course == null) {
            throw new CourseException("Course not found: " + normalizedCode);
        }

        return CourseUi.formatCourse(course);
    }

    private void save() throws CourseException {
        courseStorage.save(courseList);
    }
}
