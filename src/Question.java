public class Question {
    private String prompt;
    private String[] options;
    private int correctOptionIndex;

    public Question(String prompt, String[] options, int correctOptionIndex) {
        this.prompt = prompt;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    public String getPrompt() {
        return prompt;
    }

    public String[] getOptions() {
        return options;
    }

    public boolean checkAnswer(int optionIndex) {
        return optionIndex == correctOptionIndex;
    }
}