package App;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class AlgorithmDataset {
    public enum Condition {
        RANDOM, SORTED, REVERSE_SORTED, DUPLICATES, EMPTY, SINGLE_RECORD
    }

    private final List<PerformanceRecord> records = new ArrayList<>();

    public void loadFromUserSession(UserSession session, String section) {
        records.clear();
        int id = 1;

        for (UserSession.StudentRecord student : session.getStudentsInSection(section)) {
            int xp = student.getXp();
            int quizzes = student.getCompletedQuizzes();
            int score = quizzes == 0 ? 50 : Math.min(100, (xp / quizzes) % 101);
            double accuracy = round(score * 0.9 + 10);

            PerformanceRecord record = new PerformanceRecord(
                    String.format("STU-%04d", id++),
                    student.getName(),
                    score,
                    xp,
                    accuracy);
            if (isValidRecord(record)) {
                records.add(record);
            }
        }
    }

    public List<PerformanceRecord> generateDataset(int size, Condition condition) {
        if (size < 0) {
            throw new IllegalArgumentException("Dataset size cannot be negative.");
        }

        records.clear();
        if (condition == Condition.EMPTY) {
            return getDatasetCopy();
        }

        int count = condition == Condition.SINGLE_RECORD ? 1 : size;
        Random random = new Random();
        for (int i = 1; i <= count; i++) {
            int score = condition == Condition.DUPLICATES
                    ? (random.nextInt(5) + 1) * 20
                    : random.nextInt(101);
            int xp = score * 15 + random.nextInt(500);
            double accuracy = round(Math.min(100, score * 0.8 + random.nextDouble() * 20));

            PerformanceRecord record = new PerformanceRecord(
                    String.format("STU-%04d", i),
                    "Student_" + i,
                    score,
                    xp,
                    accuracy);
            if (isValidRecord(record)) {
                records.add(record);
            }
        }

        switch (condition) {
            case SORTED:
                records.sort(Comparator.comparingInt(PerformanceRecord::getQuizScore));
                break;
            case REVERSE_SORTED:
                records.sort(Comparator.comparingInt(PerformanceRecord::getQuizScore).reversed());
                break;
            case RANDOM:
            case DUPLICATES:
                Collections.shuffle(records);
                break;
            default:
                break;
        }

        return getDatasetCopy();
    }

    public boolean isValidRecord(PerformanceRecord record) {
        return record != null
                && hasText(record.getStudentId())
                && hasText(record.getUsername())
                && record.getQuizScore() >= 0
                && record.getQuizScore() <= 100
                && record.getTotalXp() >= 0
                && record.getAccuracy() >= 0
                && record.getAccuracy() <= 100
                && Double.isFinite(record.getAccuracy());
    }

    private double round(double value) {
        return Math.round(value * 100) / 100.0;
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public List<PerformanceRecord> getDatasetCopy() {
        List<PerformanceRecord> copy = new ArrayList<>();
        for (PerformanceRecord record : records) {
            copy.add(new PerformanceRecord(
                    record.getStudentId(),
                    record.getUsername(),
                    record.getQuizScore(),
                    record.getTotalXp(),
                    record.getAccuracy()));
        }
        return copy;
    }

    public void exportToCSV(List<PerformanceRecord> data, String filePath) {
        Path path = Path.of(filePath);
        try {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                writer.write("Student_ID,Username,Quiz_Score,Total_XP,Accuracy_Percentage");
                writer.newLine();
                for (PerformanceRecord record : data) {
                    writer.write(csv(record.getStudentId()) + ","
                            + csv(record.getUsername()) + ","
                            + record.getQuizScore() + ","
                            + record.getTotalXp() + ","
                            + String.format(java.util.Locale.ROOT, "%.2f", record.getAccuracy()));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not export dataset to " + path, e);
        }
    }

    private String csv(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    public void printLeaderboardDemo(List<PerformanceRecord> data, String title, int limit) {
        System.out.println("\n=== " + title + " ===");
        if (data.isEmpty()) {
            System.out.println("No student records available.");
            return;
        }

        System.out.printf("%-6s | %-10s | %-15s | %-8s | %-10s | %-10s%n",
                "Rank", "ID", "Username", "Score", "Total XP", "Accuracy");
        System.out.println("-------------------------------------------------------------------------");
        for (int i = 0; i < Math.min(Math.max(0, limit), data.size()); i++) {
            PerformanceRecord record = data.get(i);
            System.out.printf("#%-5d | %-10s | @%-14s | %-8d | %-10d | %.2f%%%n",
                    i + 1,
                    record.getStudentId(),
                    record.getUsername(),
                    record.getQuizScore(),
                    record.getTotalXp(),
                    record.getAccuracy());
        }
    }

    public static void main(String[] args) {
        AlgorithmDataset manager = new AlgorithmDataset();
        manager.loadFromUserSession(new UserSession(), "OOP101");
        manager.exportToCSV(manager.getDatasetCopy(), "datasets/dataset_session_OOP101.csv");

        int[] sizes = {100, 500, 1000, 5000};
        for (int size : sizes) {
            for (Condition condition : Condition.values()) {
                if (condition == Condition.EMPTY || condition == Condition.SINGLE_RECORD) {
                    continue;
                }
                List<PerformanceRecord> data = manager.generateDataset(size, condition);
                String file = String.format("datasets/dataset_%d_%s.csv",
                        size, condition.name().toLowerCase());
                manager.exportToCSV(data, file);
            }
        }

        manager.exportToCSV(manager.generateDataset(0, Condition.EMPTY), "datasets/dataset_0_empty.csv");
        manager.exportToCSV(manager.generateDataset(1, Condition.SINGLE_RECORD),
                "datasets/dataset_1_single_record.csv");

        List<PerformanceRecord> leaderboard = manager.generateDataset(100, Condition.RANDOM);
        manager.printLeaderboardDemo(leaderboard, "Before sorting", 5);
        leaderboard.sort(Comparator.comparingInt(PerformanceRecord::getQuizScore).reversed());
        manager.printLeaderboardDemo(leaderboard, "After sorting", 5);
    }

    public static class PerformanceRecord {
        private final String studentId;
        private final String username;
        private final int quizScore;
        private final int totalXp;
        private final double accuracy;

        public PerformanceRecord(String studentId, String username, int quizScore, int totalXp, double accuracy) {
            this.studentId = studentId;
            this.username = username;
            this.quizScore = quizScore;
            this.totalXp = totalXp;
            this.accuracy = accuracy;
        }

        public String getStudentId() { return studentId; }
        public String getUsername() { return username; }
        public int getQuizScore() { return quizScore; }
        public int getTotalXp() { return totalXp; }
        public double getAccuracy() { return accuracy; }
    }
}
