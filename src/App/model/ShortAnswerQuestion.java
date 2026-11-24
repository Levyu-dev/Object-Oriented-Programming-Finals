package App.model;

public class ShortAnswerQuestion extends QuizQuestion {
    public ShortAnswerQuestion(String questionText, String correctAnswer) {
        super(questionText, correctAnswer);
    }

    public ShortAnswerQuestion(String questionText, String correctAnswer, int points) {
        super(questionText, correctAnswer, points);
    }

    @Override
    void printQuestion() {
        System.out.println(questionText);
    }
}
