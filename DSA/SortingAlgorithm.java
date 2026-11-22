import java.util.List;

public interface SortingAlgorithm {

    // Sorting criteria (everyone uses these same constants)
    String CRITERIA_SCORE = "score";
    String CRITERIA_XP = "xp";
    String CRITERIA_ACCURACY = "accuracy";

    // Sorts the given list IN PLACE
    void sort(List<PerformanceRecord> records, String criteria, boolean ascending);

    long getComparisons();

    // Bubble/Quick = swaps, Merge = record movements
    long getSwaps();

    // Execution time of the last sort, in nanoseconds
    long getExecutionTime();

    String getName();

    // Shared helper: gets the value of a record based on the criteria.
    // If your PerformanceRecord getters have different names, change them HERE only.
    default double getValue(PerformanceRecord record, String criteria) {
        switch (criteria) {
            case CRITERIA_SCORE:
                return record.getQuizScore();
            case CRITERIA_XP:
                return record.getTotalXP();
            case CRITERIA_ACCURACY:
                return record.getAccuracy();
            default:
                throw new IllegalArgumentException("Unknown criteria: " + criteria);
        }
    }
}