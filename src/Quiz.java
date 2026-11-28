package Quiz;

import java.util.*;

public class Quiz implements GameMode {
    private final String title;
    protected final List<Questions> questions = new ArrayList<>();
    private QuizSettings settings = QuizSettings.defaults(); 
    private int score;
    private int attempts;
    private boolean resultClaimed; 

    public Quiz(String title) {
        this.title = title;
    }

    public void addQuestion(Questions q) throws InvalidQuizSubmissionException {
        if (q == null) {
            throw new InvalidQuizSubmissionException("Cannot add an empty question to the quiz.");
        }
        if (containsQuestion(q)) { 
            throw new InvalidQuizSubmissionException("Duplicate question: " + q.getQuestionText()); 
        } 
        questions.add(q);
    }

    public void addAll(List<Questions> list) throws InvalidQuizSubmissionException {
        for (Questions q : list) {
            if (q != null && containsQuestion(q)) continue; 
        }
    }

    private boolean containsQuestion(Questions q) {
        String text = q.getQuestionText().trim(); 
        for (Questions existing : questions) { 
            if (existing.getQuestionText().trim().equalsIgnoreCase(text)) { 
                return true; 
            }
        }
        return false; 
    } 

    protected List<Questions> getShuffledQuestions() { 
        List<Questions> copy = new ArrayList<>(questions); 
        Collections.shuffle(copy); 
        return copy;
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
        resultClaimed = false; 
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
        for (Questions q : getShuffledQuestions()) { 
            recordResult(q.askQuestions(input));
        }
        showSummary();
    }

    protected void showSummary() {
        System.out.println("\nQuiz Over!");
        getResult().display(); 
    }

    public QuizResult getResult() { 
        return new QuizResult(title, questions.size(), attempts, score, getXpEarned(), settings.getPassingScore()); 
    } //new code line

    // Returns true only the first time per finished attempt, so XP is saved once
    public boolean claimResult() { 
        if (resultClaimed || attempts == 0) return false; 
        resultClaimed = true; 
        return true; 
    }

    public void setSettings(QuizSettings settings) { 
        if (settings == null) throw new IllegalArgumentException("Settings cannot be null."); 
        this.settings = settings; 
    } 

    public QuizSettings getSettings() { return settings; } 

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