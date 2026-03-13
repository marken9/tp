package seedu.duke.coursestracker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles saving and loading course data from disk.
 * Similar role to Storage in your Duke iP.
 * MICHAEL:
 * - Your manager/main app should call load() at startup
 *   and save(...) after any modification.
 */
public class CourseStorage {

    private final String filePath;

    public CourseStorage(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<Course> load() throws CourseException {
        ensureParentDirectoryExists();

        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        ArrayList<Course> loadedCourses = new ArrayList<>();

        try (Scanner scanner = new Scanner(file)) {
            Course currentCourse = null;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                if (line.isEmpty()) {
                    continue;
                }

                if (line.startsWith("COURSE:")) {
                    String courseCode = line.substring("COURSE:".length()).trim();
                    currentCourse = new Course(courseCode);

                } else if (line.equals("END")) {
                    if (currentCourse != null) {
                        loadedCourses.add(currentCourse);
                        currentCourse = null;
                    }

                } else {
                    if (currentCourse != null) {
                        Assessment assessment = Assessment.decode(line);
                        if (assessment != null) {
                            currentCourse.addAssessment(assessment);
                        }
                    }
                }
            }

        } catch (IOException e) {
            throw new CourseException("Could not load courses from disk.");
        }

        return loadedCourses;
    }//Loads all courses from the file, returns a NULL file if empty

    public void save(CourseList courseList) throws CourseException {
        ensureParentDirectoryExists();

        try (FileWriter writer = new FileWriter(filePath)) {
            for (Course course : courseList.getAll()) {
                writer.write(course.encode());
                writer.write(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new CourseException("Could not save courses to disk.");
        }
    }//Saves all courses into the file, overwriting previous contents

    private void ensureParentDirectoryExists() throws CourseException {
        File file = new File(filePath);
        File parent = file.getParentFile();

        if (parent != null && !parent.exists()) {
            boolean success = parent.mkdirs();
            if (!success) {
                throw new CourseException("Could not create data folder.");
            }
        }
    }//Ensures the parent directory exists before reading/writing
}

