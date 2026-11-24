package App.model;

public class CodeCompletionQuestion extends QuizQuestion {
    public CodeCompletionQuestion(String questionText, String correctAnswer) {
        super(questionText, correctAnswer);
    }

    @Override
    void printQuestion() {
        System.out.println(questionText);
    }
}
