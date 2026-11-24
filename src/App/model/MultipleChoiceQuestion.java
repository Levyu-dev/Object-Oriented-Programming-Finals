package App.model;

public class MultipleChoiceQuestion extends QuizQuestion {
    private final String[] choices;

    public MultipleChoiceQuestion(String questionText, String[] choices, String correctAnswer) {
        this(questionText, choices, correctAnswer, 50);
    }

    public MultipleChoiceQuestion(String questionText, String[] choices, String correctAnswer, int points) {
        super(questionText, correctAnswer, points);
        this.choices = choices.clone();
    }

    public String[] getChoices() {
        return choices.clone();
    }

    @Override
    void printQuestion() {
        System.out.println(questionText);
        for (String choice : choices) {
            System.out.println(choice);
        }
    }
}
