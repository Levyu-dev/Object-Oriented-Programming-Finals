package DSA.algorithms;

import DSA.model.PerformanceRecord;
import DSA.model.Student;
import java.util.ArrayList;

public class BubbleSort {

    // Sorting performance measurements
    public static class SortStats {
        public int comparisons;
        public int swaps;
        public long executionTime;

        public void displayStats() {
            System.out.println("Comparisons: " + comparisons);
            System.out.println("Swaps: " + swaps);
            System.out.println("Execution Time: " + executionTime + " ns");
        }
    }

    // Sort topic records by quiz score
    public static SortStats sortByQuizScore(
            ArrayList<PerformanceRecord> records, boolean ascending) {

        SortStats stats = new SortStats();
        long start = System.nanoTime();

        for (int i = 0; i < records.size() - 1; i++) {
            boolean swapped = false;

            for (int j = 0; j < records.size() - 1 - i; j++) {
                stats.comparisons++;

                int first = records.get(j).getTotalCorrect();
                int second = records.get(j + 1).getTotalCorrect();

                if (ascending ? first > second : first < second) {
                    PerformanceRecord temp = records.get(j);
                    records.set(j, records.get(j + 1));
                    records.set(j + 1, temp);

                    stats.swaps++;
                    swapped = true;
                }
            }

            if (!swapped) {
                break;
            }
        }

        stats.executionTime = System.nanoTime() - start;
        return stats;
    }

    // Sort topic records by accuracy
    public static SortStats sortByAccuracy(
            ArrayList<PerformanceRecord> records, boolean ascending) {

        SortStats stats = new SortStats();
        long start = System.nanoTime();

        for (int i = 0; i < records.size() - 1; i++) {
            boolean swapped = false;

            for (int j = 0; j < records.size() - 1 - i; j++) {
                stats.comparisons++;

                double first = records.get(j).getAccuracy();
                double second = records.get(j + 1).getAccuracy();

                if (ascending ? first > second : first < second) {
                    PerformanceRecord temp = records.get(j);
                    records.set(j, records.get(j + 1));
                    records.set(j + 1, temp);

                    stats.swaps++;
                    swapped = true;
                }
            }

            if (!swapped) {
                break;
            }
        }

        stats.executionTime = System.nanoTime() - start;
        return stats;
    }

    // NEW: Sort records by total XP
    public static SortStats sortByTotalXP(
            ArrayList<PerformanceRecord> records, boolean ascending) {

        SortStats stats = new SortStats();
        long start = System.nanoTime();

        for (int i = 0; i < records.size() - 1; i++) {
            boolean swapped = false;

            for (int j = 0; j < records.size() - 1 - i; j++) {
                stats.comparisons++;

                int first = records.get(j).getTotalXp();
                int second = records.get(j + 1).getTotalXp();

                if (ascending ? first > second : first < second) {
                    PerformanceRecord temp = records.get(j);
                    records.set(j, records.get(j + 1));
                    records.set(j + 1, temp);

                    stats.swaps++;
                    swapped = true;
                }
            }

            if (!swapped) {
                break;
            }
        }

        stats.executionTime = System.nanoTime() - start;
        return stats;
    }

    // Sort students by XP
    public static SortStats sortByXP(
            ArrayList<Student> students, boolean ascending) {

        SortStats stats = new SortStats();
        long start = System.nanoTime();

        for (int i = 0; i < students.size() - 1; i++) {
            boolean swapped = false;

            for (int j = 0; j < students.size() - 1 - i; j++) {
                stats.comparisons++;

                int first = students.get(j).getXP();
                int second = students.get(j + 1).getXP();

                if (ascending ? first > second : first < second) {
                    Student temp = students.get(j);
                    students.set(j, students.get(j + 1));
                    students.set(j + 1, temp);

                    stats.swaps++;
                    swapped = true;
                }
            }

            if (!swapped) {
                break;
            }
        }

        stats.executionTime = System.nanoTime() - start;
        return stats;
    }

    // Sort students by overall accuracy
    public static SortStats sortByOverallAccuracy(
            ArrayList<Student> students, boolean ascending) {

        SortStats stats = new SortStats();
        long start = System.nanoTime();

        for (int i = 0; i < students.size() - 1; i++) {
            boolean swapped = false;

            for (int j = 0; j < students.size() - 1 - i; j++) {
                stats.comparisons++;

                double first = students.get(j).getOverallAccuracy();
                double second = students.get(j + 1).getOverallAccuracy();

                if (ascending ? first > second : first < second) {
                    Student temp = students.get(j);
                    students.set(j, students.get(j + 1));
                    students.set(j + 1, temp);

                    stats.swaps++;
                    swapped = true;
                }
            }

            if (!swapped) {
                break;
            }
        }

        stats.executionTime = System.nanoTime() - start;
        return stats;
    }

    // Verify accuracy sorting
    public static boolean verifyAccuracy(
            ArrayList<PerformanceRecord> records, boolean ascending) {

        for (int i = 0; i < records.size() - 1; i++) {
            if (ascending) {
                if (records.get(i).getAccuracy()
                        > records.get(i + 1).getAccuracy()) {
                    return false;
                }
            } else {
                if (records.get(i).getAccuracy()
                        < records.get(i + 1).getAccuracy()) {
                    return false;
                }
            }
        }

        return true;
    }

    // Verify quiz score sorting
    public static boolean verifyQuizScore(
            ArrayList<PerformanceRecord> records, boolean ascending) {

        for (int i = 0; i < records.size() - 1; i++) {
            if (ascending) {
                if (records.get(i).getTotalCorrect()
                        > records.get(i + 1).getTotalCorrect()) {
                    return false;
                }
            } else {
                if (records.get(i).getTotalCorrect()
                        < records.get(i + 1).getTotalCorrect()) {
                    return false;
                }
            }
        }

        return true;
    }

    // NEW: Verify total XP sorting
    public static boolean verifyTotalXP(
            ArrayList<PerformanceRecord> records, boolean ascending) {

        for (int i = 0; i < records.size() - 1; i++) {
            if (ascending) {
                if (records.get(i).getTotalXp()
                        > records.get(i + 1).getTotalXp()) {
                    return false;
                }
            } else {
                if (records.get(i).getTotalXp()
                        < records.get(i + 1).getTotalXp()) {
                    return false;
                }
            }
        }

        return true;
    }

    // Verify XP sorting
    public static boolean verifyXP(
            ArrayList<Student> students, boolean ascending) {

        for (int i = 0; i < students.size() - 1; i++) {
            if (ascending) {
                if (students.get(i).getXP()
                        > students.get(i + 1).getXP()) {
                    return false;
                }
            } else {
                if (students.get(i).getXP()
                        < students.get(i + 1).getXP()) {
                    return false;
                }
            }
        }

        return true;
    }

    // Verify overall accuracy sorting
    public static boolean verifyOverallAccuracy(
            ArrayList<Student> students, boolean ascending) {

        for (int i = 0; i < students.size() - 1; i++) {
            if (ascending) {
                if (students.get(i).getOverallAccuracy()
                        > students.get(i + 1).getOverallAccuracy()) {
                    return false;
                }
            } else {
                if (students.get(i).getOverallAccuracy()
                        < students.get(i + 1).getOverallAccuracy()) {
                    return false;
                }
            }
        }

        return true;
    }
}
//