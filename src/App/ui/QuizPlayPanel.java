package App.ui;

import App.controller.ApplicationController;
import App.model.QuizQuestionBank;
import App.model.QuizQuestion;
import App.model.MultipleChoiceQuestion;
import App.model.ShortAnswerQuestion;
import App.model.UserSession;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizPlayPanel extends BaseScreen {
    public enum QuizMode {
        STANDARD,
        QUICK_PRACTICE,
        TIMED,
        FINAL_BOSS,
        PARTY
    }

    private List<QuizQuestion> quizQuestions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int earnedExperiencePoints = 0;

    private JLabel progressLabel;
    private JLabel badgeLabel;
    private JTextArea questionArea;
    private JPanel questionCard;
    private JPanel quizContent;
    private JPanel answerPanel;
    private QuizMode quizMode = QuizMode.STANDARD;
    private javax.swing.Timer timedQuizTimer;
    private int secondsRemaining;
    private int bossHp = 100;
    private int playerHp = 100;
    private final String[] partyPlayerNames = {"You", "Jumana", "Iyce"};
    private int currentPartyPlayerIndex;
    private String activeQuickPracticeTopic = "";

    public QuizPlayPanel(ApplicationController controller) {
        super(controller);
        setLayout(new BorderLayout(10, 0));
        setBackground(Theme.PALE_LIME);
        setBorder(new EmptyBorder(10, 12, 10, 12));

        JPanel topPanel = new JPanel(new BorderLayout(4, 4));
        topPanel.setBackground(Theme.PALE_LIME);

        progressLabel = new JLabel("Question 1");
        progressLabel.setFont(new Font(Theme.UI_FONT, Font.BOLD, 15));
        progressLabel.setForeground(Theme.TEXT_DARK);

        badgeLabel = new JLabel("OOP QUIZ");
        badgeLabel.setFont(new Font(Theme.UI_FONT, Font.BOLD, 11));
        badgeLabel.setForeground(Theme.DEEP_TEAL);

        topPanel.add(progressLabel, BorderLayout.NORTH);
        topPanel.add(badgeLabel, BorderLayout.SOUTH);

        questionCard = Theme.createRoundedCard(Theme.SOFT_LIME_CARD, 2);
        questionCard.setLayout(new BorderLayout());

        questionArea = new JTextArea(4, 40);
        questionArea.setEditable(false);
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        questionArea.setFont(new Font("Monospaced", Font.BOLD, 12));
        questionArea.setForeground(Theme.TEXT_DARK);
        questionArea.setBackground(Theme.SOFT_LIME_CARD);
        questionArea.setBorder(new EmptyBorder(0, 2, 0, 2));

        JScrollPane scroll = new JScrollPane(questionArea);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Theme.SOFT_LIME_CARD);
        questionCard.add(scroll, BorderLayout.CENTER);

        quizContent = new JPanel();
        quizContent.setLayout(new BoxLayout(quizContent, BoxLayout.Y_AXIS));
        quizContent.setBackground(Theme.PALE_LIME);
        quizContent.add(topPanel);
        quizContent.add(questionCard);
        quizContent.add(Box.createVerticalStrut(18));
        add(quizContent, BorderLayout.NORTH);
    }

    private List<QuizQuestion> createStandardQuizQuestions() {
        return new ArrayList<>(QuizQuestionBank.createCoreQuizQuestions());
    }

    public void startStandardQuiz(int questionCount) {
        quizMode = QuizMode.STANDARD;
        activeQuickPracticeTopic = "";
        startShuffledQuiz(questionCount);
    }

    public void startQuickPractice(String topic, int questionCount) {
        List<QuizQuestion> topicQuestions = QuizQuestionBank.createQuestionsForTopic(topic);
        if (topicQuestions.isEmpty()) {
            throw new IllegalArgumentException("Unknown quick-practice topic: " + topic);
        }
        quizMode = QuizMode.QUICK_PRACTICE;
        activeQuickPracticeTopic = topic;
        Collections.shuffle(topicQuestions);
        int selectedQuestionCount = Math.min(questionCount, topicQuestions.size());
        startQuizWithQuestions(new ArrayList<>(topicQuestions.subList(0, selectedQuestionCount)));
    }

    private void startShuffledQuiz(int questionCount) {
        List<QuizQuestion> availableQuestions = createStandardQuizQuestions();
        Collections.shuffle(availableQuestions);
        int selectedQuestionCount = Math.min(questionCount, availableQuestions.size());
        startQuizWithQuestions(
                new ArrayList<>(availableQuestions.subList(0, selectedQuestionCount)));
    }

    public void startTimedQuiz(int questionCount) {
        quizMode = QuizMode.TIMED;
        activeQuickPracticeTopic = "";
        secondsRemaining = 60;
        startShuffledQuiz(questionCount);
        badgeLabel.setText("TIMED QUIZ - 60 seconds");
        if (timedQuizTimer != null) {
            timedQuizTimer.stop();
        }
        timedQuizTimer = new javax.swing.Timer(1000, timerEvent -> {
            secondsRemaining--;
            if (secondsRemaining <= 0) {
                timedQuizTimer.stop();
                finishQuiz("Time is up!");
            } else {
                badgeLabel.setText("TIMED QUIZ - " + secondsRemaining + " seconds");
            }
        });
        timedQuizTimer.start();
    }

    public void startFinalBossQuiz(int questionCount) {
        quizMode = QuizMode.FINAL_BOSS;
        activeQuickPracticeTopic = "";
        bossHp = 100;
        playerHp = 100;
        startShuffledQuiz(questionCount);
    }

    public void startPartyQuiz(int questionCount) {
        quizMode = QuizMode.PARTY;
        activeQuickPracticeTopic = "";
        currentPartyPlayerIndex = 0;
        startShuffledQuiz(questionCount);
    }

    public void startTeacherQuiz(UserSession.TeacherQuiz quiz) {
        quizMode = QuizMode.STANDARD;
        activeQuickPracticeTopic = "";
        List<QuizQuestion> quizQuestions = new ArrayList<>();
        for (UserSession.TeacherQuestion question : quiz.getQuestions()) {
            if (question.getType() == UserSession.QuestionType.MULTIPLE_CHOICE) {
                quizQuestions.add(new MultipleChoiceQuestion(
                        question.getPrompt(),
                        question.getChoices(),
                        question.getCorrectAnswer(),
                        question.getPoints()));
            } else {
                quizQuestions.add(new ShortAnswerQuestion(
                        question.getPrompt(),
                        question.getCorrectAnswer(),
                        question.getPoints()));
            }
        }
        startQuizWithQuestions(quizQuestions);
    }

    private void startQuizWithQuestions(List<QuizQuestion> selectedQuestions) {
        if (timedQuizTimer != null) {
            timedQuizTimer.stop();
        }
        quizQuestions = selectedQuestions;
        currentQuestionIndex = 0;
        score = 0;
        earnedExperiencePoints = 0;
        refresh();
    }

    @Override
    public void refresh() {
        if (quizQuestions.isEmpty()) {
            return;
        }

        QuizQuestion currentQuestion = quizQuestions.get(currentQuestionIndex);
        progressLabel.setText("Question " + (currentQuestionIndex + 1) + " of "
                + quizQuestions.size() + "  |  Score: " + score);
        String questionType = currentQuestion instanceof MultipleChoiceQuestion ? "MULTIPLE CHOICE"
                : currentQuestion instanceof ShortAnswerQuestion ? "SHORT ANSWER"
                : currentQuestion.getClass().getSimpleName().replace("Question", "").toUpperCase();
        String quizModeLabel = quizMode == QuizMode.TIMED ? "TIMED"
                : quizMode == QuizMode.FINAL_BOSS ? "FINAL BOSS"
                : quizMode == QuizMode.PARTY
                        ? "PARTY - " + partyPlayerNames[currentPartyPlayerIndex]
                : quizMode == QuizMode.QUICK_PRACTICE
                        ? "QUICK PRACTICE - " + activeQuickPracticeTopic
                : "OOP QUIZ";
        if (quizMode == QuizMode.TIMED) {
            badgeLabel.setText(quizModeLabel + " - " + secondsRemaining + " seconds");
        } else if (quizMode == QuizMode.FINAL_BOSS) {
            badgeLabel.setText(quizModeLabel + " - Boss " + bossHp + " HP / You " + playerHp + " HP");
        } else {
            badgeLabel.setText(quizModeLabel + " - " + questionType + " - " + currentQuestion.getPoints() + " POINTS");
        }
        questionArea.setText(currentQuestion.getQuestionText());
        questionArea.setCaretPosition(0);
        questionCard.setVisible(!currentQuestion.getQuestionText().trim().isEmpty());

        if (answerPanel != null) {
            quizContent.remove(answerPanel);
        }

        if (currentQuestion instanceof MultipleChoiceQuestion) {
            JPanel answerChoiceGrid = new JPanel(new GridLayout(4, 1, 0, 8));
            answerChoiceGrid.setBackground(Theme.PALE_LIME);

            for (String choiceText : ((MultipleChoiceQuestion) currentQuestion).getChoices()) {
                JButton answerButton = Theme.createPrimaryButton(
                        "<html><div style='width:240px; text-align:left;'>"
                                + choiceText + "</div></html>");
                answerButton.setHorizontalAlignment(SwingConstants.LEFT);
                answerButton.setMargin(new Insets(0, 12, 0, 12));
                String answerLetter = choiceText.substring(0, 1);
                answerButton.addActionListener(
                        event -> submitAnswer(currentQuestion, answerLetter));
                answerChoiceGrid.add(answerButton);
            }
            answerPanel = answerChoiceGrid;
        } else {
            JPanel inputBox = Theme.createRoundedCard(Theme.SOFT_LIME_CARD, 12);
            inputBox.setLayout(new BorderLayout(8, 8));

            JLabel prompt = new JLabel("Enter your answer:");
            prompt.setFont(new Font(Theme.UI_FONT, Font.BOLD, 12));
            prompt.setForeground(Theme.TEXT_DARK);

            JTextField answerField = new JTextField();
            Theme.styleTextField(answerField);

            JButton submitBtn = Theme.createPrimaryButton("SUBMIT ANSWER");
            submitBtn.addActionListener(
                    event -> submitAnswer(currentQuestion, answerField.getText()));

            inputBox.add(prompt, BorderLayout.NORTH);
            inputBox.add(answerField, BorderLayout.CENTER);
            inputBox.add(submitBtn, BorderLayout.SOUTH);
            answerPanel = inputBox;
        }

        quizContent.add(answerPanel);
        quizContent.revalidate();
        revalidate();
        repaint();
    }

    private void submitAnswer(QuizQuestion question, String userAnswer) {
        boolean correct = question.checkAnswer(userAnswer);
        if (correct) {
            score++;
            earnedExperiencePoints += question.getPoints();
            controller.showAlert("Correct! (+" + question.getPoints() + " points)");
            if (quizMode == QuizMode.FINAL_BOSS) {
                bossHp = Math.max(0, bossHp - 20);
            }
        } else {
            controller.showError("Wrong Answer! The correct answer is: " + question.getCorrectAnswer());
            if (quizMode == QuizMode.FINAL_BOSS) {
                playerHp = Math.max(0, playerHp - 15);
            }
        }

        if (quizMode == QuizMode.PARTY) {
            currentPartyPlayerIndex =
                    (currentPartyPlayerIndex + 1) % partyPlayerNames.length;
        }

        if (quizMode == QuizMode.FINAL_BOSS && (bossHp == 0 || playerHp == 0)) {
            finishQuiz(bossHp == 0 ? "You defeated the final boss!" : "The boss defeated you!");
            return;
        }

        currentQuestionIndex++;
        if (currentQuestionIndex < quizQuestions.size()) {
            refresh();
            return;
        }

        finishQuiz("Quiz complete!");
    }

    private void finishQuiz(String resultMessage) {
        if (timedQuizTimer != null) {
            timedQuizTimer.stop();
        }
        controller.getSession().addExperiencePoints(
                earnedExperiencePoints, score, quizQuestions.size());
        controller.showAlert(resultMessage + "\nYour Final Score: " + score + "/" + quizQuestions.size()
                + "\nEarned: +" + earnedExperiencePoints + " points");
        controller.showStudentScreen("STUDENT_DASH");
    }
}
