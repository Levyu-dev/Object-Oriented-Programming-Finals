package App.ui;

import App.controller.ApplicationController;
import DSA.SortingAlgorithm;
import DSA.adapters.BubbleSortAdapter;
import DSA.adapters.MergeSortAdapter;
import DSA.adapters.QuickSortAdapter;
import DSA.benchmark.AlgorithmDataset;
import DSA.model.PerformanceRecord;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AlgorithmBenchmarkPanel extends BaseScreen {
    private static final int BENCHMARK_TRIALS = 5;

    private final List<JButton> datasetSizeButtons = new ArrayList<>();
    private final DefaultTableModel resultsModel = new DefaultTableModel(
            new Object[]{"Algorithm", "Median Time (ms)", "Mean Comparisons",
                "Mean Swaps / Moves", "Correct"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final DefaultTableModel leaderboardModel = new DefaultTableModel(
            new Object[]{"Rank", "Student ID", "Name", "Quiz Score", "XP", "Accuracy"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JComboBox<String> sortCriterionBox =
            new JComboBox<>(new String[]{"Quiz Score", "XP", "Accuracy"});
    private final JLabel benchmarkSummaryLabel = new JLabel();
    private int selectedDatasetSize = 100;

    public AlgorithmBenchmarkPanel(ApplicationController controller) {
        super(controller);
        setLayout(new BorderLayout(12, 12));
        setBackground(Theme.PALE_LIME);
        setBorder(new EmptyBorder(16, 18, 16, 18));

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);

        JLabel titleLabel = new JLabel("Algorithm Benchmark");
        titleLabel.setFont(new Font(Theme.UI_FONT, Font.BOLD, 20));
        titleLabel.setForeground(Theme.TEXT_DARK);

        JLabel descriptionLabel = new JLabel(
                "Compare Bubble, Merge, and Quick Sort on reproducible pseudo-random datasets.");
        descriptionLabel.setFont(new Font(Theme.UI_FONT, Font.PLAIN, 13));
        descriptionLabel.setForeground(Theme.TEXT_DARK);
        header.add(titleLabel);
        header.add(Box.createVerticalStrut(6));
        header.add(descriptionLabel);

        JPanel benchmarkCard = Theme.createRoundedCard(Theme.SOFT_LIME_CARD, 14);
        benchmarkCard.setLayout(new BorderLayout(10, 10));

        JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        controls.setOpaque(false);

        JPanel sizeControls = new JPanel(new GridLayout(2, 0, 8, 6));
        sizeControls.setOpaque(false);
        JLabel sizeLabel = new JLabel("Dataset size:");
        sizeLabel.setFont(new Font(Theme.UI_FONT, Font.BOLD, 13));
        sizeLabel.setForeground(Theme.TEXT_DARK);
        sizeControls.add(sizeLabel);

        int[] datasetSizes = {100, 500, 1000, 5000};
        for (int datasetSize : datasetSizes) {
            JButton sizeButton = Theme.createWhiteButton(Integer.toString(datasetSize));
            sizeButton.setPreferredSize(new Dimension(0, 34));
            sizeButton.addActionListener(event -> {
                selectedDatasetSize = datasetSize;
                updateSizeButtonStyles();
                refreshBenchmark();
            });
            datasetSizeButtons.add(sizeButton);
            sizeControls.add(sizeButton);
        }

        JPanel criterionControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        criterionControls.setOpaque(false);
        JLabel criterionLabel = new JLabel("Sort by:");
        criterionLabel.setFont(new Font(Theme.UI_FONT, Font.BOLD, 13));
        criterionLabel.setForeground(Theme.TEXT_DARK);
        Theme.styleComboBox(sortCriterionBox);
        sortCriterionBox.addActionListener(event -> refreshBenchmark());
        criterionControls.add(criterionLabel);
        criterionControls.add(sortCriterionBox);
        criterionControls.add(new JLabel("Descending"));

        controls.add(sizeControls);
        controls.add(Box.createVerticalStrut(10));
        controls.add(criterionControls);
        benchmarkCard.add(controls, BorderLayout.NORTH);

        JPanel resultsPanel = new JPanel(new BorderLayout(8, 8));
        resultsPanel.setOpaque(false);
        benchmarkSummaryLabel.setFont(new Font(Theme.UI_FONT, Font.BOLD, 13));
        benchmarkSummaryLabel.setForeground(Theme.TEXT_DARK);
        resultsPanel.add(benchmarkSummaryLabel, BorderLayout.NORTH);

        JPanel tablesPanel = new JPanel();
        tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS));
        tablesPanel.setOpaque(false);

        JTable resultsTable = new JTable(resultsModel);
        resultsTable.setAutoCreateRowSorter(true);
        resultsTable.setFillsViewportHeight(true);
        resultsTable.setRowHeight(26);
        resultsTable.setForeground(Theme.TEXT_DARK);
        resultsTable.setBackground(Theme.WHITE);
        resultsTable.setGridColor(Theme.INPUT_BORDER);
        resultsTable.setSelectionBackground(Theme.BRIGHT_LIME);
        resultsTable.setSelectionForeground(Theme.TEXT_DARK);
        resultsTable.getTableHeader().setForeground(Theme.TEXT_DARK);
        resultsTable.getTableHeader().setBackground(Theme.PALE_LIME);
        JScrollPane resultsScrollPane = new JScrollPane(resultsTable);
        resultsScrollPane.setBorder(BorderFactory.createLineBorder(Theme.INPUT_BORDER));
        resultsScrollPane.setPreferredSize(new Dimension(0, 145));
        resultsScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 145));
        tablesPanel.add(resultsScrollPane);

        JLabel leaderboardTitleLabel = new JLabel("Dataset Leaderboard");
        leaderboardTitleLabel.setFont(new Font(Theme.UI_FONT, Font.BOLD, 14));
        leaderboardTitleLabel.setForeground(Theme.TEXT_DARK);
        leaderboardTitleLabel.setBorder(new EmptyBorder(12, 0, 6, 0));
        leaderboardTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tablesPanel.add(leaderboardTitleLabel);

        JTable leaderboardTable = new JTable(leaderboardModel);
        leaderboardTable.setAutoCreateRowSorter(true);
        leaderboardTable.setFillsViewportHeight(true);
        leaderboardTable.setRowHeight(24);
        leaderboardTable.setForeground(Theme.TEXT_DARK);
        leaderboardTable.setBackground(Theme.WHITE);
        leaderboardTable.setGridColor(Theme.INPUT_BORDER);
        leaderboardTable.setSelectionBackground(Theme.BRIGHT_LIME);
        leaderboardTable.setSelectionForeground(Theme.TEXT_DARK);
        leaderboardTable.getTableHeader().setForeground(Theme.TEXT_DARK);
        leaderboardTable.getTableHeader().setBackground(Theme.PALE_LIME);
        JScrollPane leaderboardScrollPane = new JScrollPane(leaderboardTable);
        leaderboardScrollPane.setBorder(BorderFactory.createLineBorder(Theme.INPUT_BORDER));
        leaderboardScrollPane.setPreferredSize(new Dimension(0, 320));
        leaderboardScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 320));
        tablesPanel.add(leaderboardScrollPane);

        resultsPanel.add(tablesPanel, BorderLayout.CENTER);
        benchmarkCard.add(resultsPanel, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
        add(benchmarkCard, BorderLayout.CENTER);
        updateSizeButtonStyles();
        refreshBenchmark();
    }

    private void updateSizeButtonStyles() {
        for (JButton button : datasetSizeButtons) {
            boolean selected = Integer.toString(selectedDatasetSize).equals(button.getText());
            button.setBackground(selected ? Theme.BRIGHT_LIME : Theme.WHITE);
            button.setForeground(Theme.TEXT_DARK);
        }
    }

    private String getSortCriterion() {
        String selection = (String) sortCriterionBox.getSelectedItem();
        if ("XP".equals(selection)) {
            return SortingAlgorithm.CRITERIA_XP;
        }
        if ("Accuracy".equals(selection)) {
            return SortingAlgorithm.CRITERIA_ACCURACY;
        }
        return SortingAlgorithm.CRITERIA_SCORE;
    }

    private void refreshBenchmark() {
        String criterion = getSortCriterion();
        benchmarkSummaryLabel.setText(
                "Results for " + selectedDatasetSize + " random records across "
                        + BENCHMARK_TRIALS + " seeded trials, sorted by "
                        + sortCriterionBox.getSelectedItem() + " (descending)");
        resultsModel.setRowCount(0);

        SortingAlgorithm[] algorithms = {
                new BubbleSortAdapter(),
                new MergeSortAdapter(),
                new QuickSortAdapter()
        };
        long[][] executionTimes = new long[algorithms.length][BENCHMARK_TRIALS];
        long[][] comparisons = new long[algorithms.length][BENCHMARK_TRIALS];
        long[][] swaps = new long[algorithms.length][BENCHMARK_TRIALS];
        boolean correct = true;
        AlgorithmDataset datasetGenerator = new AlgorithmDataset();
        List<PerformanceRecord> leaderboardRecords = null;

        for (int runIndex = 1; runIndex <= BENCHMARK_TRIALS; runIndex++) {
            long seed = AlgorithmDataset.DEFAULT_DATASET_SEED + runIndex;
            List<PerformanceRecord> records = datasetGenerator.generateDataset(
                    selectedDatasetSize, AlgorithmDataset.Condition.RANDOM, seed);
            if (leaderboardRecords == null) {
                leaderboardRecords = copyRecords(records);
            }

            double[] referenceValues = null;
            for (int algorithmIndex = 0; algorithmIndex < algorithms.length; algorithmIndex++) {
                SortingAlgorithm algorithm = algorithms[algorithmIndex];
                List<PerformanceRecord> sortedRecords = copyRecords(records);
                algorithm.sort(sortedRecords, criterion, false);
                double[] sortedValues = extractValues(algorithm, sortedRecords, criterion);
                correct &= isSorted(sortedValues, false)
                        && (referenceValues == null
                                || Arrays.equals(referenceValues, sortedValues));
                if (referenceValues == null) {
                    referenceValues = sortedValues;
                }

                int trialIndex = runIndex - 1;
                executionTimes[algorithmIndex][trialIndex] = algorithm.getExecutionTime();
                comparisons[algorithmIndex][trialIndex] = algorithm.getComparisons();
                swaps[algorithmIndex][trialIndex] = algorithm.getSwaps();
            }
        }

        printBenchmarkBreakdown(algorithms, executionTimes, comparisons, swaps);
        for (int algorithmIndex = 0; algorithmIndex < algorithms.length; algorithmIndex++) {
            resultsModel.addRow(new Object[]{
                    algorithms[algorithmIndex].getName(),
                    getMedian(executionTimes[algorithmIndex]) / 1_000_000.0,
                    getRoundedMean(comparisons[algorithmIndex]),
                    getRoundedMean(swaps[algorithmIndex]),
                    correct ? "YES" : "NO"
            });
        }

        new MergeSortAdapter().sort(leaderboardRecords, criterion, false);
        Object[][] leaderboardRows = new Object[leaderboardRecords.size()][6];
        for (int index = 0; index < leaderboardRecords.size(); index++) {
            PerformanceRecord record = leaderboardRecords.get(index);
            leaderboardRows[index] = new Object[]{
                    index + 1,
                    record.getStudentId(),
                    record.getUsername(),
                    record.getQuizScore(),
                    record.getTotalXp(),
                    record.getAccuracy()
            };
        }
        leaderboardModel.setDataVector(leaderboardRows, new Object[]{
                "Rank", "Student ID", "Name", "Quiz Score", "XP", "Accuracy"
        });
    }

    private List<PerformanceRecord> copyRecords(List<PerformanceRecord> records) {
        List<PerformanceRecord> copy = new ArrayList<>(records.size());
        for (PerformanceRecord record : records) {
            copy.add(new PerformanceRecord(
                    record.getStudentId(),
                    record.getUsername(),
                    record.getQuizScore(),
                    record.getTotalXp(),
                    record.getAccuracy()));
        }
        return copy;
    }

    private long getMedian(long[] values) {
        long[] sortedValues = values.clone();
        Arrays.sort(sortedValues);
        return sortedValues[sortedValues.length / 2];
    }

    private long getRoundedMean(long[] values) {
        long total = 0;
        for (long value : values) {
            total += value;
        }
        return Math.round((double) total / values.length);
    }

    private void printBenchmarkBreakdown(
            SortingAlgorithm[] algorithms,
            long[][] executionTimes,
            long[][] comparisons,
            long[][] swaps) {
        System.out.printf(
                Locale.ROOT,
                "%n5-Run Benchmark Breakdown | Dataset: %d | Criterion: %s | Descending%n",
                selectedDatasetSize,
                sortCriterionBox.getSelectedItem());
        System.out.println("Run\tSeed\tAlgorithm\tTime (ms)\tComparisons\tSwaps / Moves");
        for (int trialIndex = 0; trialIndex < BENCHMARK_TRIALS; trialIndex++) {
            long seed = AlgorithmDataset.DEFAULT_DATASET_SEED + trialIndex + 1;
            for (int algorithmIndex = 0; algorithmIndex < algorithms.length; algorithmIndex++) {
                System.out.printf(
                        Locale.ROOT,
                        "%d\t%d\t%s\t%.6f\t%d\t%d%n",
                        trialIndex + 1,
                        seed,
                        algorithms[algorithmIndex].getName(),
                        executionTimes[algorithmIndex][trialIndex] / 1_000_000.0,
                        comparisons[algorithmIndex][trialIndex],
                        swaps[algorithmIndex][trialIndex]);
            }
        }
    }

    private double[] extractValues(
            SortingAlgorithm algorithm, List<PerformanceRecord> records, String criterion) {
        double[] values = new double[records.size()];
        for (int index = 0; index < records.size(); index++) {
            values[index] = algorithm.getValue(records.get(index), criterion);
        }
        return values;
    }

    private boolean isSorted(double[] values, boolean ascending) {
        for (int index = 1; index < values.length; index++) {
            if (ascending ? values[index - 1] > values[index]
                    : values[index - 1] < values[index]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void refresh() {
        refreshBenchmark();
    }
}
