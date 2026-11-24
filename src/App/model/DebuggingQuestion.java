package App.model;

public class DebuggingQuestion extends QuizQuestion {
    public DebuggingQuestion(String questionText, String correctAnswer) {
        super(questionText, correctAnswer);
    }

    @Override
    void printQuestion() {
        System.out.println(questionText);
    }
}
