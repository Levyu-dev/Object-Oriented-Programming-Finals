package App.model;

public class CodeOutputQuestion extends QuizQuestion {
    public CodeOutputQuestion(String questionText, String correctAnswer) {
        super(questionText, correctAnswer);
    }

    @Override
    void printQuestion() {
        System.out.println(questionText);
    }
}
