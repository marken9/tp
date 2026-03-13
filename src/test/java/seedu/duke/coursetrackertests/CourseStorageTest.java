package seedu.duke.coursetrackertests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import seedu.duke.coursestracker.Course;
import seedu.duke.coursestracker.CourseStorage;
import seedu.duke.coursestracker.Assessment;
import seedu.duke.coursestracker.CourseList;
import seedu.duke.coursestracker.CourseException;

public class CourseStorageTest {

    private static final String TEST_FILE_PATH = "data/test-courses.txt";

    @AfterEach
    public void tearDown() {
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                throw new RuntimeException("Could not delete test file.");
            }
        }
    }

    @Test
    public void load_missingFile_returnsEmptyList() throws CourseException {
        CourseStorage storage = new CourseStorage(TEST_FILE_PATH);
        ArrayList<Course> loaded = storage.load();

        assertTrue(loaded.isEmpty());
    }

    @Test
    public void saveAndLoad_singleCourse_success() throws CourseException {
        CourseList courseList = new CourseList();
        Course course = new Course("CS2113");
        course.addAssessment(new Assessment("Finals", 40.0, 85.0, 100.0));
        course.addAssessment(new Assessment("Midterm", 20.0, 20.0, 25.0));
        courseList.add(course);

        CourseStorage storage = new CourseStorage(TEST_FILE_PATH);
        storage.save(courseList);

        ArrayList<Course> loaded = storage.load();

        assertEquals(1, loaded.size());
        assertEquals("CS2113", loaded.get(0).getCourseCode());
        assertEquals(2, loaded.get(0).getAssessmentCount());
        assertNotNull(loaded.get(0).getAssessment("Finals"));
        assertNotNull(loaded.get(0).getAssessment("Midterm"));
    }

    @Test
    public void saveAndLoad_multipleCourses_success() throws CourseException {
        CourseList courseList = new CourseList();

        Course cs2113 = new Course("CS2113");
        cs2113.addAssessment(new Assessment("Finals", 40.0, 85.0, 100.0));

        Course ma1521 = new Course("MA1521");
        ma1521.addAssessment(new Assessment("Quiz", 10.0, 8.0, 10.0));

        courseList.add(cs2113);
        courseList.add(ma1521);

        CourseStorage storage = new CourseStorage(TEST_FILE_PATH);
        storage.save(courseList);

        ArrayList<Course> loaded = storage.load();

        assertEquals(2, loaded.size());
        assertNotNull(findCourse(loaded, "CS2113"));
        assertNotNull(findCourse(loaded, "MA1521"));
    }

    private Course findCourse(ArrayList<Course> courses, String courseCode) {
        for (Course course : courses) {
            if (course.getCourseCode().equalsIgnoreCase(courseCode)) {
                return course;
            }
        }
        return null;
    }
}

