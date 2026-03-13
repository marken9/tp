package seedu.duke.coursestracker;
/**
 * Represents one assessment component inside a course.
 * Example:
 * Finals, 40% weightage, scored 85 out of 100
 * MICHAEL:
 * - Your parser/manager will create Assessment objects when the user types:
 *   add-assessment CS2113 /n Finals /w 40 /ms 100
 * - Your score command will later call recordScore(...)
 */

public class Assessment {

    private final String name;
    private final double weightage;
    private double scoreObtained;
    private final double maxScore;


    public Assessment(String name, double weightage, double maxScore) {
        this.name = name;
        this.weightage = weightage;
        this.maxScore = maxScore;
        this.scoreObtained = -1;
    }
    //Creates an assessment that has not been graded yet.
    //scoreObtained = -1 means "not graded".

    public Assessment(String name, double weightage, double scoreObtained, double maxScore) {
        this.name = name;
        this.weightage = weightage;
        this.scoreObtained = scoreObtained;
        this.maxScore = maxScore;
    }
    //Creates an assessment with an already recorded score.

    public String getName() {
        return name;
    }

    public double getWeightage() {
        return weightage;
    }

    public double getScoreObtained() {
        return scoreObtained;
    }

    public double getMaxScore() {
        return maxScore;
    }


    public boolean isGraded() {
        return scoreObtained >= 0;
    }
    //returns true if a score has been recorded

    /**
     * MICHAEL:
     * - Your "score" command can call this after locating the correct assessment.
     */

    public void recordScore(double score) {
        this.scoreObtained = score;
    }
    //records the student's score


    public double getWeightedScore() {
        if (!isGraded() || maxScore == 0) {
            return 0;
        }
        return (scoreObtained / maxScore) * weightage;
    }
    //Computes this assessment's weighted contribution to the final grade.
    //Returns 0 if not graded yet.


    public String encode() {
        return name + "|" + weightage + "|" + scoreObtained + "|" + maxScore;
    }
    //Encodes the assessment into one line for saving to disk.
    // Format: name|weightage|scoreObtained|maxScore


    public static Assessment decode(String encoded) {
        String[] parts = encoded.split("\\|");
        if (parts.length != 4) {
            return null;
        }

        try {
            String name = parts[0];
            double weightage = Double.parseDouble(parts[1]);
            double scoreObtained = Double.parseDouble(parts[2]);
            double maxScore = Double.parseDouble(parts[3]);
            return new Assessment(name, weightage, scoreObtained, maxScore);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    //Decodes one saved line back into an Assessment object.
    //Returns null if the line is invalid.

    @Override
    public String toString() {
        String scoreText = isGraded()
                ? String.format("%.1f / %.1f", scoreObtained, maxScore)
                : "Not graded";

        return String.format("%s (weight: %.1f%%, score: %s)", name, weightage, scoreText);
    }
}

