package App.model;

public class MultipleChoiceQuestions extends Questions {
    private final String[] choices;

    public MultipleChoiceQuestions(String questionText, String[] choices, String correctAnswer) {
        this(questionText, choices, correctAnswer, 50);
    }

    public MultipleChoiceQuestions(String questionText, String[] choices, String correctAnswer, int points) {
        super(questionText, correctAnswer, points);
        this.choices = choices.clone();
    }

    public String[] getChoices() {
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
