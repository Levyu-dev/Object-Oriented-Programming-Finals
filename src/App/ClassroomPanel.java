package App;

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
    private JTextField quizTitleField;
    private DefaultListModel<String> draftQuestionModel;
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

        if (!isTeacher) {
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
                        controller.launchQuizSession(extractQuestionCount(selected));
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
            JList<String> draftList = new JList<>(draftQuestionModel);
            draftList.setFont(new Font(Theme.UI_FONT, Font.PLAIN, 12));
            draftList.setForeground(Theme.TEXT_DARK);
            draftList.setBackground(Theme.INPUT_BACKGROUND);
            draftList.setSelectionBackground(Theme.BRIGHT_LIME);
            JScrollPane draftScroll = new JScrollPane(draftList);
            draftScroll.setBorder(BorderFactory.createLineBorder(Theme.INPUT_BORDER));

            JButton addQuestionButton = Theme.createWhiteButton("ADD QUESTION");
            addQuestionButton.addActionListener(e -> addDraftQuestion());
            JButton publishButton = Theme.createPrimaryButton("PUBLISH QUIZ");
            publishButton.addActionListener(e -> publishQuiz());
            JButton randomActivityButton = Theme.createWhiteButton("RANDOM ACTIVITY");
            randomActivityButton.addActionListener(e -> postPracticeActivity());

            JPanel buttons = new JPanel(new GridLayout(1, 3, 6, 0));
            buttons.setOpaque(false);
            buttons.add(addQuestionButton);
            buttons.add(publishButton);
            buttons.add(randomActivityButton);

            builder.add(titlePanel, BorderLayout.NORTH);
            builder.add(draftScroll, BorderLayout.CENTER);
            builder.add(buttons, BorderLayout.SOUTH);
            bottomCard.add(builder, BorderLayout.CENTER);
        } else {
            JLabel boardTitle = new JLabel("Section Leaderboard");
            boardTitle.setFont(new Font(Theme.UI_FONT, Font.BOLD, 14));
            boardTitle.setForeground(Theme.TEXT_DARK);
            bottomCard.add(boardTitle, BorderLayout.NORTH);

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
        String type = question.getType() == UserSession.QuestionType.MULTIPLE_CHOICE
                ? "Multiple choice" : "Short answer";
        String prompt = question.getPrompt();
        if (prompt.length() > 32) {
            prompt = prompt.substring(0, 29) + "...";
        }
        draftQuestionModel.addElement(draftQuestions.size() + ". " + type + " - "
                + prompt + " (" + question.getPoints() + " pts)");
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

    private void postPracticeActivity() {
        JTextField titleField = new JTextField();
        Theme.styleTextField(titleField);
        JTextField countField = new JTextField("5");
        Theme.styleTextField(countField);

        JPanel form = new JPanel(new GridLayout(2, 2, 6, 6));
        form.add(new JLabel("Activity title:"));
        form.add(titleField);
        form.add(new JLabel("Question count (1-19):"));
        form.add(countField);

        int result = JOptionPane.showConfirmDialog(this, form, "Post Random Activity",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            controller.showError("Enter an activity title.");
            return;
        }

        int count;
        try {
            count = Integer.parseInt(countField.getText().trim());
        } catch (NumberFormatException ex) {
            controller.showError("Enter a valid question count.");
            return;
        }
        if (count < 1 || count > 19) {
            controller.showError("Questions must be between 1 and 19.");
            return;
        }

        DefaultListModel<String> assignments = controller.getSession()
                .getAssignmentsForSection(controller.getSession().getCurrentSection());
        String label = title + " (" + count + " Qs)";
        for (int i = 0; i < assignments.size(); i++) {
            if (assignments.get(i).equalsIgnoreCase(label)) {
                controller.showError("An activity with that title and question count already exists.");
                return;
            }
        }

        assignments.addElement(label);
        refresh();
        controller.showAlert("Activity posted to Section " + controller.getSession().getCurrentSection() + ".");
    }

    private int extractQuestionCount(String activityText) {
        try {
            int open = activityText.lastIndexOf('(');
            int close = activityText.lastIndexOf(')');
            if (open != -1 && close > open) {
                String inside = activityText.substring(open + 1, close).replaceAll("[^0-9]", "");
                if (!inside.isEmpty()) {
                    int parsed = Integer.parseInt(inside);
                    return Math.max(1, Math.min(19, parsed));
                }
            }
        } catch (Exception e) {
            // Fallback to 5 questions
        }
        return 5;
    }

    @Override
    public void refresh() {
        UserSession session = controller.getSession();
        String sec = session.getCurrentSection();
        titleLabel.setText((isTeacher ? "Manage Class" : "Classroom") + " — " + sec);

        DefaultListModel<String> model = session.getAssignmentsForSection(sec);
        assignmentList.setModel(model);
        if (!model.isEmpty() && assignmentList.getSelectedIndex() == -1) {
            assignmentList.setSelectedIndex(0);
        }

        if (!isTeacher && leaderboardArea != null) {
            StringBuilder sb = new StringBuilder();
            int rank = 1;
            for (UserSession.StudentRecord s : session.getStudentsInSection(sec)) {
                sb.append(rank++).append(". @").append(s.getName()).append(" — ").append(s.getXp()).append(" XP\n");
            }
            if (sb.length() == 0) {
                sb.append("No students in this section yet.");
            }
            leaderboardArea.setText(sb.toString());
            leaderboardArea.setCaretPosition(0);
        }
    }
}
