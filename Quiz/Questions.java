package Quiz;

import java.util.*;

abstract class Questions{
    
    //Attributes
    protected String questionText;
    protected String correctAnswer;

    //Question parameter
    Questions(String questionText, String correctAnswer) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
    }

    //Abstract operation
    abstract void showQuestions();
    
    //Boolean Constructor that checks the answer if it's correct or not
    boolean askQuestions (Scanner input) {
        showQuestions();
        System.out.println("Enter your answer: ");
        String userAnswer = input.nextLine();

        if (checkAnswer(userAnswer)) {
            System.out.println("Correct!");
            return true;
        }

        else {
            System.out.println("Wrong Answer! The correct answer is " + correctAnswer);
            return false;
        } 
    }

    //Ignores the case difference in scanner inputs
    protected boolean checkAnswer (String userAnswer) {
        return userAnswer.equalsIgnoreCase(correctAnswer);
    }
}

//Sub classes for every type of questions using overriding

class MultipleChoiceQuestions extends Questions {
    private String[] choices;

    MultipleChoiceQuestions(String questionText, String[] choices, String correctAnswer) {
        super(questionText, correctAnswer);
        this.choices = choices;
    }

    @Override
    void showQuestions() {
        System.out.println("[MULTIPLE CHOICE] " + questionText);
        for (String c :choices) {
            System.out.println(c);
        }
    }
}

class IdentificationQuestions extends Questions {

    IdentificationQuestions(String questionText, String correctAnswer) {
        super(questionText, correctAnswer);
    }

    @Override
    void showQuestions() {
        System.out.println("[IDENTIFICATION] " + questionText);
    }
}

class CodeOutputQuestions extends Questions {
    CodeOutputQuestions(String questionText, String correctAnswer) {
        super(questionText, correctAnswer);
    }

    @Override
    void showQuestions() {
        System.out.println("\n[CODE TRACING] Enter the code's predicted output.\n" + questionText);
    }
}

class CodeCompletionQuestions extends Questions {
    CodeCompletionQuestions(String questionText, String correctAnswer) {
        super(questionText, correctAnswer);
    }

    @Override
    void showQuestions() {
        System.out.println("\n[CODE COMPLETION] Fill the missing keywords in the code to make it run.\n" + questionText);
    }
}

class DebuggingQuestions extends Questions {
    DebuggingQuestions(String questionText, String correctAnswer) {
        super(questionText, correctAnswer);
    }

    @Override
    void showQuestions() {
        System.out.println("\n[DEBUGGING] Find the errors present in the code and enter the right or missing code.\n" + questionText);
    }
}