package seedu.duke.coursetrackertests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import seedu.duke.coursestracker.Assessment;
import seedu.duke.coursestracker.Course;

public class CourseTest {

    @Test
    public void addAssessment_success() {
        Course course = new Course("CS2113");
        course.addAssessment(new Assessment("Finals", 40.0, 100.0));

        assertEquals(1, course.getAssessmentCount());
        assertEquals("Finals", course.getAssessment("Finals").getName());
    }

    @Test
    public void getAssessment_success() {
        Course course = new Course("CS2113");
        course.addAssessment(new Assessment("Midterm", 20.0, 50.0));

        assertNotNull(course.getAssessment("Midterm"));
    }

    @Test
    public void removeAssessment_success() {
        Course course = new Course("CS2113");
        course.addAssessment(new Assessment("Finals", 40.0, 100.0));

        assertTrue(course.removeAssessment("Finals"));
        assertEquals(0, course.getAssessmentCount());
    }

    @Test
    public void removeAssessment_failure() {
        Course course = new Course("CS2113");

        assertFalse(course.removeAssessment("Finals"));
    }
}

