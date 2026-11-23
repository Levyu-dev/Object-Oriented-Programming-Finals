package App.model;

public class DebuggingQuestions extends Questions {
    public DebuggingQuestions(String questionText, String correctAnswer) {
        super(questionText, correctAnswer);
    }

    @Override
    void showQuestion() {
        System.out.println(questionText);
    }
}
