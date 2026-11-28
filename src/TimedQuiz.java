package Quiz;
import java.util.*;

public class TimedQuiz extends Quiz {
    private final int timeLimitSeconds;
    private boolean finishedInTime;

    public TimedQuiz(String title, int timeLimitSeconds) {
        super(title);
        if (timeLimitSeconds <= 0) {
            throw new IllegalArgumentException("Time limit must be greater than 0.");
        }
        this.timeLimitSeconds = timeLimitSeconds;
    }

    @Override
    public void play(Scanner input) {
        if (!prepare()) return;

        finishedInTime = true;
        long limitMs = timeLimitSeconds * 1000L;
        long start = System.currentTimeMillis();

        System.out.println("\n=== " + getModeName() + " (TIMED) ===");
        System.out.println("You have " + timeLimitSeconds + " seconds. Go!");

        for (Questions q : getShuffledQuestions()) { //new code line
            long remaining = limitMs - (System.currentTimeMillis() - start);
            if (remaining <= 0) {
                System.out.println("\nTIME'S UP!");
                finishedInTime = false;
                break;
            }

            System.out.println("\nTime left: " + (remaining / 1000) + "s");
            boolean correct = q.askQuestions(input);

            if (System.currentTimeMillis() - start > limitMs) {
                System.out.println("(Too late! That answer doesn't count, even if it was correct.)");
                recordResult(false);
                finishedInTime = false;
                break;
            }
            recordResult(correct);
        }

        showSummary();
    }

    private boolean earnedBonus() {
        return finishedInTime && getScore() > 0;
    }

    @Override
    protected void showSummary() {
        super.showSummary();
        if (!finishedInTime) {
            System.out.println("You ran out of time.");
        } else if (earnedBonus()) {
            System.out.println("You finished before the timer ran out! (+20 bonus XP included)");
        } else {
            System.out.println("You finished in time, but with no correct answers there is no bonus.");
        }
    }

    @Override
    public int getXpEarned() {
        return super.getXpEarned() + (earnedBonus() ? 20 : 0);
    }

    public int getTimeLimitSeconds() { return timeLimitSeconds; }
}