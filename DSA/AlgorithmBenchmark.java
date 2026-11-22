import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AlgorithmBenchmark {

    private final SortingAlgorithm[] algorithms;

    public AlgorithmBenchmark() {
        algorithms = new SortingAlgorithm[] {
                new BubbleSortAdapter(), new MergeSortAdapter(), new QuickSortAdapter()
        };
    }

    public void runAllSizes(Map<String, List<PerformanceRecord>> datasets,
                            String criteria, boolean ascending) {
        printHeader();
        for (Map.Entry<String, List<PerformanceRecord>> entry : datasets.entrySet()) {
            runBenchmark(entry.getKey(), entry.getValue(), criteria, ascending);
        }
    }

    // Runs all three algorithms on separate copies of ONE dataset
    public void runBenchmark(String label, List<PerformanceRecord> original,
                             String criteria, boolean ascending) {
        double[] reference = null;

        for (SortingAlgorithm algo : algorithms) {
            List<PerformanceRecord> copy = new ArrayList<>(original); // separate copy per algorithm

            algo.sort(copy, criteria, ascending); // time is measured inside the algorithm with System.nanoTime()

            double[] values = extractValues(algo, copy, criteria);
            boolean correct = isSorted(values, ascending);

            if (reference == null) {
                reference = values;
            } else if (!Arrays.equals(reference, values)) {
                correct = false; // all algorithms must give the same ordering
            }

            printRow(label, algo, correct);
        }
    }

    private double[] extractValues(SortingAlgorithm algo, List<PerformanceRecord> list, String criteria) {
        double[] values = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            values[i] = algo.getValue(list.get(i), criteria);
        }
        return values;
    }

    private boolean isSorted(double[] values, boolean ascending) {
        for (int i = 1; i < values.length; i++) {
            if (ascending && values[i - 1] > values[i]) return false;
            if (!ascending && values[i - 1] < values[i]) return false;
        }
        return true;
    }

    private void printHeader() {
        System.out.println();
        System.out.printf("%-26s %-12s %12s %14s %12s %8s%n",
                "Dataset", "Algorithm", "Time (ms)", "Comparisons", "Swaps/Moves", "Correct");
        System.out.println("-".repeat(90));
    }

    private void printRow(String label, SortingAlgorithm algo, boolean correct) {
        System.out.printf("%-26s %-12s %12.3f %14d %12d %8s%n",
                label, algo.getName(), algo.getExecutionTime() / 1_000_000.0,
                algo.getComparisons(), algo.getSwaps(), correct ? "YES" : "NO");
    }
}