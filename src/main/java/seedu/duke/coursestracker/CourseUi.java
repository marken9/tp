package seedu.duke.coursestracker;

public class CourseUi {
    //Formats the full list of courses for display.
    public static String formatCourseList(CourseList courseList) {
        if (courseList.isEmpty()) {
            return "No courses found.";
        }

        StringBuilder sb = new StringBuilder("Courses:\n");
        int index = 1;
        for (Course course : courseList.getAll()) {
            sb.append(index)
                    .append(". ")
                    .append(course.toString())
                    .append(System.lineSeparator());
            index++;
        }
        return sb.toString().trim();
    }

    /**
     * Formats a detailed view of a single course, including all assessments
     * and current grading progress.
     */
    public static String formatCourse(Course course) {
        StringBuilder sb = new StringBuilder();
        sb.append("Course: ").append(course.getCourseCode()).append(System.lineSeparator());
        sb.append("Assessments:").append(System.lineSeparator());

        if (course.getAssessments().isEmpty()) {
            sb.append("None").append(System.lineSeparator());
        } else {
            int index = 1;
            for (Assessment assessment : course.getAssessments()) {
                sb.append(index)
                        .append(". ")
                        .append(assessment.toString())
                        .append(System.lineSeparator());
                index++;
            }
        }

        sb.append("Current weighted score: ")
                .append(String.format("%.1f", course.getTotalWeightedScore()))
                .append("%")
                .append(System.lineSeparator());

        sb.append("Graded weightage: ")
                .append(String.format("%.1f", course.getGradedWeightage()))
                .append("%")
                .append(System.lineSeparator());

        sb.append("Total planned weightage: ")
                .append(String.format("%.1f", course.getTotalWeightage()))
                .append("%");

        return sb.toString();
    }


    //Formats a short success message for a generic course operation.
    public static String formatMessage(String message) {
        return message;
    }
}