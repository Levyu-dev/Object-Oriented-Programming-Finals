import java.util.ArrayList;
import java.util.HashMap;

public class PartyMode {
    private ArrayList<String> playerNames;
    private HashMap<String, Integer> playerScores;
    private int currentPlayerIndex;
    private Quiz sharedQuiz;

    public PartyMode(ArrayList<String> playerNames, Quiz sharedQuiz) {
        this.playerNames = playerNames;
        this.sharedQuiz = sharedQuiz;
        this.currentPlayerIndex = 0;
        this.playerScores = new HashMap<>();

        for (String name : playerNames) {
            playerScores.put(name, 0);
        }
    }

    public String getCurrentPlayer() {
        if (playerNames == null || playerNames.isEmpty()) {
            return null;
        }
        return playerNames.get(currentPlayerIndex);
    }

    public boolean submitTurn(String userAnswer) {
        if (sharedQuiz == null || sharedQuiz.isFinished()) {
            return false;
        }

        String currentPlayer = getCurrentPlayer();
        boolean isCorrect = sharedQuiz.submitAnswer(userAnswer);

        if (isCorrect) {
            int currentScore = playerScores.get(currentPlayer);
            playerScores.put(currentPlayer, currentScore + 10);
        }

        currentPlayerIndex = (currentPlayerIndex + 1) % playerNames.size();
        return isCorrect;
    }

    public HashMap<String, Integer> getPlayerScores() {
        return playerScores;
    }

    public Quiz getSharedQuiz() {
        return sharedQuiz;
    }
}
