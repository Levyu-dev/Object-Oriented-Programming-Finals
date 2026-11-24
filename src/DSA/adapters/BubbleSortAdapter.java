package DSA.adapters;

import DSA.algorithms.BubbleSort;
import DSA.model.PerformanceRecord;
import java.util.ArrayList;
import java.util.List;

public class BubbleSortAdapter extends BaseAdapter {
    public String getName() { return "Bubble Sort"; }

    public void sort(List<PerformanceRecord> records, String criteria, boolean ascending) {
        ArrayList<PerformanceRecord> list = new ArrayList<>(records);
        BubbleSort.SortStats stats;
        switch (criteria) {
            case CRITERIA_SCORE: stats = BubbleSort.sortByQuizScore(list, ascending); break;
            case CRITERIA_XP: stats = BubbleSort.sortByTotalXP(list, ascending); break;
            case CRITERIA_ACCURACY: stats = BubbleSort.sortByAccuracy(list, ascending); break;
            default: throw new IllegalArgumentException("Unknown criteria: " + criteria);
        }
        comparisons = stats.comparisons;
        swaps = stats.swaps;
        time = stats.executionTime;
        for (int i = 0; i < list.size(); i++) records.set(i, list.get(i));
    }
}
//
