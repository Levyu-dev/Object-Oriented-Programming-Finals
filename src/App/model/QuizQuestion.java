package App.model;

import java.util.Scanner;

public abstract class QuizQuestion {
    protected final String questionText;
    protected final String correctAnswer;
    private final int points;

    QuizQuestion(String questionText, String correctAnswer) {
        this(questionText, correctAnswer, 50);
    }

    QuizQuestion(String questionText, String correctAnswer, int points) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.points = points;
    }

    abstract void printQuestion();

    public boolean askQuestion(Scanner scanner) {
        printQuestion();
        System.out.print("Answer: ");
        String userAnswer = scanner.nextLine();
        return checkAnswer(userAnswer);
    }

    public boolean checkAnswer(String userAnswer) {
        return userAnswer != null
                && userAnswer.trim().equalsIgnoreCase(correctAnswer.trim());
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public int getPoints() {
        return points;
    }
}
