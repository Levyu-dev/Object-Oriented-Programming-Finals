package Quiz;
import java.util.*;

public class FinalBossChallenge extends Quiz {
    private final String bossName;
    private final int bossMaxHealth;
    private final int damagePerHit;
    private final int startingLives;

    private int bossHealth;
    private int lives;
    private boolean victory;

    public FinalBossChallenge(String bossName, int bossMaxHealth, int damagePerHit, int startingLives) {
        super("FINAL BOSS: " + bossName);
        if (bossMaxHealth <= 0 || damagePerHit <= 0 || startingLives <= 0) {
            throw new IllegalArgumentException("Boss stats must be greater than 0.");
        }
        this.bossName = bossName;
        this.bossMaxHealth = bossMaxHealth;
        this.damagePerHit = damagePerHit;
        this.startingLives = startingLives;
    }

    @Override
    public void play(Scanner input) {
        if (!prepare()) return;

        bossHealth = bossMaxHealth;
        lives = startingLives;
        victory = false;

        List<Questions> pool = new ArrayList<>(questions);
        Collections.shuffle(pool);

        System.out.println("\n=== FINAL BOSS MODE ===");
        System.out.println("A wild " + bossName + " appears!");
        System.out.println("Answer correctly to attack. Wrong answers cost you a life.");

        for (Questions q : pool) {
            if (bossHealth <= 0 || lives <= 0) break;

            printStatus();
            boolean correct = q.askQuestions(input);
            recordResult(correct);

            if (correct) {
                bossHealth = Math.max(0, bossHealth - damagePerHit);
                System.out.println("You hit " + bossName + " for " + damagePerHit + " damage!");
            } else {
                lives--;
                System.out.println(bossName + " strikes back! You lose a life.");
            }
        }

        victory = bossHealth <= 0;
        showSummary();
    }

    private void printStatus() {
        System.out.println("\n[" + bossName + " HP: " + bossHealth + "/" + bossMaxHealth
                + "]  [Your Lives: " + lives + "]");
    }

    @Override
    protected void showSummary() {
        System.out.println("\n--- Boss Battle Over! ---");
        if (victory) {
            System.out.println("VICTORY! You defeated " + bossName + "!");
        } else if (lives <= 0) {
            System.out.println("DEFEAT... " + bossName + " was too strong. Review and try again!");
        } else {
            System.out.println("You ran out of questions and " + bossName + " is still standing!");
        }
        System.out.println("Correct answers: " + getScore() + "/" + getAttempts());
        System.out.println("XP Earned: " + getXpEarned());
    }

    @Override
    public int getXpEarned() {
        return super.getXpEarned() + (victory ? 100 : 0);
    }

    public boolean isVictory() { return victory; }
}