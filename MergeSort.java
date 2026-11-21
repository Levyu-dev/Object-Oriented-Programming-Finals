
import java.util.Comparator;
import student.PerformanceRecord;
import student.Student;

public class MergeSort {

    private static int comparisons = 0;
    private static int movements = 0;
    private static long executionTime = 0;

    // Sort by accuracy
    public static void sortByAccuracy(PerformanceRecord[] array, boolean ascending) {
        Comparator<PerformanceRecord> order =
                Comparator.comparingDouble(PerformanceRecord::getAccuracy);

        if (!ascending) {
            order = order.reversed();
        }

        sort(array, order);
    }

    // Sort by quiz score
    public static void sortByQuizScore(PerformanceRecord[] array, boolean ascending) {
        Comparator<PerformanceRecord> order =
                Comparator.comparingInt(PerformanceRecord::getTotalCorrect);

        if (!ascending) {
            order = order.reversed();
        }

        sort(array, order);
    }

    // Sort by XP
    public static void sortByXP(Student[] array, boolean ascending) {
        Comparator<Student> order =
                Comparator.comparingInt(Student::getXP);

        if (!ascending) {
            order = order.reversed();
        }

        sort(array, order);
    }

    // Main Merge Sort method
    private static <T> void sort(T[] array, Comparator<T> order) {
        comparisons = 0;
        movements = 0;

        long start = System.nanoTime();

        if (array != null && array.length > 1) {
            mergeSort(array, 0, array.length - 1, order);
        }

        executionTime = System.nanoTime() - start;
    }

    // Divide the array recursively
    private static <T> void mergeSort(T[] array, int left, int right,
            Comparator<T> order) {

        if (left >= right) {
            return;
        }

        int middle = left + (right - left) / 2;

        mergeSort(array, left, middle, order);
        mergeSort(array, middle + 1, right, order);

        merge(array, left, middle, right, order);
    }

    // Merge the sorted sections
    private static <T> void merge(T[] array, int left, int middle,
            int right, Comparator<T> order) {

        int leftSize = middle - left + 1;
        int rightSize = right - middle;

        T[] leftArray = java.util.Arrays.copyOfRange(
                array, left, middle + 1);

        T[] rightArray = java.util.Arrays.copyOfRange(
                array, middle + 1, right + 1);

        movements += leftSize + rightSize;

        int i = 0;
        int j = 0;
        int k = left;

        while (i < leftSize && j < rightSize) {
            comparisons++;

            if (order.compare(leftArray[i], rightArray[j]) <= 0) {
                array[k++] = leftArray[i++];
            } else {
                array[k++] = rightArray[j++];
            }

            movements++;
        }

        while (i < leftSize) {
            array[k++] = leftArray[i++];
            movements++;
        }

        while (j < rightSize) {
            array[k++] = rightArray[j++];
            movements++;
        }
    }

    // Get sorting statistics
    public static int getComparisons() {
        return comparisons;
    }

    public static int getMovements() {
        return movements;
    }

    public static long getExecutionTime() {
        return executionTime;
    }
}
