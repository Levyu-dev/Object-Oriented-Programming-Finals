package App.model;

import java.util.Scanner;

public abstract class Questions {
    protected String questionText;
    protected String correctAnswer;
    private final int points;

    Questions(String questionText, String correctAnswer) {
        this(questionText, correctAnswer, 50);
    }

    Questions(String questionText, String correctAnswer, int points) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.points = points;
    }

    abstract void showQuestion();

    public boolean askQuestion(Scanner input) {
        showQuestion();
        System.out.print("Answer: ");
        String answer = input.nextLine();
        return checkAnswer(answer);
    }

    public boolean checkAnswer(String answer) {
        return answer != null
                && answer.trim().equalsIgnoreCase(correctAnswer.trim());
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
