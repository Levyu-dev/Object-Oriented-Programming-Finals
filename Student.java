
import java.util.ArrayList;

public class Student {

    private String name;
    private String username;
    private int xp;
    private int level;

    private ArrayList<String> completedChallenges;
    private ArrayList<PerformanceRecord> performanceRecords;

    // Constructor
    public Student(String name, String username) {
        this.name = name;
        this.username = username;
        this.xp = 0;
        this.level = 1;

        completedChallenges = new ArrayList<>();
        performanceRecords = new ArrayList<>();
    }

    // Add XP and update level
    public void addXP(int earnedXP) {
        if (earnedXP > 0) {
            xp += earnedXP;
            level = (xp / 100) + 1;
        }
    }

    // Add completed challenge
    public void completeChallenge(String challengeName) {
        completedChallenges.add(challengeName);
    }

    // Save quiz result
    public void recordQuizResult(String topic, int correctAnswers, int totalQuestions) {

        if (topic == null || topic.trim().isEmpty()
                || totalQuestions <= 0
                || correctAnswers < 0
                || correctAnswers > totalQuestions) {

            System.out.println("Invalid quiz result.");
            return;
        }

        PerformanceRecord record = null;

        // Check if topic already exists
        for (PerformanceRecord p : performanceRecords) {
            if (p.getTopic().equalsIgnoreCase(topic)) {
                record = p;
                break;
            }
        }

        // Create new record if topic does not exist
        if (record == null) {
            record = new PerformanceRecord(topic);
            performanceRecords.add(record);
        }

        record.addResult(correctAnswers, totalQuestions);

        // Earn 10 XP per correct answer
        addXP(correctAnswers * 10);
    }

    // Calculate overall accuracy
    public double getOverallAccuracy() {

        int correct = 0;
        int total = 0;

        for (PerformanceRecord p : performanceRecords) {
            correct += p.getTotalCorrect();
            total += p.getTotalQuestions();
        }

        if (total == 0) {
            return 0;
        }

        return (double) correct / total * 100;
    }

    // Display student progress
    public void displayProgress() {

        System.out.println("\n===== STUDENT PROGRESS =====");
        System.out.println("Name: " + name);
        System.out.println("Username: " + username);
        System.out.println("Level: " + level);
        System.out.println("XP: " + xp);

        System.out.println("\nCompleted Challenges:");

        for (String challenge : completedChallenges) {
            System.out.println("- " + challenge);
        }

        System.out.println("\nTopic Performance:");

        for (PerformanceRecord p : performanceRecords) {
            p.displayRecord();
        }

        System.out.printf("\nOverall Accuracy: %.2f%%\n",
                getOverallAccuracy());
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public int getXP() {
        return xp;
    }

    public int getLevel() {
        return level;
    }

    public ArrayList<String> getCompletedChallenges() {
        return completedChallenges;
    }

    public ArrayList<PerformanceRecord> getPerformanceRecords() {
        return performanceRecords;
    }
}
