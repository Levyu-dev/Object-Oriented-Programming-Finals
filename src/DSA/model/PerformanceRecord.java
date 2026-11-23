package DSA.model;

public class PerformanceRecord {

    private String topic;
    private int attempts;
    private int totalCorrect;
    private int totalQuestions;

    // ---- NEW for DSA (additive only) ----
    private String studentId = "";
    private String username = "";
    private int totalXp = 0;
    private Double accuracyOverride = null;

    // Original Phase 1 constructor (unchanged)
    public PerformanceRecord(String topic) {
        this.topic = topic;
        this.attempts = 0;
        this.totalCorrect = 0;
        this.totalQuestions = 0;
    }

    // NEW: constructor used by AlgorithmDataset
    public PerformanceRecord(String studentId, String username, int quizScore,
                             int totalXp, double accuracy) {
        this.topic = username;
        this.attempts = 1;
        this.totalCorrect = quizScore;   // so getTotalCorrect() == quiz score
        this.totalQuestions = 100;
        this.studentId = studentId;
        this.username = username;
        this.totalXp = totalXp;
        this.accuracyOverride = accuracy;
    }

    // Add quiz result
    public boolean addResult(int correctAnswers, int questions) {
        if (questions <= 0 || correctAnswers < 0
                || correctAnswers > questions) {
            return false;
        }

        attempts++;
        totalCorrect += correctAnswers;
        totalQuestions += questions;

        return true;
    }

    // Calculate accuracy
    public double getAccuracy() {
        if (accuracyOverride != null) {      // NEW
            return accuracyOverride;
        }

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

    // NEW getters used by AlgorithmDataset and the benchmark
    public String getStudentId() {
        return studentId;
    }

    public String getUsername() {
        return username;
    }

    public int getQuizScore() {
        return totalCorrect;
    }

    public int getTotalXp() {
        return totalXp;
    }
}