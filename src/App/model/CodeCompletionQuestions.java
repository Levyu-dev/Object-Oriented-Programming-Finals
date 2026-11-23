package App.model;

public class CodeCompletionQuestions extends Questions {
    public CodeCompletionQuestions(String questionText, String correctAnswer) {
        super(questionText, correctAnswer);
    }

    @Override
    void showQuestion() {
        System.out.println(questionText);
    }
}
