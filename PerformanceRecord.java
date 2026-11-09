
public class PerformanceRecord {

    private String topic;
    private int attempts;
    private int totalCorrect;
    private int totalQuestions;

    // Constructor
    public PerformanceRecord(String topic) {
        this.topic = topic;
        this.attempts = 0;
        this.totalCorrect = 0;
        this.totalQuestions = 0;
    }

    // Add quiz result
    public void addResult(int correctAnswers, int questions) {

        if (questions <= 0 || correctAnswers < 0
                || correctAnswers > questions) {

            System.out.println("Invalid result.");
            return;
        }

        attempts++;
        totalCorrect += correctAnswers;
        totalQuestions += questions;
    }

    // Calculate accuracy
    public double getAccuracy() {

        if (totalQuestions == 0) {
            return 0;
        }

        return (double) totalCorrect / totalQuestions * 100;
    }

    // Display performance
    public void displayRecord() {

        System.out.println("\nTopic: " + topic);
        System.out.println("Attempts: " + attempts);
        System.out.println("Correct Answers: " + totalCorrect);
        System.out.println("Total Questions: " + totalQuestions);

        System.out.printf("Accuracy: %.2f%%\n", getAccuracy());
    }

    // Getters
    public String getTopic() {
        return topic;
    }

    public int getAttempts() {
        return attempts;
    }

    public int getTotalCorrect() {
        return totalCorrect;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }
}
