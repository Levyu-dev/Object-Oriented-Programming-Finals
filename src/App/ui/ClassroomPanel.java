package App.ui;

import App.controller.ApplicationController;
import App.model.UserSession;
import DSA.model.PerformanceRecord;
import DSA.SortingAlgorithm;
import DSA.adapters.BubbleSortAdapter;
import DSA.adapters.MergeSortAdapter;
import DSA.adapters.QuickSortAdapter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ClassroomPanel extends BaseScreen {
    private final boolean teacherMode;
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

    public ClassroomPanel(ApplicationController controller, boolean teacherMode) {
        super(controller);
        this.teacherMode = teacherMode;

        setLayout(new BorderLayout(10, 10));
        setBackground(Theme.PALE_LIME);
        setBorder(new EmptyBorder(14, 16, 14, 16));

        titleLabel = new JLabel("Classroom");
        titleLabel.setFont(new Font(Theme.UI_FONT, Font.BOLD, 18));
        titleLabel.setForeground(Theme.TEXT_DARK);

        JPanel classroomSections = new JPanel(new GridLayout(2, 1, 0, 12));
        classroomSections.setBackground(Theme.PALE_LIME);

        JPanel activitiesCard = Theme.createRoundedCard(Theme.SOFT_LIME_CARD, 12);
        activitiesCard.setLayout(new BorderLayout(6, 6));

        JLabel activitiesTitleLabel = new JLabel("Section Activities");
        activitiesTitleLabel.setFont(new Font(Theme.UI_FONT, Font.BOLD, 14));
        activitiesTitleLabel.setForeground(Theme.TEXT_DARK);
        activitiesCard.add(activitiesTitleLabel, BorderLayout.NORTH);

        assignmentList = new JList<>();
        assignmentList.setFont(new Font(Theme.UI_FONT, Font.PLAIN, 13));
        assignmentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        assignmentList.setBackground(Theme.WHITE);
        assignmentList.setForeground(Theme.TEXT_DARK);
        assignmentList.setSelectionBackground(Theme.BRIGHT_LIME);
        assignmentList.setSelectionForeground(Theme.TEXT_DARK);

        JScrollPane listScroll = new JScrollPane(assignmentList);
        listScroll.setBorder(BorderFactory.createLineBorder(new Color(0, 84, 95, 60)));
        activitiesCard.add(listScroll, BorderLayout.CENTER);

        if (teacherMode) {
            JButton deleteActivityButton = Theme.createWhiteButton("DELETE SELECTED ACTIVITY");
            deleteActivityButton.setPreferredSize(new Dimension(0, 34));
            deleteActivityButton.addActionListener(event -> deleteSelectedActivity());
            activitiesCard.add(deleteActivityButton, BorderLayout.SOUTH);
        } else {
            JButton startActivityButton = Theme.createPrimaryButton("Start Selected Activity");
            startActivityButton.setPreferredSize(new Dimension(0, 38));
            startActivityButton.addActionListener(event -> {
                String selectedAssignmentLabel = assignmentList.getSelectedValue();
                if (selectedAssignmentLabel == null) {
                    controller.showError("Tap an activity from the list first.");
                } else {
                    UserSession userSession = controller.getSession();
                    UserSession.TeacherQuiz teacherQuiz = userSession.getTeacherQuiz(
                            userSession.getCurrentSection(), selectedAssignmentLabel);
                    if (teacherQuiz != null) {
                        controller.launchTeacherQuiz(teacherQuiz);
                    } else {
                        controller.showError("This activity has no published quiz.");
                    }
                }
            });
            activitiesCard.add(startActivityButton, BorderLayout.SOUTH);
        }

        JPanel classToolsCard = Theme.createRoundedCard(Theme.SOFT_LIME_CARD, 12);
        classToolsCard.setLayout(new BorderLayout(6, 6));

        if (teacherMode) {
            JLabel quizBuilderTitleLabel = new JLabel("Create Quiz for Students");
            quizBuilderTitleLabel.setFont(new Font(Theme.UI_FONT, Font.BOLD, 14));
            quizBuilderTitleLabel.setForeground(Theme.TEXT_DARK);
            classToolsCard.add(quizBuilderTitleLabel, BorderLayout.NORTH);

            JPanel quizBuilderPanel = new JPanel(new BorderLayout(6, 6));
            quizBuilderPanel.setOpaque(false);

            JPanel quizTitlePanel = new JPanel(new BorderLayout(4, 4));
            quizTitlePanel.setOpaque(false);
            JLabel quizTitleLabel = new JLabel("Quiz title:");
            quizTitleLabel.setFont(new Font(Theme.UI_FONT, Font.BOLD, 12));
            quizTitleLabel.setForeground(Theme.TEXT_DARK);
            quizTitleField = new JTextField();
            Theme.styleTextField(quizTitleField);
            quizTitlePanel.add(quizTitleLabel, BorderLayout.NORTH);
            quizTitlePanel.add(quizTitleField, BorderLayout.CENTER);

            draftQuestionModel = new DefaultListModel<>();
            draftQuestionList = new JList<>(draftQuestionModel);
            draftQuestionList.setFont(new Font(Theme.UI_FONT, Font.PLAIN, 12));
            draftQuestionList.setForeground(Theme.TEXT_DARK);
            draftQuestionList.setBackground(Theme.INPUT_BACKGROUND);
            draftQuestionList.setSelectionBackground(Theme.BRIGHT_LIME);
            JScrollPane draftQuestionScrollPane = new JScrollPane(draftQuestionList);
            draftQuestionScrollPane.setBorder(BorderFactory.createLineBorder(Theme.INPUT_BORDER));

            JButton addQuestionButton = Theme.createWhiteButton("ADD QUESTION");
            addQuestionButton.addActionListener(event -> addDraftQuestion());
            JButton deleteQuestionButton = Theme.createWhiteButton("DELETE QUESTION");
            deleteQuestionButton.addActionListener(event -> deleteDraftQuestion());
            JButton publishButton = Theme.createPrimaryButton("PUBLISH QUIZ");
            publishButton.addActionListener(event -> publishQuiz());

            JPanel quizActionButtons = new JPanel(new GridLayout(1, 3, 6, 0));
            quizActionButtons.setOpaque(false);
            quizActionButtons.add(addQuestionButton);
            quizActionButtons.add(deleteQuestionButton);
            quizActionButtons.add(publishButton);

            quizBuilderPanel.add(quizTitlePanel, BorderLayout.NORTH);
            quizBuilderPanel.add(draftQuestionScrollPane, BorderLayout.CENTER);
            quizBuilderPanel.add(quizActionButtons, BorderLayout.SOUTH);
            classToolsCard.add(quizBuilderPanel, BorderLayout.CENTER);
        } else {
            JPanel leaderboardHeader = new JPanel(new BorderLayout(8, 4));
            leaderboardHeader.setOpaque(false);

            JLabel leaderboardTitleLabel = new JLabel("Section Leaderboard");
            leaderboardTitleLabel.setFont(new Font(Theme.UI_FONT, Font.BOLD, 14));
            leaderboardTitleLabel.setForeground(Theme.TEXT_DARK);

            leaderboardSortBox = new JComboBox<>(new String[]{"XP", "Completed Quizzes"});
            Theme.styleComboBox(leaderboardSortBox);
            leaderboardSortBox.addActionListener(event -> refreshLeaderboard());

            leaderboardAlgorithmBox = new JComboBox<>(new String[]{
                    "Merge Sort", "Bubble Sort", "Quick Sort"});
            Theme.styleComboBox(leaderboardAlgorithmBox);
            leaderboardAlgorithmBox.addActionListener(event -> refreshLeaderboard());

            leaderboardMetricsButton = Theme.createWhiteButton("Show DSA Metrics");
            leaderboardMetricsButton.setPreferredSize(new Dimension(150, 32));
            leaderboardMetricsButton.addActionListener(event -> {
                showLeaderboardMetrics = !showLeaderboardMetrics;
                leaderboardMetricsButton.setText(showLeaderboardMetrics
                        ? "Hide DSA Metrics" : "Show DSA Metrics");
                refreshLeaderboard();
            });

            JPanel algorithmControls = new JPanel(new BorderLayout(6, 0));
            algorithmControls.setOpaque(false);
            JLabel algorithmNameLabel = new JLabel("Algorithm:");
            algorithmNameLabel.setFont(new Font(Theme.UI_FONT, Font.BOLD, 12));
            algorithmNameLabel.setForeground(Theme.TEXT_DARK);
            algorithmControls.add(algorithmNameLabel, BorderLayout.WEST);
            algorithmControls.add(leaderboardAlgorithmBox, BorderLayout.CENTER);
            algorithmControls.add(leaderboardMetricsButton, BorderLayout.EAST);

            leaderboardHeader.add(leaderboardTitleLabel, BorderLayout.WEST);
            leaderboardHeader.add(leaderboardSortBox, BorderLayout.EAST);
            classToolsCard.add(leaderboardHeader, BorderLayout.NORTH);
            classToolsCard.add(algorithmControls, BorderLayout.SOUTH);

            leaderboardArea = new JTextArea();
            leaderboardArea.setEditable(false);
            leaderboardArea.setFont(new Font(Theme.UI_FONT, Font.PLAIN, 14));
            leaderboardArea.setForeground(Theme.TEXT_DARK);
            leaderboardArea.setBackground(Theme.INPUT_BACKGROUND);
            leaderboardArea.setCaretColor(Theme.DEEP_TEAL);
            leaderboardArea.setBorder(new EmptyBorder(10, 12, 10, 12));

            JScrollPane boardScroll = new JScrollPane(leaderboardArea);
            boardScroll.setBorder(BorderFactory.createLineBorder(Theme.INPUT_BORDER));
            classToolsCard.add(boardScroll, BorderLayout.CENTER);
        }

        classroomSections.add(activitiesCard);
        classroomSections.add(classToolsCard);
        add(titleLabel, BorderLayout.NORTH);
        add(classroomSections, BorderLayout.CENTER);
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
        String questionTypeName = question.getType() == UserSession.QuestionType.MULTIPLE_CHOICE
                ? "Multiple choice" : "Short answer";
        String questionPrompt = question.getPrompt();
        if (questionPrompt.length() > 32) {
            questionPrompt = questionPrompt.substring(0, 29) + "...";
        }
        return number + ". " + questionTypeName + " - " + questionPrompt
                + " (" + question.getPoints() + " pts)";
    }

    private void deleteDraftQuestion() {
        int selectedIndex = draftQuestionList.getSelectedIndex();
        if (selectedIndex < 0) {
            controller.showError("Select a draft question to delete.");
            return;
        }

        int confirmationResult = JOptionPane.showConfirmDialog(
                this, "Delete the selected draft question?", "Delete Question",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirmationResult != JOptionPane.YES_OPTION) return;

        draftQuestions.remove(selectedIndex);
        draftQuestionModel.remove(selectedIndex);
        for (int questionIndex = 0; questionIndex < draftQuestions.size(); questionIndex++) {
            draftQuestionModel.set(
                    questionIndex,
                    formatDraftQuestion(questionIndex + 1, draftQuestions.get(questionIndex)));
        }
    }

    private void deleteSelectedActivity() {
        String selectedAssignmentLabel = assignmentList.getSelectedValue();
        if (selectedAssignmentLabel == null) {
            controller.showError("Select an activity to delete.");
            return;
        }

        int confirmationResult = JOptionPane.showConfirmDialog(
                this,
                "Delete \"" + selectedAssignmentLabel + "\" and all of its questions?",
                "Delete Activity",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (confirmationResult != JOptionPane.YES_OPTION) return;

        UserSession userSession = controller.getSession();
        if (!userSession.deleteTeacherQuiz(
                userSession.getCurrentSection(), selectedAssignmentLabel)) {
            controller.showError("The selected activity could not be found.");
            return;
        }
        refresh();
    }

    private UserSession.TeacherQuestion showAddQuestionDialog() {
        JComboBox<String> questionTypeSelector =
                new JComboBox<>(new String[]{"Short Answer", "Multiple Choice"});

        JTextArea questionPromptArea = new JTextArea(3, 24);
        questionPromptArea.setLineWrap(true);
        questionPromptArea.setWrapStyleWord(true);
        questionPromptArea.setFont(new Font(Theme.UI_FONT, Font.PLAIN, 14));
        questionPromptArea.setForeground(Theme.TEXT_DARK);
        questionPromptArea.setBackground(Theme.INPUT_BACKGROUND);
        questionPromptArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.INPUT_BORDER),
                new EmptyBorder(6, 8, 6, 8)));

        JTextField pointsField = new JTextField("50");
        Theme.styleTextField(pointsField);
        JTextField correctAnswerField = new JTextField();
        Theme.styleTextField(correctAnswerField);

        JPanel shortAnswerPanel = new JPanel(new BorderLayout(4, 4));
        shortAnswerPanel.setOpaque(false);
        shortAnswerPanel.add(new JLabel("Correct answer:"), BorderLayout.NORTH);
        shortAnswerPanel.add(correctAnswerField, BorderLayout.CENTER);

        JTextField[] answerOptionFields = new JTextField[4];
        JPanel answerOptionsPanel = new JPanel(new GridLayout(5, 2, 4, 4));
        answerOptionsPanel.setOpaque(false);
        for (int optionIndex = 0; optionIndex < answerOptionFields.length; optionIndex++) {
            answerOptionFields[optionIndex] = new JTextField();
            Theme.styleTextField(answerOptionFields[optionIndex]);
            answerOptionsPanel.add(new JLabel("Option " + (char) ('A' + optionIndex) + ":"));
            answerOptionsPanel.add(answerOptionFields[optionIndex]);
        }
        JComboBox<String> correctOptionSelector = new JComboBox<>(new String[]{"A", "B", "C", "D"});
        Theme.styleComboBox(correctOptionSelector);
        answerOptionsPanel.add(new JLabel("Correct option:"));
        answerOptionsPanel.add(correctOptionSelector);

        CardLayout answerTypeLayout = new CardLayout();
        JPanel answerTypeCards = new JPanel(answerTypeLayout);
        answerTypeCards.add(shortAnswerPanel, "SHORT");
        answerTypeCards.add(answerOptionsPanel, "MULTIPLE");
        questionTypeSelector.addActionListener(event -> answerTypeLayout.show(
                answerTypeCards, questionTypeSelector.getSelectedIndex() == 0 ? "SHORT" : "MULTIPLE"));

        JPanel questionForm = new JPanel();
        questionForm.setLayout(new BoxLayout(questionForm, BoxLayout.Y_AXIS));
        questionForm.add(new JLabel("Question type:"));
        questionForm.add(questionTypeSelector);
        questionForm.add(Box.createVerticalStrut(6));
        questionForm.add(new JLabel("Question:"));
        questionForm.add(new JScrollPane(questionPromptArea));
        questionForm.add(Box.createVerticalStrut(6));
        questionForm.add(new JLabel("Points for a correct answer:"));
        questionForm.add(pointsField);
        questionForm.add(Box.createVerticalStrut(6));
        questionForm.add(answerTypeCards);

        int dialogResult = JOptionPane.showConfirmDialog(this, questionForm, "Add Question",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (dialogResult != JOptionPane.OK_OPTION) {
            return null;
        }

        String questionPrompt = questionPromptArea.getText().trim();
        if (questionPrompt.isEmpty()) {
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
        } catch (NumberFormatException numberFormatException) {
            controller.showError("Enter a valid points value.");
            return null;
        }

        if (questionTypeSelector.getSelectedIndex() == 0) {
            String correctAnswer = correctAnswerField.getText().trim();
            if (correctAnswer.isEmpty()) {
                controller.showError("Enter the correct answer.");
                return null;
            }
            return new UserSession.TeacherQuestion(
                    UserSession.QuestionType.SHORT_ANSWER, questionPrompt, correctAnswer, null, points);
        }

        String[] answerChoices = new String[answerOptionFields.length];
        for (int optionIndex = 0; optionIndex < answerOptionFields.length; optionIndex++) {
            String optionText = answerOptionFields[optionIndex].getText().trim();
            if (optionText.isEmpty()) {
                controller.showError("Fill in all four answer options.");
                return null;
            }
            answerChoices[optionIndex] = (char) ('A' + optionIndex) + ") " + optionText;
        }
        return new UserSession.TeacherQuestion(
                UserSession.QuestionType.MULTIPLE_CHOICE,
                questionPrompt,
                (String) correctOptionSelector.getSelectedItem(),
                answerChoices,
                points);
    }

    private void publishQuiz() {
        String quizTitle = quizTitleField.getText().trim();
        if (quizTitle.isEmpty()) {
            controller.showError("Enter a quiz title.");
            return;
        }
        if (draftQuestions.isEmpty()) {
            controller.showError("Add at least one question before publishing.");
            return;
        }

        String sectionName = controller.getSession().getCurrentSection();
        UserSession.TeacherQuiz teacherQuiz = new UserSession.TeacherQuiz(quizTitle, draftQuestions);
        if (!controller.getSession().addTeacherQuiz(sectionName, teacherQuiz)) {
            controller.showError("An activity with that title and question count already exists.");
            return;
        }

        draftQuestions.clear();
        draftQuestionModel.clear();
        quizTitleField.setText("");
        refresh();
        controller.showAlert("Quiz published to Section " + sectionName + ".");
    }

    public void clearDraftQuiz() {
        if (!teacherMode) return;
        draftQuestions.clear();
        draftQuestionModel.clear();
        quizTitleField.setText("");
    }

    @Override
    public void refresh() {
        UserSession userSession = controller.getSession();
        String sectionName = userSession.getCurrentSection();
        titleLabel.setText((teacherMode ? "Manage Class" : "Classroom") + " - " + sectionName);

        DefaultListModel<String> assignmentLabels =
                userSession.getAssignmentsForSection(sectionName);
        assignmentList.setModel(assignmentLabels);
        if (!assignmentLabels.isEmpty() && assignmentList.getSelectedIndex() == -1) {
            assignmentList.setSelectedIndex(0);
        }

        if (!teacherMode && leaderboardArea != null) {
            refreshLeaderboard();
        }
    }

    private void refreshLeaderboard() {
        UserSession userSession = controller.getSession();
        String sectionName = userSession.getCurrentSection();
        List<UserSession.StudentRecord> students = userSession.getStudentsInSection(sectionName);
        ArrayList<PerformanceRecord> studentPerformanceRecords = new ArrayList<>();
        StringBuilder leaderboardText = new StringBuilder();

        for (UserSession.StudentRecord student : students) {
            studentPerformanceRecords.add(new PerformanceRecord(
                    student.getName(),
                    student.getName(),
                    student.getCompletedQuizzes(),
                    student.getXp(),
                    0));
        }

        if (studentPerformanceRecords.isEmpty()) {
            leaderboardText.append("No students in this section yet.");
            leaderboardArea.setText(leaderboardText.toString());
            leaderboardArea.setCaretPosition(0);
            return;
        }

        if (leaderboardSortBox != null) {
            String sortCriterion = (String) leaderboardSortBox.getSelectedItem();
            String sortingCriterion = "Completed Quizzes".equals(sortCriterion)
                    ? SortingAlgorithm.CRITERIA_SCORE
                    : SortingAlgorithm.CRITERIA_XP;

            String selectedAlgorithmName = (String) leaderboardAlgorithmBox.getSelectedItem();
            SortingAlgorithm sortingAlgorithm;
            if ("Bubble Sort".equals(selectedAlgorithmName)) {
                sortingAlgorithm = new BubbleSortAdapter();
            } else if ("Quick Sort".equals(selectedAlgorithmName)) {
                sortingAlgorithm = new QuickSortAdapter();
            } else {
                sortingAlgorithm = new MergeSortAdapter();
            }
            sortingAlgorithm.sort(studentPerformanceRecords, sortingCriterion, false);

            if (showLeaderboardMetrics) {
                leaderboardText.append("\n");
                leaderboardText.append("Algorithm: ").append(sortingAlgorithm.getName()).append("\n");
                leaderboardText.append("Comparisons: ").append(sortingAlgorithm.getComparisons()).append("\n");
                leaderboardText.append("Swaps/Moves: ").append(sortingAlgorithm.getSwaps()).append("\n");
                leaderboardText.append(String.format("Time: %.3f ms",
                        sortingAlgorithm.getExecutionTime() / 1_000_000.0));

                leaderboardText.append("\n\nAlgorithm Comparison:\n");
                leaderboardText.append(String.format("%-12s %12s %12s %10s%n",
                        "Algorithm", "Comparisons", "Swaps/Moves", "Time(ms)"));
                SortingAlgorithm[] comparisonAlgorithms = {
                        new BubbleSortAdapter(),
                        new MergeSortAdapter(),
                        new QuickSortAdapter()
                };
                for (SortingAlgorithm benchmarkAlgorithm : comparisonAlgorithms) {
                    ArrayList<PerformanceRecord> recordsToSort =
                            new ArrayList<>(studentPerformanceRecords);
                    benchmarkAlgorithm.sort(recordsToSort, sortingCriterion, false);
                    leaderboardText.append(String.format("%-12s %12d %12d %10.3f%n",
                            benchmarkAlgorithm.getName(),
                            benchmarkAlgorithm.getComparisons(),
                            benchmarkAlgorithm.getSwaps(),
                            benchmarkAlgorithm.getExecutionTime() / 1_000_000.0));
                }
            }
        }

        int rank = 1;
        for (PerformanceRecord studentRecord : studentPerformanceRecords) {
            leaderboardText.append(rank++)
                    .append(". @").append(studentRecord.getUsername())
                    .append(" - ").append(studentRecord.getTotalXp()).append(" XP")
                    .append(" | Quizzes: ").append(studentRecord.getQuizScore())
                    .append("\n");
        }
        if (leaderboardText.length() == 0) {
            leaderboardText.append("No students in this section yet.");
        }
        leaderboardArea.setText(leaderboardText.toString());
        leaderboardArea.setCaretPosition(0);
    }
}
