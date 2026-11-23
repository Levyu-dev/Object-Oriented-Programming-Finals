package App.model;

public class IdentificationQuestions extends Questions {
    public IdentificationQuestions(String questionText, String correctAnswer) {
        super(questionText, correctAnswer);
    }

    public IdentificationQuestions(String questionText, String correctAnswer, int points) {
        super(questionText, correctAnswer, points);
    }

    @Override
    void showQuestion() {
        System.out.println(questionText);
    }
}
