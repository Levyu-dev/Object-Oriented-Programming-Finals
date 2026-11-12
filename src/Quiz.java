package quiz;

import java.util.ArrayList;

public class Quiz {

    protected ArrayList<Questions> questions;
    protected int currentQuestionIndex;
    protected int score;
    protected int correctAnswersCount;

    public Quiz(ArrayList<Questions> questions) {

        if (questions == null) {
            throw new IllegalArgumentException(
                    "Question list cannot be null.");
        }

        this.questions = new ArrayList<>(questions);
        this.currentQuestionIndex = 0;
        this.score = 0;
        this.correctAnswersCount = 0;
    }

    public Questions getCurrentQuestion() {

        if (isFinished()) {
            throw new InvalidQuizSubmissionException(
                    "Quiz is already finished.");
        }

        return questions.get(currentQuestionIndex);
    }

    public boolean isFinished() {
        return currentQuestionIndex >= questions.size();
    }

    public boolean submitAnswer(String userAnswer) {

        if (isFinished()) {
            throw new InvalidQuizSubmissionException(
                    "Quiz is already finished.");
        }

        if (userAnswer == null || userAnswer.trim().isEmpty()) {
            throw new InvalidQuizSubmissionException(
                    "Answer cannot be empty.");
        }

        Questions currentQuestion = getCurrentQuestion();

        boolean isCorrect = currentQuestion.checkAnswer(userAnswer);

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
