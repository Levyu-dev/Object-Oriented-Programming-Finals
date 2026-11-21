package Quiz;
import java.util.*;

public class PartyMode implements GameMode {

    private static class PartyPlayer {
        private final String name;
        private boolean alive = true;
        private int score = 0;

        PartyPlayer(String name) { this.name = name; }
    }

    private final List<Questions> questions;
    private final List<PartyPlayer> players = new ArrayList<>();

    public PartyMode(List<Questions> questions) {
        this.questions = new ArrayList<>(questions);
    }

    public void addPlayer(String name) throws InvalidQuizSubmissionException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidQuizSubmissionException("Player name cannot be blank.");
        }
        for (PartyPlayer p : players) {
            if (p.name.equalsIgnoreCase(name.trim())) {
                throw new InvalidQuizSubmissionException("The name '" + name.trim() + "' is already taken.");
            }
        }
        players.add(new PartyPlayer(name.trim()));
    }

    @Override
    public void play(Scanner input) {
        if (players.size() < 2) {
            System.out.println("Party Mode needs at least 2 players.");
            return;
        }
        if (questions.isEmpty()) {
            System.out.println("Party Mode has no questions.");
            return;
        }

        Collections.shuffle(questions);
        System.out.println("\n=== PARTY MODE ===");
        System.out.println("Wrong answer = eliminated. Last player standing wins!");

        int round = 1;
        for (Questions q : questions) {
            List<PartyPlayer> alive = getAlivePlayers();
            if (alive.size() <= 1) break;

            System.out.println("\n----- Round " + round + " (" + alive.size() + " players left) -----");

            List<PartyPlayer> wrong = new ArrayList<>();
            for (PartyPlayer p : alive) {
                System.out.println("\n>> " + p.name + "'s turn (pass the keyboard!)");
                q.showQuestions();
                System.out.print("Enter your answer: ");
                String answer = input.nextLine();

                if (q.checkAnswer(answer)) {
                    p.score++;
                } else {
                    wrong.add(p);
                }
                hideScreen();
            }

            System.out.println("----- Round " + round + " results -----");
            System.out.println("The correct answer was: " + q.correctAnswer);
            for (PartyPlayer p : alive) {
                System.out.println(" - " + p.name + ": " + (wrong.contains(p) ? "Wrong" : "Correct"));
            }

            if (wrong.size() == alive.size()) {
                System.out.println("Everyone missed it! Nobody is eliminated this round.");
            } else {
                for (PartyPlayer p : wrong) {
                    p.alive = false;
                    System.out.println(p.name + " has been ELIMINATED!");
                }
            }
            round++;
        }

        showResults();
    }

    private void hideScreen() {
        for (int i = 0; i < 40; i++) System.out.println();
    }

    private List<PartyPlayer> getAlivePlayers() {
        List<PartyPlayer> alive = new ArrayList<>();
        for (PartyPlayer p : players) {
            if (p.alive) alive.add(p);
        }
        return alive;
    }

    private void showResults() {
        List<PartyPlayer> alive = getAlivePlayers();
        System.out.println("\n=== PARTY MODE OVER ===");
        if (alive.size() == 1) {
            System.out.println("WINNER: " + alive.get(0).name + "!");
        } else {
            System.out.print("Questions ran out! It's a tie between: ");
            for (int i = 0; i < alive.size(); i++) {
                System.out.print(alive.get(i).name + (i < alive.size() - 1 ? ", " : ""));
            }
            System.out.println();
        }
        System.out.println("\nFinal scores:");
        for (PartyPlayer p : players) {
            System.out.println(" - " + p.name + ": " + p.score + (p.alive ? "" : " (eliminated)"));
        }
    }

    @Override
    public String getModeName() { return "Party Mode"; }
}