package App;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizPlayPanel extends BaseScreen {
    private List<Questions> activeQuestions = new ArrayList<>();
    private int currentIndex = 0;
    private int score = 0;
    private int earnedXP = 0;

    private JLabel progressLabel;
    private JLabel badgeLabel;
    private JTextArea questionArea;
    private JPanel answerContainer;

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
        List<Questions> bank = new ArrayList<>();

        bank.add(new MultipleChoiceQuestions(
            "A method that calculates and returns a value.",
            new String[]{"A) Void Method", "B) Value-returning method", "C) User-defined Method", "D) Equivalent Method"},
            "B"
        ));
        bank.add(new MultipleChoiceQuestions(
            "A keyword used to make a class inherit from another.",
            new String[]{"A) super", "B) static", "C) override", "D) extends"},
            "D"
        ));
        bank.add(new MultipleChoiceQuestions(
            "This keyword is used so that variables are only visible to the class, its subclasses, and its package.",
            new String[]{"A) public", "B) final", "C) protected", "D) private"},
            "C"
        ));
        bank.add(new MultipleChoiceQuestions(
            "How do you connect the child to its parent when making a UML diagram?",
            new String[]{"A) Dashed line with hollow triangle", "B) Solid line with hollow triangle", "C) Solid line with bold triangle", "D) Dashed line with bold triangle"},
            "B"
        ));
        bank.add(new MultipleChoiceQuestions(
            "What does the final method do?",
            new String[]{"A) It cannot be overridden", "B) It cannot be inherited", "C) Hides the parent method", "D) Hides the object"},
            "A"
        ));

        bank.add(new IdentificationQuestions("A method override that resolves at runtime.", "dynamic method dispatch"));
        bank.add(new IdentificationQuestions("What does Polymorphism mean?", "many forms"));
        bank.add(new IdentificationQuestions("A concept that establishes a Parent-child relationship.", "Inheritance"));
        bank.add(new IdentificationQuestions("A method that resolves at compile-time.", "Overloading"));
        bank.add(new IdentificationQuestions("Hiding unnecessary details and showing only essential features.", "Abstraction"));

        bank.add(new CodeOutputQuestions(
            "class Low { Low() { System.out.println(1); } }\nclass High extends Low { High() { System.out.println(2); } }\nLow obj = new High();",
            "12"
        ));
        bank.add(new CodeOutputQuestions(
            "class Parent { String name = \"P\"; void show() { System.out.println(\"Parent\"); } }\nclass Child extends Parent { String name = \"C\"; void show() { System.out.println(\"Child\"); } }\nParent p = new Child();\nSystem.out.println(p.name);\np.show();",
            "PChild"
        ));
        bank.add(new CodeOutputQuestions(
            "abstract class CoffeeMachine {\n  final void makeCoffee() { System.out.println(\"Boil \"); brew(); }\n  abstract void brew();\n}\nclass Espresso extends CoffeeMachine {\n  void brew() { System.out.println(\"Pressurize \"); }\n}\nCoffeeMachine m = new Espresso();\nm.makeCoffee();",
            "Boil Pressurize"
        ));

        bank.add(new CodeCompletionQuestions(
            "Expected Outcome: Start Run.\nclass Vehicle { void start() { System.out.println(\"Start\"); } }\nclass Car ____ Vehicle {\n  void drive() { _____(); System.out.println(\"Run\"); }\n}",
            "extends, super.start"
        ));
        bank.add(new CodeCompletionQuestions(
            "Expected Outcome: Meow.\nclass Animal { void speak() { System.out.println(\"Sound\"); } }\nclass Cat extends Animal { void speak() { System.out.println(\"Meow\"); } }\n_____ pet = new ____();\npet.speak();",
            "Animal, Cat"
        ));
        bank.add(new CodeCompletionQuestions(
            "Expected Outcome: Draw Circle.\n_____ class Shape { _____ void draw(); }\nclass Circle extends Shape { void draw() { System.out.println(\"Draw Circle\"); } }",
            "abstract, abstract"
        ));

        bank.add(new DebuggingQuestions(
            "class Parent { Parent(String msg) { System.out.println(msg); } }\nclass Child extends Parent {\n  Child() { System.out.println(\"Child code\"); }\n}",
            "super(msg);"
        ));
        bank.add(new DebuggingQuestions(
            "class Employee { void work() { System.out.println(\"Working\"); } }\nclass Developer extends Employee { void code() { System.out.println(\"Coding\"); } }\nEmployee e = new Developer();\ne.code();",
            "((Developer)e).code();"
        ));
        bank.add(new DebuggingQuestions(
            "abstract class Device { private abstract void boot(); }\nclass Laptop extends Device { void boot() {} }",
            "protected abstract void boot();"
        ));

        return bank;
    }

    public void startQuiz(int count) {
        List<Questions> all = buildTeammateQuestions();
        Collections.shuffle(all);
        int limit = Math.min(count, all.size());
        startQuestions(new ArrayList<>(all.subList(0, limit)));
    }

    public void startTeacherQuiz(UserSession.TeacherQuiz quiz) {
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
        badgeLabel.setText(questionType + " - " + q.getPoints() + " POINTS");
        questionArea.setText(q.questionText);
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
        if (question.checkAnswer(userAnswer)) {
            score++;
            earnedXP += question.getPoints();
            controller.showAlert("Correct! (+" + question.getPoints() + " points)");
        } else {
            controller.showError("Wrong Answer! The correct answer is: " + question.correctAnswer);
        }

        currentIndex++;
        if (currentIndex < activeQuestions.size()) {
            refresh();
            return;
        }

        controller.getSession().addXP(earnedXP);
        controller.showAlert("Quiz Over!\nYour Final Score: " + score + "/" + activeQuestions.size()
                + "\nEarned: +" + earnedXP + " points");
        controller.showStudentScreen("STUDENT_DASH");
    }
}
