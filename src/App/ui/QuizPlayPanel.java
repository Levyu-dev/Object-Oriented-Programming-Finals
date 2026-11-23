package App.ui;

import App.controller.ApplicationController;
import App.model.QuizQuestionBank;
import App.model.Questions;
import App.model.MultipleChoiceQuestions;
import App.model.IdentificationQuestions;
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

    private List<Questions> activeQuestions = new ArrayList<>();
    private int currentIndex = 0;
    private int score = 0;
    private int earnedXP = 0;

    private JLabel progressLabel;
    private JLabel badgeLabel;
    private JTextArea questionArea;
    private JPanel answerContainer;
    private QuizMode mode = QuizMode.STANDARD;
    private javax.swing.Timer timedQuizTimer;
    private int secondsRemaining;
    private int bossHp = 100;
    private int playerHp = 100;
    private final String[] partyPlayers = {"You", "Jumana", "Iyce"};
    private int partyPlayerIndex;
    private String activeQuickPracticeTopic = "";

    public QuizPlayPanel(ApplicationController controller) {
        super(controller);
        setLayout(new BorderLayout(10, 10));
        setBackground(Theme.PALE_LIME);
        setBorder(new EmptyBorder(14, 16, 14, 16));

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

        JPanel questionCard = Theme.createRoundedCard(Theme.SOFT_LIME_CARD, 12);
        questionCard.setLayout(new BorderLayout());

        questionArea = new JTextArea();
        questionArea.setEditable(false);
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        questionArea.setFont(new Font("Monospaced", Font.BOLD, 12));
        questionArea.setForeground(Theme.TEXT_DARK);
        questionArea.setBackground(Theme.SOFT_LIME_CARD);

        JScrollPane scroll = new JScrollPane(questionArea);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Theme.SOFT_LIME_CARD);
        questionCard.add(scroll, BorderLayout.CENTER);

        answerContainer = new JPanel(new BorderLayout());
        answerContainer.setBackground(Theme.PALE_LIME);
        answerContainer.setPreferredSize(new Dimension(0, 220));

        add(topPanel, BorderLayout.NORTH);
        add(questionCard, BorderLayout.CENTER);
        add(answerContainer, BorderLayout.SOUTH);
    }

    private List<Questions> buildTeammateQuestions() {
        return new ArrayList<>(QuizQuestionBank.createQuestions());
    }

    public void startQuiz(int count) {
        mode = QuizMode.STANDARD;
        activeQuickPracticeTopic = "";
        startShuffledQuestions(count);
    }

    public void startQuickPractice(String topic, int count) {
        List<Questions> questions = QuizQuestionBank.createQuestions(topic);
        if (questions.isEmpty()) {
            throw new IllegalArgumentException("Unknown quick-practice topic: " + topic);
        }
        mode = QuizMode.QUICK_PRACTICE;
        activeQuickPracticeTopic = topic;
        Collections.shuffle(questions);
        int limit = Math.min(count, questions.size());
        startQuestions(new ArrayList<>(questions.subList(0, limit)));
    }

    private void startShuffledQuestions(int count) {
        List<Questions> all = buildTeammateQuestions();
        Collections.shuffle(all);
        int limit = Math.min(count, all.size());
        startQuestions(new ArrayList<>(all.subList(0, limit)));
    }

    public void startTimedQuiz(int count) {
        mode = QuizMode.TIMED;
        activeQuickPracticeTopic = "";
        secondsRemaining = 60;
        startShuffledQuestions(count);
        badgeLabel.setText("TIMED QUIZ - 60 seconds");
        if (timedQuizTimer != null) {
            timedQuizTimer.stop();
        }
        timedQuizTimer = new javax.swing.Timer(1000, e -> {
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

    public void startFinalBossQuiz(int count) {
        mode = QuizMode.FINAL_BOSS;
        activeQuickPracticeTopic = "";
        bossHp = 100;
        playerHp = 100;
        startShuffledQuestions(count);
    }

    public void startPartyQuiz(int count) {
        mode = QuizMode.PARTY;
        activeQuickPracticeTopic = "";
        partyPlayerIndex = 0;
        startShuffledQuestions(count);
    }

    public void startTeacherQuiz(UserSession.TeacherQuiz quiz) {
        mode = QuizMode.STANDARD;
        activeQuickPracticeTopic = "";
        List<Questions> questions = new ArrayList<>();
        for (UserSession.TeacherQuestion question : quiz.getQuestions()) {
            if (question.getType() == UserSession.QuestionType.MULTIPLE_CHOICE) {
                questions.add(new MultipleChoiceQuestions(
                        question.getPrompt(),
                        question.getChoices(),
                        question.getCorrectAnswer(),
                        question.getPoints()));
            } else {
                questions.add(new IdentificationQuestions(
                        question.getPrompt(),
                        question.getCorrectAnswer(),
                        question.getPoints()));
            }
        }
        startQuestions(questions);
    }

    private void startQuestions(List<Questions> questions) {
        if (timedQuizTimer != null) {
            timedQuizTimer.stop();
        }
        activeQuestions = questions;
        currentIndex = 0;
        score = 0;
        earnedXP = 0;
        refresh();
    }

    @Override
    public void refresh() {
        if (activeQuestions.isEmpty()) {
            return;
        }

        Questions q = activeQuestions.get(currentIndex);
        progressLabel.setText("Question " + (currentIndex + 1) + " of " + activeQuestions.size() + "  |  Score: " + score);
        String questionType = q instanceof MultipleChoiceQuestions ? "MULTIPLE CHOICE"
                : q instanceof IdentificationQuestions ? "SHORT ANSWER"
                : q.getClass().getSimpleName().replace("Questions", "").toUpperCase();
        String modeLabel = mode == QuizMode.TIMED ? "TIMED"
                : mode == QuizMode.FINAL_BOSS ? "FINAL BOSS"
                : mode == QuizMode.PARTY ? "PARTY - " + partyPlayers[partyPlayerIndex]
                : mode == QuizMode.QUICK_PRACTICE ? "QUICK PRACTICE - " + activeQuickPracticeTopic
                : "OOP QUIZ";
        if (mode == QuizMode.TIMED) {
            badgeLabel.setText(modeLabel + " - " + secondsRemaining + " seconds");
        } else if (mode == QuizMode.FINAL_BOSS) {
            badgeLabel.setText(modeLabel + " - Boss " + bossHp + " HP / You " + playerHp + " HP");
        } else {
            badgeLabel.setText(modeLabel + " - " + questionType + " - " + q.getPoints() + " POINTS");
        }
        questionArea.setText(q.getQuestionText());
        questionArea.setCaretPosition(0);

        answerContainer.removeAll();

        if (q instanceof MultipleChoiceQuestions) {
            JPanel btnGrid = new JPanel(new GridLayout(4, 1, 0, 8));
            btnGrid.setBackground(Theme.PALE_LIME);

            for (String choice : ((MultipleChoiceQuestions) q).getChoices()) {
                JButton btn = Theme.createPrimaryButton("<html><div style='width:240px;'>" + choice + "</div></html>");
                String letter = choice.substring(0, 1);
                btn.addActionListener(e -> submitAnswer(q, letter));
                btnGrid.add(btn);
            }
            answerContainer.add(btnGrid, BorderLayout.CENTER);
        } else {
            JPanel inputBox = Theme.createRoundedCard(Theme.SOFT_LIME_CARD, 12);
            inputBox.setLayout(new BorderLayout(8, 8));

            JLabel prompt = new JLabel("Enter your answer:");
            prompt.setFont(new Font(Theme.UI_FONT, Font.BOLD, 12));
            prompt.setForeground(Theme.TEXT_DARK);

            JTextField answerField = new JTextField();
            Theme.styleTextField(answerField);

            JButton submitBtn = Theme.createPrimaryButton("SUBMIT ANSWER");
            submitBtn.setPreferredSize(new Dimension(0, 42));
            submitBtn.addActionListener(e -> submitAnswer(q, answerField.getText()));

            inputBox.add(prompt, BorderLayout.NORTH);
            inputBox.add(answerField, BorderLayout.CENTER);
            inputBox.add(submitBtn, BorderLayout.SOUTH);
            answerContainer.add(inputBox, BorderLayout.CENTER);
        }

        answerContainer.revalidate();
        answerContainer.repaint();
    }

    private void submitAnswer(Questions question, String userAnswer) {
        boolean correct = question.checkAnswer(userAnswer);
        if (correct) {
            score++;
            earnedXP += question.getPoints();
            controller.showAlert("Correct! (+" + question.getPoints() + " points)");
            if (mode == QuizMode.FINAL_BOSS) {
                bossHp = Math.max(0, bossHp - 20);
            }
        } else {
            controller.showError("Wrong Answer! The correct answer is: " + question.getCorrectAnswer());
            if (mode == QuizMode.FINAL_BOSS) {
                playerHp = Math.max(0, playerHp - 15);
            }
        }

        if (mode == QuizMode.PARTY) {
            partyPlayerIndex = (partyPlayerIndex + 1) % partyPlayers.length;
        }

        if (mode == QuizMode.FINAL_BOSS && (bossHp == 0 || playerHp == 0)) {
            finishQuiz(bossHp == 0 ? "You defeated the final boss!" : "The boss defeated you!");
            return;
        }

        currentIndex++;
        if (currentIndex < activeQuestions.size()) {
            refresh();
            return;
        }

        finishQuiz("Quiz complete!");
    }

    private void finishQuiz(String prefix) {
        if (timedQuizTimer != null) {
            timedQuizTimer.stop();
        }
        controller.getSession().addXP(earnedXP);
        controller.showAlert(prefix + "\nYour Final Score: " + score + "/" + activeQuestions.size()
                + "\nEarned: +" + earnedXP + " points");
        controller.showStudentScreen("STUDENT_DASH");
    }
}
