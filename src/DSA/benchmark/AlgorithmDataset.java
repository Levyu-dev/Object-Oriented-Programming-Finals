package DSA.benchmark;

import DSA.algorithms.MergeSort;
import DSA.model.PerformanceRecord;
import DSA.model.Student;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class AlgorithmDataset {

    public enum Condition {
        RANDOM,
        SORTED,
        REVERSE_SORTED,
        DUPLICATES,
        EMPTY,
        SINGLE_RECORD
    }

    private List<PerformanceRecord> masterDataset;

    public AlgorithmDataset() {
        this.masterDataset = new ArrayList<>();
    }

    // 1. Retrieve & convert real student data (Student objects) into dataset records.
    //    (If your Phase 1 UserSession is in this folder you can add your old
    //     loadFromUserSession method back here.)
    public void loadFromStudents(List<Student> students) {
        this.masterDataset.clear();
        int idCounter = 1;

        for (Student s : students) {
            String studentId = String.format("STU-%04d", idCounter++);
            double accuracy = Math.min(100.0, Math.round(s.getOverallAccuracy() * 100.0) / 100.0);
            int quizScore = (int) Math.round(accuracy);

            PerformanceRecord record = new PerformanceRecord(
                    studentId, s.getUsername(), quizScore, s.getXP(), accuracy);
            if (isValidRecord(record)) {
                this.masterDataset.add(record);
            }
        }
    }

    // 2. Generate datasets for sizes (100, 500, 1000, 5000) and all 6 conditions
    public List<PerformanceRecord> generateDataset(int size, Condition condition) {
        List<PerformanceRecord> dataset = new ArrayList<>();

        if (condition == Condition.EMPTY) {
            this.masterDataset = dataset;
            return getDatasetCopy();
        }

        int targetSize = (condition == Condition.SINGLE_RECORD) ? 1 : size;
        Random rand = new Random();

        for (int i = 1; i <= targetSize; i++) {
            String studentId = String.format("STU-%04d", i);
            String username = "Student_" + i;

            int quizScore = (condition == Condition.DUPLICATES)
                    ? (rand.nextInt(5) + 1) * 20   // Forces duplicate scores: 20, 40, 60, 80, 100
                    : rand.nextInt(101);           // 0 to 100

            int totalXp = quizScore * 15 + rand.nextInt(500);
            double accuracy = Math.min(100.0, Math.round((quizScore * 0.8 + rand.nextDouble() * 20.0) * 100.0) / 100.0);

            PerformanceRecord record = new PerformanceRecord(studentId, username, quizScore, totalXp, accuracy);

            if (isValidRecord(record)) {
                dataset.add(record);
            }
        }

        // Arranging the TEST DATA (this is data preparation, not one of the 3 algorithms)
        switch (condition) {
            case SORTED:
                dataset.sort(Comparator.comparingInt(PerformanceRecord::getQuizScore));
                break;
            case REVERSE_SORTED:
                dataset.sort(Comparator.comparingInt(PerformanceRecord::getQuizScore).reversed());
                break;
            case RANDOM:
            case DUPLICATES:
                Collections.shuffle(dataset);
                break;
            default:
                break;
        }

        this.masterDataset = dataset;
        return getDatasetCopy();
    }

    // 3. Preprocessing: Check for missing or invalid values before testing
    public boolean isValidRecord(PerformanceRecord record) {
        if (record == null) return false;
        if (record.getStudentId() == null || record.getStudentId().trim().isEmpty()) return false;
        if (record.getUsername() == null || record.getUsername().trim().isEmpty()) return false;
        if (record.getQuizScore() < 0 || record.getQuizScore() > 100) return false;
        if (record.getTotalXp() < 0) return false;
        if (record.getAccuracy() < 0.0 || record.getAccuracy() > 100.0) return false;
        return true;
    }

    // 4. Deep Copy: Ensures Bubble, Merge, and Quick Sort get identical independent copies
    public List<PerformanceRecord> getDatasetCopy() {
        List<PerformanceRecord> copy = new ArrayList<>();
        for (PerformanceRecord r : this.masterDataset) {
            copy.add(new PerformanceRecord(
                    r.getStudentId(),
                    r.getUsername(),
                    r.getQuizScore(),
                    r.getTotalXp(),
                    r.getAccuracy()
            ));
        }
        return copy;
    }

    // 5. Export prepared datasets to CSV files for documentation and submission
    public void exportToCSV(List<PerformanceRecord> records, String filePath) {
        File file = new File(filePath);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("Student_ID,Username,Quiz_Score,Total_XP,Accuracy_Percentage");
            for (PerformanceRecord r : records) {
                writer.printf("%s,%s,%d,%d,%.2f%n",
                        r.getStudentId(), r.getUsername(), r.getQuizScore(), r.getTotalXp(), r.getAccuracy());
            }
        } catch (IOException e) {
            System.err.println("Error exporting dataset to CSV: " + e.getMessage());
        }
    }

    // 6. Formatted Leaderboard Sorting Demonstration
    public void printLeaderboardDemo(List<PerformanceRecord> records, String stageTitle, int limit) {
        System.out.println("\n=== " + stageTitle + " ===");
        if (records.isEmpty()) {
            System.out.println("No student records available.");
            return;
        }
        System.out.printf("%-6s | %-10s | %-15s | %-8s | %-10s | %-10s%n",
                "Rank", "ID", "Username", "Score", "Total XP", "Accuracy");
        System.out.println("-------------------------------------------------------------------------");
        for (int i = 0; i < Math.min(limit, records.size()); i++) {
            PerformanceRecord r = records.get(i);
            System.out.printf("#%-5d | %-10s | @%-14s | %-8d | %-10d | %.2f%%%n",
                    (i + 1), r.getStudentId(), r.getUsername(), r.getQuizScore(), r.getTotalXp(), r.getAccuracy());
        }
    }

    public static void main(String[] args) {
        AlgorithmDataset manager = new AlgorithmDataset();

        // 1. Load & export real student data
        ArrayList<Student> students = new ArrayList<>();
        Student s1 = new Student("Iyce", "iyce123");
        s1.recordQuizResult("Java", 8, 10);
        s1.recordQuizResult("HTML", 5, 10);
        Student s2 = new Student("Clint", "clint456");
        s2.recordQuizResult("Java", 9, 10);
        Student s3 = new Student("Gab", "gab789");
        s3.recordQuizResult("Java", 6, 10);
        students.add(s1);
        students.add(s2);
        students.add(s3);

        manager.loadFromStudents(students);
        List<PerformanceRecord> sessionData = manager.getDatasetCopy();
        manager.exportToCSV(sessionData, "datasets/dataset_students.csv");
        manager.printLeaderboardDemo(sessionData, "REAL STUDENT DATA", 10);

        // 2. Generate, verify, and export all 16 standard test datasets
        int[] sizes = {100, 500, 1000, 5000};
        System.out.println("\n=== GENERATING & EXPORTING STANDARD DATASETS ===");
        for (int size : sizes) {
            for (Condition cond : Condition.values()) {
                if (cond == Condition.EMPTY || cond == Condition.SINGLE_RECORD) continue;
                List<PerformanceRecord> ds = manager.generateDataset(size, cond);
                String fileName = String.format("datasets/dataset_%d_%s.csv", size, cond.name().toLowerCase());
                manager.exportToCSV(ds, fileName);
                System.out.printf("Verified & Exported -> %-40s | Valid Records: %d%n", fileName, ds.size());
            }
        }

        // 3. Generate, verify, and export Edge Cases
        System.out.println("\n=== GENERATING & EXPORTING EDGE CASE DATASETS ===");
        List<PerformanceRecord> emptyDs = manager.generateDataset(0, Condition.EMPTY);
        manager.exportToCSV(emptyDs, "datasets/dataset_0_empty.csv");
        System.out.println("Verified & Exported -> datasets/dataset_0_empty.csv          | Valid Records: " + emptyDs.size());

        List<PerformanceRecord> singleDs = manager.generateDataset(1, Condition.SINGLE_RECORD);
        manager.exportToCSV(singleDs, "datasets/dataset_1_single_record.csv");
        System.out.println("Verified & Exported -> datasets/dataset_1_single_record.csv  | Valid Records: " + singleDs.size());

        // 4. Before & After Leaderboard Sorting Demonstration (uses the REAL Merge Sort)
        List<PerformanceRecord> unsorted = manager.generateDataset(100, Condition.RANDOM);
        manager.printLeaderboardDemo(unsorted, "LEADERBOARD BEFORE SORTING (RANDOM - 100 RECORDS)", 5);

        ArrayList<PerformanceRecord> sorted = new ArrayList<>(manager.getDatasetCopy());
        MergeSort.sortByQuizScore(sorted, false);
        manager.printLeaderboardDemo(sorted, "LEADERBOARD AFTER SORTING (DESCENDING QUIZ SCORE)", 5);
    }
}
//