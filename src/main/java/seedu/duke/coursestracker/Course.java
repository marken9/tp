package seedu.duke.coursestracker;

import java.util.ArrayList;

/**
 * Represents one university course, e.g. CS2113.
 * Each course contains a list of assessments.
 * MICHAEL:
 * - Your commands will usually first locate a Course,
 *   then perform actions on it (add assessment, list assessments, score, grade).
 */

public class Course {

    private final String courseCode;
    private final ArrayList<Assessment> assessments;

    public Course(String courseCode) {
        this.courseCode = courseCode.toUpperCase().trim();
        this.assessments = new ArrayList<>();
    }

    public String getCourseCode() {
        return courseCode;
    }

    public ArrayList<Assessment> getAssessments() {
        return assessments;
    }


    public void addAssessment(Assessment assessment) {
        assessments.add(assessment);
    }
    //Adds an assessment into this course.

    /**
     * MICHAEL:
     * - Your add-assessment command should call this.
     */


    public boolean hasAssessment(String name) {
        return getAssessment(name) != null;
    }
    //Returns true if an assessment with this name exists.


    public Assessment getAssessment(String name) {
        for (Assessment assessment : assessments) {
            if (assessment.getName().equalsIgnoreCase(name)) {
                return assessment;
            }
        }
        return null;
    }
    //Finds an assessment by name, case-insensitive.

    /**
     * MICHAEL:
     * - Your score/delete-assessment command can use this.
     */


    public boolean removeAssessment(String name) {
        Assessment assessment = getAssessment(name);
        if (assessment == null) {
            return false;
        }
        assessments.remove(assessment);
        return true;
    }
    //Removes an assessment by name, case-insensitive.

    /**
     * MICHAEL:
     * - Your delete-assessment command can call this.
     */

    public int getAssessmentCount() {
        return assessments.size();
    }


    public double getTotalWeightedScore() {
        double total = 0;
        for (Assessment assessment : assessments) {
            total += assessment.getWeightedScore();
        }
        return total;
    }
    //Returns the total weighted score earned so far.


    public double getTotalWeightage() {
        double total = 0;
        for (Assessment assessment : assessments) {
            total += assessment.getWeightage();
        }
        return total;
    }
    //Returns the sum of all assessment weightages.

    public double getGradedWeightage() {
        double total = 0;
        for (Assessment assessment : assessments) {
            if (assessment.isGraded()) {
                total += assessment.getWeightage();
            }
        }
        return total;
    }
    //Returns the sum of weightages for only graded assessments.

    /**
     * Useful if Michael wants to show:
     * "You have completed 60% of the course grading components."
     */


    public String encode() {
        StringBuilder sb = new StringBuilder();
        sb.append("COURSE:").append(courseCode).append(System.lineSeparator());

        for (Assessment assessment : assessments) {
            sb.append(assessment.encode()).append(System.lineSeparator());
        }

        sb.append("END").append(System.lineSeparator());
        return sb.toString();
    }
    //Encodes the course and all its assessments into a block for saving.
    // Format:
    // COURSE:CS2113
    // Finals|40.0|85.0|100.0
    // Midterm|20.0|-1.0|25.0
    // END


    @Override
    public String toString() {
        return courseCode + " (" + assessments.size() + " assessment(s))";
    }
}

