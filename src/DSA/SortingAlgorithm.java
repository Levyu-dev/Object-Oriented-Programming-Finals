import java.util.List;

public interface SortingAlgorithm {

    String CRITERIA_SCORE = "score";
    String CRITERIA_XP = "xp";
    String CRITERIA_ACCURACY = "accuracy";

    // Sorts the given list IN PLACE
    void sort(List<PerformanceRecord> records, String criteria, boolean ascending);

    long getComparisons();

    // Bubble/Quick = swaps, Merge = record movements
    long getSwaps();

    // Sort time in nanoseconds (taken from the algorithm's own System.nanoTime() measurement)
    long getExecutionTime();

    String getName();

    // Reads the value being sorted. Getter names match Angel's dataset fields.
    default double getValue(PerformanceRecord record, String criteria) {
        switch (criteria) {
            case CRITERIA_SCORE:
                return record.getQuizScore();
            case CRITERIA_XP:
                return record.getTotalXp();
            case CRITERIA_ACCURACY:
                return record.getAccuracy();
            default:
                throw new IllegalArgumentException("Unknown criteria: " + criteria);
        }
    }
}