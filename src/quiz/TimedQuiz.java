
package quiz;

import java.util.ArrayList;

public class TimedQuiz extends Quiz {

    private int timeLimitSeconds;
    private long questionStartTime;

    public TimedQuiz(ArrayList<Questions> questions,
            int timeLimitSeconds) {

        super(questions);

        if (timeLimitSeconds <= 0) {
            throw new IllegalArgumentException(
                    "Time limit must be greater than zero.");
        }

        this.timeLimitSeconds = timeLimitSeconds;
        this.questionStartTime = System.currentTimeMillis();
    }

    public void startTimer() {
        this.questionStartTime = System.currentTimeMillis();
    }

    @Override
    public boolean submitAnswer(String userAnswer) {

        if (isFinished()) {
            throw new InvalidQuizSubmissionException(
                    "Quiz is already finished.");
        }

        long currentTime = System.currentTimeMillis();
        long elapsedMilliseconds = currentTime - questionStartTime;
        long secondsElapsed = elapsedMilliseconds / 1000;

        if (elapsedMilliseconds >= timeLimitSeconds * 1000L) {

            currentQuestionIndex++;

            if (!isFinished()) {
                startTimer();
            }

            throw new InvalidQuizSubmissionException(
                    "Time expired. You took "
                    + secondsElapsed + " seconds.");
        }

        boolean isCorrect = super.submitAnswer(userAnswer);

        if (!isFinished()) {
            startTimer();
        }

        return isCorrect;
    }

    public int getTimeLimitSeconds() {
        return timeLimitSeconds;
    }
}
