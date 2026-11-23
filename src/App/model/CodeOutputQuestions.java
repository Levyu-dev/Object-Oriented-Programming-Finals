package App.model;

public class CodeOutputQuestions extends Questions {
    public CodeOutputQuestions(String questionText, String correctAnswer) {
        super(questionText, correctAnswer);
    }

    @Override
    void showQuestion() {
        System.out.println(questionText);
    }
}
