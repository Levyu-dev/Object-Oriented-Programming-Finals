package DSA.adapters;

import DSA.algorithms.MergeSort;
import DSA.model.PerformanceRecord;
import java.util.ArrayList;
import java.util.List;

public class MergeSortAdapter extends BaseAdapter {
    public String getName() { return "Merge Sort"; }

    public void sort(List<PerformanceRecord> records, String criteria, boolean ascending) {
        ArrayList<PerformanceRecord> list = new ArrayList<>(records);
        MergeSort.SortStats stats;
        switch (criteria) {
            case CRITERIA_SCORE: stats = MergeSort.sortByQuizScore(list, ascending); break;
            case CRITERIA_XP: stats = MergeSort.sortByTotalXP(list, ascending); break;
            case CRITERIA_ACCURACY: stats = MergeSort.sortByAccuracy(list, ascending); break;
            default: throw new IllegalArgumentException("Unknown criteria: " + criteria);
        }
        comparisons = stats.comparisons;
        swaps = stats.movements;
        time = stats.executionTime;
        for (int i = 0; i < list.size(); i++) records.set(i, list.get(i));
    }
}
