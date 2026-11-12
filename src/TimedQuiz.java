import java.util.ArrayList;

public class TimedQuiz extends Quiz {
    private int timeLimitSeconds;
    private long questionStartTime;

    public TimedQuiz(ArrayList<Question> questions, int timelimitSeconds) {
        super(questions);
        this.timeLimitSeconds = timeLimitSeconds;
        this.questionStartTime = System.currentTimeMillis();
    }

    public void startTimer() {
        this.questionStartTime = System.currentTimeMillis();
    }
       
    @Override 
    public boolean submitAnswer(int optionIndex) {
        long currentTime = System.currentTimeMillis();
        long secondsElapsed = (currentTime - questionStartTime) / 1000;

        if (secondsElapsed > timeLimitSeconds) {
            currentQuestionIndex++;
            if (!isFinished()) {
                startTimer();
            }
            throw new InvalidQuizSubmissionException(" YOU time is Expired You took " + secondsElapsed + " seconds. ");
        }
        boolean isCorrect = super.submitAnswer(optionIndex);
        if (!isFinished()) {
            startTimer();
        }
        return isCorrect;
    }

    public int getTimeLimitSeconds() {
        return timeLimitSeconds;
            }
        }
