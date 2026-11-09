import java.util.ArrayList;

public class Quiz {
    protected ArrayList<Questions> questions;
    protected int currentQuestionIndex;
    protected int score;
    protected int correctAnswersCount;
    
    public Quiz(ArrayList<Questions> questions) {
        this.questions = questions;
        this.currentQuestionIndex = 0;
        this.score = 0;
        this.correctAnswersCount = 0;
    }

    public Questions getCurrentQuestion() {
        return questions.get(currentQuestionIndex);
    }

    public boolean isFinished() {
        return currentQuestionIndex >= questions.size();
    }

    public boolean submitAnswer(String userAnswer) {
        if (isFinished()) {
            throw new InvalidQuizSubmissionException("Quiz is already finished.");
        }

        Questions currentQuestion = getCurrentQuestion();

        if (optionIndex < 0 || optionIndex >= currentQuestion.getOptions().length) {
            throw new InvalidQuizSubmissionException("Invalid option index: " + optionIndex);
        }

        boolean isCorrect = currentQuestion.checkAnswer(optionIndex);

        if (isCorrect) {
            score += 10;
            correctAnswersCount++;
        }

        currentQuestionIndex++;
        return isCorrect;
    }

    public double calculateAccuracy() {
        if (questions.isEmpty()) {
            return 0.0;
        }

        return ((double) correctAnswersCount / questions.size()) * 100.0;
    }

    public int getScore() {
        return score;
    }

    public int getCorrectAnswersCount() {
        return correctAnswersCount;
    }

    public int getTotalQuestions() {
        return questions.size();
    }
}