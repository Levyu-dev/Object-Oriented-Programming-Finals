
package quiz;

import java.util.ArrayList;
import java.util.HashMap;

public class PartyMode {

    private ArrayList<String> playerNames;
    private HashMap<String, Integer> playerScores;
    private int currentPlayerIndex;
    private Quiz sharedQuiz;

    public PartyMode(ArrayList<String> playerNames, Quiz sharedQuiz) {

        if (playerNames == null || playerNames.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one player is required.");
        }

        if (sharedQuiz == null) {
            throw new IllegalArgumentException("Quiz cannot be null.");
        }

        this.playerNames = new ArrayList<>();
        this.sharedQuiz = sharedQuiz;
        this.currentPlayerIndex = 0;
        this.playerScores = new HashMap<>();

        for (String name : playerNames) {
            if (name == null || name.trim().isEmpty()
                    || this.playerNames.contains(name.trim())) {
                throw new IllegalArgumentException(
                        "Player names must be valid and unique.");
            }

            String playerName = name.trim();
            this.playerNames.add(playerName);
            this.playerScores.put(playerName, 0);
        }
    }

    public String getCurrentPlayer() {
        return playerNames.get(currentPlayerIndex);
    }

    public boolean submitTurn(String userAnswer) {

        if (sharedQuiz.isFinished()) {
            throw new InvalidQuizSubmissionException(
                    "Party quiz is already finished.");
        }

        String currentPlayer = getCurrentPlayer();
        boolean isCorrect = sharedQuiz.submitAnswer(userAnswer);

        if (isCorrect) {
            int currentScore = playerScores.get(currentPlayer);
            playerScores.put(currentPlayer, currentScore + 10);
        }

        currentPlayerIndex =
                (currentPlayerIndex + 1) % playerNames.size();

        return isCorrect;
    }

    public HashMap<String, Integer> getPlayerScores() {
        return new HashMap<>(playerScores);
    }

    public Quiz getSharedQuiz() {
        return sharedQuiz;
    }
}
