package DSA.benchmark;

import App.model.UserSession;
import DSA.SortingAlgorithm;
import DSA.adapters.BaseAdapter;
import DSA.adapters.BubbleSortAdapter;
import DSA.adapters.MergeSortAdapter;
import DSA.adapters.QuickSortAdapter;
import DSA.model.PerformanceRecord;
import DSA.model.Student;
import DSA.algorithms.MergeSort;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class AlgorithmDataset {
    private static final String DATASET_CSV_HEADER =
            "Student_ID,Username,Quiz_Score,Total_XP,Accuracy_Percentage";

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

    public void loadFromUserSession(UserSession session, String section) {
        if (session == null) {
            throw new IllegalArgumentException("User session cannot be null.");
        }
        if (section == null || section.trim().isEmpty()) {
            throw new IllegalArgumentException("Section cannot be blank.");
        }

        List<PerformanceRecord> records = new ArrayList<>();
        int idCounter = 1;
        for (UserSession.StudentRecord student : session.getStudentsInSection(section.trim())) {
            PerformanceRecord record = new PerformanceRecord(
                    String.format(Locale.ROOT, "STU-%04d", idCounter++),
                    student.getName(),
                    student.getAverageQuizScore(),
                    student.getXp(),
                    student.getAccuracy());
            if (isValidRecord(record)) {
                records.add(record);
            }
        }
        masterDataset = records;
    }

    // 2. Generate datasets for sizes (100, 500, 1000, 5000) and all 6 conditions
    public List<PerformanceRecord> generateDataset(int size, Condition condition) {
        if (size < 0) {
            throw new IllegalArgumentException("Dataset size cannot be negative.");
        }
        if (condition == null) {
            throw new IllegalArgumentException("Dataset condition cannot be null.");
        }

        List<PerformanceRecord> dataset = new ArrayList<>();

        if (condition == Condition.EMPTY) {
            this.masterDataset = dataset;
            return getDatasetCopy();
        }

        int targetSize = (condition == Condition.SINGLE_RECORD) ? 1 : size;
        Random rand = new Random(42);

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
                Collections.shuffle(dataset, rand);
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
        if (Double.isNaN(record.getAccuracy()) || Double.isInfinite(record.getAccuracy())
                || record.getAccuracy() < 0.0 || record.getAccuracy() > 100.0) return false;
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
    public void exportToCSV(List<PerformanceRecord> records, String filePath) throws IOException {
        File file = new File(filePath);
        createParentDirectory(file);
        try (BufferedWriter writer = Files.newBufferedWriter(
                file.toPath(), StandardCharsets.UTF_8)) {
            writer.write(DATASET_CSV_HEADER);
            writer.newLine();
            for (PerformanceRecord r : records) {
                writer.write(String.format(Locale.ROOT, "%s,%s,%d,%d,%.2f%n",
                        escapeCsv(r.getStudentId()), escapeCsv(r.getUsername()),
                        r.getQuizScore(), r.getTotalXp(), r.getAccuracy()));
            }
        }
    }

    public List<PerformanceRecord> loadFromCSV(String filePath) throws IOException {
        List<PerformanceRecord> records = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(
                new File(filePath).toPath(), StandardCharsets.UTF_8)) {
            String header = reader.readLine();
            if (!DATASET_CSV_HEADER.equals(header)) {
                throw new IOException("Unexpected dataset CSV header in " + filePath);
            }

            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }

                List<String> fields;
                try {
                    fields = parseCsvLine(line);
                    if (fields.size() != 5) {
                        throw new IllegalArgumentException("Expected 5 columns.");
                    }
                    PerformanceRecord record = new PerformanceRecord(
                            fields.get(0),
                            fields.get(1),
                            Integer.parseInt(fields.get(2)),
                            Integer.parseInt(fields.get(3)),
                            Double.parseDouble(fields.get(4)));
                    if (!isValidRecord(record)) {
                        throw new IllegalArgumentException("Record contains invalid values.");
                    }
                    records.add(record);
                } catch (IllegalArgumentException exception) {
                    throw new IOException(
                            "Invalid dataset CSV at line " + lineNumber + ": "
                                    + exception.getMessage(),
                            exception);
                }
            }
        }

        masterDataset = records;
        return getDatasetCopy();
    }

    public void exportBenchmarkResultsCSV(
            String filePath, String criteria, boolean ascending) throws IOException {
        if (!SortingAlgorithm.CRITERIA_SCORE.equals(criteria)
                && !SortingAlgorithm.CRITERIA_XP.equals(criteria)
                && !SortingAlgorithm.CRITERIA_ACCURACY.equals(criteria)) {
            throw new IllegalArgumentException("Unknown sorting criteria: " + criteria);
        }

        File file = new File(filePath);
        createParentDirectory(file);
        try (BufferedWriter writer = Files.newBufferedWriter(
                file.toPath(), StandardCharsets.UTF_8)) {
            writer.write("Dataset_Size,Condition,Algorithm,Time_ms,Comparisons,Swaps_Moves");
            writer.newLine();

            int[] sizes = {100, 500, 1000, 5000};
            for (int size : sizes) {
                List<PerformanceRecord> original = generateDataset(size, Condition.RANDOM);
                BaseAdapter[] algorithms = {
                        new BubbleSortAdapter(),
                        new MergeSortAdapter(),
                        new QuickSortAdapter()
                };
                for (BaseAdapter algorithm : algorithms) {
                    List<PerformanceRecord> sortedRecords = copyRecords(original);
                    algorithm.sort(sortedRecords, criteria, ascending);
                    writer.write(String.format(
                            Locale.ROOT,
                            "%d,RANDOM,%s,%.6f,%d,%d%n",
                            size,
                            escapeCsv(algorithm.getName()),
                            algorithm.getExecutionTime() / 1_000_000.0,
                            algorithm.getComparisons(),
                            algorithm.getSwaps()));
                }
            }
        }
    }

    private static void createParentDirectory(File file) throws IOException {
        File parent = file.getParentFile();
        if (parent != null && !parent.isDirectory() && !parent.mkdirs()) {
            throw new IOException("Unable to create directory: " + parent);
        }
    }

    private static List<PerformanceRecord> copyRecords(List<PerformanceRecord> records) {
        List<PerformanceRecord> copy = new ArrayList<>(records.size());
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

    private static List<String> parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean quoted = false;
        boolean quoteClosed = false;

        for (int index = 0; index < line.length(); index++) {
            char character = line.charAt(index);
            if (quoted) {
                if (character == '"') {
                    if (index + 1 < line.length() && line.charAt(index + 1) == '"') {
                        field.append('"');
                        index++;
                    } else {
                        quoted = false;
                        quoteClosed = true;
                    }
                } else {
                    field.append(character);
                }
            } else if (character == ',') {
                fields.add(field.toString());
                field.setLength(0);
                quoteClosed = false;
            } else if (character == '"' && field.length() == 0 && !quoteClosed) {
                quoted = true;
            } else if (quoteClosed) {
                throw new IllegalArgumentException("Unexpected character after quoted field.");
            } else {
                field.append(character);
            }
        }

        if (quoted) {
            throw new IllegalArgumentException("Unclosed quoted field.");
        }
        fields.add(field.toString());
        return fields;
    }

    private static String escapeCsv(String value) {
        if (value.indexOf(',') < 0 && value.indexOf('"') < 0
                && value.indexOf('\n') < 0 && value.indexOf('\r') < 0) {
            return value;
        }
        return "\"" + value.replace("\"", "\"\"") + "\"";
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

    public static void main(String[] args) throws IOException {
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

        manager.exportBenchmarkResultsCSV(
                "datasets/benchmark_results.csv", SortingAlgorithm.CRITERIA_SCORE, false);
        System.out.println("\nExported benchmark results to datasets/benchmark_results.csv");
    }
}
