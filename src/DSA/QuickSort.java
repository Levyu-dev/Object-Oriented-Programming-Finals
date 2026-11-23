import java.util.*;

public class QuickSort {

    // NEW: XP added
    public enum SortBy {
        Accuracy, Score, XP
    }

    // One shared Random (was created on every recursive call)
    private static final Random RANDOM = new Random();

    public static class SortStats{
        public int comparisons;
        public int swaps;
        public long exetime;

        public void showStats() {
            System.out.println("Comparisons: " + comparisons);
            System.out.println("Swaps:" + swaps);
            System.out.println("Execution Time (ns): " + exetime);
        }
    }

    //Recursive Quicksort with a randomized pivot (Sorted by Accuracy, Score and XP)

    public static SortStats sortRecord(PerformanceRecord[] array, boolean ascending, SortBy criteria) {
        SortStats stats = new SortStats();
        long startTime = System.nanoTime();

        quicksort(array, 0 , array.length - 1, ascending, criteria, stats);

        stats.exetime = System.nanoTime() - startTime;
        return stats;
    }

    public static void quicksort(PerformanceRecord[] array, int lowIndex, int highIndex, boolean ascending, SortBy criteria, SortStats stats) {
        if (lowIndex >= highIndex) {
            return;
        }

        int pivotIndex = RANDOM.nextInt(highIndex - lowIndex + 1) + lowIndex;
        double pivot = getValue(array[pivotIndex], criteria);
        swap(array, pivotIndex, highIndex, stats);

        int leftpoint = partition(array, lowIndex, highIndex, pivot, ascending, criteria, stats);

        quicksort(array, lowIndex, leftpoint - 1, ascending, criteria, stats);
        quicksort(array, leftpoint + 1, highIndex, ascending, criteria, stats);
    }

    //Partitioning (Ascending and Descending Order)
    private static int partition(PerformanceRecord[] array, int lowIndex, int highIndex, double pivot, boolean ascending, SortBy criteria, SortStats stats) {
        int leftpoint = lowIndex;
        int rightpoint = highIndex - 1;

        while (leftpoint < rightpoint) {
            if (ascending) {
                while (leftpoint < rightpoint && compare(getValue(array[leftpoint], criteria), pivot, stats) <= 0){
                    leftpoint++;
                }

                while (leftpoint < rightpoint && compare(getValue(array[rightpoint], criteria), pivot, stats) >= 0) {
                    rightpoint--;
                }
            } 

            else {
                while (leftpoint < rightpoint && compare(getValue(array[leftpoint], criteria), pivot, stats) >= 0){
                    leftpoint++;
                }

                while (leftpoint < rightpoint && compare(getValue(array[rightpoint], criteria), pivot, stats) <= 0) {
                    rightpoint--;
                }
            }

            if (leftpoint < rightpoint) {
                swap(array, leftpoint, rightpoint, stats);
            }
        }

        double leftVal = getValue(array[leftpoint], criteria);
        double highVal = getValue(array[highIndex], criteria);
        boolean shouldSwap = ascending ? compare(leftVal, highVal, stats) > 0 : compare(leftVal, highVal, stats) < 0;

        if (shouldSwap) {
            swap(array, leftpoint, highIndex, stats);
        }
        else {
            leftpoint = highIndex;
        }
        return leftpoint;
    }

    private static double getValue(PerformanceRecord record, SortBy criteria) {
        switch (criteria) {
            case Accuracy:
                return record.getAccuracy();
            case Score:
                return record.getTotalCorrect();
            case XP:                                   // NEW
                return record.getTotalXp();
            default:
                throw new IllegalArgumentException("Unknown Sort Criteria");
        }
    }

    //Switches two objects at given positions in the array
    private static void swap(PerformanceRecord[] array, int index1, int index2, SortStats stats) {
        if (index1 != index2){
            PerformanceRecord temp = array[index1];
            array[index1] = array[index2];
            array[index2] = temp;
            stats.swaps++;
        }
    }


    //Recursive Quicksort with a randomized pivot (Sorted by XP)

    public static SortStats sortStudent(Student[] array, boolean ascending) {
        SortStats stats = new SortStats();
        long startTime = System.nanoTime();

        quicksort(array, 0 , array.length - 1, ascending, stats);

        stats.exetime = System.nanoTime() - startTime;
        return stats;
    }

    public static void quicksort(Student[] array, int lowIndex, int highIndex, boolean ascending, SortStats stats) {
        if (lowIndex >= highIndex) {
            return;
        }

        int pivotIndex = RANDOM.nextInt(highIndex - lowIndex + 1) + lowIndex;
        double pivot = array[pivotIndex].getXP();
        swap(array, pivotIndex, highIndex, stats);

        int leftpoint = partition(array, lowIndex, highIndex, pivot, ascending, stats);

        quicksort(array, lowIndex, leftpoint - 1, ascending, stats);
        quicksort(array, leftpoint + 1, highIndex, ascending, stats);
    }

    //Partitioning (Ascending and Descending Order)
    private static int partition(Student[] array, int lowIndex, int highIndex, double pivot, boolean ascending, SortStats stats) {
        int leftpoint = lowIndex;
        int rightpoint = highIndex - 1;

        while (leftpoint < rightpoint) {
            if (ascending) {
                while (leftpoint < rightpoint && compare(array[leftpoint].getXP(), pivot, stats) <= 0){
                    leftpoint++;
                }

                while (leftpoint < rightpoint && compare(array[rightpoint].getXP(), pivot, stats) >= 0) {
                    rightpoint--;
                }
            } 

            else {
                while (leftpoint < rightpoint && compare(array[leftpoint].getXP(), pivot, stats) >= 0){
                    leftpoint++;
                }

                while (leftpoint < rightpoint && compare(array[rightpoint].getXP(), pivot, stats) <= 0) {
                    rightpoint--;
                }
            }

            if (leftpoint < rightpoint) {
                swap(array, leftpoint, rightpoint, stats);
            }
        }

        double leftVal = array[leftpoint].getXP();
        double highVal = array[highIndex].getXP();
        boolean shouldSwap = ascending ? compare(leftVal, highVal, stats) > 0 : compare(leftVal, highVal, stats) < 0;

        if (shouldSwap) {
            swap(array, leftpoint, highIndex, stats);
        }
        else {
            leftpoint = highIndex;
        }
        return leftpoint;
    }

    //Switches two objects at given positions in the array
    private static void swap(Student[] array, int index1, int index2, SortStats stats) {
        if (index1 != index2){
            Student temp = array[index1];
            array[index1] = array[index2];
            array[index2] = temp;
            stats.swaps++;
        }
    }

    private static int compare(double a , double b, SortStats stats) {
        stats.comparisons++;
        return Double.compare(a, b);
    }
}