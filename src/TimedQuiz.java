import java.util.ArrayList;

public class TimedQuiz extends Quiz {
    private int timeLimitSeconds;
    private long questionStartTime;

    public TimedQuiz(ArrayList<Questions> questions, int timelimitSeconds) {
        super(questions);
        this.timeLimitSeconds = timelimitSeconds;
        this.questionStartTime = System.currentTimeMillis();
    }

    public void startTimer() {
        this.questionStartTime = System.currentTimeMillis();
    }

    @Override
    public boolean submitAnswer(String userAnswer) {
        long currentTime = System.currentTimeMillis();
        long secondsElapsed = (currentTime - questionStartTime) / 1000;

        if (secondsElapsed > timeLimitSeconds) {
            currentQuestionIndex++;
            if (!isFinished()) {
                startTimer();
            }
            throw new InvalidQuizSubmissionException("YOU time is Expired You took " + secondsElapsed + " seconds.");
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