package Quiz;

public class QuizResult {
    private final String modeName;
    private final int totalQuestions;
    private final int answered;
    private final int correct;
    private final int incorrect;
    private final double accuracy;
    private final int xpEarned;
    private final int passingScore;
    private final boolean passed;

    public QuizResult(String modeName, int totalQuestions, int answered,
                      int correct, int xpEarned, int passingScore) {
        this.modeName = modeName;
        this.totalQuestions = totalQuestions;
        this.answered = answered;
        this.correct = correct;
        this.incorrect = answered - correct;
        this.accuracy = answered == 0 ? 0 : (correct * 100.0) / answered;
        this.xpEarned = xpEarned;
        this.passingScore = passingScore;
        double percentOfTotal = totalQuestions == 0 ? 0 : (correct * 100.0) / totalQuestions;
        this.passed = percentOfTotal >= passingScore;
    }

    public String getModeName() { return modeName; }
    public int getTotalQuestions() { return totalQuestions; }
    public int getAnswered() { return answered; }
    public int getCorrect() { return correct; }
    public int getIncorrect() { return incorrect; }
    public double getAccuracy() { return accuracy; }
    public int getXpEarned() { return xpEarned; }
    public int getPassingScore() { return passingScore; }
    public boolean isPassed() { return passed; }

    public void display() {
        System.out.println("Total questions: " + totalQuestions);
        System.out.println("Correct answers: " + correct);
        System.out.println("Incorrect answers: " + incorrect);
        if (answered < totalQuestions) {
            System.out.println("Unanswered: " + (totalQuestions - answered));
        }
        System.out.printf("Accuracy: %.1f%%%n", accuracy);
        System.out.println("XP Earned: " + xpEarned);
        if (passingScore > 0) {
            System.out.println("Passing score: " + passingScore + "% -> " + (passed ? "PASSED" : "NOT PASSED"));
        }
    }
}