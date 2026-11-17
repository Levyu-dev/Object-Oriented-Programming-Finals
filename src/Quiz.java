package Quiz;

import java.util.*;

public class Quiz implements GameMode {
    private final String title;
    protected final List<Questions> questions = new ArrayList<>();
    private int score;
    private int attempts;

    public Quiz(String title) {
        this.title = title;
    }

    public void addQuestion(Questions q) throws InvalidQuizSubmissionException {
        if (q == null) {
            throw new InvalidQuizSubmissionException("Cannot add an empty question to the quiz.");
        }
        questions.add(q);
    }

    public void addAll(List<Questions> list) throws InvalidQuizSubmissionException {
        for (Questions q : list) {
            addQuestion(q);
        }
    }

    // Throws if the quiz can't be started
    protected void validateReady() throws InvalidQuizSubmissionException {
        if (questions.isEmpty()) {
            throw new InvalidQuizSubmissionException("The quiz has no questions yet.");
        }
    }

    // Checks the quiz and resets progress; returns false if it can't start
    protected boolean prepare() {
        try {
            validateReady();
            resetProgress();
            return true;
        } catch (InvalidQuizSubmissionException e) {
            System.out.println("Cannot start: " + e.getMessage());
            return false;
        }
    }

    protected void resetProgress() {
        score = 0;
        attempts = 0;
    }

    protected void recordResult(boolean correct) {
        attempts++;
        if (correct) {
            score++;
        }
    }

    @Override
    public void play(Scanner input) {
        if (!prepare()) return;

        System.out.println("\n=== " + title + " ===");
        for (Questions q : questions) {
            recordResult(q.askQuestions(input));
        }
        showSummary();
    }

    protected void showSummary() {
        System.out.println("\n--- Quiz Over! ---");
        System.out.println("Score: " + score + "/" + questions.size());
        System.out.printf("Accuracy: %.1f%%%n", getAccuracy());
        System.out.println("XP Earned: " + getXpEarned());
    }

    @Override
    public String getModeName() { return title; }

    public int getScore() { return score; }

    public int getAttempts() { return attempts; }

    public int getTotalQuestions() { return questions.size(); }

    public double getAccuracy() {
        return attempts == 0 ? 0 : (score * 100.0) / attempts;
    }

    // Subclasses override this (polymorphism)
    public int getXpEarned() {
        return score * 10;
    }
}