package App;

import java.util.Scanner;

abstract class Questions {
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

    boolean askQuestion(Scanner input) {
        showQuestion();
        System.out.print("Answer: ");
        String answer = input.nextLine();
        return checkAnswer(answer);
    }

    boolean checkAnswer(String answer) {
        return answer != null
                && answer.trim().equalsIgnoreCase(correctAnswer.trim());
    }

    String getQuestionText() {
        return questionText;
    }

    String getCorrectAnswer() {
        return correctAnswer;
    }

    int getPoints() {
        return points;
    }
}

class MultipleChoiceQuestions extends Questions {
    private final String[] choices;

    MultipleChoiceQuestions(String questionText, String[] choices, String correctAnswer) {
        this(questionText, choices, correctAnswer, 50);
    }

    MultipleChoiceQuestions(String questionText, String[] choices, String correctAnswer, int points) {
        super(questionText, correctAnswer, points);
        this.choices = choices.clone();
    }

    String[] getChoices() {
        return choices.clone();
    }

    @Override
    void showQuestion() {
        System.out.println(questionText);
        for (String choice : choices) {
            System.out.println(choice);
        }
    }
}

class IdentificationQuestions extends Questions {
    IdentificationQuestions(String questionText, String correctAnswer) {
        super(questionText, correctAnswer);
    }

    IdentificationQuestions(String questionText, String correctAnswer, int points) {
        super(questionText, correctAnswer, points);
    }

    @Override
    void showQuestion() {
        System.out.println(questionText);
    }
}

class CodeOutputQuestions extends Questions {
    CodeOutputQuestions(String questionText, String correctAnswer) {
        super(questionText, correctAnswer);
    }

    @Override
    void showQuestion() {
        System.out.println(questionText);
    }
}

class CodeCompletionQuestions extends Questions {
    CodeCompletionQuestions(String questionText, String correctAnswer) {
        super(questionText, correctAnswer);
    }

    @Override
    void showQuestion() {
        System.out.println(questionText);
    }
}

class DebuggingQuestions extends Questions {
    DebuggingQuestions(String questionText, String correctAnswer) {
        super(questionText, correctAnswer);
    }

    @Override
    void showQuestion() {
        System.out.println(questionText);
    }
}
