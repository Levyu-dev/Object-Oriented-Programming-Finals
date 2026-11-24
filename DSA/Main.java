import java.util.List;

// Demo entry point: opens the algorithm-powered leaderboard on 20 sample students.
public class Main {
    public static void main(String[] args) {
        AlgorithmDataset dataset = new AlgorithmDataset();
        List<PerformanceRecord> leaderboard =
                dataset.generateDataset(20, AlgorithmDataset.Condition.RANDOM);

        new SortingManager(leaderboard).run();
    }
}