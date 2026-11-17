package Quiz;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        List<Questions> bank = loadQuestions();
        boolean running = true;

        System.out.println("---");
        System.out.println("  CODING PRACTICE PLATFORM  ");
        System.out.println("---");

        while (running) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Standard Quiz");
            System.out.println("2. Timed Quiz");
            System.out.println("3. Final Boss Mode");
            System.out.println("4. Party Mode");
            System.out.println("5. Exit");
            System.out.print("Choose a mode: ");
            String choice = input.nextLine().trim();

            try {
                switch (choice) {
                    case "1": {
                        Quiz quiz = new Quiz("Standard Quiz");
                        quiz.addAll(bank);
                        quiz.play(input);
                        break;
                    }
                    case "2": {
                        TimedQuiz timed = new TimedQuiz("Timed Quiz", 90);
                        timed.addAll(bank);
                        timed.play(input);
                        break;
                    }
                    case "3": {
                        FinalBossChallenge boss = new FinalBossChallenge("The Null Pointer", 100, 20, 3);
                        boss.addAll(bank);
                        boss.play(input);
                        break;
                    }
                    case "4": {
                        PartyMode party = new PartyMode(bank);
                        System.out.print("How many players (2-4)? ");
                        int n = Integer.parseInt(input.nextLine().trim());
                        if (n < 2 || n > 4) {
                            System.out.println("Please choose 2 to 4 players.");
                            break;
                        }
                        for (int i = 1; i <= n; i++) {
                            System.out.print("Name of player " + i + ": ");
                            party.addPlayer(input.nextLine());
                        }
                        party.play(input);
                        break;
                    }
                    case "5":
                        running = false;
                        System.out.println("Keep practicing! Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Enter 1-5.");
                }
            } catch (InvalidQuizSubmissionException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
        input.close();
    }


    private static List<Questions> loadQuestions() {
        List<Questions> bank = new ArrayList<>();

        bank.add(new MultipleChoiceQuestions("A keyword used to make a class inherit from another.",
                new String[] {"A) super", "B) static", "C) override", "D) extends"}, "D"));
        bank.add(new MultipleChoiceQuestions("What does the final method do?",
                new String[] {"A) It cannot be overriden", "B) It cannot be inherited", "C) Hides the parent method", "D) Hides the object"}, "A"));

        bank.add(new IdentificationQuestions("What does Polymorphism mean?", "many forms"));
        bank.add(new IdentificationQuestions("A method that resolves at compile-time.", "Overloading"));
        bank.add(new IdentificationQuestions("A concept that establishes a Parent-child relationship", "Inheritance"));

        bank.add(new CodeOutputQuestions("class Low { Low() { System.out.println(1); }}\n class High extends Low { High() { System.out.println(2); } }\n Low obj = new High();", "12"));

        bank.add(new CodeCompletionQuestions("Expected Outcome: Meow.\n class Animal {\n void speak() { System.out.println(Sound); }\n}\n class Cat extends Animal {\n void speak() { System.out.println(Meow); }\n}\n _____ pet = new ____();\n pet.speak();", "Animal, Cat"));

        bank.add(new DebuggingQuestions("abstract class Device {\n private abstract void boot();\n }\n class Laptop extends Device {\n void boot()\n }", "protected abstract void boot();"));

        return bank;
    }
}