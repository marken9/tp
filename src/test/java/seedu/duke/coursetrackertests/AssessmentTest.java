package seedu.duke.coursetrackertests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.duke.coursestracker.Assessment;

public class AssessmentTest {

    @Test
    public void constructor_withoutScore_notGraded() {
        Assessment assessment = new Assessment("Finals", 40.0, 100.0);

        assertFalse(assessment.isGraded());
        assertEquals(-1, assessment.getScoreObtained(), 0.001);
    }

    @Test
    public void constructor_withScore_graded() {
        Assessment assessment = new Assessment("Finals", 40.0, 85.0, 100.0);

        assertTrue(assessment.isGraded());
        assertEquals(85.0, assessment.getScoreObtained(), 0.001);
    }

    @Test
    public void recordScore_success() {
        Assessment assessment = new Assessment("Midterm", 20.0, 50.0);
        assessment.recordScore(18.0);

        assertTrue(assessment.isGraded());
        assertEquals(18.0, assessment.getScoreObtained(), 0.001);
    }

    @Test
    public void getWeightedScore_success() {
        Assessment assessment = new Assessment("Finals", 40.0, 85.0, 100.0);

        assertEquals(34.0, assessment.getWeightedScore(), 0.001);
    }

    @Test
    public void getWeightedScore_notGraded_returnsZero() {
        Assessment assessment = new Assessment("Quiz", 10.0, 20.0);

        assertEquals(0.0, assessment.getWeightedScore(), 0.001);
    }

    @Test
    public void encodeDecode_success() {
        Assessment assessment = new Assessment("Finals", 40.0, 85.0, 100.0);
        Assessment decoded = Assessment.decode(assessment.encode());

        assertNotNull(decoded);
        assertEquals("Finals", decoded.getName());
        assertEquals(40.0, decoded.getWeightage(), 0.001);
        assertEquals(85.0, decoded.getScoreObtained(), 0.001);
        assertEquals(100.0, decoded.getMaxScore(), 0.001);
    }

    @Test
    public void decode_invalidInput_returnsNull() {
        assertNull(Assessment.decode("bad|data"));
        assertNull(Assessment.decode("Finals|abc|85|100"));
    }
}

