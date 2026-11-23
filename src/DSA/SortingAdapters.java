package DSA;

import java.util.ArrayList;
import java.util.List;

// Adapters: wrap the teammates' static sorting classes (BubbleSort, MergeSort, QuickSort)
// so they can be used through the common SortingAlgorithm interface.
// Teammates do NOT need to change their class structure.

abstract class BaseAdapter implements SortingAlgorithm {
    protected long comparisons;
    protected long swaps;
    protected long time;

    public long getComparisons() { return comparisons; }
    public long getSwaps() { return swaps; }
    public long getExecutionTime() { return time; }
}

class BubbleSortAdapter extends BaseAdapter {
    public String getName() { return "Bubble Sort"; }

    public void sort(List<PerformanceRecord> records, String criteria, boolean ascending) {
        ArrayList<PerformanceRecord> list = new ArrayList<>(records);
        BubbleSort.SortStats stats;
        switch (criteria) {
            case CRITERIA_SCORE:    stats = BubbleSort.sortByQuizScore(list, ascending); break;
            case CRITERIA_XP:       stats = BubbleSort.sortByTotalXP(list, ascending); break;
            case CRITERIA_ACCURACY: stats = BubbleSort.sortByAccuracy(list, ascending); break;
            default: throw new IllegalArgumentException("Unknown criteria: " + criteria);
        }
        comparisons = stats.comparisons;
        swaps = stats.swaps;
        time = stats.executionTime;
        for (int i = 0; i < list.size(); i++) records.set(i, list.get(i));
    }
}

class MergeSortAdapter extends BaseAdapter {
    public String getName() { return "Merge Sort"; }

    public void sort(List<PerformanceRecord> records, String criteria, boolean ascending) {
        ArrayList<PerformanceRecord> list = new ArrayList<>(records);
        MergeSort.SortStats stats;
        switch (criteria) {
            case CRITERIA_SCORE:    stats = MergeSort.sortByQuizScore(list, ascending); break;
            case CRITERIA_XP:       stats = MergeSort.sortByTotalXP(list, ascending); break;
            case CRITERIA_ACCURACY: stats = MergeSort.sortByAccuracy(list, ascending); break;
            default: throw new IllegalArgumentException("Unknown criteria: " + criteria);
        }
        comparisons = stats.comparisons;
        swaps = stats.movements;          // Merge Sort reports movements
        time = stats.executionTime;
        for (int i = 0; i < list.size(); i++) records.set(i, list.get(i));
    }
}

class QuickSortAdapter extends BaseAdapter {
    public String getName() { return "Quick Sort"; }

    public void sort(List<PerformanceRecord> records, String criteria, boolean ascending) {
        PerformanceRecord[] array = records.toArray(new PerformanceRecord[0]); // Quick Sort uses arrays
        QuickSort.SortBy by;
        switch (criteria) {
            case CRITERIA_SCORE:    by = QuickSort.SortBy.Score; break;
            case CRITERIA_XP:       by = QuickSort.SortBy.XP; break;
            case CRITERIA_ACCURACY: by = QuickSort.SortBy.Accuracy; break;
            default: throw new IllegalArgumentException("Unknown criteria: " + criteria);
        }
        QuickSort.SortStats stats = QuickSort.sortRecord(array, ascending, by);
        comparisons = stats.comparisons;
        swaps = stats.swaps;
        time = stats.exetime;
        for (int i = 0; i < array.length; i++) records.set(i, array[i]);
    }
}