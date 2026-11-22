
package student;

import java.util.ArrayList;
import java.util.Comparator;

public class MergeSort {

    // Sorting performance measurements
    public static class SortStats {
        public int comparisons;
        public int movements;
        public long executionTime;

        public void displayStats() {
            System.out.println("Comparisons: " + comparisons);
            System.out.println("Movements: " + movements);
            System.out.println("Execution Time: " + executionTime + " ns");
        }
    }

    // Sort topic records by quiz score
    public static SortStats sortByQuizScore(
            ArrayList<PerformanceRecord> records, boolean ascending) {

        Comparator<PerformanceRecord> order =
                Comparator.comparingInt(PerformanceRecord::getTotalCorrect);

        return sort(records, ascending ? order : order.reversed());
    }

    // Sort topic records by accuracy
    public static SortStats sortByAccuracy(
            ArrayList<PerformanceRecord> records, boolean ascending) {

        Comparator<PerformanceRecord> order =
                Comparator.comparingDouble(PerformanceRecord::getAccuracy);

        return sort(records, ascending ? order : order.reversed());
    }

    // Sort students by XP
    public static SortStats sortByXP(
            ArrayList<Student> students, boolean ascending) {

        Comparator<Student> order =
                Comparator.comparingInt(Student::getXP);

        return sort(students, ascending ? order : order.reversed());
    }

    // Sort students by overall accuracy
    public static SortStats sortByOverallAccuracy(
            ArrayList<Student> students, boolean ascending) {

        Comparator<Student> order =
                Comparator.comparingDouble(Student::getOverallAccuracy);

        return sort(students, ascending ? order : order.reversed());
    }

    // Main Merge Sort method
    private static <T> SortStats sort(
            ArrayList<T> records, Comparator<T> order) {

        SortStats stats = new SortStats();

        long start = System.nanoTime();

        if (records != null && records.size() > 1) {
            mergeSort(records, 0, records.size() - 1, order, stats);
        }

        stats.executionTime = System.nanoTime() - start;

        return stats;
    }

    // Divide the list recursively
    private static <T> void mergeSort(
            ArrayList<T> records, int left, int right,
            Comparator<T> order, SortStats stats) {

        if (left >= right) {
            return;
        }

        int middle = left + (right - left) / 2;

        mergeSort(records, left, middle, order, stats);
        mergeSort(records, middle + 1, right, order, stats);

        merge(records, left, middle, right, order, stats);
    }

    // Merge the sorted sections
    private static <T> void merge(
            ArrayList<T> records, int left, int middle,
            int right, Comparator<T> order, SortStats stats) {

        ArrayList<T> leftList =
                new ArrayList<>(records.subList(left, middle + 1));

        ArrayList<T> rightList =
                new ArrayList<>(records.subList(middle + 1, right + 1));

        // Count records copied into temporary lists
        stats.movements += leftList.size() + rightList.size();

        int i = 0;
        int j = 0;
        int k = left;

        // Compare and merge both sections
        while (i < leftList.size() && j < rightList.size()) {

            stats.comparisons++;

            if (order.compare(leftList.get(i), rightList.get(j)) <= 0) {
                records.set(k++, leftList.get(i++));
            } else {
                records.set(k++, rightList.get(j++));
            }

            stats.movements++;
        }

        // Copy remaining records from the left section
        while (i < leftList.size()) {
            records.set(k++, leftList.get(i++));
            stats.movements++;
        }

        // Copy remaining records from the right section
        while (j < rightList.size()) {
            records.set(k++, rightList.get(j++));
            stats.movements++;
        }
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
