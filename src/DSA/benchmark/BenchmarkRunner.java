package DSA.benchmark;

import DSA.SortingAlgorithm;
import DSA.model.PerformanceRecord;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Runs the full required test: 4 sizes x 4 conditions + empty + single record.
// Usage: java BenchmarkRunner [score|xp|accuracy] [asc|desc]
public class BenchmarkRunner {
    public static void main(String[] args) {
        String criteria = args.length > 0 ? args[0] : SortingAlgorithm.CRITERIA_SCORE;
        boolean ascending = args.length < 2 || !args[1].equalsIgnoreCase("desc");

        AlgorithmDataset ds = new AlgorithmDataset();
        Map<String, List<PerformanceRecord>> datasets = new LinkedHashMap<>();

        int[] sizes = {100, 500, 1000, 5000};
        AlgorithmDataset.Condition[] conditions = {
                AlgorithmDataset.Condition.RANDOM,
                AlgorithmDataset.Condition.SORTED,
                AlgorithmDataset.Condition.REVERSE_SORTED,
                AlgorithmDataset.Condition.DUPLICATES
        };
        for (int size : sizes) {
            for (AlgorithmDataset.Condition c : conditions) {
                datasets.put(size + " " + c.name().toLowerCase(), ds.generateDataset(size, c));
            }
        }
        datasets.put("0 empty", ds.generateDataset(0, AlgorithmDataset.Condition.EMPTY));
        datasets.put("1 single_record", ds.generateDataset(1, AlgorithmDataset.Condition.SINGLE_RECORD));

        System.out.println("Criteria: " + criteria + ", " + (ascending ? "ascending" : "descending"));
        new AlgorithmBenchmark().runAllSizes(datasets, criteria, ascending);
    }
}