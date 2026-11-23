import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SortingManager {

    private final List<PerformanceRecord> leaderboard;
    private final Scanner scanner = new Scanner(System.in);

    public SortingManager(List<PerformanceRecord> leaderboard) {
        this.leaderboard = leaderboard;
    }

    public void run() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== ALGORITHM-POWERED LEADERBOARD ===");
            System.out.println("1. Bubble Sort");
            System.out.println("2. Merge Sort");
            System.out.println("3. Quick Sort");
            System.out.println("4. Compare all three (benchmark)");
            System.out.println("0. Back");
            int choice = readInt("Choose: ", 0, 4);

            if (choice == 0) {
                running = false;
            } else if (choice == 4) {
                runBenchmark();
            } else {
                sortAndDisplay(createAlgorithm(choice));
            }
        }
    }

    private SortingAlgorithm createAlgorithm(int choice) {
        switch (choice) {
            case 1:  return new SortingAdapters.BubbleSortAdapter();
            case 2:  return new SortingAdapters.MergeSortAdapter();
            default: return new SortingAdapters.QuickSortAdapter();
        }
    }

    private String chooseCriteria() {
        System.out.println("\nSort by:");
        System.out.println("1. Quiz score");
        System.out.println("2. Total XP");
        System.out.println("3. Accuracy percentage");
        int choice = readInt("Choose: ", 1, 3);
        if (choice == 1) return SortingAlgorithm.CRITERIA_SCORE;
        if (choice == 2) return SortingAlgorithm.CRITERIA_XP;
        return SortingAlgorithm.CRITERIA_ACCURACY;
    }

    private boolean chooseAscending() {
        System.out.println("\nOrder:");
        System.out.println("1. Ascending (lowest to highest)");
        System.out.println("2. Descending (highest to lowest)");
        return readInt("Choose: ", 1, 2) == 1;
    }

    private void sortAndDisplay(SortingAlgorithm algorithm) {
        String criteria = chooseCriteria();
        boolean ascending = chooseAscending();

        List<PerformanceRecord> sorted = new ArrayList<>(leaderboard); // keep original untouched
        algorithm.sort(sorted, criteria, ascending);

        System.out.println("\n--- Leaderboard sorted by " + criteria
                + " (" + (ascending ? "ascending" : "descending") + ") using "
                + algorithm.getName() + " ---");
        int rank = 1;
        for (PerformanceRecord r : sorted) {
            System.out.printf("%3d. %-10s %-15s score=%-4d xp=%-6d acc=%6.2f%%%n",
                    rank++, r.getStudentId(), r.getUsername(),
                    r.getQuizScore(), r.getTotalXp(), r.getAccuracy());
        }

        System.out.println("\nComparisons : " + algorithm.getComparisons());
        System.out.println("Swaps/Moves : " + algorithm.getSwaps());
        System.out.printf("Time        : %.3f ms%n", algorithm.getExecutionTime() / 1_000_000.0);
    }

    private void runBenchmark() {
        String criteria = chooseCriteria();
        boolean ascending = chooseAscending();

        Map<String, List<PerformanceRecord>> datasets = new LinkedHashMap<>();
        datasets.put("Leaderboard (" + leaderboard.size() + ")", leaderboard);

        new AlgorithmBenchmark().runAllSizes(datasets, criteria, ascending);
    }

    private int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // fall through
            }
            System.out.println("Invalid input. Enter a number from " + min + " to " + max + ".");
        }
    }
}