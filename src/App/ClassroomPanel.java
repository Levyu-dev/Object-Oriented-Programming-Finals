package App;

import DSA.PerformanceRecord;
import DSA.SortingAlgorithm;
import DSA.BubbleSortAdapter;
import DSA.MergeSortAdapter;
import DSA.QuickSortAdapter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ClassroomPanel extends BaseScreen {
    private final boolean isTeacher;
    private JLabel titleLabel;
    private JList<String> assignmentList;
    private JTextArea leaderboardArea;
    private JComboBox<String> leaderboardSortBox;
    private JComboBox<String> leaderboardAlgorithmBox;
    private JButton leaderboardMetricsButton;
    private boolean showLeaderboardMetrics;
    private JTextField quizTitleField;
    private DefaultListModel<String> draftQuestionModel;
    private JList<String> draftQuestionList;
    private final List<UserSession.TeacherQuestion> draftQuestions = new ArrayList<>();

    public ClassroomPanel(ApplicationController controller, boolean isTeacher) {
        super(controller);
        this.isTeacher = isTeacher;

        setLayout(new BorderLayout(10, 10));
        setBackground(Theme.PALE_LIME);
        setBorder(new EmptyBorder(14, 16, 14, 16));

        titleLabel = new JLabel("Classroom");
        titleLabel.setFont(new Font(Theme.UI_FONT, Font.BOLD, 18));
        titleLabel.setForeground(Theme.TEXT_DARK);

        JPanel stack = new JPanel(new GridLayout(2, 1, 0, 12));
        stack.setBackground(Theme.PALE_LIME);

        // Top Card: Section Activities
        JPanel topCard = Theme.createRoundedCard(Theme.SOFT_LIME_CARD, 12);
        topCard.setLayout(new BorderLayout(6, 6));

        JLabel actHeader = new JLabel("Section Activities");
        actHeader.setFont(new Font(Theme.UI_FONT, Font.BOLD, 14));
        actHeader.setForeground(Theme.TEXT_DARK);
        topCard.add(actHeader, BorderLayout.NORTH);

        assignmentList = new JList<>();
        assignmentList.setFont(new Font(Theme.UI_FONT, Font.PLAIN, 13));
        assignmentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        assignmentList.setBackground(Theme.WHITE);
        assignmentList.setForeground(Theme.TEXT_DARK);
        assignmentList.setSelectionBackground(Theme.BRIGHT_LIME);
        assignmentList.setSelectionForeground(Theme.TEXT_DARK);

        JScrollPane listScroll = new JScrollPane(assignmentList);
        listScroll.setBorder(BorderFactory.createLineBorder(new Color(0, 84, 95, 60)));
        topCard.add(listScroll, BorderLayout.CENTER);

        if (isTeacher) {
            JButton deleteActivityButton = Theme.createWhiteButton("DELETE SELECTED ACTIVITY");
            deleteActivityButton.setPreferredSize(new Dimension(0, 34));
            deleteActivityButton.addActionListener(e -> deleteSelectedActivity());
            topCard.add(deleteActivityButton, BorderLayout.SOUTH);
        } else {
            JButton startBtn = Theme.createPrimaryButton("Start Selected Activity");
            startBtn.setPreferredSize(new Dimension(0, 38));
            startBtn.addActionListener(e -> {
                String selected = assignmentList.getSelectedValue();
                if (selected == null) {
                    controller.showError("Tap an activity from the list first.");
                } else {
                    UserSession session = controller.getSession();
                    UserSession.TeacherQuiz quiz = session.getTeacherQuiz(session.getCurrentSection(), selected);
                    if (quiz != null) {
                        controller.launchTeacherQuiz(quiz);
                    } else {
                        controller.showError("This activity has no published quiz.");
                    }
                }
            });
            topCard.add(startBtn, BorderLayout.SOUTH);
        }

        // Bottom Card: Teacher Assignment Form OR Student Leaderboard
        JPanel bottomCard = Theme.createRoundedCard(Theme.SOFT_LIME_CARD, 12);
        bottomCard.setLayout(new BorderLayout(6, 6));

        if (isTeacher) {
            JLabel formTitle = new JLabel("Create Quiz for Students");
            formTitle.setFont(new Font(Theme.UI_FONT, Font.BOLD, 14));
            formTitle.setForeground(Theme.TEXT_DARK);
            bottomCard.add(formTitle, BorderLayout.NORTH);

            JPanel builder = new JPanel(new BorderLayout(6, 6));
            builder.setOpaque(false);

            JPanel titlePanel = new JPanel(new BorderLayout(4, 4));
            titlePanel.setOpaque(false);
            JLabel titlePrompt = new JLabel("Quiz title:");
            titlePrompt.setFont(new Font(Theme.UI_FONT, Font.BOLD, 12));
            titlePrompt.setForeground(Theme.TEXT_DARK);
            quizTitleField = new JTextField();
            Theme.styleTextField(quizTitleField);
            titlePanel.add(titlePrompt, BorderLayout.NORTH);
            titlePanel.add(quizTitleField, BorderLayout.CENTER);

            draftQuestionModel = new DefaultListModel<>();
            draftQuestionList = new JList<>(draftQuestionModel);
            draftQuestionList.setFont(new Font(Theme.UI_FONT, Font.PLAIN, 12));
            draftQuestionList.setForeground(Theme.TEXT_DARK);
            draftQuestionList.setBackground(Theme.INPUT_BACKGROUND);
            draftQuestionList.setSelectionBackground(Theme.BRIGHT_LIME);
            JScrollPane draftScroll = new JScrollPane(draftQuestionList);
            draftScroll.setBorder(BorderFactory.createLineBorder(Theme.INPUT_BORDER));

            JButton addQuestionButton = Theme.createWhiteButton("ADD QUESTION");
            addQuestionButton.addActionListener(e -> addDraftQuestion());
            JButton deleteQuestionButton = Theme.createWhiteButton("DELETE QUESTION");
            deleteQuestionButton.addActionListener(e -> deleteDraftQuestion());
            JButton publishButton = Theme.createPrimaryButton("PUBLISH QUIZ");
            publishButton.addActionListener(e -> publishQuiz());

            JPanel buttons = new JPanel(new GridLayout(1, 3, 6, 0));
            buttons.setOpaque(false);
            buttons.add(addQuestionButton);
            buttons.add(deleteQuestionButton);
            buttons.add(publishButton);

            builder.add(titlePanel, BorderLayout.NORTH);
            builder.add(draftScroll, BorderLayout.CENTER);
            builder.add(buttons, BorderLayout.SOUTH);
            bottomCard.add(builder, BorderLayout.CENTER);
        } else {
            JPanel leaderboardHeader = new JPanel(new BorderLayout(8, 4));
            leaderboardHeader.setOpaque(false);

            JLabel boardTitle = new JLabel("Section Leaderboard");
            boardTitle.setFont(new Font(Theme.UI_FONT, Font.BOLD, 14));
            boardTitle.setForeground(Theme.TEXT_DARK);

            leaderboardSortBox = new JComboBox<>(new String[]{"XP", "Completed Quizzes"});
            Theme.styleComboBox(leaderboardSortBox);
            leaderboardSortBox.addActionListener(e -> refreshLeaderboard());

            leaderboardAlgorithmBox = new JComboBox<>(new String[]{
                    "Merge Sort", "Bubble Sort", "Quick Sort"});
            Theme.styleComboBox(leaderboardAlgorithmBox);
            leaderboardAlgorithmBox.addActionListener(e -> refreshLeaderboard());

            leaderboardMetricsButton = Theme.createWhiteButton("Show DSA Metrics");
            leaderboardMetricsButton.setPreferredSize(new Dimension(150, 32));
            leaderboardMetricsButton.addActionListener(e -> {
                showLeaderboardMetrics = !showLeaderboardMetrics;
                leaderboardMetricsButton.setText(showLeaderboardMetrics
                        ? "Hide DSA Metrics" : "Show DSA Metrics");
                refreshLeaderboard();
            });

            JPanel algorithmControls = new JPanel(new BorderLayout(6, 0));
            algorithmControls.setOpaque(false);
            JLabel algorithmLabel = new JLabel("Algorithm:");
            algorithmLabel.setFont(new Font(Theme.UI_FONT, Font.BOLD, 12));
            algorithmLabel.setForeground(Theme.TEXT_DARK);
            algorithmControls.add(algorithmLabel, BorderLayout.WEST);
            algorithmControls.add(leaderboardAlgorithmBox, BorderLayout.CENTER);
            algorithmControls.add(leaderboardMetricsButton, BorderLayout.EAST);

            leaderboardHeader.add(boardTitle, BorderLayout.WEST);
            leaderboardHeader.add(leaderboardSortBox, BorderLayout.EAST);
            bottomCard.add(leaderboardHeader, BorderLayout.NORTH);
            bottomCard.add(algorithmControls, BorderLayout.SOUTH);

            leaderboardArea = new JTextArea();
            leaderboardArea.setEditable(false);
            leaderboardArea.setFont(new Font(Theme.UI_FONT, Font.PLAIN, 14));
            leaderboardArea.setForeground(Theme.TEXT_DARK);
            leaderboardArea.setBackground(Theme.INPUT_BACKGROUND);
            leaderboardArea.setCaretColor(Theme.DEEP_TEAL);
            leaderboardArea.setBorder(new EmptyBorder(10, 12, 10, 12));

            JScrollPane boardScroll = new JScrollPane(leaderboardArea);
            boardScroll.setBorder(BorderFactory.createLineBorder(Theme.INPUT_BORDER));
            bottomCard.add(boardScroll, BorderLayout.CENTER);
        }

        stack.add(topCard);
        stack.add(bottomCard);
        add(titleLabel, BorderLayout.NORTH);
        add(stack, BorderLayout.CENTER);
    }

    private void addDraftQuestion() {
        UserSession.TeacherQuestion question = showAddQuestionDialog();
        if (question == null) {
            return;
        }

        draftQuestions.add(question);
        draftQuestionModel.addElement(formatDraftQuestion(draftQuestions.size(), question));
    }

    private String formatDraftQuestion(int number, UserSession.TeacherQuestion question) {
        String type = question.getType() == UserSession.QuestionType.MULTIPLE_CHOICE
                ? "Multiple choice" : "Short answer";
        String prompt = question.getPrompt();
        if (prompt.length() > 32) prompt = prompt.substring(0, 29) + "...";
        return number + ". " + type + " - " + prompt + " (" + question.getPoints() + " pts)";
    }

    private void deleteDraftQuestion() {
        int selectedIndex = draftQuestionList.getSelectedIndex();
        if (selectedIndex < 0) {
            controller.showError("Select a draft question to delete.");
            return;
        }

        int result = JOptionPane.showConfirmDialog(
                this, "Delete the selected draft question?", "Delete Question",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (result != JOptionPane.YES_OPTION) return;

        draftQuestions.remove(selectedIndex);
        draftQuestionModel.remove(selectedIndex);
        for (int i = 0; i < draftQuestions.size(); i++) {
            draftQuestionModel.set(i, formatDraftQuestion(i + 1, draftQuestions.get(i)));
        }
    }

    private void deleteSelectedActivity() {
        String selected = assignmentList.getSelectedValue();
        if (selected == null) {
            controller.showError("Select an activity to delete.");
            return;
        }

        int result = JOptionPane.showConfirmDialog(
                this,
                "Delete \"" + selected + "\" and all of its questions?",
                "Delete Activity",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (result != JOptionPane.YES_OPTION) return;

        UserSession session = controller.getSession();
        if (!session.deleteTeacherQuiz(session.getCurrentSection(), selected)) {
            controller.showError("The selected activity could not be found.");
            return;
        }
        refresh();
    }

    private UserSession.TeacherQuestion showAddQuestionDialog() {
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Short Answer", "Multiple Choice"});

        JTextArea promptField = new JTextArea(3, 24);
        promptField.setLineWrap(true);
        promptField.setWrapStyleWord(true);
        promptField.setFont(new Font(Theme.UI_FONT, Font.PLAIN, 14));
        promptField.setForeground(Theme.TEXT_DARK);
        promptField.setBackground(Theme.INPUT_BACKGROUND);
        promptField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.INPUT_BORDER),
                new EmptyBorder(6, 8, 6, 8)));

        JTextField pointsField = new JTextField("50");
        Theme.styleTextField(pointsField);
        JTextField shortAnswerField = new JTextField();
        Theme.styleTextField(shortAnswerField);

        JPanel shortAnswerPanel = new JPanel(new BorderLayout(4, 4));
        shortAnswerPanel.setOpaque(false);
        shortAnswerPanel.add(new JLabel("Correct answer:"), BorderLayout.NORTH);
        shortAnswerPanel.add(shortAnswerField, BorderLayout.CENTER);

        JTextField[] optionFields = new JTextField[4];
        JPanel multipleChoicePanel = new JPanel(new GridLayout(5, 2, 4, 4));
        multipleChoicePanel.setOpaque(false);
        for (int i = 0; i < optionFields.length; i++) {
            optionFields[i] = new JTextField();
            Theme.styleTextField(optionFields[i]);
            multipleChoicePanel.add(new JLabel("Option " + (char) ('A' + i) + ":"));
            multipleChoicePanel.add(optionFields[i]);
        }
        JComboBox<String> correctChoiceBox = new JComboBox<>(new String[]{"A", "B", "C", "D"});
        Theme.styleComboBox(correctChoiceBox);
        multipleChoicePanel.add(new JLabel("Correct option:"));
        multipleChoicePanel.add(correctChoiceBox);

        CardLayout answerLayout = new CardLayout();
        JPanel answerCards = new JPanel(answerLayout);
        answerCards.add(shortAnswerPanel, "SHORT");
        answerCards.add(multipleChoicePanel, "MULTIPLE");
        typeBox.addActionListener(e -> answerLayout.show(
                answerCards, typeBox.getSelectedIndex() == 0 ? "SHORT" : "MULTIPLE"));

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.add(new JLabel("Question type:"));
        form.add(typeBox);
        form.add(Box.createVerticalStrut(6));
        form.add(new JLabel("Question:"));
        form.add(new JScrollPane(promptField));
        form.add(Box.createVerticalStrut(6));
        form.add(new JLabel("Points for a correct answer:"));
        form.add(pointsField);
        form.add(Box.createVerticalStrut(6));
        form.add(answerCards);

        int result = JOptionPane.showConfirmDialog(this, form, "Add Question",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        String prompt = promptField.getText().trim();
        if (prompt.isEmpty()) {
            controller.showError("Enter a question.");
            return null;
        }

        int points;
        try {
            points = Integer.parseInt(pointsField.getText().trim());
            if (points < 1) {
                controller.showError("Points must be at least 1.");
                return null;
            }
        } catch (NumberFormatException ex) {
            controller.showError("Enter a valid points value.");
            return null;
        }

        if (typeBox.getSelectedIndex() == 0) {
            String answer = shortAnswerField.getText().trim();
            if (answer.isEmpty()) {
                controller.showError("Enter the correct answer.");
                return null;
            }
            return new UserSession.TeacherQuestion(
                    UserSession.QuestionType.SHORT_ANSWER, prompt, answer, null, points);
        }

        String[] choices = new String[optionFields.length];
        for (int i = 0; i < optionFields.length; i++) {
            String option = optionFields[i].getText().trim();
            if (option.isEmpty()) {
                controller.showError("Fill in all four answer options.");
                return null;
            }
            choices[i] = (char) ('A' + i) + ") " + option;
        }
        return new UserSession.TeacherQuestion(
                UserSession.QuestionType.MULTIPLE_CHOICE,
                prompt,
                (String) correctChoiceBox.getSelectedItem(),
                choices,
                points);
    }

    private void publishQuiz() {
        String title = quizTitleField.getText().trim();
        if (title.isEmpty()) {
            controller.showError("Enter a quiz title.");
            return;
        }
        if (draftQuestions.isEmpty()) {
            controller.showError("Add at least one question before publishing.");
            return;
        }

        String section = controller.getSession().getCurrentSection();
        UserSession.TeacherQuiz quiz = new UserSession.TeacherQuiz(title, draftQuestions);
        if (!controller.getSession().addTeacherQuiz(section, quiz)) {
            controller.showError("An activity with that title and question count already exists.");
            return;
        }

        draftQuestions.clear();
        draftQuestionModel.clear();
        quizTitleField.setText("");
        refresh();
        controller.showAlert("Quiz published to Section " + section + ".");
    }

    void clearDraftQuiz() {
        if (!isTeacher) return;
        draftQuestions.clear();
        draftQuestionModel.clear();
        quizTitleField.setText("");
    }

    @Override
    public void refresh() {
        UserSession session = controller.getSession();
        String sec = session.getCurrentSection();
        titleLabel.setText((isTeacher ? "Manage Class" : "Classroom") + " - " + sec);

        DefaultListModel<String> model = session.getAssignmentsForSection(sec);
        assignmentList.setModel(model);
        if (!model.isEmpty() && assignmentList.getSelectedIndex() == -1) {
            assignmentList.setSelectedIndex(0);
        }

        if (!isTeacher && leaderboardArea != null) {
            refreshLeaderboard();
        }
    }

    private void refreshLeaderboard() {
        UserSession session = controller.getSession();
        String section = session.getCurrentSection();
        List<UserSession.StudentRecord> students = session.getStudentsInSection(section);
        ArrayList<PerformanceRecord> records = new ArrayList<>();
        StringBuilder output = new StringBuilder();

        for (UserSession.StudentRecord student : students) {
            records.add(new PerformanceRecord(
                    student.getName(),
                    student.getName(),
                    student.getCompletedQuizzes(),
                    student.getXp(),
                    0));
        }

        if (records.isEmpty()) {
            output.append("No students in this section yet.");
            leaderboardArea.setText(output.toString());
            leaderboardArea.setCaretPosition(0);
            return;
        }

        if (leaderboardSortBox != null) {
            String criterion = (String) leaderboardSortBox.getSelectedItem();
            String criterionName = "Completed Quizzes".equals(criterion)
                    ? SortingAlgorithm.CRITERIA_SCORE
                    : SortingAlgorithm.CRITERIA_XP;

            String algorithmName = (String) leaderboardAlgorithmBox.getSelectedItem();
            SortingAlgorithm algorithm;
            if ("Bubble Sort".equals(algorithmName)) {
                algorithm = new BubbleSortAdapter();
            } else if ("Quick Sort".equals(algorithmName)) {
                algorithm = new QuickSortAdapter();
            } else {
                algorithm = new MergeSortAdapter();
            }
            algorithm.sort(records, criterionName, false);

            if (showLeaderboardMetrics) {
                output.append("\n");
                output.append("Algorithm: ").append(algorithm.getName()).append("\n");
                output.append("Comparisons: ").append(algorithm.getComparisons()).append("\n");
                output.append("Swaps/Moves: ").append(algorithm.getSwaps()).append("\n");
                output.append(String.format("Time: %.3f ms",
                        algorithm.getExecutionTime() / 1_000_000.0));

                output.append("\n\nAlgorithm Comparison:\n");
                output.append(String.format("%-12s %12s %12s %10s%n",
                        "Algorithm", "Comparisons", "Swaps/Moves", "Time(ms)"));
                SortingAlgorithm[] algorithms = {
                        new BubbleSortAdapter(),
                        new MergeSortAdapter(),
                        new QuickSortAdapter()
                };
                for (SortingAlgorithm benchmarkAlgorithm : algorithms) {
                    ArrayList<PerformanceRecord> copy = new ArrayList<>(records);
                    benchmarkAlgorithm.sort(copy, criterionName, false);
                    output.append(String.format("%-12s %12d %12d %10.3f%n",
                            benchmarkAlgorithm.getName(),
                            benchmarkAlgorithm.getComparisons(),
                            benchmarkAlgorithm.getSwaps(),
                            benchmarkAlgorithm.getExecutionTime() / 1_000_000.0));
                }
            }
        }

        int rank = 1;
        for (PerformanceRecord record : records) {
            output.append(rank++)
                    .append(". @").append(record.getUsername())
                    .append(" - ").append(record.getTotalXp()).append(" XP")
                    .append(" | Quizzes: ").append(record.getQuizScore())
                    .append("\n");
        }
        if (output.length() == 0) {
            output.append("No students in this section yet.");
        }
        leaderboardArea.setText(output.toString());
        leaderboardArea.setCaretPosition(0);
    }
}
