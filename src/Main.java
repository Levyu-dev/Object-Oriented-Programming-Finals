import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        System.out.println("Updated questions using gab's ");

        //THE QUESTIONS
        ArrayList<Question> quizBank = new ArrayList<>();
        quizBank.add(new MultipleChoiceQuestion("A method that calculates and returns a value"., new String[] {"A) Void Method", "B) Value returning method", "C) User Defined Method", "D) Equivalent Method"},"B"));
        quizBank.add(new IdentificationQuestions("What does Polymorphism mean?", "Many forms"));
        quizBank.add(new CodeOutputQuestions("Class low { low() {System.out.println(1); }}/n class High extends low { High() {System.out.println(2); }}/n low obj = new High();", "12"));

        //testing base quiz with gabs part
        System.out.println("Testing base Quiz engine");
        Quiz quiz = new Quiz(quizBank);
        System.out.println("Q1: " +  quiz.getCurrentQuestion().getQuestionText());
        boolean ans1 = quiz.submitAnswer("B");
        System.out.println("submitted answer 'B' (Correct?): " + ans1);
        System.out.println("Current score: " + quiz.getScore());
        
        // the final boss challenge
        System.out.println("testing the boss");
        FinalBossChallenge bossQuiz = new FinalBossChallenge(quizBank, 50, 30);
        System.out.println("initial boss hp : " + bossQuiz.getBossCurrentHp());
        bossQuiz.submitAnswer("B");
        System.out.println("boss hp after correct answer : " + bossQuiz.getBossCurrentHp());

        //now the party mode testing
        System.out.println("party mode testing");
        ArrayList<String> players = new ArrayList<>();
        players.add("Jumana");
        players.add("Angel");
        PartyMode party = new PartyMode(players, new Quiz(quizBank));

        System.out.println("current turn: " + party.getCurrentPlayer());
        party.submitTurn("B");
         System.out.println("next turn: " + party.getCurrentPlayer());
         System.out.println("current party/player scores: " + party.getPlayerScores());
         System.out.println("please work");
    }
}