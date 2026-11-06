import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        System.out.println("I HOPE THIS SHI WORKS");

        //THE QUESTIONS
        ArrayList<Question> testingQuestion = new ArrayList<>();
        testingQuestion.add(new Question("what is 4 + 4", new String[]{"3","8","5","9"},1));
        testingQuestion.add(new Question("which keyword is used for inheritance? in java??", new String[]{"implements","Extends","super","this"},1));
        testingQuestion.add(new Question("whats the int value in java?", new String[]{"1","null","0","-1"},0));

        //the quiz part
        System.out.println("this is just for testing");
        Quiz quiz = new Quiz(testingQuestion);
        System.out.println("Q1: " +  quiz.getCurrentQuestion().getPrompt());
        boolean ans1 = quiz.submitAnswer(1);
        System.out.println("submitted answer(correct?): " + ans1);
        System.out.println("current score: " + quiz.getScore());
        
        // the final boss challenge
        System.out.println("testing the boss");
        FinalBossChallenge bossQuiz = new FinalBossChallenge(testingQuestion, 50, 30);
        System.out.println("initial boss hp : " + bossQuiz.getBossCurrentHp());
        bossQuiz.submitAnswer(1);
        System.out.println("boss hp after correct answer : " + bossQuiz.getBossCurrentHp());

        //now the party mode testing
        System.out.println("party mode testing");
        ArrayList<String> players = new ArrayList<>();
        players.add("Jumana");
        players.add("Angel");
        PartyMode party = new PartyMode(players, new Quiz(testingQuestion));

        System.out.println("current turn: " + party.getCurrentPlayer());
        party.submitTurn(1);
         System.out.println("next turn: " + party.getCurrentPlayer());
         System.out.println("current party/player scores: " + party.getPlayerScores());
         System.out.println("please work");
    }
}