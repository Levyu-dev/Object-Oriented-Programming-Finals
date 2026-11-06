import java.util.ArrayList;

public class FinalBossChallenge extends Quiz {
    private int bossCurrentHp;
    private int bossMaxHp;
    private int playerCurrentHp;
    private int playerMaxHp;
    private int damageToBoss = 20;
    private int damageToPlayer = 15;

    public FinalBossChallenge(ArrayList<Question> questions, int bossHp, int playerHp) {
        super(questions);
        this.bossMaxHp = bossHp;
        this.bossCurrentHp = bossHp;
        this.playerMaxHp = playerHp;
        this.playerCurrentHp = playerHp;
    }

    @Override
    public boolean submitAnswer(int optionIndex) {
        boolean isCorrect = super.submitAnswer(optionIndex);

        if (isCorrect) {
            bossCurrentHp -= damageToBoss;
            if (bossCurrentHp < 0) {
                bossCurrentHp = 0;
            }
        } else {
            playerCurrentHp -= damageToPlayer;
            if (playerCurrentHp < 0) {
                playerCurrentHp = 0;
            }
        }

        return isCorrect;
    }

    @Override
    public boolean isFinished() {
        return super.isFinished() || isBossDefeated() || isPlayerDefeated();
    }

    public boolean isBossDefeated() {
        return bossCurrentHp <= 0;
    }

    public boolean isPlayerDefeated() {
        return playerCurrentHp <= 0;
    }

    public int getBossCurrentHp() {
        return bossCurrentHp;
    }

    public int getPlayerCurrentHp() {
        return playerCurrentHp;
    }
}