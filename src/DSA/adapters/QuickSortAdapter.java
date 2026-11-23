package DSA.adapters;

import DSA.algorithms.QuickSort;
import DSA.model.PerformanceRecord;
import java.util.List;

public class QuickSortAdapter extends BaseAdapter {
    public String getName() { return "Quick Sort"; }

    public void sort(List<PerformanceRecord> records, String criteria, boolean ascending) {
        PerformanceRecord[] array = records.toArray(new PerformanceRecord[0]);
        QuickSort.SortBy by;
        switch (criteria) {
            case CRITERIA_SCORE: by = QuickSort.SortBy.Score; break;
            case CRITERIA_XP: by = QuickSort.SortBy.XP; break;
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
