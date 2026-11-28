package Quiz;

import java.time.LocalDateTime;

public class QuizSettings {
    private final int timeLimitSeconds;
    private final int passingScore;
    private final int maxAttempts;
    private final LocalDateTime deadline;

    public QuizSettings(int timeLimitSeconds, int passingScore, int maxAttempts, LocalDateTime deadline) {
        if (timeLimitSeconds < 0 || maxAttempts < 0 || passingScore < 0 || passingScore > 100) {
            throw new IllegalArgumentException("Invalid quiz settings.");
        }
        this.timeLimitSeconds = timeLimitSeconds;
        this.passingScore = passingScore;
        this.maxAttempts = maxAttempts;
        this.deadline = deadline;
    }

    public static QuizSettings defaults() {
        return new QuizSettings(0, 0, 0, null);
    }

    public void checkCanStart(int attemptsUsed) throws InvalidQuizSubmissionException {
        if (deadline != null && LocalDateTime.now().isAfter(deadline)) {
            throw new InvalidQuizSubmissionException("The deadline for this quiz has passed.");
        }
        if (maxAttempts > 0 && attemptsUsed >= maxAttempts) {
            throw new InvalidQuizSubmissionException("No retakes left for this quiz.");
        }
    }

    public int getTimeLimitSeconds() { return timeLimitSeconds; }
    public int getPassingScore() { return passingScore; }
    public int getMaxAttempts() { return maxAttempts; }
    public LocalDateTime getDeadline() { return deadline; }
}