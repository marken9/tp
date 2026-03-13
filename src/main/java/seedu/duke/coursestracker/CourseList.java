package seedu.duke.coursestracker;

import java.util.ArrayList;

/**
 * Stores all courses in memory.
 * Similar role to TaskList in your Duke iP.
 * MICHAEL:
 * - Your manager class will likely keep one CourseList object
 *   and use it to add/get/remove courses.
 */

public class CourseList {

    private final ArrayList<Course> courses = new ArrayList<>();

    /**
     * MICHAEL:
     * - add-course should call this.
     */
    public void add(Course course) {
        courses.add(course);
    } //Adds a course to the list

    /**
     * MICHAEL:
     * - delete-course should call this.
     */
    public boolean remove(String courseCode) {
        Course course = get(courseCode);
        if (course == null) {
            return false;
        }
        courses.remove(course);
        return true;
    } //Removes a course by course code

    /**
     * MICHAEL:
     * - many commands will begin by calling this.
     */
    public Course get(String courseCode) {
        for (Course course : courses) {
            if (course.getCourseCode().equalsIgnoreCase(courseCode)) {
                return course;
            }
        }
        return null;
    } //Gets a course by course code

    public boolean contains(String courseCode) {
        return get(courseCode) != null;
    }

    public ArrayList<Course> getAll() {
        return courses;
    }

    public int size() {
        return courses.size();
    }

    public boolean isEmpty() {
        return courses.isEmpty();
    }

    public void setAll(ArrayList<Course> loadedCourses) {
        courses.clear();
        courses.addAll(loadedCourses);
    }//Replaces all current courses with loaded data from storage.

}

